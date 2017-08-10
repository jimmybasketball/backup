package com.sfebiz.supplychain.provider.command.send.wms.pingtan;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.order.OrderConfig;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.exposed.stockout.enums.TplIntrState;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.ProductDeclareManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.ProductDeclarePtDirectmailManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.protocol.wms.ptwms.*;
import com.sfebiz.supplychain.protocol.wms.ptwms.createOrder.StockoutInfo;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCreateCommand;
import com.sfebiz.supplychain.provider.entity.ResponseState;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.XMLUtil;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 平潭保税仓,海外通接口(下发订单)
 */
public class PingTanOrderCreateCommand extends WmsOrderCreateCommand {
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private StockoutOrderManager stockoutOrderManager;
    private RouteService routeService;
    private ProductDeclareManager productDeclareManager;
    private ProductDeclarePtDirectmailManager productDeclarePtDirectmailManager;

    public boolean execute() {
        PTOrderInfo request = null;
        try {
            if (stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue()
                    || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_STOCKOUT.getValue()) {
                return true;
            }

            stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
            routeService = (RouteService) CommandConfig.getSpringBean("routeService");
            productDeclareManager = (ProductDeclareManager) CommandConfig.getSpringBean("productDeclareManager");
            productDeclarePtDirectmailManager = (ProductDeclarePtDirectmailManager) CommandConfig.getSpringBean("productDeclarePtDirectmailManager");
            request = buildRequest();
            return set2wms(request);

        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .addParm("平潭WMS下发失败", e)
                    .setException(e)
                    .log();
            return false;
        }
    }

    /**
     * 记录日常路由
     *
     * @param errMsg
     */
    private void writeCreateCommandFailureLog(String errMsg) {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "平潭保税仓物流下单失败," + errMsg, SystemConstants.WARN_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

    /**
     * 记录成功路由信息                   `
     */
    private void writeCreateCommandSuccessLog() {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "平潭保税仓物流下单成功", SystemConstants.INFO_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }


    /**
     * 添加消息体内容
     *
     * @return
     */
    public PTOrderInfo buildRequest() throws Exception {

        String meta = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("meta");
        Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
        String appKey = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
        String appToken = metaData.get("token").toString();
        String warehouseCode = metaData.get("warehouseCode").toString();
        String countryCode = metaData.get("countryCode").toString();

        PTOrderInfo ptOrderInfo = new PTOrderInfo();
        PTOrderBody ptOrderBody = new PTOrderBody();
        PTOrderRequest ptOrderRequest = new PTOrderRequest();
        StockoutInfo stockoutInfo = new StockoutInfo();
        List<SkuItem> skuItems = new ArrayList<SkuItem>();
        stockoutInfo.setReference_no(stockoutOrderBO.getBizId());
        stockoutInfo.setPlatform(stockoutOrderBO.getOrderSource());
        //收货人地址
        stockoutInfo.setCountry_code(stockoutOrderBO.getBuyerBO().getBuyerCountry());
        stockoutInfo.setProvince(OrderConfig.getOrderBuyerAddressMapping(stockoutOrderBO.getBuyerBO().getBuyerProvince()));
        stockoutInfo.setCity(OrderConfig.getOrderBuyerAddressMapping(stockoutOrderBO.getBuyerBO().getBuyerCity()));
        stockoutInfo.setRegion(OrderConfig.getOrderBuyerAddressMapping(stockoutOrderBO.getBuyerBO().getBuyerRegion()));
        stockoutInfo.setAddress1(OrderConfig.getOrderBuyerAddressMapping(stockoutOrderBO.getBuyerBO().getBuyerProvince())
                + OrderConfig.getOrderBuyerAddressMapping(stockoutOrderBO.getBuyerBO().getBuyerCity())
                + OrderConfig.getOrderBuyerAddressMapping(stockoutOrderBO.getBuyerBO().getBuyerRegion())
                + OrderConfig.getOrderBuyerAddressMapping(stockoutOrderBO.getBuyerBO().getBuyerAddress()));
        stockoutInfo.setZipcode(stockoutOrderBO.getBuyerBO().getBuyerZipcode());//邮编


        //收货人信息
        stockoutInfo.setName(stockoutOrderBO.getBuyerBO().getBuyerName());
        stockoutInfo.setPhone(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        stockoutInfo.setIdentityNo(stockoutOrderBO.getBuyerBO().getBuyerCertNo());

        stockoutInfo.setWarehouse_code(warehouseCode);
        stockoutInfo.setCountry_code(countryCode);
        stockoutInfo.setShipping_method(stockoutOrderBO.getIntrCarrierCode());
        stockoutInfo.setTransaction_no(stockoutOrderBO.getMerchantPayNo());
        stockoutInfo.setVerify(1);
        stockoutInfo.setForceVerify(1);

        List<StockoutOrderDetailBO> resultAfterMerge = CommonUtil.mergeStockoutOrderSku(stockoutOrderDetailBOs, Boolean.FALSE);

        //商品信息
        for (StockoutOrderDetailBO orderSkuDO : resultAfterMerge) {
            SkuItem skuItem = new SkuItem();
            ProductDeclareDO productDeclareDO = productDeclareManager.queryDeclaredSku(orderSkuDO.getSkuId(), PortNid.PINGTAN.getValue());
            skuItem.setProduct_sku(productDeclarePtDirectmailManager.getByProductDeclareId(productDeclareDO.getId()).getProductCode());
            skuItem.setQuantity(orderSkuDO.getQuantity());
            skuItems.add(skuItem);
        }
        stockoutInfo.setItems(skuItems);
        String stockInfoJson = JSONObject.fromObject(stockoutInfo).toString();

        ptOrderRequest.setService("createOrder");
        ptOrderRequest.setParamsJson(stockInfoJson);
        ptOrderRequest.setAppToken(appToken);
        ptOrderRequest.setAppKey(appKey);

        ptOrderBody.setRequest(ptOrderRequest);
        ptOrderInfo.setBody(ptOrderBody);

        return ptOrderInfo;
    }

    private boolean set2wms(PTOrderInfo ptOrderInfo) {
        String url = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
        String request2XmlFormat = null;
        try {
            request2XmlFormat = XMLUtil.convertToXml(ptOrderInfo);
            String responseString = PTClient.send(url, request2XmlFormat, TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP));
            PTStockoutResponse stockoutResponse = XMLUtil.converyToJavaBean(responseString, PTStockoutResponse.class);
            StockoutOrderManager stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
            StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("StockoutOrderRecordManager");
            if (stockoutResponse == null) {
                LogBetter.instance(logger).setLevel(LogLevel.WARN)
                        .setMsg("创建出库单失败").setParms(request2XmlFormat)
                        .setParms(responseString).log();
                writeCreateCommandFailureLog("请求无响应");
                this.setCreateFailureMessage("请求无响应；");
                return false;
            }
            String responseJson = stockoutResponse.getBody().getResponseBody().getResponse();
            PTOrderResponse ptOrderResponse = JSONUtil.parseJSONMessage(responseJson, PTOrderResponse.class);
            // 下单成功
            if (ResponseState.SUCCESS.getCode().equalsIgnoreCase(ptOrderResponse.getAsk())) {
                stockoutOrderBO.getRecordBO().setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                stockoutOrderBO.setBizId(ptOrderResponse.getOrder_code());
                stockoutOrderBO.getRecordBO().setTplIntrState(TplIntrState.CONFIRM_SUCC.getValue());
                StockoutOrderDO stockoutOrderDO = new StockoutOrderDO();
                stockoutOrderDO.setBizId(ptOrderResponse.getOrder_code());
                stockoutOrderManager.update(stockoutOrderDO);
                stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                stockoutOrderRecordManager.updateTplIntrState(stockoutOrderBO.getId(), TplIntrState.CONFIRM_SUCC.getValue());
                writeCreateCommandSuccessLog();
                return true;
            }
        } catch (Exception e1) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .addParm("平潭WMS下发失败", e1)
                    .setException(e1)
                    .log();
        }
        return false;
    }
}