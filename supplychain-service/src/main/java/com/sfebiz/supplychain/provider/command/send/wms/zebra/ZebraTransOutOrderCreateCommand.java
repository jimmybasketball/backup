/*
 * @(#) com.sfebiz.logistics.provider.command.send.wms.zebra/ZebraTransOutOrderCreateCommand.java
 * 
 */
package com.sfebiz.supplychain.provider.command.send.wms.zebra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.pocrd.entity.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.stockout.entity.LogisticsClearanceDetailEntity;
import com.sfebiz.supplychain.exposed.stockout.enums.LiuLianType;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.common.PinyinUtil;
import com.sfebiz.supplychain.provider.command.send.wms.WmsTransferOutOrderCreateCommand;
import com.sfebiz.supplychain.provider.entity.PriceUnit;
import com.sfebiz.supplychain.provider.entity.ZebraConstants;
import com.sfebiz.supplychain.sdk.protocol.Attachments;
import com.sfebiz.supplychain.sdk.protocol.Buyer;
import com.sfebiz.supplychain.sdk.protocol.ClearanceDetail;
import com.sfebiz.supplychain.sdk.protocol.ContactDetail;
import com.sfebiz.supplychain.sdk.protocol.EventBody;
import com.sfebiz.supplychain.sdk.protocol.EventHeader;
import com.sfebiz.supplychain.sdk.protocol.EventType;
import com.sfebiz.supplychain.sdk.protocol.Item;
import com.sfebiz.supplychain.sdk.protocol.LogisticsDetail;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEvent;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsRequest;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsResponse;
import com.sfebiz.supplychain.sdk.protocol.LogisticsOrder;
import com.sfebiz.supplychain.sdk.protocol.Paid;
import com.sfebiz.supplychain.sdk.protocol.PaymentDetail;
import com.sfebiz.supplychain.sdk.protocol.ReceiptDetail;
import com.sfebiz.supplychain.sdk.protocol.ReceiptFooter;
import com.sfebiz.supplychain.sdk.protocol.ReceiptHeader;
import com.sfebiz.supplychain.sdk.protocol.Response;
import com.sfebiz.supplychain.sdk.protocol.Sku;
import com.sfebiz.supplychain.sdk.protocol.SkuDetail;
import com.sfebiz.supplychain.sdk.protocol.TradeDetail;
import com.sfebiz.supplychain.sdk.protocol.TradeOrder;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;

/**
 * 斑马仓下发调拨出库单指令
 * <p/>
 * 创建日期: 2015-09-28
 *
 * @author jackiehff
 */
public class ZebraTransOutOrderCreateCommand extends WmsTransferOutOrderCreateCommand {

    private static final String            serviceName = "OutboundOrder";

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");

    @Override
    public boolean execute() {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("斑马 下发调拨出库单消息指令: start")
            .addParm("调拨出库单信息", stockoutOrderBO).log();

        // 是否使用mock方式
        boolean isMockAutoCreated = MockConfig.isMocked("zebra", "createCommand");
        if (isMockAutoCreated) {
            //直接返回仓库已发货
            logger.info("MOCK：斑马仓库 下发调拨出库单 采用MOCK实现");
            return mockWmsTransferOutOrderCreateSuccess();
        }

        // 判断物流状态(0 INIT 1 物流下单成功 2 物流拒单 3 海外仓接受订单 4 海外仓包裹收齐 5 海外包裹异常 6 海外仓称重回传 7回传仓内异常信息 8 海外仓运费回传 9 下发发货指令成功 10 海外仓已发货 11 取消订单 12 取消成功 13 物流取消失败 14 物流下单失败 15 物流发货失败)
        if (stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS
            .getValue()
            || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_GOODS_WEIGHT
                .getValue()
            || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_SEND_SUCCESS
                .getValue()
            || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_STOCKOUT
                .getValue()) {
            return true;
        }

        boolean result = false;
        try {
            String msgType = WmsMessageType.STOCK_OUT.getValue();
            String url = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO()
                .getInterfaceMeta().get("interfaceUrl");
            String key = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO()
                .getInterfaceMeta().get("interfaceKey");
            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
                throw new Exception("路线配置错误" + logisticsLineBO.getLogisticsLineNid());
            }
            if (!url.endsWith("/")) {
                url = url.concat("/");
            }
            url = url.concat(serviceName);
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[斑马仓库 下发调拨出库单]")
                .addParm("线路信息", logisticsLineBO)
                .addParm("wmsProviderEntity",
                    logisticsLineBO.getWarehouseBO().getLogisticsProviderBO())
                .addParm("url信息", url).addParm("interfaceKey", key).log();
            LogisticsEventsRequest request = buildCreateCommandRequest();

            // 下发指令
            LogisticsEventsResponse responses = ProviderBizService.getInstance().send(
                request,
                msgType,
                url,
                key,
                TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                    SystemConstants.TRACE_APP), ZebraConstants.PARTNER_CODE);

            // 处理接口响应
            List<Response> responseList = responses.getResponseItems();
            if (CollectionUtils.isEmpty(responseList)) {
                this.setCreateFailureMessage("请求无响应");
                throw new Exception("斑马仓下发调拨出库单反馈报文异常");
            }
            result = processResponse(responseList.get(0));
        } catch (Exception e) {
            writeCreateCommandFailureLog(e.getMessage());
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.ERROR)
                .setException(e)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getId(), "supplychain"))
                .setMsg(
                    "[供应链报文-向斑马仓库下发调拨出库单指令异常]: " + "[调拨单号" + stockoutOrderBO.getBizId()
                            + ", 异常信息: " + e.getMessage() + "]")
                .addParm("调拨单号:", stockoutOrderBO.getBizId())
                .addParm("调拨出库单ID:", stockoutOrderBO.getId()).log();
            this.setCreateFailureMessage(e.getMessage());
        } finally {
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("斑马 下发调拨出库单消息指令: end")
                .addParm("调拨出库单信息", stockoutOrderBO).log();
        }
        return result;
    }

    /**
     * 构建出库指令请求
     * COE区分 自营发货&转运发货
     *
     * @return
     */
    public LogisticsEventsRequest buildCreateCommandRequest() throws ServiceException {
        String meta = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta()
            .get("meta");
        Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
        String eventSource = "";
        String sendEnInfo = "false";

        if (metaData != null) {
            if (metaData.containsKey("event_source")) {
                eventSource = (String) metaData.get("event_source");
            }
            if (metaData.containsKey("sendEnInfo")) {
                sendEnInfo = "true";
            }
        }

        TradeOrder tradeOrder = new TradeOrder();

        WarehouseBO warehouse = logisticsLineBO.getWarehouseBO();

        EventHeader eventHeader = new EventHeader();
        eventHeader.setEventSource(eventSource);
        eventHeader.setEventTarget(warehouse.getWarehouseNid());
        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
        eventHeader.setEventType(EventType.LOGISTICS_SKU_PAID.value);

        EventBody eventBody = new EventBody();
        ClearanceDetail clearanceDetail = new ClearanceDetail();
        LogisticsClearanceDetailEntity clearanceDetailEntity = CommonUtil
            .buildClearanceDetailEntity(logisticsLineBO, stockoutOrderBO);
        clearanceDetail.carrierCode = clearanceDetailEntity.carrierCode;
        clearanceDetail.mailNo = clearanceDetailEntity.mailNo;
        clearanceDetail.orderId = clearanceDetailEntity.orderId;
        clearanceDetail.shipperCode = clearanceDetailEntity.shipperCode;
        clearanceDetail.deliveryCode = clearanceDetailEntity.deliveryCode;
        clearanceDetail.custId = clearanceDetailEntity.custId;
        clearanceDetail.payMethod = clearanceDetailEntity.payMethod;
        clearanceDetail.senderAddress = clearanceDetailEntity.senderAddress;
        clearanceDetail.logo = clearanceDetailEntity.logo;
        eventBody.setClearanceDetail(clearanceDetail);

        //        if (logisticsLineBO.tplProviderEntity != null && StringUtils.isBlank(clearanceDetail.deliveryCode)) {
        //            throw new IllegalArgumentException("目的地代码不能为空");
        //        }

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
        boolean isSelfType = true;
        boolean isSupportBatch = logisticsLineBO.getWarehouseBO().getIsSupportBatch();
        List<StockoutOrderDetailBO> skuDOListForRequest = CommonUtil.mergeStockoutOrderSku(
            stockoutOrderDetailBOs, isSupportBatch);
        //转运出库单
        if (stockoutOrderBO.getOrderType() == StockoutOrderType.TRANSPORT_STOCK_OUT.value) {
            for (StockoutOrderDetailBO sku : skuDOListForRequest) {
                orderValue += sku.getQuantity() * sku.getMerchantPrice();
                if (sku.getWeight() == null) {
                    weight = 0;
                } else {
                    weight += sku.getWeight() * sku.getQuantity();
                }
                itemIds.append(sku.getSkuId() + ",");
                Item item = new Item();
                item.setItemId(sku.getSkuId());
                item.setItemName(sku.getSkuName());
                item.setItemUnitPrice((double) sku.getMerchantPrice());
                item.setItemQuantity(sku.getQuantity());
                item.setBrand(sku.getBrandName());
                item.setSpecification(sku.getSkuMerchantBO().getSkuBO().getAttributesDesc());
                item.setNetWeight(sku.getWeight() == null ? 0 : sku.getWeight().intValue());
                //                item.setItemCategoryName(sku.getCategory());
                item.setItemRemark(sku.getRemark());
                items.add(item);
            }
            isSelfType = false;
        }

        if (sendEnInfo.equals("true")) {
            Buyer buyerEn = new Buyer();
            buyerEn.setName(PinyinUtil.convertToPinyin(userName));
            buyerEn.setMobile(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
            buyerEn.setCountry(PinyinUtil.convertToPinyin(stockoutOrderBO.getBuyerBO()
                .getBuyerCountry()));
            buyerEn.setProvince(PinyinUtil.convertToPinyin(stockoutOrderBO.getBuyerBO()
                .getBuyerProvince()));
            buyerEn
                .setCity(PinyinUtil.convertToPinyin(stockoutOrderBO.getBuyerBO().getBuyerCity()));
            buyerEn.setDistrict(PinyinUtil.convertToPinyin(stockoutOrderBO.getBuyerBO()
                .getBuyerRegion()));
            String addressEn = PinyinUtil.convertToPinyin(stockoutOrderBO.getBuyerBO()
                .getBuyerAddress());
            addressEn = addressEn.replaceAll("，", ",");
            addressEn = addressEn.replaceAll("。", ".");
            addressEn = addressEn.replaceAll("（", "");
            addressEn = addressEn.replaceAll("）", "");
            addressEn = addressEn.trim();

            //英文地址最大长度105，分三段传，限制90可适应三行都已zhuang结尾的情况
            if (addressEn.length() > 90) {
                addressEn = addressEn.substring(0, 90);
            }
            buyerEn.setStreetAddress(addressEn);
            buyerEn.setZipCode(stockoutOrderBO.getBuyerBO().getBuyerZipcode());
            buyerEn.setIdentityNumber(stockoutOrderBO.getBuyerBO().getBuyerCertNo());
            tradeOrder.setBuyerEn(buyerEn);
        }

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

        tradeOrder.setBuyer(buyer);
        tradeOrder.setItems(items);
        tradeOrder.setTradeOrderId(stockoutOrderBO.getId());

        List<TradeOrder> tradeOrders = new ArrayList<TradeOrder>();
        tradeOrders.add(tradeOrder);

        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setTradeType(stockoutOrderBO.getOrderSource());
        tradeDetail.setTradeOrders(tradeOrders);

        ContactDetail sender = new ContactDetail();
        sender.setName(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderName()); // 获取包材配置的联系人
        sender.setPhone(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderTelephone());// 获取包材配置的联系电话
        sender.setMobile(logisticsLineBO.getWarehouseBO().getSenderBO().getSenderTelephone());
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

        List<LogisticsOrder> logisticsOrders = new ArrayList<LogisticsOrder>();
        if (isSelfType) {
            //自营订单内容体
            LogisticsOrder logisticsOrder = new LogisticsOrder();
            logisticsOrder.setPoNo("" + stockoutOrderBO.getId());
            logisticsOrder.setStockoutOrderType("2");
            logisticsOrder.setCarrierCode(eventBody.getClearanceDetail().getCarrierCode());
            logisticsOrder.setMailNo(stockoutOrderBO.getIntrMailNo());
            if (StringUtils.isBlank(logisticsLineBO.getRouteBizCode())) {
                throw new IllegalArgumentException("路线中RouteBizCode不能为空");
            }

            //默认管道编码从数据库中读取，如果diamond中有特殊配置则从diamond中读取配置
            logisticsOrder.setRouteId(logisticsLineBO.getRouteBizCode());

            //管道代码，跟仓绑定，不同的仓使用不同的管道代码, 还要区分不同的渠道类型
            String routeIdConf = stockoutOrderBO.getMerchantPackageMaterialBO().getRouteId();
            if (StringUtils.isNotEmpty(routeIdConf)) {
                Map<String, String> routeIdMap = JSON.parseObject(routeIdConf, Map.class);
                if (null != routeIdMap
                    && routeIdMap.containsKey(stockoutOrderBO.getWarehouseId().toString())) {
                    logisticsOrder.setRouteId(routeIdMap.get(stockoutOrderBO.getWarehouseId()
                        .toString()));
                }
            }

            logisticsOrder.setOccurTime(DateUtil.getCurrentDateTime());
            logisticsOrder.setSenderDetail(sender);
            logisticsOrder.setReceiverDetail(receiver);
            logisticsOrder.setNeedCheck(LiuLianType.NEED_CHECK_NO.getValue());
            //配置小票格式
            //            String receiptFormat = OpenApiConfig.getInstance().getRule(stockoutOrderBO.getBizType(), OpenApiConfigKeys.RECEIPT_FORMAT);
            //            if (StringUtils.isEmpty(receiptFormat)) {
            //                logisticsOrder.setReceiptFormat(1);
            //            } else {
            //                logisticsOrder.setReceiptFormat(Integer.parseInt(receiptFormat));
            //            }
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

            if (sendEnInfo.equals("true")) {
                skus = new ArrayList<Sku>();
                for (StockoutOrderDetailBO item : skuDOListForRequest) {
                    Sku sku = new Sku();
                    sku.setSkuId("" + item.getSkuId());

                    String foreignName = item.getSkuForeignName();
                    if (StringUtils.isNotBlank(foreignName)) {
                        //usps item英文名字最大字符长度为24
                        if (foreignName.length() > 24) {
                            foreignName = foreignName.substring(foreignName.length() - 24,
                                foreignName.length());
                        }
                    } else {
                        throw new IllegalArgumentException("商品外文名称不能为空，skuId：" + item.getSkuId());
                    }
                    sku.setSkuBillName(foreignName);
                    sku.setSkuUnitPrice((double) item.getMerchantPrice());
                    sku.setSkuPriceCurrency(PriceUnit.CNF);
                    sku.setSkuQty(item.getQuantity());
                    skus.add(sku);
                }
                logisticsOrder.skuDetailEn = new SkuDetail();
                logisticsOrder.skuDetailEn.skus = skus;
            }

            logisticsOrder.setCustomerCode(CommonUtil.getCustomerCode());

            ReceiptDetail receiptDetail = new ReceiptDetail();
            receiptDetail.setHeader(new ReceiptHeader());
            receiptDetail.setFooter(new ReceiptFooter());
            receiptDetail.getHeader().setLogo(
                stockoutOrderBO.getMerchantPackageMaterialBO().getHeaderLogo());
            receiptDetail.getHeader().setTitle(
                stockoutOrderBO.getMerchantPackageMaterialBO().getHeaderTitle());
            receiptDetail.getFooter().setAdvert(
                stockoutOrderBO.getMerchantPackageMaterialBO().getFooterAdvert());
            receiptDetail.getFooter().setDesc(
                stockoutOrderBO.getMerchantPackageMaterialBO().getFooterDesc());
            logisticsOrder.setReceiptDetail(receiptDetail);
            logisticsOrder.setPackingMaterials(stockoutOrderBO.getMerchantPackageMaterialBO()
                .getPackageMaterialType());

            logisticsOrders.add(logisticsOrder);
        } else {
            //转运订单内容体
            eventHeader.setEventType(EventType.LOGISTICS_TRADE_PAID.value);
            for (StockoutOrderDetailBO item : stockoutOrderDetailBOs) {
                LogisticsOrder logisticsOrder = new LogisticsOrder();
                logisticsOrder.setPoNo(stockoutOrderBO.getId() + "M"
                                       + stockoutOrderBO.getIntrMailNo());
                logisticsOrder.setCarrierCode("");
                logisticsOrder.setMailNo(stockoutOrderBO.getIntrMailNo());
                logisticsOrder.setSenderDetail(sender);
                logisticsOrder.setReceiverDetail(receiver);
                logisticsOrder.setNeedCheck(LiuLianType.NEED_CHECK_NO.getValue());
                String itemsIncluded = "";
                for (StockoutOrderDetailBO packItem : stockoutOrderDetailBOs) {
                    if (stockoutOrderBO.getIntrMailNo().equals(stockoutOrderBO.getIntrMailNo())) {
                        if (itemsIncluded.length() > 0) {
                            itemsIncluded = itemsIncluded + "," + packItem.getSkuId();
                        } else {
                            itemsIncluded = packItem.getSkuId() + "";
                        }
                    }
                }
                logisticsOrder.setItemsIncluded(itemsIncluded);
                logisticsOrders.add(logisticsOrder);
            }

        }
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

}
