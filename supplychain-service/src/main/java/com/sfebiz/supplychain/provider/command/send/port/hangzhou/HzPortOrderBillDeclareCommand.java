package com.sfebiz.supplychain.provider.command.send.port.hangzhou;

import cn.gov.zjport.newyork.ws.CheckReceived;
import cn.gov.zjport.newyork.ws.CheckReceivedResponse;
import cn.gov.zjport.newyork.ws.bo.*;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.config.port.PortConfig;
import com.sfebiz.supplychain.exposed.common.enums.PortState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.port.PortOrderBillDeclareCommand;
import com.sfebiz.supplychain.provider.entity.PortParamType;
import com.sfebiz.supplychain.service.port.PortUtil;
import com.sfebiz.supplychain.service.port.biz.PortBizService;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.NumberUtil;
import com.sfebiz.supplychain.util.XMLUtil;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HzPortOrderBillDeclareCommand extends PortOrderBillDeclareCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private StockoutOrderRecordManager stockoutOrderRecordManager;


    @Override
    public boolean execute() {
        try {
            if (recordBO.getPortState() == PortState.SUCCESS.getState()
                    || recordBO.getPortState() == PortState.NO.getState()) {
                return true;
            }

            boolean isMockAutoCreated = MockConfig.isMocked("hzport", "createCommand");
            if (isMockAutoCreated) {
                //直接返回仓库已发货
                logger.info("MOCK：杭州口岸申报 采用MOCK实现");
                return mockPortStockoutCreateSuccess();
            }
            stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
            Request request = buildImportOrder();
            String importOrder2XmlFormat = XMLUtil.convertToXml(request);
            if (importOrder2XmlFormat == null) {
                return false;
            }
            boolean result = sendMessage2ImportOrder(importOrder2XmlFormat);
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .addParm("[供应链-口岸]订单口岸申报异常,订单ID", stockoutOrderBO.getBizId())
                    .addParm("异常信息", e.getMessage())
                    .setException(e)
                    .log();
            return false;
        }
    }

    /**
     * 给杭州电子口岸发送订单申报信息
     *
     * @param importOrderInfo
     * @return
     */
    private boolean sendMessage2ImportOrder(String importOrderInfo) {
        JAXBContext jaxb;
        try {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .addParm("杭州口岸订单申报请求报文", importOrderInfo)
                    .log();
            jaxb = JAXBContext.newInstance("cn.gov.zjport.newyork.ws");

            QName serviceName = new QName("http://impl.ws.newyork.zjport.gov.cn/", "ReceivedDeclareServiceImplService");
            QName portName = new QName("http://impl.ws.newyork.zjport.gov.cn/", "ReceivedDeclareServiceImplPort");

            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向杭州口岸下发指令]")
                    .addParm("线路信息", lineBO)
                    .addParm("口岸信息", lineBO.getPortBO())
                    .addParm("url", lineBO.getPortBO().getUrl())
                    .log();
            Service service = Service.create(new URL(lineBO.getPortBO().getUrl()), serviceName);
            Dispatch<Object> dispatch = service.createDispatch(portName, jaxb, Service.Mode.PAYLOAD);
            CheckReceived checkReceived = new CheckReceived();
            checkReceived.setXmlStr(importOrderInfo);
            checkReceived.setXmlType(HzPortBusinessType.IMPORTORDER.getType());
            checkReceived.setSourceType("1");
            JAXBElement<CheckReceivedResponse> resp = (JAXBElement<CheckReceivedResponse>) dispatch.invoke(checkReceived);
            String responseString = resp.getValue().getReturn();


            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向杭州口岸下发指令成功]")
                    .addParm("请求报文", importOrderInfo)
                    .addParm("回复报文", responseString)
                    .log();

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO,
                    "[供应链报文-向杭州口岸下发指令成功]: "
                            + "[请求报文: " + importOrderInfo
                            + ", 回复报文: " + responseString
                            + "]"
            ));

            Response response = XMLUtil.converyToJavaBean(responseString, Response.class);
            if (response != null && response.getBody().getList().size() > 0 && "1".equals(response.getBody().getList().get(0).getChkMark())) {
                recordBO.setPortState(PortState.SUCCESS.getState());
                int result = stockoutOrderRecordManager.updatePortState(stockoutOrderBO.getId(), PortState.SUCCESS.getState());
                return result > 0;
            } else {
                recordBO.setPortState(PortState.FAILURE.getState());
                stockoutOrderRecordManager.updatePortState(stockoutOrderBO.getId(), PortState.FAILURE.getState());
                return false;
            }
        } catch (Exception e) {

            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向杭州口岸下发指令异常]")
                    .addParm("请求报文", importOrderInfo)
                    .addParm("原因", e.getMessage())
                    .setException(e)
                    .log();

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.ERROR,
                    "[供应链报文-向杭州口岸下发指令异常]: "
                            + "[请求报文: " + importOrderInfo
                            + ", 异常信息: " + e.getMessage()
                            + "]"
            ));

        }
        return false;
    }


    /**
     * 构建 口岸需要的订单数据
     *
     * @return
     */
    private Request buildImportOrder() {
        Map<String, Object> meta = JSONUtil.parseJSONMessage(lineBO.getPortBO().getMeta());
        Request request = new Request();
        request.getHead().setBusinessType(HzPortBusinessType.IMPORTORDER.getType());
        OrderInfo orderInfo = new OrderInfo();
        request.getBody().getOrderInfoList().add(orderInfo);

        int index = 1;
        String buyerName = CommonUtil.getBuyerName(stockoutOrderBO);
        String buyerIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);

        PortBizService portBizService = (PortBizService) CommandConfig.getSpringBean("portBizService");
        List<StockoutOrderDetailBO> resultAfterMerge = CommonUtil.mergeStockoutOrderSku(stockoutOrderBO.getDetailBOs(), Boolean.FALSE);
        for (StockoutOrderDetailBO item : resultAfterMerge) {
            JKFOrderDetail orderDetail = new JKFOrderDetail();
            orderDetail.setGoodsOrder(index);
            orderDetail.setCurrency("142");
            orderDetail.setGoodsModel(item.getSkuMerchantBO().getSkuBO().getAttributesDesc());
            orderDetail.setOriginCountry(portBizService
                    .getPortParamCode(lineBO.getPortBO().getId(), PortParamType.ORIGIN.getValue(), item.getSkuMerchantBO().getOriginLand(),
                            true));


            SkuDeclareBO skuDeclareBO = skuDeclareMap.get(item.getSkuId());
            if (skuDeclareBO == null || StringUtils.isBlank(skuDeclareBO.getDeclareName())
                    || StringUtils.isBlank(skuDeclareBO.getMeasuringUnit())
                    || StringUtils.isBlank(skuDeclareBO.getHsCode())) {
                throw new IllegalArgumentException("杭州口岸备案数据部分字段为空，SKUID：" + item.getSkuId());
            } else {
                orderDetail.setGoodsName(CommonUtil.removeJapanWordOnSkuName(skuDeclareBO.getDeclareName()));
                orderDetail.setCodeTs(skuDeclareBO.getHsCode());
                orderDetail.setGoodsUnit(portBizService.getPortParamCode(
                        lineBO.getPortBO().getId(), PortParamType.UNIT.getValue(), skuDeclareBO.getMeasuringUnit(), true));
            }

            if (item.getWeight() == null || item.getWeight() == 0) {
                orderDetail.setGrossWeight(0);
            } else {
                BigDecimal weight = new BigDecimal(item.getWeight());
                BigDecimal trans = new BigDecimal(1000);
                Double weightDouble = weight.divide(trans, 2, BigDecimal.ROUND_CEILING).doubleValue();
                orderDetail.setGrossWeight(weightDouble);
            }

            //商品申报单价
            int skuPrice = declarePriceDetailMap.get(item.getSkuId()).getDeclarePrice();
            String orderSkuUnitPrice = NumberUtil.defaultParsePriceFeng2Yuan(skuPrice, null);
            orderDetail.setUnitPrice(orderSkuUnitPrice);

            orderDetail.setGoodsCount(item.getQuantity());
            orderInfo.getJkfOrderDetailList().add(orderDetail);
            index++;
        }

        orderInfo.getJkfOrderImportHead().setCompanyCode((String) meta.get("custom_code"));
        orderInfo.getJkfOrderImportHead().setCompanyName(PortUtil.getCustomsName(lineBO));
        orderInfo.getJkfOrderImportHead().setConsignee(buyerName);
        orderInfo.getJkfOrderImportHead().setConsigneeAddress(CommonUtil.getConsigneeFullAddress(stockoutOrderBO));

        orderInfo.getJkfOrderImportHead().setConsigneeEmail(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        orderInfo.getJkfOrderImportHead().setConsigneeTel(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        orderInfo.getJkfOrderImportHead().setCurrCode("142");
        orderInfo.getJkfOrderImportHead().setDiscount(NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getDiscountTotalPrice(), null));

        orderInfo.getJkfOrderImportHead().seteCommerceCode(lineBO.getPortBO().geteCommerceCode());
        orderInfo.getJkfOrderImportHead().seteCommerceName(lineBO.getPortBO().geteCommerceName());

        orderInfo.getJkfOrderImportHead().setIeFlag("I");

        String logisCompanyCode = PortConfig.getLogisCompanyCode(lineBO.getPortBO().getPortNid(), stockoutOrderBO.getIntrCarrierCode());
        String logisCompanyName = PortConfig.getLogisCompanyName(lineBO.getPortBO().getPortNid(), stockoutOrderBO.getIntrCarrierCode());
        if (StringUtils.isBlank(logisCompanyCode) || StringUtils.isBlank(logisCompanyName)) {
            throw new IllegalArgumentException("物流企业备案信息不能为空");
        }
        orderInfo.getJkfOrderImportHead().setLogisCompanyCode(logisCompanyCode);
        orderInfo.getJkfOrderImportHead().setLogisCompanyName(logisCompanyName);
        orderInfo.getJkfOrderImportHead().setNote("");

        orderInfo.getJkfOrderImportHead().setOrderNo(stockoutOrderBO.getBizId());

        //订单总税款
        String taxAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getTaxFee(), null);
        orderInfo.getJkfOrderImportHead().setOrderTaxAmount(taxAmount);

        //订单运费
        String feeAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getFreightFee(), null);
        orderInfo.getJkfOrderImportHead().setFeeAmount(feeAmount);

        //订单保费
        String insuranceFee = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getInsuranceFee(), null);
        orderInfo.getJkfOrderImportHead().setInsureAmount(insuranceFee);

        //订单货款:1⃣️（货款(orderGoodsAmount)必须等于成交总价(totalAmount)）2⃣️ 各项商品申报单价(unitPrice)*数量(goodsCount)必须等于货款
        String goodsAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getGoodsTotalPrice(), null);
        orderInfo.getJkfOrderImportHead().setOrderGoodsAmount(goodsAmount);

        //订单成交总价,与订单货款一致
        String totalAmount = goodsAmount;
        orderInfo.getJkfOrderImportHead().setTotalAmount(totalAmount);

        //订单总金额：货款+运费+税款,  总额(orderTotalAmount) = 货款（orderGoodsAmount） + 税款（orderTaxAmount） + 运费（feeAmount）
        String orderTotalAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getOrderTotalPrice(), null);
        orderInfo.getJkfOrderImportHead().setOrderTotalAmount(orderTotalAmount);


        //支付信息
        orderInfo.getJkfOrderImportHead().setPayType("02");

        if (stockoutOrderBO.getDeclarePayType() == null || SystemConstants.DECLARE_EMPTY.equals(stockoutOrderBO.getDeclarePayType())) {
            throw new IllegalArgumentException("支付申报方式不能为空");
        }


        //如果支付单走代理申报，则需要使用换号后的支付单号pay_no
        //StockoutOrderDO orderDO = stockoutOrderManager.getById(stockoutOrderDO.getId());
        if (StringUtils.isBlank(stockoutOrderBO.getDeclarePayNo()) || stockoutOrderBO.getDeclarePayNo().equalsIgnoreCase(stockoutOrderBO.getBizId())) {
            throw new IllegalArgumentException("支付流水号非法");
        }
        orderInfo.getJkfOrderImportHead().setPayNumber(stockoutOrderBO.getDeclarePayNo());
        //如果支付单走代理申报，则需要使用代理支付平台在跨境平台备案编号
        String payCodeOnPort = PayConfig.getPayCodeOnPort(payBillDeclareProviderNid, lineBO.getPortBO().getPortNid());
        String payName = PayConfig.getPayCompanyName(payBillDeclareProviderNid);
        orderInfo.getJkfOrderImportHead().setPayCompanyCode(payCodeOnPort);
        orderInfo.getJkfOrderImportHead().setPayCompanyName(payName);

        orderInfo.getJkfOrderImportHead().setPostMode("2");
        orderInfo.getJkfOrderImportHead().setPurchaserId(stockoutOrderBO.getUserId() == null ? "" : stockoutOrderBO.getUserId().toString());
        orderInfo.getJkfOrderImportHead().setSenderCountry(portBizService.getPortParamCode(lineBO.getPortBO().getId(), PortParamType.ORIGIN.getValue(), lineBO.getWarehouseBO().getSenderBO().getSenderCountry(), true));
        orderInfo.getJkfOrderImportHead().setSenderName(stockoutOrderBO.getMerchantPackageMaterialBO().getSenderName());

        orderInfo.getJkfOrderImportHead().setTotalCount(1);
        orderInfo.getJkfOrderImportHead().setTradeTime(DateUtil.getDateForLong("yyyy-MM-dd HH:mm:ss", stockoutOrderBO.getGmtCreate().getTime()));

        orderInfo.getJkfOrderImportHead().setWayBills("");
        orderInfo.getJkfOrderImportHead().setZipCode(stockoutOrderBO.getBuyerBO().getBuyerZipcode());
        orderInfo.getJkfOrderImportHead().setUserProcotol("本人承诺所购买商品系个人合理自用，现委托商家代理申报、代缴税款等通关事宜，本人保证遵守《海关法》和国家相关法律法规，保证所提供的身份信息和收货信息真实完整，无侵犯他人权益的行为，以上委托关系系如实填写，本人愿意接受海关、检验检疫机构及其他监管部门的监管，并承担相应法律责任。");
        orderInfo.getJkfGoodsPurchaser().setId(stockoutOrderBO.getUserId() == null ? "" : stockoutOrderBO.getUserId().toString());
        orderInfo.getJkfGoodsPurchaser().setEmail(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        orderInfo.getJkfGoodsPurchaser().setName(buyerName);
        orderInfo.getJkfGoodsPurchaser().setPaperType("01");
        orderInfo.getJkfGoodsPurchaser().setPaperNumber(buyerIdNo);
        orderInfo.getJkfGoodsPurchaser().setTelNumber(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        orderInfo.getJkfGoodsPurchaser().setAddress(CommonUtil.getConsigneeFullAddress(stockoutOrderBO));
        orderInfo.getJkfSign().setBusinessNo(stockoutOrderBO.getBizId());
        orderInfo.getJkfSign().setCompanyCode((String) meta.get("custom_code"));
        orderInfo.getJkfSign().setDeclareType(declareType.getType());
        orderInfo.getJkfSign().setBusinessType(HzPortBusinessType.IMPORTORDER.getType());
        orderInfo.getJkfSign().setNote("");
        orderInfo.getJkfSign().setCebFlag("03");
        return request;
    }

}
