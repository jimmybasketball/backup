package com.sfebiz.supplychain.provider.command.send.wms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.entity.PriceUnit;
import com.sfebiz.supplychain.provider.entity.ResponseState;
import com.sfebiz.supplychain.sdk.protocol.ContactDetail;
import com.sfebiz.supplychain.sdk.protocol.EventBody;
import com.sfebiz.supplychain.sdk.protocol.EventHeader;
import com.sfebiz.supplychain.sdk.protocol.EventType;
import com.sfebiz.supplychain.sdk.protocol.LogisticsDetail;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEvent;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsRequest;
import com.sfebiz.supplychain.sdk.protocol.LogisticsOrder;
import com.sfebiz.supplychain.sdk.protocol.Response;
import com.sfebiz.supplychain.sdk.protocol.Sku;
import com.sfebiz.supplychain.sdk.protocol.SkuDetail;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.merchant.Model.MerchantPackageMaterialBO;
import com.sfebiz.supplychain.service.sku.model.SkuType;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBuyerBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderRecordBO;
import com.sfebiz.supplychain.service.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;

public abstract class WmsTransferOutOrderCreateCommand extends AbstractCommand {

    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                             .getTraceLogger("order");

    /**
     * 调拨出库单对象
     */
    protected StockoutOrderBO                stockoutOrderBO;

    /**
     * 调拨出库单明细
     */
    protected List<StockoutOrderDetailBO>    stockoutOrderDetailBOList;

    /**
     * 调拨出库单关联的路线实体
     */
    protected LogisticsLineBO                lineBO;

    /**
     * 调拨出库单下发失败的原因
     */
    private String                           createFailureMessage;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setStockoutOrderDetailBOList(List<StockoutOrderDetailBO> stockoutOrderDetailBOList) {
        this.stockoutOrderDetailBOList = stockoutOrderDetailBOList;
    }

    public void setLineBO(LogisticsLineBO lineBO) {
        this.lineBO = lineBO;
    }

    public void setCreateFailureMessage(String createFailureMessage) {
        this.createFailureMessage = createFailureMessage;
    }

    public String getCreateFailureMessage() {
        return createFailureMessage;
    }

    /**
     * 模拟仓库下发调拨出库单成功
     *
     * @return mock结果
     */
    protected boolean mockWmsTransferOutOrderCreateSuccess() {
        logger.info("创建调拨出库单成功,调拨出库单号 :" + stockoutOrderBO.getBizId());
        StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig
            .getSpringBean("stockoutOrderRecordManager");
        stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(),
            LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
        traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(),
            TraceLog.TraceLevel.INFO, "[供应链报文状态]下发调拨出库单给仓库：成功"));
        return true;
    }

    /**
     * 创建调拨出库单请求
     *
     * @return 请求对象
     */
    public LogisticsEventsRequest buildCreateTransferOutOrderCommandRequest() {
        Map<String, String> metaData = lineBO.getWarehouseBO().getLogisticsProviderBO()
            .getBizMeta();
        String eventSource = "";
        if (metaData != null && metaData.containsKey("event_source")) {
            eventSource = (String) metaData.get("event_source");
        }
        WarehouseBO warehouse = lineBO.getWarehouseBO();

        EventHeader eventHeader = new EventHeader();
        eventHeader.setEventSource(eventSource);
        eventHeader.setEventTarget(warehouse.getWarehouseNid());
        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
        eventHeader.setEventType(EventType.LOGISTICS_SKU_PAID.value);

        // 发货仓库信息
        MerchantPackageMaterialBO merchantPackageMaterialBO = stockoutOrderBO
            .getMerchantPackageMaterialBO();
        ContactDetail sender = new ContactDetail();
        sender.setName(merchantPackageMaterialBO.getSenderName());
        sender.setPhone(merchantPackageMaterialBO.getSenderTelephone());
        sender.setMobile(merchantPackageMaterialBO.getSenderTelephone());
        sender.setProvince(warehouse.getSenderBO().getSenderProvince());
        sender.setCity(warehouse.getSenderBO().getSenderCity());
        sender.setDistrict(warehouse.getSenderBO().getSenderDistrict());
        sender.setStreetAddress(warehouse.getSenderBO().getSenderAddress());
        sender.setZipCode(warehouse.getSenderBO().getSenderZipcode());

        // 收货仓库信息
        StockoutOrderBuyerBO buyerBO = stockoutOrderBO.getBuyerBO();
        ContactDetail receiver = new ContactDetail();
        receiver.setName(buyerBO.getBuyerName());
        receiver.setPhone(buyerBO.getBuyerTelephone());
        receiver.setEmail("");
        receiver.setMobile(buyerBO.getBuyerTelephone());
        receiver.setProvince(buyerBO.getBuyerProvince());
        receiver.setCity(buyerBO.getBuyerCity());
        receiver.setDistrict(buyerBO.getBuyerRegion());
        receiver.setStreetAddress(buyerBO.getBuyerAddress());
        receiver.setZipCode(buyerBO.getBuyerZipcode());

        List<LogisticsOrder> logisticsOrders = new ArrayList<LogisticsOrder>();
        LogisticsOrder logisticsOrder = new LogisticsOrder();
        logisticsOrder.setPoNo("" + stockoutOrderBO.getId());
        logisticsOrder.setCarrierCode("SF");
        logisticsOrder.setMailNo("");
        logisticsOrder.setSenderDetail(sender);
        logisticsOrder.setReceiverDetail(receiver);
        List<Sku> skuList = new ArrayList<Sku>();
        for (StockoutOrderDetailBO item : stockoutOrderDetailBOList) {
            Sku sku = new Sku();
            sku.setSkuId("" + item.getSkuId());
            sku.setSkuBillName(item.getSkuBillName());
            sku.setType(SkuType.BASIC_SKU.getValue()); // TODO matt
            sku.setSkuUnitPrice((double) item.getMerchantPrice());
            sku.setSkuPriceCurrency(PriceUnit.CNF);
            sku.setBatchNo(item.getSkuBatch());
            sku.setSkuQty(item.getQuantity());
            skuList.add(sku);
        }
        logisticsOrder.skuDetail = new SkuDetail();
        logisticsOrder.skuDetail.skus = skuList;
        logisticsOrder.setCustomerCode(CommonUtil.getCustomerCode());
        logisticsOrders.add(logisticsOrder);
        LogisticsDetail logisticsDetail = new LogisticsDetail();
        logisticsDetail.setLogisticsOrders(logisticsOrders);

        EventBody eventBody = new EventBody();
        // 设置出库单类型为调拨出库单
        eventBody.setStockoutOrderType(3);
        eventBody.setLogisticsDetail(logisticsDetail);

        LogisticsEvent logisticsEvent = new LogisticsEvent();
        logisticsEvent.setEventHeader(eventHeader);
        logisticsEvent.setEventBody(eventBody);

        LogisticsEventsRequest request = new LogisticsEventsRequest();
        request.setLogisticsEvent(logisticsEvent);
        return request;
    }

    /**
     * 处理接口响应
     *
     * @param response
     * @return
     */
    public boolean processResponse(Response response) {
        boolean result = false;
        StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig
            .getSpringBean("stockoutOrderRecordManager");
        StockoutOrderRecordBO stockoutOrderRecordBO = stockoutOrderBO.getRecordBO();
        if (ResponseState.TRUE.getCode().equalsIgnoreCase(response.success)) {
            stockoutOrderRecordBO.setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS
                .getValue());
            stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(),
                LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
            writeCreateCommandSuccessLog();
            result = true;
        } else if (ResponseState.ORDER_EXIST.getCode().equalsIgnoreCase(response.getReason())) {
            stockoutOrderRecordBO.setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS
                .getValue());
            stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(),
                LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
            writeCreateCommandSuccessLog();
            result = true;
        } else if (ResponseState.NET_TIMEOUT.getCode().equalsIgnoreCase(response.getReason())
                   || "S07".equals(response.getReason())) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("系统下发调拨出库单指令失败等待重试")
                .setParms(stockoutOrderBO.getBizId()).setParms(response).log();
            writeCreateCommandFailureLog("网络超时");
            this.setCreateFailureMessage("网络超时");
        } else {
            stockoutOrderRecordBO.setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_ERROR
                .getValue());
            stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(),
                LogisticsState.LOGISTICS_STATE_CREATE_ERROR.getValue());
            writeCreateCommandFailureLog(response.getReasonDesc());
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.ERROR)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                        SystemConstants.TRACE_APP)).setMsg("[供应链-下发调拨出库单失败]")
                .setParms("", stockoutOrderBO.getBizId()).setParms("").setParms(response).log();
            this.setCreateFailureMessage(response.getReasonDesc());
        }
        return result;
    }

    /**
     * 记录下发出库失败日志
     *
     * @param errMsg
     */
    protected void writeCreateCommandFailureLog(String errMsg) {
        //        RouteService routeService = (RouteService) CommandConfig.getSpringBean("routeService");
        //        routeService.appandSystemRoute(stockoutOrderDO.getBizId(), "下发调拨出库单失败," + errMsg,
        //            SystemConstants.WARN_LEVEL, new Date(), SystemUserName.SFHT.getValue());
    }

    /**
     * 记录下发出库指令成功日志                   `
     *
     */
    protected void writeCreateCommandSuccessLog() {
        //        RouteService routeService = (RouteService) CommandConfig.getSpringBean("routeService");
        //        routeService.appandSystemRoute(stockoutOrderDO.getBizId(), "下发调拨出库单成功",
        //            SystemConstants.INFO_LEVEL, new Date(), SystemUserName.SFHT.getValue());
    }
}
