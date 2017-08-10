package com.sfebiz.supplychain.provider.command.send.wms.nbbs.xhtd;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.protocol.nbport.*;
import com.sfebiz.supplychain.protocol.nbport.response.OrderResponse;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCreateCommand;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.*;
import net.pocrd.entity.ServiceException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Description: 用于查询宁波保税(鑫海通达)   EDI创建订单接口
 * Created by yanghua on 2017/3/21.
 */
public class NBBSXHTDOrderCreateCommand extends WmsOrderCreateCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private static final String EXIST = "S003";
    private static final String IFUNQ_PRODUCT_OWNER_NAME = "FQ";
    private static final String GXS_PRODUCT_OWNER_NAME = "GXS";
    private RouteService routeService;

    public static String MD5AndBase64(String str) throws Exception {
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        messagedigest.update((str).getBytes("UTF-8"));
        byte[] abyte0 = messagedigest.digest();
        String data_digest = new String(Base64.encodeBase64(abyte0));
        return data_digest.toUpperCase();
    }

    @Override
    public boolean execute() throws ServiceException {
        String importOrder2XmlFormat = null;
        try {
            boolean isMockAutoCreated = MockConfig.isMocked("NBXHTD", "createCommand");
            if (isMockAutoCreated) {
                logger.info("MOCK：宁波鑫海通达订单下发 采用MOCK实现");
                return mockWarehouseStockoutCreateSuccess();
            }

            OrderRequest orderRequest = buildOrderMessage();
            importOrder2XmlFormat = XMLUtil.convertToXml(orderRequest);
            if (importOrder2XmlFormat == null) {
                return false;
            }
            Boolean result = sendMessage2ImportOrder(importOrder2XmlFormat);
            if (result) {
                StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
                stockoutOrderBO.getRecordBO().setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                writeCreateCommandSuccessLog();
            }

            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setMsg("[供应链-宁波鑫海通达订单下发异常],订单号" + stockoutOrderBO.getBizId())
                    .addParm("报文信息", importOrder2XmlFormat)
                    .addParm("异常信息", e.getMessage())
                    .setException(e)
                    .log();
            return false;
        }
    }

    private void writeCreateCommandSuccessLog() {
        routeService = (RouteService) CommandConfig.getSpringBean("routeService");
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), " 宁波新海通达仓订单(出库单)下发创建成功", SystemConstants.INFO_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

    /**
     * 构建订单消息对象
     */
    private OrderRequest buildOrderMessage() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        OrderHead orderHead = buildOrderHead();
        OrderBody orderBody = new OrderBody();
        Order order = buildOrder();
        orderBody.setOrder(order);

        orderRequest.setOrderBody(orderBody);
        orderRequest.setOrderHead(orderHead);

        return orderRequest;
    }

    /**
     * 构建订单的基本信息
     */
    private OrderHead buildOrderHead() {
        Map<String, Object> wmsMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("meta"));

        LogBetter.instance(logger)
                .addParm("wmsMeta", wmsMeta).log();
        OrderHead orderHead = new OrderHead();
        //modified by yanghua 2017-05-25 customer字段变更，根据货主判断推送店铺代码 begin
        String productOwner = stockoutOrderBO.getMerchantAccountId();
        if (IFUNQ_PRODUCT_OWNER_NAME.equals(productOwner)) {
            orderHead.setBusCode(wmsMeta.get("customer").toString());
        } else if (GXS_PRODUCT_OWNER_NAME.equals(productOwner)) {
            orderHead.setBusCode(wmsMeta.get("customer_gxs").toString());
        } else {
            orderHead.setBusCode(wmsMeta.get("customer").toString());
        }
        //modified by yanghua 2017-05-25 customer字段变更，根据货主判断推送店铺代码 end

        orderHead.setHgCode(wmsMeta.get("hgCode").toString());
        orderHead.setMethodType("create"); //新增
        return orderHead;
    }

    /**
     * 构建商品详情信息
     */
    private Order buildOrder() throws Exception {
        Order order = new Order();

        Map<String, Object> wmsmeta = JSONUtil.parseJSONMessage(logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("meta"));

        //通过BizType 和 货主查询对应的orderShop
        String orderShop = stockoutOrderBO.getMerchantPackageMaterialBO().getNbShopNumber();
        order.setOrderShop(orderShop);

        String customsCode = null;
        if (wmsmeta.containsKey("platform_eport_code")) {
            customsCode = wmsmeta.get("platform_eport_code").toString();
        }

        if (StringUtils.isEmpty(customsCode)) {
            throw new Exception("申报海关编码错误。");
        }

        order.setHgArea(customsCode);
        order.setOrderFrom(wmsmeta.get("order_source").toString());
        order.setPackageFlag("0");
        order.setBusOrderNo(stockoutOrderBO.getBizId());
        order.setPostFee(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getFreightFee()));
        order.setAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getOrderActualPrice()));
        order.setBuyerAccount(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        order.setPhone(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        order.setTaxAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getTaxFee()));
        order.setTariffAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getTariffFee()));
        order.setAddedValueTaxAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getAddedValueTax()));
        order.setConsumptionDutyAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getConsumptionDutyTax()));
        order.setDisAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getDiscountTotalPrice()));
        order.setDealDate(DateUtil.dateToString(stockoutOrderBO.getRecordBO().getStockoutCmdsSuccessSendTime(), DateUtil.DATETIME_FORMAT));

        OrderGoods orderGoods = buildOrderGoods(order);
        OrderPay orderPay = buildOrderPay();
        OrderLogistics orderLogistics = buildOrderLogistics();

        order.setOrderGoods(orderGoods);
        order.setOrderPay(orderPay);
        order.setOrderLogistics(orderLogistics);

        return order;

    }

    private Boolean sendMessage2ImportOrder(String request) throws Exception {

        Map<String, Object> wmsMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("meta"));
        String date = DateUtil.getCurrentDate(DateUtil.DATETIME_FORMAT);
        StringBuffer buf = new StringBuffer();
        buf.append("method=").append("order.create&");
        buf.append("serialno=").append(stockoutOrderBO.getBizId()).append("&");
        buf.append("datetime=").append(date).append("&");
        buf.append("msgtype=xml&");
        buf.append("customer=").append(wmsMeta.get("customer")).append("&");
        buf.append("version=0.0.1&");
        buf.append("sign=").append(URLEncoder.encode(MD5AndBase64(request + wmsMeta.get("secret_key") + date), "utf-8")).append("&");
        buf.append("sign_method=md5&");
        buf.append("data=").append(URLEncoder.encode(request, "utf-8")).append("&");
        buf.append("appkey=").append(wmsMeta.get("app_key"));

        String responseString = HttpUtil.postFormByHttp(logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl"), buf.toString());
        //处理乱码
        responseString = new String(responseString.getBytes("ISO-8859-1"), "utf-8");

        OrderResponse orderResponse = XMLUtil.converyToJavaBean(responseString, OrderResponse.class);

        if (orderResponse.getResult().equalsIgnoreCase("SUCCESS")) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setMsg("[供应链报文-向EDI发送指令成功]订单号" + stockoutOrderBO.getBizId())
                    .addParm("请求报文", buf.toString())
                    .addParm("回复报文", responseString)
                    .log();
            return true;
        } else if (EXIST.equals(orderResponse.getCode()) && orderResponse.getMessage().contains("重复")) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setMsg("[供应链报文-向EDI发送指令成功]订单号" + stockoutOrderBO.getBizId())
                    .addParm("请求报文", buf.toString())
                    .addParm("回复报文", responseString)
                    .log();
            return true;
        } else {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setMsg("[供应链报文-向EDI发送指令失败]订单号" + stockoutOrderBO.getBizId())
                    .addParm("请求报文", buf.toString())
                    .addParm("回复报文", responseString)
                    .log();
            return false;
        }
    }

    private OrderGoods buildOrderGoods(Order order) {
        OrderGoods orderGoods = new OrderGoods();
        List<OrderGoodDetail> orderGoodDetails = new ArrayList<OrderGoodDetail>();

        List<StockoutOrderDetailBO> resultAfterMerge = CommonUtil.mergeStockoutOrderSku(stockoutOrderDetailBOs, Boolean.FALSE);

        BigDecimal grossWeight = new BigDecimal(0);
        for (StockoutOrderDetailBO item : resultAfterMerge) {

            Integer price = stockoutOrderDeclarePriceBO.getStockoutOrderSkuDeclareMap().get(item.getSkuId());
            OrderGoodDetail orderGoodDetail = new OrderGoodDetail();
            orderGoodDetail.setProductId(skuDeclareBOMap.get(item.getSkuId()).getProductId());
            orderGoodDetail.setAmount(NumberUtil.defaultParsePriceFeng2Yuan(new BigDecimal(price).multiply(new BigDecimal(item.getQuantity())).intValue()));
            orderGoodDetail.setPrice(NumberUtil.defaultParsePriceFeng2Yuan(price));
            orderGoodDetail.setQty(item.getQuantity().toString());
            if (skuDeclareBOMap.get(item.getSkuId()).getGrossWeight() == null) {
                LogBetter.instance(logger).addParm("申报重量未找到，默认为0", "").log();
                grossWeight.add(new BigDecimal(0));
            } else {
                grossWeight.add(new BigDecimal(skuDeclareBOMap.get(item.getSkuId()).getGrossWeight()));
            }
            orderGoodDetails.add(orderGoodDetail);
        }

        orderGoods.setOrderGoodDetailList(orderGoodDetails);
        order.setGrossWeight(grossWeight.toString());
        return orderGoods;
    }

    private OrderPay buildOrderPay() throws Exception {
        OrderPay orderPay = new OrderPay();
        orderPay.setPaytime(DateUtil.dateToString(stockoutOrderBO.getRecordBO().getStockoutCmdsSuccessSendTime(), DateUtil.DATETIME_FORMAT));
        orderPay.setPaymentNo(stockoutOrderBO.getDeclarePayNo());
        orderPay.setIdnum(stockoutOrderBO.getDeclarePayerCertNo());
        orderPay.setOrderSeqNo(stockoutOrderBO.getDeclarePayNo());
        orderPay.setName(stockoutOrderBO.getDeclarePayerName());
        //获取宁波仓动态配置
        if ("DECLARE_EMPTY".equals(stockoutOrderBO.getDeclarePayType())) {
            throw new ServiceException(LogisticsReturnCode.LOGISTICS_ORDER_PAYINFO_ERR, "支付方式缺失");
        } else {
            orderPay.setSource(PayConfig.getNBXHTDPayProviderNidByPayType(stockoutOrderBO.getDeclarePayType()));
        }
        return orderPay;
    }

    private OrderLogistics buildOrderLogistics() {
        OrderLogistics orderLogistics = new OrderLogistics();

        //如果没查到编号  使用<logisticsCode>YTO</logisticsCode> 默认圆通
        String carrierCode = LogisticsDynamicConfig.getLine().getRule("nb-logisticscode", stockoutOrderBO.getIntrCarrierCode());
        if (StringUtils.isBlank(carrierCode)) {
            orderLogistics.setLogisticsCode("YTO");
        } else {
            orderLogistics.setLogisticsCode(carrierCode);
        }

        orderLogistics.setConsignee(stockoutOrderBO.getBuyerBO().getBuyerName());
        orderLogistics.setProvince(stockoutOrderBO.getBuyerBO().getBuyerProvince());
        orderLogistics.setCity(stockoutOrderBO.getBuyerBO().getBuyerCity());
        orderLogistics.setDistrict(stockoutOrderBO.getBuyerBO().getBuyerRegion());
        //由于韵达按照详细地址识别大头笔,推送时全按照省市区+收货地址的形式推送
        orderLogistics.setConsigneeAddr(stockoutOrderBO.getBuyerBO().getBuyerProvince() + stockoutOrderBO.getBuyerBO().getBuyerCity() + stockoutOrderBO.getBuyerBO().getBuyerRegion() + stockoutOrderBO.getBuyerBO().getBuyerAddress());
        orderLogistics.setConsigneeTel(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        return orderLogistics;
    }

}
