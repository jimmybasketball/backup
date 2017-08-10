package com.sfebiz.supplychain.provider.command.send.wms.coe;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSenderCommand;

import java.util.Date;

/**
 * 下发发货指令
 */
public class COESendCommand extends WmsOrderSenderCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private StockoutOrderManager stockoutOrderManager;
    private RouteService routeService;

    @Override
    public boolean execute() {
        logger.info("COE 下发发货指令,直接返回成功");
        return true;
//        try {
//            if (stockoutOrderBO.getLogisticsState() == LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue()
//                    || stockoutOrderBO.getLogisticsState() == LogisticsState.LOGISTICS_STATE_STOCKOUT.getValue()) {
//                LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("下发发货指令成功").
//                        setParms(stockoutOrderBO.getBizId()).log();
//                return true;
//            }
//
//            if (stockoutOrderBO.getLogisticsState() == LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue()) {
//                return true;
//            }
//            stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
//            routeService = (RouteService) CommandConfig.getSpringBean("routeService");
//            String msgType = WmsMessageType.DELIVER_GOODS.getValue();
//            String url = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
//            String key = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
//
//            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
//                throw new Exception("线路配置错误" + logisticsLineBO.lineNid);
//            }
//
//            LogisticsEventsRequest request = buildDeliverGoodsRequest();
//
//            LogisticsEventsResponse responses = ProviderBO.getInstance().send(request, msgType, url, key,
//                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP));
//
//            if (responses.responseItems == null || responses.getResponseItems().size() == 0) {
//                throw new Exception("COE下发发货指令反馈信息异常");
//            }
//
//            Response response = responses.getResponseItems().get(0);
//            if (ResponseState.TRUE.getCode().equalsIgnoreCase(response.success)) {
//                stockoutOrderBO.setLogisticsState(LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue());
//                stockoutOrderManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue());
//                writeSendCommandSuccessLog();
//                return true;
//            } else if (ResponseState.NET_TIMEOUT.getCode().equalsIgnoreCase(response.getReason())) {
//                writeSendCommandFailureLog("网络超时");
//                LogBetter.instance(logger).setLevel(LogLevel.INFO)
//                        .setMsg("系统下发发货指令失败等待重试")
//                        .setParms(stockoutOrderBO.getBizId())
//                        .setParms(response).log();
//                return false;
//            } else {
//                stockoutOrderBO.setLogisticsState(LogisticsState.LOGISTICS_STATE_SEND_ERROR.getValue());
//                stockoutOrderManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_SEND_ERROR.getValue());
//                writeSendCommandFailureLog(responses.getResponseItems().get(0).getReasonDesc());
//                LogBetter.instance(logger).setLevel(LogLevel.WARN)
//                        .setParms("下发发货指令异常")
//                        .setParms(response).log();
//            }
//        } catch (Exception e) {
//            writeSendCommandFailureLog(e.getMessage());
//            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e).setMsg("COE下发出库指令异常").log();
//            return false;
//        } finally {
//            logger.info("COE 下发发货指令：end");
//        }
//        return false;
    }

    /**
     * 记录日常路由
     *
     * @param errMsg
     */
    private void writeSendCommandFailureLog(String errMsg) {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "通知仓库发货失败," + errMsg, SystemConstants.WARN_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

    /**
     * 记录成功路由信息                   `
     *
     */
    private void writeSendCommandSuccessLog() {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "通知仓库发货成功", SystemConstants.INFO_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }
}
