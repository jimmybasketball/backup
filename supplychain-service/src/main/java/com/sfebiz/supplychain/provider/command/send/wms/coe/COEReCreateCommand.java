package com.sfebiz.supplychain.provider.command.send.wms.coe;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.tracelog.TraceLog.TraceLevel;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.entity.LogisticsClearanceDetailEntity;
import com.sfebiz.supplychain.exposed.stockout.enums.LiuLianType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCreateCommand;
import com.sfebiz.supplychain.provider.entity.PriceUnit;
import com.sfebiz.supplychain.provider.entity.ResponseState;
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
 * 重新下发物流订单消息指令 logistics.event.wms.recreate SF -> LP
 */
public class COEReCreateCommand extends WmsOrderCreateCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private static final String LIULIAN_TRATE_TYPE = "LiuLian";
    private static final String HEIKE_TRATE_TYPE = "HeiKe";
    private static final String HAITAO_TRATE_TYPE = "HaiTao";
    private RouteService routeService;

    @Override
    public boolean execute() {
        logger.info("COE 重新下发物流订单消息指令: start");
        try {

            boolean isMockAutoCreated = MockConfig.isMocked("coe", "reCreateCommand");
            if (isMockAutoCreated) {
                //直接返回仓库已发货
                logger.info("MOCK：COE仓库 重新物流下单 采用MOCK实现");
                return true;
            }
            String msgType = WmsMessageType.RE_STOCK_OUT.getValue();
            String url = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
            String key = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
                throw new Exception("线路配置错误" + logisticsLineBO.getLogisticsLineNid());
            }

            routeService = (RouteService) CommandConfig.getSpringBean("routeService");
            LogisticsEventsRequest request = buildCreateCommandRequest();
            //配置指令类型：重发指令
            request.getLogisticsEvent().getEventHeader().setEventType(EventType.LOGISTICS_SKU_REPAID.value);

            LogisticsEventsResponse responses = ProviderBizService.getInstance().send(request, msgType, url, key,
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP));

            if (responses.getResponseItems() == null || responses.getResponseItems().size() == 0) {
                throw new Exception("COE物流下单反馈报文异常");
            }

            Response response = responses.getResponseItems().get(0);
            if (ResponseState.TRUE.getCode().equalsIgnoreCase(response.success)) {
                writeCreateCommandSuccessLog();
                return true;
            } else if (ResponseState.ORDER_EXIST.getCode().equalsIgnoreCase(response.getReason())) {
                writeCreateCommandSuccessLog();
                return true;
            } else if (ResponseState.NET_TIMEOUT.getCode().equalsIgnoreCase(response.getReason()) || "S07".equals(response.getReason())) {
                LogBetter.instance(logger).setLevel(LogLevel.WARN)
                        .setMsg("系统下发发货指令失败等待重试")
                        .setParms(stockoutOrderBO.getBizId())
                        .setParms(response).log();
                writeCreateCommandFailureLog(responses.getResponseItems().get(0).getReasonDesc());
                traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLevel.ERROR, "[供应链]海外物流商下发物流下单失败"));
                this.setCreateFailureMessage(responses.getResponseItems().get(0).getReasonDesc());
                return false;
            } else {
                writeCreateCommandFailureLog("海外物流商下发物流下单失败");
                LogBetter.instance(logger).setLevel(LogLevel.WARN)
                        .setMsg("海外物流商下发物流下单失败")
                        .setParms(stockoutOrderBO.getBizId())
                        .setParms(response)
                        .log();
                this.setCreateFailureMessage(responses.getResponseItems().get(0).getReasonDesc());
            }
        } catch (Exception e) {
            writeCreateCommandFailureLog(e.getMessage());
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .setMsg("海外物流商下发物流下单失败")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .log();
            this.setCreateFailureMessage("[供应链-海外物流商下发物流下单]" + e.getMessage());
            return false;
        } finally {
            logger.info("COE 下发物流订单消息指令:end");
        }
        return false;
    }

    /**
     * 构建出库指令请求
     * COE区分 自营发货&转运发货
     *
     * @return
     */
    private LogisticsEventsRequest buildCreateCommandRequest() {
        String meta = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("meta");
        Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
        String eventSource = "";
        if (metaData != null && metaData.containsKey("event_source")) {
            eventSource = (String) metaData.get("event_source");
        }
        WarehouseBO warehouse = logisticsLineBO.getWarehouseBO();

        EventHeader eventHeader = new EventHeader();
        eventHeader.setEventSource(eventSource);
        eventHeader.setEventTarget(warehouse.getWarehouseNid());
        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
        eventHeader.setEventType(EventType.LOGISTICS_SKU_REPAID.value);

        //买家信息
        Buyer buyer = new Buyer();
        String userName = CommonUtil.getBuyerName(stockoutOrderBO);
        String userIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);
        buyer.setName(userName);
        buyer.setPhone(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        buyer.setMobile(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        buyer.setProvince(stockoutOrderBO.getBuyerBO().getBuyerProvince());
        buyer.setCity(stockoutOrderBO.getBuyerBO().getBuyerCity());
        buyer.setDistrict(stockoutOrderBO.getBuyerBO().getBuyerRegion());
        buyer.setStreetAddress(stockoutOrderBO.getBuyerBO().getBuyerAddress());
        buyer.setZipCode(stockoutOrderBO.getBuyerBO().getBuyerZipcode());
        buyer.setIdentityNumber(userIdNo);
        Attachments attachments = new Attachments();
        attachments.setAttachment1(stockoutOrderBO.getBuyerBO().getBuyerIdCardFrontPhotoUrl());
        attachments.setAttachment2(stockoutOrderBO.getBuyerBO().getBuyerIdCardBackPhotoUrl());
        buyer.setAttachments(attachments);
        int orderValue = 0;
        int weight = 0;
        List<Item> items = new ArrayList<Item>();
        StringBuilder itemIds = new StringBuilder();
        boolean isSelf = true;
        boolean isSupportBatch = false;
        if (logisticsLineBO.getWarehouseBO().getIsSupportBatch() > 0) {
            isSupportBatch = true;
        }
        List<StockoutOrderDetailBO> skuDOListForRequest = CommonUtil.mergeStockoutOrderSku(stockoutOrderDetailBOs, isSupportBatch);

        //支付详情
        PaymentDetail paymentDetail = new PaymentDetail();
        Paid paid = new Paid();
        paid.setTradeOrderValueUnit(PriceUnit.CNF);
        paid.setTradeOrderValue(stockoutOrderBO.getDeclarePriceBO().getOrderActualPrice());
        paid.setTradeOrderValueUnit(PriceUnit.CNF);
        paid.setTotalShippingFee(stockoutOrderBO.getUserFreightFee());
        paid.setTotalShippingFeeUnit(PriceUnit.CNF);
        paid.setPayableWeight(weight);
        paid.setDiscountValue(stockoutOrderBO.getUserDiscountPrice());
        paid.setDiscountValueUnit(PriceUnit.CNF);
        paid.setGoodsValue(stockoutOrderBO.getUserGoodsPrice());
        paid.setGoodsValueUnit(PriceUnit.CNF);
        paymentDetail.setPaid(paid);


        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setBuyer(buyer);
        tradeOrder.setItems(items);
        tradeOrder.setTradeOrderId(stockoutOrderBO.getId());


        List<TradeOrder> tradeOrders = new ArrayList<TradeOrder>();
        tradeOrders.add(tradeOrder);


        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setTradeType("HaiTao");
        tradeDetail.setTradeOrders(tradeOrders);

        ContactDetail sender = new ContactDetail();
        sender.setName(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderName()); // 获取包材配置的联系人
        sender.setPhone(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderTelephone());// 获取包材配置的联系电话
        sender.setMobile(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderTelephone());
        sender.setProvince(logisticsLineBO.getWarehouseBO().getSenderBO().getSenderProvince());
        sender.setCity(logisticsLineBO.getWarehouseBO().getSenderBO().getSenderCity());
        sender.setDistrict(logisticsLineBO.getWarehouseBO().getSenderBO().getSenderDistrict());
        sender.setStreetAddress(logisticsLineBO.getWarehouseBO().getSenderBO().getSenderAddress());
        sender.setZipCode(logisticsLineBO.getWarehouseBO().getSenderBO().getSenderZipcode());

        ContactDetail receiver = new ContactDetail();
        receiver.setName(logisticsLineBO.getWarehouseBO().getContactBO().getContactName());
        receiver.setPhone(logisticsLineBO.getWarehouseBO().getContactBO().getContactCellphone());
        receiver.setMobile(logisticsLineBO.getWarehouseBO().getContactBO().getContactTelephone());
        receiver.setEmail(logisticsLineBO.getWarehouseBO().getContactBO().getContactEmail());
        receiver.setProvince(logisticsLineBO.getWarehouseBO().getAddressBO().getProvince());
        receiver.setCity(logisticsLineBO.getWarehouseBO().getAddressBO().getCity());
        receiver.setDistrict(logisticsLineBO.getWarehouseBO().getAddressBO().getDistrict());
        receiver.setStreetAddress(logisticsLineBO.getWarehouseBO().getAddressBO().getAddress());
        receiver.setZipCode(logisticsLineBO.getWarehouseBO().getAddressBO().getZipcode());

        EventBody eventBody = new EventBody();
        ClearanceDetail clearanceDetail = new ClearanceDetail();
        LogisticsClearanceDetailEntity clearanceDetailEntity = CommonUtil.buildClearanceDetailEntity(logisticsLineBO, stockoutOrderBO);
        clearanceDetail.carrierCode = clearanceDetailEntity.carrierCode;
        clearanceDetail.mailNo = clearanceDetailEntity.mailNo;
        clearanceDetail.orderId = clearanceDetailEntity.orderId;
        clearanceDetail.shipperCode = clearanceDetailEntity.shipperCode;
        clearanceDetail.deliveryCode = clearanceDetailEntity.deliveryCode;
        clearanceDetail.custId = clearanceDetailEntity.custId;
        clearanceDetail.payMethod = clearanceDetailEntity.payMethod;
        clearanceDetail.senderAddress = clearanceDetailEntity.senderAddress;
        eventBody.setClearanceDetail(clearanceDetail);


        if (logisticsLineBO.getDomesticLogisticsProviderBO() != null && StringUtils.isBlank(clearanceDetail.deliveryCode)) {
            throw new IllegalArgumentException("目的地代码不能为空");
        }

        List<LogisticsOrder> logisticsOrders = new ArrayList<LogisticsOrder>();

        LogisticsOrder logisticsOrder = new LogisticsOrder();
        logisticsOrder.setPoNo("" + stockoutOrderBO.getId());
        logisticsOrder.setCarrierCode(eventBody.getClearanceDetail().getCarrierCode());
        logisticsOrder.setMailNo(eventBody.getClearanceDetail().getMailNo());
        logisticsOrder.setSenderDetail(sender);
        logisticsOrder.setReceiverDetail(receiver);
        logisticsOrder.setNeedCheck(LiuLianType.NEED_CHECK_NO.getValue());
        //配置小票格式
//        String receiptFormat = OpenApiConfig.getInstance().getRule(stockoutOrderBO.getBizType(), OpenApiConfigKeys.RECEIPT_FORMAT);
//        if (StringUtils.isEmpty(receiptFormat)) {
//            logisticsOrder.setReceiptFormat(1);
//        } else {
//            logisticsOrder.setReceiptFormat(Integer.parseInt(receiptFormat));
//        }
        logisticsOrder.setReceiptFormat(1);
        List<Sku> skus = new ArrayList<Sku>();
        for (StockoutOrderDetailBO item : skuDOListForRequest) {
            Sku sku = new Sku();
            sku.setSkuId("" + item.getSkuId());
            sku.setSkuBillName(item.getSkuBillName());
            sku.setType(0);
            sku.setSkuUnitPrice((double) item.getMerchantPrice());
            sku.setSkuPriceCurrency(PriceUnit.CNF);
            sku.setBatchNo(item.getSkuBatch());
            sku.setSkuQty(item.getQuantity());
            skus.add(sku);
        }
        logisticsOrder.skuDetail = new SkuDetail();
        logisticsOrder.skuDetail.skus = skus;
        logisticsOrder.setCustomerCode(CommonUtil.getCustomerCode());
        logisticsOrders.add(logisticsOrder);

        LogisticsDetail logisticsDetail = new LogisticsDetail();
        logisticsDetail.setLogisticsOrders(logisticsOrders);


        eventBody.setPaymentDetail(paymentDetail);
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
     * 记录下发出库失败日志
     *
     * @param errMsg
     */
    private void writeCreateCommandFailureLog(String errMsg) {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "香港COE仓下物流订单失败," + errMsg, SystemConstants.WARN_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

    /**
     * 记录下发出库指令成功日志                   `
     *
     */
    private void writeCreateCommandSuccessLog() {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "香港COE仓物流下单成功", SystemConstants.INFO_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

}
