package com.sfebiz.supplychain.provider.command.send.wms.zebra;

import com.alibaba.fastjson.JSON;
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
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.common.PinyinUtil;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCreateCommand;
import com.sfebiz.supplychain.provider.entity.PriceUnit;
import com.sfebiz.supplychain.provider.entity.ResponseState;
import com.sfebiz.supplychain.provider.entity.ZebraConstants;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.XMLUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 下发物流订单消息指令 logistics.event.wms.create SF -> LP
 * 待发货:待出库:出库单已生成，等待下发给仓库
 * <p/>
 * LOGISTICS_SKU_PAID
 */
public class ZebraCreateCommand extends WmsOrderCreateCommand {
    private static final String serviceName = "OutboundOrder";
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private static final String HEIKE_TRATE_TYPE = "HeiKe";
    private static final String HAITAO_TRATE_TYPE = "HaiTao";
    private StockoutOrderRecordManager stockoutOrderRecordManager;
    private RouteService routeService;

    private static AtomicInteger blockCount = new AtomicInteger(0);//用于统计延迟线程数

    @Override
    public boolean execute() {
        logger.info("斑马 下发物流订单消息指令: start");
        try {
            if (stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue()
                    || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_GOODS_WEIGHT.getValue()
                    || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue()
                    || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_STOCKOUT.getValue()) {
                return true;
            }
            stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
            routeService = (RouteService) CommandConfig.getSpringBean("routeService");
            boolean isMockAutoCreated = MockConfig.isMocked("zebra", "createCommand");
            if (isMockAutoCreated) {
                //直接返回仓库已发货
                logger.info("MOCK：斑马仓库 物流下单 采用MOCK实现");
                return mockWarehouseStockoutCreateSuccess();
            }

            String msgType = WmsMessageType.STOCK_OUT.getValue();
            String url = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
            String key = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");

            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
                throw new Exception("路线配置错误" + logisticsLineBO.getLogisticsLineNid());
            }
            if (!url.endsWith("/")) {
                url = url.concat("/");
            }
            url = url.concat(serviceName);
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[海外物流商下发物流下单]")
                    .addParm("线路信息", logisticsLineBO).addParm("wmsProviderEntity", logisticsLineBO.getWarehouseBO().getLogisticsProviderBO())
                    .addParm("url信息", url).addParm("interfaceKey", key).log();
            LogisticsEventsRequest request = buildCreateCommandRequest();
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[海外物流商下发物流下单]")
                    .addParm("出库单信息", stockoutOrderBO).log();

            //下发指令
            LogisticsEventsResponse responses = ProviderBizService.getInstance().send(request, msgType, url, key,
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP), ZebraConstants.PARTNER_CODE);

            if (responses.getResponseItems() == null || responses.getResponseItems().size() == 0) {
                /** 2017-03-24 出库单推送异常优化 by liujunchi  **/
                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[斑马物流下单] 响应报文为空，执行出库单确认请求，延时2分钟 当前阻塞线程数：" + blockCount.incrementAndGet())
                        .log();

                TimeUnit.SECONDS.sleep(120);
                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[斑马物流下单] 响应报文为空，执行出库单确认请求，延时结束 当前阻塞线程数：" + blockCount.decrementAndGet())
                        .addParm("请求报文", XMLUtil.convertToXml(request))
                        .addParm("响应报文", JSONUtil.toJson(responses))
                        .log();

                //执行确认请求
                url = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
                if (!url.endsWith("/")) {
                    url = url.concat("/");
                }
                url = url.concat("logisticsOrderConfirm");
                logger.info("斑马出库单确认请求开始————");
                LogisticsEventsResponse confirmResponses = ProviderBizService.getInstance().send(request, "LOGISTICS_ORDER_CONFIRM", url, key,
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP), ZebraConstants.PARTNER_CODE);
                logger.info("斑马出库单确认请求完成————");
                if (confirmResponses.getResponseItems() == null || confirmResponses.getResponseItems().size() == 0) {
                    LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                            .setMsg("[斑马物流下单] 响应报文为空，执行出库单确认请求 响应依然为空")
                            .addParm("请求报文", XMLUtil.convertToXml(request))
                            .addParm("响应报文",JSONUtil.toJson(responses))
                            .log();
                    writeCreateCommandFailureLog("斑马物流下单]  出库单确认请求 响应报文依然为空");
                    this.setCreateFailureMessage("[斑马物流下单]  出库单确认请求 响应报文依然为空");
                    throw new Exception("[斑马物流下单]  出库单确认请求 响应报文依然为空");
                }else {
                    responses = confirmResponses;
                }
            }

            Response response = responses.getResponseItems().get(0);
            if (ResponseState.TRUE.getCode().equalsIgnoreCase(response.success)) {
                stockoutOrderBO.getRecordBO().setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                writeCreateCommandSuccessLog();
                return true;
            } else if (ResponseState.ORDER_EXIST.getCode().equalsIgnoreCase(response.getReason())) {
                stockoutOrderBO.getRecordBO().setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                writeCreateCommandSuccessLog();
                return true;
            } else if (ResponseState.NET_TIMEOUT.getCode().equalsIgnoreCase(response.getReason()) || "S07".equals(response.getReason())) {
                LogBetter.instance(logger).setLevel(LogLevel.WARN)
                        .setMsg("系统下发发货指令失败等待重试")
                        .setParms(stockoutOrderBO.getBizId())
                        .setParms(response).log();
                writeCreateCommandFailureLog("网络超时");
                this.setCreateFailureMessage("网络超时");
                return false;
            } else {
                stockoutOrderBO.getRecordBO().setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_ERROR.getValue());
                stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_ERROR.getValue());
                writeCreateCommandFailureLog(responses.getResponseItems().get(0).getReasonDesc());
                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链-海外物流商下发物流下单失败]")
                        .setParms(stockoutOrderBO.getBizId())
                        .setParms(response)
                        .log();
                this.setCreateFailureMessage(responses.getResponseItems().get(0).getReasonDesc());
            }
        } catch (Exception e) {
            writeCreateCommandFailureLog(e.getMessage());
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setMsg("海外物流商下发物流下单失败")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .log();
            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLevel.ERROR,
                    "[供应链报文-向斑马仓库下发指令异常]: "
                            + "[订单ID:" + stockoutOrderBO.getBizId()
                            + ", 异常信息: " + e.getMessage()
                            + "]"
            ));
            this.setCreateFailureMessage(e.getMessage());
            return false;
        } finally {
            logger.info("斑马 下发物流订单消息指令:end");
        }
        return false;
    }

    /**
     * 构建出库指令请求
     * COE区分 自营发货&转运发货
     *
     * @return
     */
    public LogisticsEventsRequest buildCreateCommandRequest() throws ServiceException {
        String meta = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("meta");
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
        LogisticsClearanceDetailEntity clearanceDetailEntity = CommonUtil.buildClearanceDetailEntity(logisticsLineBO, stockoutOrderBO);
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

        if (stockoutOrderBO.getOrderType().equals(StockoutOrderType.SALES_STOCK_OUT.value) && logisticsLineBO.domesticLogisticsProviderBO != null && StringUtils.isBlank(clearanceDetail.deliveryCode)) {
            throw new IllegalArgumentException("目的地代码不能为空");
        }

        //买家信息
        Buyer buyer = new Buyer();
        String userName = stockoutOrderBO.getBuyerBO().getBuyerName();

        String userIdNo = stockoutOrderBO.getBuyerBO().getBuyerCertNo();
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
        attachments.setAttachment2(stockoutOrderBO.getBuyerBO().getBuyerIdCardFrontPhotoUrl());
        buyer.setAttachments(attachments);

        int orderValue = 0;
        int weight = 0;
        List<Item> items = new ArrayList<Item>();
        StringBuilder itemIds = new StringBuilder();
        boolean isSelfType = true;
        boolean isSupportBatch = false;
        if (logisticsLineBO.getWarehouseBO().getIsSupportBatch() > 0) {
            isSupportBatch = true;
        }
        List<StockoutOrderDetailBO> skuDOListForRequest = CommonUtil.mergeStockoutOrderSku(stockoutOrderDetailBOs, isSupportBatch);
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
            buyerEn.setCountry(PinyinUtil.convertToPinyin(stockoutOrderBO.getBuyerBO().getBuyerCountry()));
            buyerEn.setProvince(PinyinUtil.convertToPinyin(stockoutOrderBO.getBuyerBO().getBuyerProvince()));
            buyerEn.setCity(PinyinUtil.convertToPinyin(stockoutOrderBO.getBuyerBO().getBuyerCity()));
            buyerEn.setDistrict(PinyinUtil.convertToPinyin(stockoutOrderBO.getBuyerBO().getBuyerRegion()));
            String addressEn = PinyinUtil.convertToPinyin(stockoutOrderBO.getBuyerBO().getBuyerAddress());
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
        paid.setTradeOrderValue(stockoutOrderDeclarePriceBO.getOrderActualPrice());
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
        tradeDetail.setTradeType(bizType2TradeType(stockoutOrderBO.getOrderSource()));
        tradeDetail.setTradeOrders(tradeOrders);

        ContactDetail sender = new ContactDetail();
        sender.setName(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderName());
        sender.setPhone(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderTelephone());// 获取包材配置的联系电话
        sender.setMobile(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderTelephone());// 获取包材配置的联系电话
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
            logisticsOrder.setStockoutOrderType(stockoutOrderBO.getOrderType().toString());
            logisticsOrder.setCarrierCode(eventBody.getClearanceDetail().getCarrierCode());
            logisticsOrder.setMailNo(stockoutOrderBO.getIntlMailNo());
            if (StringUtils.isBlank(logisticsLineBO.getRouteBizCode())) {
                throw new IllegalArgumentException("路线中RouteBizCode不能为空");
            }

            //默认管道编码从数据库中读取，如果diamond中有特殊配置则从diamond中读取配置
            logisticsOrder.setRouteId(logisticsLineBO.getRouteBizCode());

            //管道代码，跟仓绑定，不同的仓使用不同的管道代码, 还要区分不同的渠道类型
            String routeIdConf = stockoutOrderBO.getMerchantPackageMaterialBO().getRouteId();
            if (StringUtils.isNotEmpty(routeIdConf)) {
                Map<String, String> routeIdMap = JSON.parseObject(routeIdConf, Map.class);
                if (null != routeIdMap && routeIdMap.containsKey(stockoutOrderBO.getWarehouseId().toString())) {
                    logisticsOrder.setRouteId(routeIdMap.get(stockoutOrderBO.getWarehouseId().toString()));
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
                            foreignName = foreignName.substring(foreignName.length() - 24, foreignName.length());
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
            receiptDetail.getHeader().setLogo(stockoutOrderBO.getMerchantPackageMaterialBO().getHeaderLogo());
            receiptDetail.getHeader().setTitle(stockoutOrderBO.getMerchantPackageMaterialBO().getHeaderTitle());
            receiptDetail.getFooter().setAdvert(stockoutOrderBO.getMerchantPackageMaterialBO().getFooterAdvert());
            receiptDetail.getFooter().setDesc(stockoutOrderBO.getMerchantPackageMaterialBO().getFooterDesc());
            logisticsOrder.setReceiptDetail(receiptDetail);
            logisticsOrder.setPackingMaterials(stockoutOrderBO.getMerchantPackageMaterialBO().getPackageMaterialType());


            logisticsOrders.add(logisticsOrder);
        } else {
            //转运订单内容体
            eventHeader.setEventType(EventType.LOGISTICS_TRADE_PAID.value);
            for (StockoutOrderDetailBO item : stockoutOrderDetailBOs) {
                LogisticsOrder logisticsOrder = new LogisticsOrder();
                logisticsOrder.setPoNo(stockoutOrderBO.getId() + "M" + stockoutOrderBO.getIntrMailNo());
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
     */
    private void writeCreateCommandSuccessLog() {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "香港COE仓物流下单成功", SystemConstants.INFO_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

    private String bizType2TradeType(String bizType) {
        if (HEIKE_TRATE_TYPE.equalsIgnoreCase(bizType)) {
            return HEIKE_TRATE_TYPE;
        }
        return HAITAO_TRATE_TYPE;
    }
}