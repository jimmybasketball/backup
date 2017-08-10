package com.sfebiz.supplychain.provider.command.send.wms.zebra;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.tracelog.TraceLog.TraceLevel;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCancelCommand;
import com.sfebiz.supplychain.provider.entity.ResponseState;
import com.sfebiz.supplychain.provider.entity.ZebraConstants;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 下发取消发货指令 logistics.event.wms.cancel SF -> LP
 * 卖家发货后由于一些原因取消发货了,需要通知LP那笔交易取消发货 了,取消发货的时间必须是LP审单之前,如果仓库在取消订单后收到了货,则直接拒收。
 *
 * @author xiaoxu
 */
public class ZebraCancelCommand extends WmsOrderCancelCommand {
    private static final String serviceName = "CancelOutboundOrder";
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private StockoutOrderRecordManager stockoutOrderRecordManager;
    private RouteService routeService;

    @Override
    public boolean execute() {
        try {
            if (stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_CANCEL_SUCCESS.getValue()) {
                return true;
            }
            stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
            routeService = (RouteService) CommandConfig.getSpringBean("routeService");
            String meta = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("meta");
            Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
            String eventSource = "";
            if (metaData != null && metaData.containsKey("event_source")) {
                eventSource = (String) metaData.get("event_source");
            }
            String msgType = WmsMessageType.CANCEL_DELIVER.getValue();
            String url = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
            String key = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
                throw new Exception("线路配置错误" + logisticsLineBO.getLogisticsLineNid());
            }
            if (!url.endsWith("/")) {
                url = url.concat("/");
            }
            url = url.concat(serviceName);
            LogisticsEventsRequest request = buildCancelDeliverRequest(eventSource, logisticsLineBO.getWarehouseBO());
            //下发指令
            LogisticsEventsResponse responses = ProviderBizService.getInstance().send(request, msgType, url, key,
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP), ZebraConstants.PARTNER_CODE);
            if (responses.responseItems == null || responses.getResponseItems().size() == 0) {
                throw new Exception("斑马下发取消发货指令异常");
            }

            Response response = responses.getResponseItems().get(0);
            if (ResponseState.TRUE.getCode().equalsIgnoreCase(response.success)) {
                stockoutOrderBO.getRecordBO().setLogisticsState(LogisticsState.LOGISTICS_STATE_CANCEL_SUCCESS.getValue());
                stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CANCEL_SUCCESS.getValue());
                writeCancelCommandSuccessLog();
                traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLevel.INFO, "通知物流取消发货成功"));
                return true;
            } else {
                LogBetter.instance(logger).setLevel(LogLevel.WARN)
                        .setParms("下发取消发货指令异常")
                        .setParms(stockoutOrderBO.getBizId())
                        .setParms(response).log();

                writeCancelCommandFailureLog(responses.getResponseItems().get(0).getReasonDesc());
                traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLevel.ERROR, "下发发货指令失败"));
            }
        } catch (Exception e) {
            writeCancelCommandFailureLog(e.getMessage());
            logger.error("斑马取消发货异常", e);
            return false;
        }
        return false;
    }

    /**
     * 构建取消发货指令请求
     * COE区分 自营发货&转运发货
     *
     * @param eventSource
     * @param warehouse
     * @return
     */
    private LogisticsEventsRequest buildCancelDeliverRequest(String eventSource, WarehouseBO warehouse) {
        EventHeader eventHeader = new EventHeader();
        eventHeader.setEventSource(eventSource);
        eventHeader.setEventTarget(warehouse.getWarehouseNid());
        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
        if (stockoutOrderBO.getOrderType() == StockoutOrderType.SALES_STOCK_OUT.value) {
            eventHeader.setEventType(EventType.LOGISTICS_CANCEL.value);
        } else {
            eventHeader.setEventType(EventType.LOGISTICS_STOCKIN_CANCEL.value);
        }

        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setTradeOrderId(stockoutOrderBO.getId());
        List<TradeOrder> tradeOrders = new ArrayList<TradeOrder>();
        tradeOrders.add(tradeOrder);
        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setTradeOrders(tradeOrders);

        List<LogisticsOrder> logisticsOrders = new ArrayList<LogisticsOrder>();
        for (StockoutOrderDetailBO item : stockoutOrderDetailBOList) {
            LogisticsOrder logisticsOrder = new LogisticsOrder();
            logisticsOrder.setPoNo(stockoutOrderBO.getId() + "M" + stockoutOrderBO.getIntrMailNo());
            logisticsOrder.setOccurTime(DateUtil.getCurrentDateTime());
            logisticsOrders.add(logisticsOrder);
        }

        LogisticsDetail logisticsDetail = new LogisticsDetail();
        logisticsDetail.setLogisticsOrders(logisticsOrders);

        EventBody eventBody = new EventBody();
        eventBody.setTradeDetail(tradeDetail);
        eventBody.setLogisticsDetail(logisticsDetail);

        LogisticsEvent logisticsEvent = new LogisticsEvent();
        logisticsEvent.setEventHeader(eventHeader);
        logisticsEvent.setEventBody(eventBody);

        LogisticsEventsRequest request = new LogisticsEventsRequest();
        request.setLogisticsEvent(logisticsEvent);
        return request;
    }

    /**
     * 记录取消发货失败日志
     *
     * @param errMsg
     */
    private void writeCancelCommandFailureLog(String errMsg) {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "仓库订单取消失败," + errMsg, SystemConstants.WARN_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

    /**
     * 记录下发取消发货指令成功日志                   `
     *
     */
    private void writeCancelCommandSuccessLog() {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "仓库订单取消成功", SystemConstants.INFO_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }
}
