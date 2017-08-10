package com.sfebiz.supplychain.service.route.handler.impl;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.tpl.TplOrderGetRoutesCommand;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.route.handler.AbstractRouteFetchHandler;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 国内路由获取处理
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-08-02 14:32
 **/
public class DomesticRouteFetchHandler extends AbstractRouteFetchHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomesticRouteFetchHandler.class);

    private List<String> endStates = new ArrayList<String>(){
        {
            this.add(StockoutOrderState.SIGNED.getValue());
            this.add(StockoutOrderState.STOCKOUT_CANCEL.getValue());
            this.add(StockoutOrderState.CLOSED.getValue());
        }
    };

    @Resource
    private RouteService routeService;

    /**
     * 具体执行路由获取更新的方法
     *
     * @param stockoutOrderBO 出库单业务对象
     * @return 是否需要继续发送路由获取消息，仅代表本段路由执行的态度，仅在链条尾端的处理结果才能决定是否继续轮询
     */
    @Override
    protected boolean doFetch(StockoutOrderBO stockoutOrderBO) throws ServiceException {
        //判断出库单状态是否还在运输中，否则直接返回false
        if (endStates.contains(stockoutOrderBO.getOrderState())) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台路由-国内段路由获取] 出库单状态不在运输中，结束路由轮询")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .addParm("出库单状态", stockoutOrderBO.getOrderState())
                    .log();
            return false;
        }

        //是否有国内运单号，如果没有就直接返回，跳过本次轮询
        String mailNo = stockoutOrderBO.getIntrMailNo();
        if (StringUtils.isBlank(mailNo)) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.WARN)
                    .setMsg("[物流平台路由-国内段路由获取] 未发现国内运单号")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .log();
            return true;
        }

        if (MockConfig.isMocked("routes", "fetchIntrRoutes")) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.WARN)
                    .setMsg("[物流平台路由-国内段路由获取] mock")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .log();
            return true;
        }

        /*
            出库单===》线路===》找到国内路由获取方式对应的第三方服务商NID===》
            找到配置的command ===》执行路由获取command
         */
        LogisticsProviderBO providerBO = stockoutOrderBO.getLineBO().getDomesticRouteProviderBO();
        if (providerBO == null) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台路由-国内段路由获取] 线路上未找到国内路由获取provider")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .addParm("线路ID", stockoutOrderBO.getLineBO().getId())
                    .log();
            //TODO 是否需要抛出业务异常中断整个轮询进程
            return true;
        }

        //获取国内路由获取commond
        ProviderCommand cmd = CommandFactory.createCommand(providerBO.getLogisticsProviderNid(), WmsMessageType.GET_INTR_ROUTES.getValue());
        TplOrderGetRoutesCommand tplOrderGetRoutesCommand = (TplOrderGetRoutesCommand) cmd;
        tplOrderGetRoutesCommand.setCarrierCode(stockoutOrderBO.getIntlCarrierCode());
        tplOrderGetRoutesCommand.setMailNo(stockoutOrderBO.getIntlMailNo());
        tplOrderGetRoutesCommand.setOrderId(stockoutOrderBO.getBizId());

        //执行commond
        boolean isSuccess = tplOrderGetRoutesCommand.execute();

        if (isSuccess) {
            List<LogisticsUserRouteEntity> userRouteEntities = tplOrderGetRoutesCommand.getRoutes();

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台路由-国内段路由获取] 获取成功")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .addParm("运单号", stockoutOrderBO.getIntlMailNo())
                    .addParm("承运商编码", stockoutOrderBO.getIntlCarrierCode())
                    .addParm("providerNid", providerBO.getLogisticsProviderNid())
                    .addParm("路由信息", userRouteEntities)
                    .log();

            if (userRouteEntities != null && userRouteEntities.size() > 0) {
                Collections.sort(userRouteEntities);
                //覆盖国内段路由信息
                CommonRet<Void> commonRet = routeService.overrideUserRoute(stockoutOrderBO.getBizId(), RouteType.INTERNAL.getType(), userRouteEntities);
                if (commonRet.getRetCode() == SCReturnCode.COMMON_SUCCESS.getCode()) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setErrorMsg("[物流平台路由-国内段路由获取] 保存成功")
                            .addParm("订单ID", stockoutOrderBO.getBizId())
                            .addParm("运单号", stockoutOrderBO.getIntlMailNo())
                            .log();
                } else {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setErrorMsg("[物流平台路由-国内段路由获取] 保存失败")
                            .addParm("执行结果", commonRet.toString())
                            .addParm("订单ID", stockoutOrderBO.getBizId())
                            .addParm("运单号", stockoutOrderBO.getIntlMailNo())
                            .log();
                }
            }
        } else {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台路由-国内段路由获取] 执行commond失败")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .addParm("运单号", stockoutOrderBO.getIntlMailNo())
                    .addParm("承运商编码", stockoutOrderBO.getIntlCarrierCode())
                    .addParm("providerNid", providerBO.getLogisticsProviderNid())
                    .log();
        }
        return true;
    }
}
