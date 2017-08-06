package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.entity.PriceUnit;
import com.sfebiz.supplychain.provider.entity.ResponseState;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderRecordBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class WmsTransferOutOrderCreateCommand extends AbstractCommand {

    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    /**
     * 调拨出库单对象
     */
    protected StockoutOrderBO stockoutOrderBO;

    /**
     * 出库单跟踪记录对象
     */
    protected StockoutOrderRecordBO stockoutOrderRecordBO;

    /**
     * 调拨出库单相关的商品信息
     */
    protected List<StockoutOrderDetailBO> stockoutOrderDetailBOs;

    /**
     * 调拨出库单关联的路线实体
     */
    protected LogisticsLineBO logisticsLineBO;

    /**
     * 调拨出库单下发失败的原因
     */
    private String createFailureMessage;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setStockoutOrderDetailBOs(List<StockoutOrderDetailBO> stockoutOrderDetailBOs) {
        this.stockoutOrderDetailBOs = stockoutOrderDetailBOs;
    }

    public void setLogisticsLineBO(LogisticsLineBO logisticsLineBO) {
        this.logisticsLineBO = logisticsLineBO;
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
        StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
        stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
        traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO,
                "[供应链报文状态]下发调拨出库单给仓库：成功"));
        return true;
    }

    /**
     * 创建调拨出库单请求
     *
     * @return 请求对象
     */
    public LogisticsEventsRequest buildCreateTransferOutOrderCommandRequest() {
        String meta = logisticsLineBO.internationalLogisticsProviderBO.getInterfaceMeta().get("meta");
        Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
        String eventSource = "";
        if (metaData != null && metaData.containsKey("event_source")) {
            eventSource = (String) metaData.get("event_source");
        }
        WarehouseBO warehouseBO = logisticsLineBO.getWarehouseBO();

        EventHeader eventHeader = new EventHeader();
        eventHeader.setEventSource(eventSource);
        eventHeader.setEventTarget(warehouseBO.getWarehouseNid());
        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
        eventHeader.setEventType(EventType.LOGISTICS_SKU_PAID.value);

        // 发货仓库信息
        ContactDetail sender = new ContactDetail();
        sender.setName(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderName());
        sender.setPhone(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderTelephone());
        sender.setMobile(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderTelephone());
        sender.setProvince(warehouseBO.getSenderBO().getSenderProvince());
        sender.setCity(warehouseBO.getSenderBO().getSenderCity());
        sender.setDistrict(warehouseBO.getSenderBO().getSenderDistrict());
        sender.setStreetAddress(warehouseBO.getSenderBO().getSenderAddress());
        sender.setZipCode(warehouseBO.getSenderBO().getSenderZipcode());

        // 收货仓库信息
        ContactDetail receiver = new ContactDetail();
        receiver.setName(stockoutOrderBO.getBuyerBO().getBuyerName());
        receiver.setPhone(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        receiver.setEmail(warehouseBO.getPrincipalEmail());
        receiver.setMobile(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        receiver.setProvince(stockoutOrderBO.getBuyerBO().getBuyerProvince());
        receiver.setCity(stockoutOrderBO.getBuyerBO().getBuyerCity());
        receiver.setDistrict(stockoutOrderBO.getBuyerBO().getBuyerRegion());
        receiver.setStreetAddress(stockoutOrderBO.getBuyerBO().getBuyerAddress());
        receiver.setZipCode(stockoutOrderBO.getBuyerBO().getBuyerZipcode());

        List<LogisticsOrder> logisticsOrders = new ArrayList<LogisticsOrder>();
        LogisticsOrder logisticsOrder = new LogisticsOrder();
        logisticsOrder.setPoNo("" + stockoutOrderBO.getId());
        logisticsOrder.setCarrierCode("SF");
        logisticsOrder.setMailNo("");
        logisticsOrder.setSenderDetail(sender);
        logisticsOrder.setReceiverDetail(receiver);
        List<Sku> skuList = new ArrayList<Sku>();
        for (StockoutOrderDetailBO item : stockoutOrderDetailBOs) {
            Sku sku = new Sku();
            sku.setSkuId("" + item.getSkuId());
            sku.setSkuBillName(item.getSkuBillName());
//            sku.setType(SkuType.BASIC_SKU.getValue());
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
        StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
        if (ResponseState.TRUE.getCode().equalsIgnoreCase(response.success)) {
            stockoutOrderRecordBO.setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
            stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
            writeCreateCommandSuccessLog();
            result = true;
        } else if (ResponseState.ORDER_EXIST.getCode().equalsIgnoreCase(response.getReason())) {
            stockoutOrderRecordBO.setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
            stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
            writeCreateCommandSuccessLog();
            result = true;
        } else if (ResponseState.NET_TIMEOUT.getCode().equalsIgnoreCase(response.getReason()) || "S07"
                .equals(response.getReason())) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN)
                    .setMsg("系统下发调拨出库单指令失败等待重试")
                    .setParms(stockoutOrderBO.getBizId())
                    .setParms(response).log();
            writeCreateCommandFailureLog("网络超时");
            this.setCreateFailureMessage("网络超时");
        } else {
            stockoutOrderRecordBO.setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_ERROR.getValue());
            stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_ERROR.getValue());
            writeCreateCommandFailureLog(response.getReasonDesc());
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity
                    .instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setMsg("[供应链-下发调拨出库单失败]")
                    .setParms("", stockoutOrderBO.getBizId())
                    .setParms("")
                    .setParms(response)
                    .log();
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
        RouteService routeService = (RouteService) CommandConfig.getSpringBean("routeService");
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "下发调拨出库单失败," + errMsg, SystemConstants.WARN_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

    /**
     * 记录下发出库指令成功日志                   `
     *
     */
    protected void writeCreateCommandSuccessLog() {
        RouteService routeService = (RouteService) CommandConfig.getSpringBean("routeService");
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "下发调拨出库单成功", SystemConstants.INFO_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }
}
