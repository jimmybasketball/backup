package com.sfebiz.supplychain.provider.command.send.wms.oms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutBody;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutOrderAddedService;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutOrderCarrier;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutOrderCustomsDeclarationInfo;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutOrderInvoice;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutOrderItem;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutOrderReceiverInfo;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutOrderSaleOrder;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutOrderSaleOrderRequest;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutOrderSenderInfo;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutRequest;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutRequestHead;
import com.sfebiz.supplychain.protocol.wms.oms.stockoutorder.StockoutResponse;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCreateCommand;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.MD5Util;
import com.sfebiz.supplychain.util.XMLUtil;

/**
 * <p>
 * LSCM下发出库单，不支持组合商品:除生鲜仓以外的LSCM仓
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a> Date: 15/1/21 Time:
 * 下午11:18
 */
public class OmsCreateNotSupportAssembleCommand extends WmsOrderCreateCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");

    /**
     * 出款单管理服务
     */
    private StockoutOrderManager           stockoutOrderManager;

    /**
     * 路由服务
     */
    private RouteService                   routeService;

    @Override
    public boolean execute() {
        try {

            if (stockoutOrderBO.getRecordBO().getLogisticsState().intValue() == LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS
                .getValue()) {
                return true;
            }

            stockoutOrderManager = (StockoutOrderManager) CommandConfig
                .getSpringBean("stockoutOrderManager");
            routeService = (RouteService) CommandConfig.getSpringBean("routeService");

            boolean isMockAutoCreated = MockConfig.isMocked("sf-oms", "createCommand");
            if (isMockAutoCreated) {
                // 直接返回仓库已发货
                logger.info("[MOCK]顺丰oms物流下单 采用MOCK实现，订单id：" + stockoutOrderBO.getBizId());
                mockWarehouseStockoutCreateSuccess();
                return true;
            }

            Map<String, Object> meta = JSONUtil.parseJSONMessage(logisticsLineBO.getWarehouseBO()
                .getLogisticsProviderBO().getInterfaceMeta().get("meta"));
            if (!checkLogisticsProviderMetaInfo(meta)) {
                return false;
            }

            StockoutRequest stockoutRequest = buildRequest(meta);

            return sendStockoutRequest2Wms(stockoutRequest);

        } catch (Exception e) {
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.ERROR)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                        SystemConstants.TRACE_APP)).setException(e).setMsg("海外物流商下发物流下单失败")
                .addParm("订单ID", stockoutOrderBO.getBizId()).log();
            this.setCreateFailureMessage(e.getMessage());
            return false;
        }
    }

    /**
     * 构造请求参数
     *
     * @return 请求对象
     */
    public StockoutRequest buildRequest(Map<String, Object> metaData) {
        StockoutOrderSaleOrder stockoutOrderSaleOrder = new StockoutOrderSaleOrder();
        // 仓库编码有顺丰提供
        stockoutOrderSaleOrder.setWarehouseCode(metaData.get("warehouse_code").toString());
        stockoutOrderSaleOrder.setSfOrderType("销售订单");
        stockoutOrderSaleOrder.setErpOrder(stockoutOrderBO.getBizId());
        // 和顺丰确认
        stockoutOrderSaleOrder.setShopName("丰趣海淘");
        stockoutOrderSaleOrder.setTradeOrder(stockoutOrderBO.getMerchantOrderNo());
        // 向顺丰确认
        stockoutOrderSaleOrder.setCompleteDelivery("Y");
        // 运单打印寄件方信息来源
        stockoutOrderSaleOrder.setFromFlag("10");
        // 待确认
        stockoutOrderSaleOrder.setIsAllowSplit("N");

        StockoutOrderCustomsDeclarationInfo stockoutOrderCustomsDeclarationInfo = new StockoutOrderCustomsDeclarationInfo();
        // CustomsType报关类型
        stockoutOrderCustomsDeclarationInfo.setCustomsType("");
        // 报关批次
        stockoutOrderCustomsDeclarationInfo.setCustomsBatch("");
        // stockoutOrderSaleOrder.setCustomsDeclarationInfo(stockoutOrderCustomsDeclarationInfo);

        List<StockoutOrderAddedService> stockoutOrderAddedServices = new ArrayList<StockoutOrderAddedService>();
        StockoutOrderAddedService stockoutOrderAddedService = new StockoutOrderAddedService();
        stockoutOrderAddedService.setServiceCode("");
        stockoutOrderAddedService.setAttr01("");
        stockoutOrderAddedServices.add(stockoutOrderAddedService);
        stockoutOrderSaleOrder.setOrderAddedServices(stockoutOrderAddedServices);

        StockoutOrderCarrier stockoutOrderCarrier = new StockoutOrderCarrier();
        // 与顺丰确认
        stockoutOrderCarrier.setCarrier("CP");
        // 与顺丰确认
        stockoutOrderCarrier.setCarrierProduct("SE0010");
        // 运单号
        stockoutOrderCarrier.setWaybillNo("");
        // 线路编号
        stockoutOrderCarrier.setRouteNumbers("");

        stockoutOrderCarrier.setPaymentOfCharge("寄付");
        // 月结账号 需配置由顺丰给出
        stockoutOrderCarrier.setMonthlyAccount(logisticsLineBO.getWarehouseBO()
            .getLogisticsProviderBO().getInterfaceMeta().get("custId"));
        // 需要配置是否需要回执单
        stockoutOrderCarrier.setReturnService("N");
        // 回迁单号
        stockoutOrderCarrier.setReturnTracking("");
        // 是否自提
        stockoutOrderCarrier.setIfSelfPickup("N");
        stockoutOrderSaleOrder.setOrderCarrier(stockoutOrderCarrier);

        StockoutOrderReceiverInfo stockoutOrderReceiverInfo = new StockoutOrderReceiverInfo();
        stockoutOrderReceiverInfo.setReceiverCompany("");
        stockoutOrderReceiverInfo.setReceiverName(stockoutOrderBO.getBuyerBO().getBuyerName());
        stockoutOrderReceiverInfo.setReceiverEmail("");
        stockoutOrderReceiverInfo
            .setReceiverZipCode(stockoutOrderBO.getBuyerBO().getBuyerZipcode());
        stockoutOrderReceiverInfo.setReceiverMobile(stockoutOrderBO.getBuyerBO()
            .getBuyerTelephone());
        stockoutOrderReceiverInfo
            .setReceiverPhone(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        stockoutOrderReceiverInfo
            .setReceiverCountry(stockoutOrderBO.getBuyerBO().getBuyerCountry());
        stockoutOrderReceiverInfo.setReceiverProvince(stockoutOrderBO.getBuyerBO()
            .getBuyerProvince());
        stockoutOrderReceiverInfo.setReceiverCity(stockoutOrderBO.getBuyerBO().getBuyerCity());
        stockoutOrderReceiverInfo.setReceiverArea(stockoutOrderBO.getBuyerBO().getBuyerRegion());
        stockoutOrderReceiverInfo
            .setReceiverAddress(stockoutOrderBO.getBuyerBO().getBuyerAddress());
        stockoutOrderReceiverInfo.setReceiverIdType("ID");
        stockoutOrderReceiverInfo.setReceiverIdCard(stockoutOrderBO.getDeclarePayerCertNo());
        stockoutOrderSaleOrder.setOrderReceiverInfo(stockoutOrderReceiverInfo);

        StockoutOrderSenderInfo stockoutOrderSenderInfo = new StockoutOrderSenderInfo();

        stockoutOrderSenderInfo.setSenderCompany("丰趣海淘");
        stockoutOrderSenderInfo.setSenderName(stockoutOrderBO.getMerchantPackageMaterialBO()
            .getSenderName());
        stockoutOrderSenderInfo.setSenderEmail("");
        stockoutOrderSenderInfo.setSenderZipCode(logisticsLineBO.getWarehouseBO().getSenderBO()
            .getSenderZipcode());
        stockoutOrderSenderInfo.setSenderMobile(stockoutOrderBO.getMerchantPackageMaterialBO()
            .getSenderTelephone());
        stockoutOrderSenderInfo.setSenderPhone(logisticsLineBO.getWarehouseBO().getSenderBO()
            .getSenderTelephone());
        stockoutOrderSenderInfo.setSenderCountry(logisticsLineBO.getWarehouseBO().getSenderBO()
            .getSenderCountry());
        stockoutOrderSenderInfo.setSenderProvince(logisticsLineBO.getWarehouseBO().getSenderBO()
            .getSenderProvince());
        stockoutOrderSenderInfo.setSenderCity(logisticsLineBO.getWarehouseBO().getSenderBO()
            .getSenderCity());
        stockoutOrderSenderInfo.setSenderArea(logisticsLineBO.getWarehouseBO().getSenderBO()
            .getSenderDistrict());
        stockoutOrderSenderInfo.setSenderAddress(logisticsLineBO.getWarehouseBO().getSenderBO()
            .getSenderAddress());
        stockoutOrderSaleOrder.setOrderSenderInfo(stockoutOrderSenderInfo);

        StockoutOrderInvoice stockoutOrderInvoice = new StockoutOrderInvoice();
        // 发票类型
        stockoutOrderInvoice.setInvoiceType("类型1");
        // 发票号
        stockoutOrderInvoice.setInvoiceNo("TC001");
        // stockoutOrderSaleOrder.setOrderInvoice(stockoutOrderInvoice);

        // 设置商品信息
        List<StockoutOrderItem> stockoutOrderItems = new ArrayList<StockoutOrderItem>();
        // 不支持组合商品，故先移除组合商品
        boolean isSupportBatch = logisticsLineBO.getWarehouseBO().getIsSupportBatch();
        List<StockoutOrderDetailBO> stockoutOrderDetailBOList = CommonUtil.mergeStockoutOrderSku(
            stockoutOrderDetailBOs, isSupportBatch);

        for (StockoutOrderDetailBO stockoutOrderDetailBO : stockoutOrderDetailBOList) {

            StockoutOrderItem stockoutOrderItem1 = new StockoutOrderItem();
            stockoutOrderItem1.setSkuNo(stockoutOrderDetailBO.getSkuId().toString());
            stockoutOrderItem1.setItemName(stockoutOrderDetailBO.getSkuName());
            stockoutOrderItem1.setItemUom(stockoutOrderDetailBO.getSkuMerchantBO().getSkuBO()
                .getMeasuringUnit());
            stockoutOrderItem1.setItemQuantity(stockoutOrderDetailBO.getQuantity().toString());
            stockoutOrderItem1.setItemPrice("");
            stockoutOrderItem1.setItemDiscount("");
            stockoutOrderItem1.setItemBrand(stockoutOrderDetailBO.getBrandName());
            stockoutOrderItem1.setItemSpecifications("");
            stockoutOrderItem1.setItemSpecifications(stockoutOrderDetailBO.getSkuMerchantBO()
                .getSkuBO().getAttributesDesc());
            // 是否为组合商品
            stockoutOrderItem1.setBomAction("N");
            stockoutOrderItem1.setIsPresent("N");
            stockoutOrderItem1.setIsVirtualProduct("N");
            stockoutOrderItem1.setInventoryStatus("正品");
            stockoutOrderItems.add(stockoutOrderItem1);
        }
        stockoutOrderSaleOrder.setOrderItems(stockoutOrderItems);

        StockoutOrderSaleOrderRequest stockoutOrderSaleOrderRequest = new StockoutOrderSaleOrderRequest();
        // 需要与顺丰确认公司编码
        stockoutOrderSaleOrderRequest.setCompanyCode(metaData.get("code").toString());
        List<StockoutOrderSaleOrder> stockoutOrderSaleOrders = new ArrayList<StockoutOrderSaleOrder>();
        stockoutOrderSaleOrders.add(stockoutOrderSaleOrder);
        stockoutOrderSaleOrderRequest.setStockoutOrderSaleOrders(stockoutOrderSaleOrders);

        StockoutBody stockoutBody = new StockoutBody();
        stockoutBody.setSaleOrderRequest(stockoutOrderSaleOrderRequest);

        StockoutRequestHead stockoutRequestHead = new StockoutRequestHead();
        // 与顺丰确认accessCode 做成可配置
        stockoutRequestHead.setAccessCode(metaData.get("accesscode").toString());
        // 与顺丰确认CheckCode 做成可配置
        stockoutRequestHead.setCheckword(metaData.get("checkword").toString());

        StockoutRequest stockoutRequest = new StockoutRequest();
        stockoutRequest.setService("SALE_ORDER_SERVICE");
        stockoutRequest.setLang("zh-CN");
        stockoutRequest.setHead(stockoutRequestHead);
        stockoutRequest.setBody(stockoutBody);
        return stockoutRequest;
    }

    /**
     * 校验Meta信息的完整性
     *
     * @param meta
     * @return
     */
    private boolean checkLogisticsProviderMetaInfo(Map<String, Object> meta) {
        if (meta == null || meta.size() == 0) {
            return false;
        }
        if (!meta.containsKey("code")) {
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.ERROR)
                .setMsg("供应商接入码为空")
                .setParms(
                    logisticsLineBO.getWarehouseBO().getLogisticsProviderBO()
                        .getLogisticsProviderNid()).log();
            writeCreateCommandFailureLog("创建出库单失败", "供应商接入码为空");
            this.setCreateFailureMessage("供应商接入码为空");
            return false;
        }
        return true;
    }

    /**
     * 发送请求
     *
     * @param stockoutRequest
     * @return
     */

    private boolean sendStockoutRequest2Wms(StockoutRequest stockoutRequest) {

        String interfaceUrl = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO()
            .getInterfaceMeta().get("interfaceUrl");
        String interfaceKey = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO()
            .getInterfaceMeta().get("interfaceKey");

        String requestStr = null;
        String responseStr = null;

        try {
            String request2XmlFormat = null;
            try {
                request2XmlFormat = XMLUtil.convertToXml(stockoutRequest);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            // 计算签名
            String strDigest = MD5Util.md5EncodeToBase64(request2XmlFormat + interfaceKey);

            // 组装请求参数
            StringBuilder sb = new StringBuilder();
            sb.append("&logistics_interface=").append(request2XmlFormat);
            sb.append("&data_digest=").append(strDigest);
            requestStr = sb.toString();
            //采用okhttp发送报文
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(
                MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"), requestStr);

            okhttp3.Request request = new okhttp3.Request.Builder().url(interfaceUrl).post(body)
                .build();

            Response response = client.newCall(request).execute();

            StockoutResponse stockoutResponse = null;
            if (response == null || !response.isSuccessful()) {
                LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("创建出库单失败")
                    .setParms(request2XmlFormat).setParms(requestStr)
                    .setParms(response.body().string()).log();
                writeCreateCommandFailureLog("给顺丰oms下单失败", "请求无响应");
                this.setCreateFailureMessage("请求无响应；");
                return false;
            } else {
                try {
                    responseStr = response.body().string();
                    //把okhttp的response body转换成stockoutResponse
                    stockoutResponse = XMLUtil.converyToJavaBean(responseStr,
                        StockoutResponse.class);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                StockoutOrderManager stockoutOrderManager = (StockoutOrderManager) CommandConfig
                    .getSpringBean("stockoutOrderManager");

                StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig
                    .getSpringBean("stockoutOrderRecordManager");

                logger.info("打印stockoutResponse" + stockoutResponse.toString());
                logger.info("打印getResponseBody" + stockoutResponse.getResponseBody().toString());
                logger.info("打印getSaleOrderResponse"
                            + stockoutResponse.getResponseBody().getSaleOrderResponse());
                logger.info("打印getSaleOrderList"
                            + stockoutResponse.getResponseBody().getSaleOrderResponse()
                                .getSaleOrderList());
                logger.info("打印get(0)"
                            + stockoutResponse.getResponseBody().getSaleOrderResponse()
                                .getSaleOrderList().get(0));
                logger.info("打印getResult"
                            + stockoutResponse.getResponseBody().getSaleOrderResponse()
                                .getSaleOrderList().get(0).getResult());
                String result = stockoutResponse.getResponseBody().getSaleOrderResponse()
                    .getSaleOrderList().get(0).getResult();

                if ("1".equalsIgnoreCase(result)) {

                    // oms返回1代表创建出库单成功
                    LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("创建出库单成功")
                        .setParms(responseStr).log();
                    traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain",
                        new Date(), TraceLog.TraceLevel.INFO, "[供应链报文状态]下发出库单给顺丰oms：物流下单成功"));
                    this.stockoutOrderBO.getRecordBO().setLogisticsState(
                        LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                    stockoutOrderRecordManager.updateLogisticsState(this.stockoutOrderBO.getId(),
                        LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                    //                    stockoutOrderManager.updateDeclareNo(
                    //                            this.stockoutOrderBO.getId(),
                    //                            this.stockoutOrderBO.getDeclareNo());
                    writeCreateCommandSuccessLog();
                    return true;

                } else if ("2".equalsIgnoreCase(result) && responseStr.contains("订单号已存在")) {
                    // oms返回2代表订单号已存在返回成功，其他都是失败
                    LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("创建出库单成功")
                        .setParms(responseStr).log();
                    traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain",
                        new Date(), TraceLog.TraceLevel.INFO, "[供应链报文状态]下发出库单给顺丰oms：物流下单成功"));
                    this.stockoutOrderBO.getRecordBO().setLogisticsState(
                        LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                    stockoutOrderRecordManager.updateLogisticsState(this.stockoutOrderBO.getId(),
                        LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                    //                    stockoutOrderManager.updateDeclareNo(
                    //                            this.stockoutOrderBO.getId(),
                    //                            this.stockoutOrderBO.getDeclareNo());
                    writeCreateCommandSuccessLog();
                    return true;

                } else {
                    traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain",
                        new Date(), TraceLog.TraceLevel.INFO, "[供应链报文状态]下发出库单给顺丰oms：物流下单失败"));
                    LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("创建出库单失败")
                        .setParms(responseStr).log();
                    writeCreateCommandFailureLog("创建出库单失败", responseStr);
                    this.setCreateFailureMessage("创建出库单失败，请求顺丰oms结果：" + responseStr);
                    return false;
                }
            }
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setErrorMsg("顺丰oms下发出库单异常")
                .setException(e).addParm("接口请求", requestStr).addParm("接口返回", responseStr)
                .addParm("异常原因", e.getMessage()).log();
            this.setCreateFailureMessage(e.getMessage());
            return false;
        }
    }

    /**
     * 记录下发出库失败日志
     *
     * @param errMsg
     */
    private void writeCreateCommandFailureLog(String subMsg, String errMsg) {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), subMsg + errMsg,
            SystemConstants.WARN_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

    /**
     * 记录成功路由信息 `
     */
    private void writeCreateCommandSuccessLog() {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "给顺丰oms下发物流订单成功",
            SystemConstants.INFO_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

}
