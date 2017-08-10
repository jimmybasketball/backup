package com.sfebiz.supplychain.provider.command.send.tpl.kd100;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskStatus;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskType;
import com.sfebiz.supplychain.persistence.base.route.domain.RegistRoutesLogDO;
import com.sfebiz.supplychain.persistence.base.route.manager.RegistRoutesLogManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderTaskManager;
import com.sfebiz.supplychain.protocol.kd100.KD100HttpUtil;
import com.sfebiz.supplychain.protocol.kd100.KD100Request;
import com.sfebiz.supplychain.protocol.kd100.KD100Response;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.tpl.TplOrderRegistRoutesCommand;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.TransCarrierCode;
import net.pocrd.entity.ServiceException;

import java.util.HashMap;

/**
 * 快递100订阅接口
 */
public class KD100RegistRoutesCommand extends TplOrderRegistRoutesCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    @Override
    public boolean execute() throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("快递100订阅接口")
                .addParm("国内运单号", stockoutOrderDO.getIntrMailNo())
                .addParm("国际运单号", stockoutOrderDO.getIntlMailNo())
                .log();

        return checkParam() && registRoute();
    }

    // 快递公司编码 、快递单号(长度不超过32位)必传
    public boolean checkParam() {
        //快递公司编码、国际运单号、国际快递公司编码字段待加入
        if (stockoutOrderDO == null) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("快递100信息订阅失败-出库单为null")
                    .log();
            return false;
        }

        if (routeType == null) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("快递100信息订阅失败-路由类型不能为空")
                    .addParm("订单号", stockoutOrderDO.getBizId())
                    .log();
            return false;
        }

        if (StringUtils.isEmpty(stockoutOrderDO.getIntlMailNo()) && StringUtils.isEmpty(stockoutOrderDO.getIntrMailNo())) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("快递100信息订阅-运单号为空")
                    .addParm("订单", stockoutOrderDO.getBizId())
                    .log();
            return false;
        } else if (StringUtils.isNotEmpty(stockoutOrderDO.getIntlMailNo()) && stockoutOrderDO.getIntlMailNo().length() > 32) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("快递100信息订阅-国际运单号长度超过32位")
                    .addParm("订单", stockoutOrderDO.getBizId())
                    .addParm("国际运单号", stockoutOrderDO.getIntlMailNo())
                    .log();
            return false;
        } else if (StringUtils.isNotEmpty(stockoutOrderDO.getIntrMailNo()) && stockoutOrderDO.getIntrMailNo().length() > 32) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("快递100信息订阅-国内运单号长度超过32位")
                    .addParm("订单", stockoutOrderDO.getBizId())
                    .addParm("国内运单号", stockoutOrderDO.getIntrMailNo())
                    .log();
            return false;
        }
        if (StringUtils.isEmpty(stockoutOrderDO.getIntrCarrierCode()) && StringUtils.isEmpty(stockoutOrderDO.getIntlCarrierCode())) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("快递100信息订阅-快递公司编码为空")
                    .addParm("订单", stockoutOrderDO.getBizId())
                    .log();
            return false;
        }
        return true;
    }

    // 请求订阅，保存响应失败信息，再由异常处理机制重调
    private boolean registRoute() {
        //在KD100对应的国内承运商编码
        String kdCarrierCode = TransCarrierCode.getKDCarrierCode(stockoutOrderDO.getIntrCarrierCode());
        //在KD100对应的国际承运商编码
        String kdInternationalCarrierCode = TransCarrierCode.getKDCarrierCode(stockoutOrderDO.getIntlCarrierCode());
        if (StringUtils.isEmpty(kdCarrierCode) && StringUtils.isEmpty(kdInternationalCarrierCode)) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("快递100信息订阅-找不到对应的承运商编码,请检查是否配置")
                    .addParm("订单", stockoutOrderDO.getBizId())
                    .addParm("国内承运商编码", stockoutOrderDO.getIntrCarrierCode())
                    .addParm("国际承运商编码", stockoutOrderDO.getIntlCarrierCode())
                    .log();
            return false;
        }

        KD100Request request = new KD100Request();

        RegistRoutesLogDO registRoutesLogDO = new RegistRoutesLogDO();
        registRoutesLogDO.setStockoutOrderId(stockoutOrderDO.getId());

        // 快递公司编码转换
        if (RouteType.INTERNAL.getType().equalsIgnoreCase(routeType.getType())) {
            request.setCompany(kdCarrierCode);
            request.setNumber(stockoutOrderDO.getIntrMailNo());
            registRoutesLogDO.setIntrCarrierCode(stockoutOrderDO.getIntrCarrierCode());
            registRoutesLogDO.setIntrMailNo(stockoutOrderDO.getIntrMailNo());
        } else if (RouteType.INTERNATIONAL.getType().equalsIgnoreCase(routeType.getType())) {
            request.setCompany(kdInternationalCarrierCode);
            request.setNumber(stockoutOrderDO.getIntlMailNo());
            registRoutesLogDO.setIntlCarrierCode(stockoutOrderDO.getIntlCarrierCode());
            registRoutesLogDO.setIntlCarrierCode(stockoutOrderDO.getIntlMailNo());
        } else {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("快递100信息订阅-路由类型不合法，仅支持国内/国际路由类型")
                    .addParm("订单", stockoutOrderDO.getBizId())
                    .addParm("路由类型", routeType.getType())
                    .log();
        }

        // key=rNHccyMM9568、URL通过配置获取
        request.setKey(LogisticsProviderConfig.getKD100RequestKey());
        request.getParameters().put("callbackurl", LogisticsProviderConfig.getKD100ResponseUrl());

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("schema", "json");
        map.put("param", JSONUtil.toJson(request));

        // 保存订阅过的出库单信息
        RegistRoutesLogManager registRoutesLogManager = (RegistRoutesLogManager) CommandConfig.getSpringBean("registRoutesLogManager");
        try {
            String result = KD100HttpUtil.postData(LogisticsProviderConfig.getKD100RequestUrl(), map, "UTF-8");
            if (StringUtils.isEmpty(result)) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderDO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-快递100订阅失败]")
                        .addParm("请求报文", JSONUtil.toJson(request))
                        .addParm("回复报文", result)
                        .log();
                registRoutesLogDO.setIsSuccess(0);
                registRoutesLogManager.insertOrUpdate(registRoutesLogDO);
                return false;
            } else {
                KD100Response response = JSONUtil.parseJSONMessage(result, KD100Response.class);
                // 记录异常
                //200: 提交成功
                //701: 拒绝订阅的快递公司
                //700: 订阅方的订阅数据存在错误（如不支持的快递公司、单号为空、单号超长等）
                //600: 您不是合法的订阅者（即授权Key出错）
                //500: 服务器错误（即快递100的服务器处理间隙或临时性异常，有时如果因为不按规范提交请求，比如快递公司参数写错等，也会报此错误）
                //501: 重复订阅
                if (response.getResult() == false) {
                    StockoutOrderTaskDO stockoutOrderTaskDO = new StockoutOrderTaskDO();
                    stockoutOrderTaskDO.setStockoutOrderId(stockoutOrderDO.getId());
                    stockoutOrderTaskDO.setBizId(stockoutOrderDO.getBizId());
                    stockoutOrderTaskDO.setStockoutOrderState(stockoutOrderDO.getOrderState());
                    stockoutOrderTaskDO.setTaskMemo(response.getReturnCode());
                    stockoutOrderTaskDO.setTaskType(TaskType.KD100_REGIST_EXCEPTION.getValue());
                    stockoutOrderTaskDO.setFeatures(routeType.getType());
                    if ("500".equals(response.getReturnCode())) {
                        stockoutOrderTaskDO.setTaskState(TaskStatus.WAIT_HANDLE.getValue());
                    } else {
                        stockoutOrderTaskDO.setTaskState(TaskStatus.HANDLE_SUCCESS.getValue());
                    }
                    StockoutOrderTaskManager stockoutOrderTaskManager = (StockoutOrderTaskManager) CommandConfig.getSpringBean("stockoutOrderTaskManager");
                    stockoutOrderTaskManager.insertOrUpdate(stockoutOrderTaskDO);
                } else {
                    registRoutesLogDO.setIsSuccess(1);
                    registRoutesLogManager.insertOrUpdate(registRoutesLogDO);
                }

                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderDO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-快递100订阅]")
                        .addParm("请求报文", JSONUtil.toJson(request))
                        .addParm("回复报文", result)
                        .log();
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-快递100订阅异常]: " + e.getMessage())
                    .addParm("订单", stockoutOrderDO.getBizId())
                    .log();
            return false;
        }

        return true;
    }
}
