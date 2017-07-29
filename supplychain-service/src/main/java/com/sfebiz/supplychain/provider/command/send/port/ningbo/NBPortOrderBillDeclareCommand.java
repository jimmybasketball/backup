package com.sfebiz.supplychain.provider.command.send.port.ningbo;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.common.enums.PortState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.protocol.common.DeclareType;
import com.sfebiz.supplychain.protocol.nbport.*;
import com.sfebiz.supplychain.protocol.nbport.response.OrderResponse;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.port.PortOrderBillDeclareCommand;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.*;
import net.pocrd.entity.ServiceException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 宁波保税仓海关订单申报
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * MDF USER: yanghua
 * Date: 16/3/10
 * Time: 上午11:19
 */
public class NBPortOrderBillDeclareCommand extends PortOrderBillDeclareCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    @Override
    public boolean execute() throws ServiceException {
        String importOrder2XmlFormat = null;
        try {
//            if (stockoutOrderDO.getPortState() == PortState.SUCCESS.getState()
//                    || stockoutOrderDO.getPortState() == PortState.NO.getState()) {
//                return true;
//            }
            boolean isMockAutoCreated = MockConfig.isMocked("NBBSXF", "createCommand");
            if (isMockAutoCreated) {
                logger.info("MOCK：宁波口岸申报订单 采用MOCK实现");
                return mockPortStockoutCreateSuccess();
            }

            OrderRequest orderRequest = buildOrderMessage();
            importOrder2XmlFormat = XMLUtil.convertToXml(orderRequest);
            if (importOrder2XmlFormat == null) {
                return false;
            }
            Boolean result = sendMessage2ImportOrder(importOrder2XmlFormat);
            if (result) {
                recordBO.setPortState(PortState.SUCCESS.getState());
                StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
                stockoutOrderRecordManager.updatePortState(stockoutOrderBO.getId(), PortState.SUCCESS.getState());
            }

            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .addParm("[供应链-口岸]订单口岸申报异常,订单ID", stockoutOrderBO.getBizId())
                    .addParm("报文信息", importOrder2XmlFormat)
                    .addParm("异常信息", e.getMessage())
                    .setException(e)
                    .log();
            return false;
        }
    }


    /**
     * 构建订单消息对象
     *
     * @return
     */
    private OrderRequest buildOrderMessage() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        OrderHead orderHead = buildOrderHead(declareType);
        OrderBody orderBody = new OrderBody();
        Order order = buildOrder();
        orderBody.setOrder(order);

        orderRequest.setOrderBody(orderBody);
        orderRequest.setOrderHead(orderHead);

        return orderRequest;
    }

    /**
     * 构建订单的基本信息
     *
     * @param declareType
     * @return
     */
    private OrderHead buildOrderHead(DeclareType declareType) {
        Map<String, Object> portMeta = JSONUtil.parseJSONMessage(lineBO.getPortBO().getMeta());
        OrderHead orderHead = new OrderHead();
        orderHead.setBusCode(portMeta.get("customr").toString());
        orderHead.setHgCode(portMeta.get("hgCode").toString());
        orderHead.setMethodType(declareType.getValue());
        return orderHead;
    }

    /**
     * 构建商品详情信息
     *
     * @return
     */
    private Order buildOrder() throws Exception {
        Order order = new Order();
        //取BizMeta  业务扩展配置
        Map<String, String> wmsmeta = lineBO.getWarehouseBO().getLogisticsProviderBO().getBizMeta();
        StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO = stockoutOrderBO.getDeclarePriceBO();

        order.setOrderShop(wmsmeta.get("order_shop"));
        String customsCode = null;
        if (wmsmeta.containsKey("platform_eport_code")) {
            customsCode = wmsmeta.get("platform_eport_code");
        }

        if (StringUtils.isEmpty(customsCode)) {
            throw new Exception("申报海关编码错误");
        }

        order.setHgArea(customsCode);
        order.setOrderFrom(wmsmeta.get("OrderFrom"));
        order.setPackageFlag("0");
        order.setBusOrderNo(stockoutOrderBO.getBizId());
        order.setPostFee(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getFreightFee(), null));
        order.setAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getOrderActualPrice(), null));
        order.setBuyerAccount(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        order.setPhone(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        order.setTaxAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getTaxFee(), null));
        order.setTariffAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getTariffFee(), null));
        order.setAddedValueTaxAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getAddedValueTax(), null));
        order.setConsumptionDutyAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getConsumptionDutyTax(), null));
        order.setDisAmount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getDiscountTotalPrice(), null));
        order.setDealDate(DateUtil.dateToString(stockoutOrderBO.getGmtCreate(), DateUtil.DEF_PATTERN));

        OrderGoods orderGoods = buildOrderGoods(order);
        OrderPay orderPay = buildOrderPay();
        OrderLogistics orderLogistics = buildOrderLogistics();

        order.setOrderGoods(orderGoods);
        order.setOrderPay(orderPay);
        order.setOrderLogistics(orderLogistics);

        return order;

    }


    private Boolean sendMessage2ImportOrder(String request) throws Exception {

        Map<String, Object> portMeta = JSONUtil.parseJSONMessage(lineBO.getPortBO().getMeta());
        String date = DateUtil.getCurrentDate(DateUtil.DEF_PATTERN);
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .addParm("宁波口岸订单申报请求报文", request)
                .log();
        StringBuffer buf = new StringBuffer();
        buf.append("method=").append("order.create&");
        buf.append("serialno=").append(stockoutOrderBO.getBizId()).append("&");
        buf.append("datetime=").append(date).append("&");
        buf.append("msgtype=xml&");
        buf.append("customer=").append(portMeta.get("customr")).append("&");
        buf.append("version=0.0.1&");
        buf.append("sign=").append(URLEncoder.encode(MD5AndBase64(request + portMeta.get("secretKey") + date), "utf-8")).append("&");
        buf.append("sign_method=md5&");
        buf.append("data=").append(URLEncoder.encode(request, "utf-8")).append("&");
        buf.append("appkey=").append(portMeta.get("appKey"));

        String responseString = HttpUtil.postFormByHttp(lineBO.getPortBO().getUrl(), buf.toString());

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                .addParm("[供应链报文-向EDI发送指令完成]订单ID", stockoutOrderBO.getBizId())
                .addParm("请求地址", lineBO.getPortBO().getUrl())
                .addParm("请求报文", buf.toString())
                .addParm("回复报文", responseString)
                .log();

        OrderResponse orderResponse = XMLUtil.converyToJavaBean(responseString, OrderResponse.class);

        if (orderResponse.getResult().equalsIgnoreCase("SUCCESS")) {
            return true;
        }

        return false;

    }


    private OrderGoods buildOrderGoods(Order order) {
        OrderGoods orderGoods = new OrderGoods();
        List<OrderGoodDetail> orderGoodDetails = new ArrayList<OrderGoodDetail>();

        List<StockoutOrderDetailBO> resultAfterMerge = CommonUtil.mergeStockoutOrderSku(stockoutOrderBO.getDetailBOs(), Boolean.FALSE);

        BigDecimal grossWeight = new BigDecimal(0);
        for (StockoutOrderDetailBO item : resultAfterMerge) {

            Integer price = declarePriceDetailMap.get(item.getSkuId()).getDeclarePrice();

            OrderGoodDetail orderGoodDetail = new OrderGoodDetail();
            orderGoodDetail.setProductId(skuDeclareMap.get(item.getSkuId()).getProductId());
            orderGoodDetail.setAmount(NumberUtil.defaultParsePriceFeng2Yuan(new BigDecimal(price).multiply(new BigDecimal(item.getQuantity())).intValue(), null));
            orderGoodDetail.setPrice(NumberUtil.defaultParsePriceFeng2Yuan(price, null));
            orderGoodDetail.setQty(item.getQuantity().toString());

            grossWeight.add(new BigDecimal(skuDeclareMap.get(item.getSkuId()).getGrossWeight()));
            orderGoodDetails.add(orderGoodDetail);

        }

        orderGoods.setOrderGoodDetailList(orderGoodDetails);
        order.setGrossWeight(grossWeight.toString());
        return orderGoods;
    }


    private OrderPay buildOrderPay() throws Exception {
        OrderPay orderPay = new OrderPay();
        orderPay.setPaytime(DateUtil.dateToString(stockoutOrderBO.getGmtCreate(), DateUtil.DEF_PATTERN));
        orderPay.setPaymentNo(stockoutOrderBO.getDeclarePayNo());
        orderPay.setIdnum(stockoutOrderBO.getDeclarePayerCertNo());
        orderPay.setOrderSeqNo(stockoutOrderBO.getDeclarePayNo());
        orderPay.setName(stockoutOrderBO.getDeclarePayerName());
        if (StringUtils.isBlank(stockoutOrderBO.getDeclarePayType()) || stockoutOrderBO.getDeclarePayType().equalsIgnoreCase(SystemConstants.DECLARE_EMPTY)) {
            throw new Exception("支付申报方式为空");
        } else if (stockoutOrderBO.getDeclarePayType().equalsIgnoreCase("TENPAY_INTL")) {
            orderPay.setSource("TENPY");
        } else if (stockoutOrderBO.getDeclarePayType().equalsIgnoreCase(SystemConstants.DECLARE_YIHUIJINPAY)) {
            orderPay.setSource("YBAO");
        } else {
            orderPay.setSource(stockoutOrderBO.getDeclarePayType());
        }
        return orderPay;
    }


    private OrderLogistics buildOrderLogistics() {
        OrderLogistics orderLogistics = new OrderLogistics();
        orderLogistics.setLogisticsCode(stockoutOrderBO.getIntrCarrierCode());
        orderLogistics.setConsignee(stockoutOrderBO.getBuyerBO().getBuyerName());
        orderLogistics.setProvince(stockoutOrderBO.getBuyerBO().getBuyerProvince());
        orderLogistics.setCity(stockoutOrderBO.getBuyerBO().getBuyerCity());
        orderLogistics.setDistrict(stockoutOrderBO.getBuyerBO().getBuyerRegion());
        orderLogistics.setConsigneeAddr(stockoutOrderBO.getBuyerBO().getBuyerAddress());
        orderLogistics.setConsigneeTel(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        return orderLogistics;
    }


    public static String MD5AndBase64(String str) throws Exception {
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        messagedigest.update((str).getBytes("UTF-8"));
        byte[] abyte0 = messagedigest.digest();
        String data_digest = new String(Base64.encodeBase64(abyte0));
        return data_digest.toUpperCase();
    }


}
