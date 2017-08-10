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
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderRecordDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
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
import java.util.Collections;
import java.util.List;


/**
 * 国际路由获取处理
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-08-02 10:16
 **/
public class InternationalRouteFetchHandler extends AbstractRouteFetchHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(InternationalRouteFetchHandler.class);

    @Resource
    private StockoutOrderRecordManager stockoutOrderRecordManager;

    @Resource
    private RouteService routeService;

    /**
     * 执行国际段路由获取更新的方法
     *
     * @param stockoutOrderBO 出库单业务对象
     * @return 是否需要继续发送路由获取消息，仅代表本段路由执行的态度，仅在链条尾端的处理结果才能决定是否继续轮询
     */
    @Override
    protected boolean doFetch(StockoutOrderBO stockoutOrderBO) throws ServiceException {
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台路由-国际段路由获取] 开始")
                .addParm("出库单ID", stockoutOrderBO.getId())
                .log();

        //判断国际段运输状态，是否已完成国际段路由获取
        Integer intlState = stockoutOrderBO.getRecordBO().getTplIntlState();
        if (intlState != null && intlState == 2) {
            //国际运输已完成，直接return
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台路由-国际段路由获取] 国际运输已完成，不需要继续获取")
                    .addParm("出库单ID", stockoutOrderBO.getId())
                    .addParm("intlState", intlState)
                    .log();
            return false;
        }

        //是否有国际运单号，如果没有就直接返回，跳过本次轮询
        String mailNo = stockoutOrderBO.getIntlMailNo();
        if (StringUtils.isBlank(mailNo)) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.WARN)
                    .setMsg("[物流平台路由-国际段路由获取] 未发现国际运单号")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .log();
            return true;
        }

        if (MockConfig.isMocked("routes", "fetchIntlRoutes")) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.WARN)
                    .setMsg("[物流平台路由-国际段路由获取] mock")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .log();
            return true;
        }

        /*
            出库单===》线路===》找到国际路由获取方式对应的第三方服务商NID===》
            找到配置的command ===》执行路由获取command
         */
        LogisticsProviderBO providerBO = stockoutOrderBO.getLineBO().getInternationalRouteProviderBO();
        if (providerBO == null) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台路由-国际段路由获取] 线路上未找到国际路由获取provider")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .addParm("线路ID", stockoutOrderBO.getLineBO().getId())
                    .log();
            //TODO 是否需要抛出业务异常中断整个轮询进程
            return true;
        }

        //获取国际路由获取commond
        ProviderCommand cmd = CommandFactory.createCommand(providerBO.getLogisticsProviderNid(), WmsMessageType.GET_INTL_ROUTES.getValue());
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
                    .setErrorMsg("[物流平台路由-国际段路由获取] 获取成功")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .addParm("运单号", stockoutOrderBO.getIntlMailNo())
                    .addParm("承运商编码", stockoutOrderBO.getIntlCarrierCode())
                    .addParm("providerNid", providerBO.getLogisticsProviderNid())
                    .addParm("路由信息", userRouteEntities)
                    .log();

            if (userRouteEntities != null && userRouteEntities.size() > 0) {
                Collections.sort(userRouteEntities);
                //覆盖国际段路由信息
                CommonRet<Void> commonRet = routeService.overrideUserRoute(stockoutOrderBO.getBizId(), RouteType.INTERNATIONAL.getType(), userRouteEntities);
                if (commonRet.getRetCode() == SCReturnCode.COMMON_SUCCESS.getCode()) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setErrorMsg("[物流平台路由-国际段路由获取] 保存成功")
                            .addParm("订单ID", stockoutOrderBO.getBizId())
                            .addParm("运单号", stockoutOrderBO.getIntlMailNo())
                            .log();

                    //判断路由信息是否包含国际输运结束的关键字
                    if (checkInternationalRouteKeyWord(userRouteEntities.get(0))) {
                        StockoutOrderRecordDO stockoutOrderRecordDO = new StockoutOrderRecordDO();
                        stockoutOrderRecordDO.setId(stockoutOrderBO.getRecordBO().getId());
                        stockoutOrderRecordDO.setTplIntlState(2);
                        //国际运输已完成
                        stockoutOrderRecordManager.update(stockoutOrderRecordDO);
                    }
                } else {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setErrorMsg("[物流平台路由-国际段路由获取] 保存失败")
                            .addParm("执行结果", commonRet.toString())
                            .addParm("订单ID", stockoutOrderBO.getBizId())
                            .addParm("运单号", stockoutOrderBO.getIntlMailNo())
                            .log();
                }
            }
        } else {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台路由-国际段路由获取] 执行commond失败")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .addParm("运单号", stockoutOrderBO.getIntlMailNo())
                    .addParm("承运商编码", stockoutOrderBO.getIntlCarrierCode())
                    .addParm("providerNid", providerBO.getLogisticsProviderNid())
                    .log();
        }
        return true;
    }

    /**
     * 判断路由信息是否包含国际输运结束的关键字
     * @param lastInternationalRoute 最新的一条路由记录
     * @return  国际段运输是否已结束
     */
    private boolean checkInternationalRouteKeyWord(LogisticsUserRouteEntity lastInternationalRoute) {
        //TODO  关键字还不太清楚
        return false;
    }
}
