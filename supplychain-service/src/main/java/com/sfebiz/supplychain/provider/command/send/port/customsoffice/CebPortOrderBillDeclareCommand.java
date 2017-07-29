package com.sfebiz.supplychain.provider.command.send.port.customsoffice;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.common.enums.PortState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.protocol.ceb.common.BaseSign;
import com.sfebiz.supplychain.protocol.ceb.common.BaseTransfer;
import com.sfebiz.supplychain.protocol.ceb.common.CertInfo;
import com.sfebiz.supplychain.protocol.ceb.common.UserInfo;
import com.sfebiz.supplychain.protocol.ceb.order.CEB311Message;
import com.sfebiz.supplychain.protocol.ceb.order.Order;
import com.sfebiz.supplychain.protocol.ceb.order.OrderHead;
import com.sfebiz.supplychain.protocol.ceb.order.OrderList;
import com.sfebiz.supplychain.protocol.ceb.util.CebDateUtil;
import com.sfebiz.supplychain.protocol.common.DeclareType;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.port.PortOrderBillDeclareCommand;
import com.sfebiz.supplychain.provider.entity.PortParamType;
import com.sfebiz.supplychain.service.port.PortUtil;
import com.sfebiz.supplychain.service.port.biz.PortBizService;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.NumberUtil;
import com.sfebiz.supplychain.util.XMLUtil;
import com.sfebiz.supplychain.validator.SimpleValidator;
import net.pocrd.entity.ServiceException;
import net.pocrd.util.Base64Util;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 广州海关订单申报
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 上午11:19
 */
public class CebPortOrderBillDeclareCommand extends PortOrderBillDeclareCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    @Override
    public boolean execute() throws ServiceException {
        String importOrder2XmlFormat = null;
        try {
            if (recordBO.getPortState() == PortState.SUCCESS.getState()
                    || recordBO.getPortState() == PortState.NO.getState()) {
                return true;
            }

            boolean isMockAutoCreated = MockConfig.isMocked("customsoffice", "createCommand");
            if (isMockAutoCreated) {
                logger.info("MOCK：广州口岸申报订单 采用MOCK实现");
                return mockPortStockoutCreateSuccess();
            }
            CEB311Message ceb311Message = buildOrderMessage();
            checkOrderMessageField(ceb311Message);
            importOrder2XmlFormat = XMLUtil.convertToXml(ceb311Message);
            if (importOrder2XmlFormat == null) {
                return false;
            }
            PortBizService portBizService = (PortBizService) CommandConfig.getSpringBean("portBizService");
            portBizService.sendMessageToRemoteMQ(importOrder2XmlFormat, stockoutOrderBO.getBizId() + ".xml" , PortNid.GUANGZHOU.getNid());

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向海关总署下发订单指令成功]")
                    .addParm("请求报文", importOrder2XmlFormat)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .log();

            StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");

            recordBO.setPortState(PortState.SUCCESS.getState());
            stockoutOrderRecordManager.updatePortState(stockoutOrderBO.getId(), PortState.SUCCESS.getState());

            return true;
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
    private CEB311Message buildOrderMessage() {
        CEB311Message ceb311Message = new CEB311Message();
        OrderHead orderHead = buildOrderHead(declareType);
        List<OrderList> orderLists = buildOrderList();
        Order order = new Order();
        order.setOrderHead(orderHead);
        order.setOrderList(orderLists);
        BaseTransfer baseTransfer = buildBaseTransfer();
        ceb311Message.setGuid(orderHead.getGuid());
        ceb311Message.setOrder(order);
        ceb311Message.setBaseTransfer(baseTransfer);
        return ceb311Message;
    }

    /**
     * 构建订单的基本信息
     *
     * @param declareType
     * @return
     */
    private OrderHead buildOrderHead(DeclareType declareType) {
        OrderHead orderHead = new OrderHead();
        //同一个订单不同次请求uuid不同是否有影响
        orderHead.setGuid(UUID.randomUUID().toString().toUpperCase());
        orderHead.setAppType(declareType.getType());
        orderHead.setAppTime(CebDateUtil.format(new Date(), CebDateUtil.defaultCebDateStringFormat));
        orderHead.setAppStatus("2");
        orderHead.setOrderType("I");
        orderHead.setOrderNo(stockoutOrderBO.getBizId());
        orderHead.setEbpCode(PortUtil.getCustomsCode(lineBO));
        orderHead.setEbpName(PortUtil.getCustomsName(lineBO));
        orderHead.setEbcCode(PortUtil.getCustomsCode(lineBO));
        orderHead.setEbcName(PortUtil.getCustomsName(lineBO));


        //订单货款
        String goodsAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getGoodsTotalPrice(), null);
        orderHead.setGoodsValue(goodsAmount);

        //订单运费
        String feeAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getFreightFee(), null);
        orderHead.setFreight(feeAmount);

        //订单税款
        String taxAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getTaxFee(), null);
        orderHead.setTaxTotal(taxAmount);

        //订单成交总价＝订单总金额 - 折扣
        String userPayTotalPrice = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getOrderActualPrice(), null);
        orderHead.setActuralPaid(userPayTotalPrice);

        //折扣总额
        String discountAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderBO.getDeclarePriceBO().getDiscountTotalPrice(), null);
        orderHead.setDiscount(discountAmount);
        orderHead.setCurrency("142");

        String buyerName = CommonUtil.getBuyerName(stockoutOrderBO);
        String buyerIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);

        orderHead.setBuyerRegNo(CommonUtil.getBuyerRegNo(buyerName, buyerIdNo));
        orderHead.setBuyerName(buyerName);
        orderHead.setBuyerIdNumber(buyerIdNo);
        orderHead.setBuyerIdType("1");
        orderHead.setConsignee(CommonUtil.getConsigneeName(stockoutOrderBO));
        orderHead.setConsigneeTelephone(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        orderHead.setConsigneeAddress(CommonUtil.getConsigneeFullAddress(stockoutOrderBO));

        return orderHead;
    }

    /**
     * 构建商品详情信息
     *
     * @return
     */
    private List<OrderList> buildOrderList() {
        List<OrderList> orderLists = new ArrayList<OrderList>();

        List<StockoutOrderDetailBO> resultAfterMerge = CommonUtil.mergeStockoutOrderSku(stockoutOrderBO.getDetailBOs(), Boolean.FALSE);

        PortBizService portBizService = (PortBizService) CommandConfig.getSpringBean("portBizService");
        int index = 1;
        for (StockoutOrderDetailBO item : resultAfterMerge) {
            OrderList orderList = new OrderList();
            orderList.setGnum(index);
            orderList.setQty(item.getQuantity().toString());
            orderList.setItemNo(item.getSkuId().toString());


            SkuDeclareBO skuDeclareBO = skuDeclareMap.get(item.getSkuId());
            if (skuDeclareBO == null || StringUtils.isBlank(skuDeclareBO.getDeclareName())
                    || StringUtils.isBlank(skuDeclareBO.getMeasuringUnit())
                    || StringUtils.isBlank(skuDeclareBO.getPostTaxNo())) {
                orderList.setItemName(CommonUtil.removeJapanWordOnSkuName(item.getSkuName()));
                orderList.setUnit(portBizService.getPortParamCode(
                        lineBO.getPortBO().getId(), PortParamType.UNIT.getValue(), item.getSkuMerchantBO().getSkuBO().getMeasuringUnit(), true));
                orderList.setCountry(portBizService.getPortParamCode(
                        lineBO.getPortBO().getId(), PortParamType.ORIGIN.getValue(), item.getSkuMerchantBO().getOriginLand(),
                                false));
            } else {
                orderList.setItemName(skuDeclareBO.getDeclareName());
                orderList.setUnit(portBizService.getPortParamCode(
                        lineBO.getPortBO().getId(), PortParamType.UNIT.getValue(), skuDeclareBO.getMeasuringUnit(), true));
                orderList.setCountry(portBizService.getPortParamCode(
                        lineBO.getPortBO().getId(), PortParamType.ORIGIN.getValue(), skuDeclareBO.getOrigin(),
                                false));
            }

            if (StringUtils.isBlank(orderList.getCountry())) {
                throw new IllegalArgumentException("商品 " + item.getSkuId() + "原产地不能为空。");
            }

            if (StringUtils.isBlank(orderList.getUnit())) {
                throw new IllegalArgumentException("商品 " + item.getSkuId() + "计量单位不能为空。");
            }

            //商品申报单价
            int skuPrice = declarePriceDetailMap.get(item.getSkuId()).getDeclarePrice();
            String orderSkuUnitPrice = NumberUtil.defaultParsePriceFeng2Yuan(skuPrice, null);
            orderList.setPrice(orderSkuUnitPrice);
            String skuTotalPrice = NumberUtil.defaultParsePriceFeng2Yuan(skuPrice * item.getQuantity(), null);
            orderList.setTotalPrice(skuTotalPrice);
            orderList.setCurrency("142");

            orderLists.add(orderList);
            index++;
        }
        return orderLists;
    }


    /**
     * 构建传输头信息
     *
     * @return
     */
    private BaseTransfer buildBaseTransfer() {
        BaseTransfer baseTransfer = new BaseTransfer();
        baseTransfer.setCopCode(PortUtil.getCustomsCode(lineBO));
        baseTransfer.setCopName(PortUtil.getCustomsName(lineBO));
        baseTransfer.setDxpMode("DXP");
        baseTransfer.setDxpId("DXPENT0000011398");
        return baseTransfer;
    }

    /**
     * 构建签名体信息
     *
     * @return
     */
    private BaseSign buildBaseSign(OrderHead orderHead) {
        BaseSign baseSign = new BaseSign();
        String signTime = CebDateUtil.format(new Date(), CebDateUtil.defaultCebDateStringFormat);
        String signData = getOrderHeaderAppandValue(orderHead);
        baseSign.setSignData(signData);
        baseSign.setSignResult(Base64Util.encodeToString(String.valueOf(signData + signTime).getBytes()));
        baseSign.setSignTime(signTime);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("123456");
        userInfo.setUserName("丰趣海淘");
        userInfo.setCopCode(PortUtil.getCustomsCode(lineBO));
        userInfo.setCopName(PortUtil.getCustomsName(lineBO));
        baseSign.setUserInfo(userInfo);

        CertInfo certInfo = new CertInfo();
        certInfo.setCertType("E");
        certInfo.setCertNo("123456");
        baseSign.setCertInfo(certInfo);
        return baseSign;
    }


    /**
     * 获取OrderHead数据值顺序连接而成的字符串
     *
     * @param orderHead
     * @return
     */
    private String getOrderHeaderAppandValue(OrderHead orderHead) {
        return orderHead.generateValueString();
    }

    /**
     * 检查订单Message中数据合法性
     *
     * @param cEB311Message
     */
    @SuppressWarnings("uncheck")
    private void checkOrderMessageField(CEB311Message cEB311Message) {
        OrderHead orderHead = cEB311Message.getOrder().getOrderHead();
        SimpleValidator.create().isNotBlank(orderHead.getGuid(), "GUID")
                .isNotBlank(orderHead.getAppType(), "AppType")
                .isNotBlank(orderHead.getAppTime(), "AppTime")
                .isNotBlank(orderHead.getAppStatus(), "AppStatus")
                .isNotBlank(orderHead.getOrderType(), "OrderType")
                .isNotBlank(orderHead.getOrderNo(), "OrderNo")
                .isNotBlank(orderHead.getEbpCode(), "EbpCode")
                .isNotBlank(orderHead.getEbpName(), "EbpName")
                .isNotBlank(orderHead.getEbcCode(), "EbcCode")
                .isNotBlank(orderHead.getEbcName(), "EbcName")
                .isNotBlank(orderHead.getGoodsValue(), "GoodsValue")
                .isNotZero(stockoutOrderBO.getDeclarePriceBO().getOrderTotalPrice().longValue(), "GoodsValue")
                .isNotBlank(orderHead.getFreight(), "Freight")
                .isNotBlank(orderHead.getDiscount(), "Discount")
                .isNotBlank(orderHead.getTaxTotal(), "TaxTotal")
                .isNotBlank(orderHead.getActuralPaid(), "ActuralPaid")
                .isNotZero(stockoutOrderBO.getDeclarePriceBO().getOrderActualPrice().longValue(), "ActuralPaid")
                .isNotBlank(orderHead.getCurrency(), "Currency")
                .isNotBlank(orderHead.getBuyerRegNo(), "BuyerRegNo")
                .isNotBlank(orderHead.getBuyerName(), "BuyerName")
                .isNotBlank(orderHead.getBuyerIdType(), "BuyerIdType")
                .isNotBlank(orderHead.getBuyerIdNumber(), "BuyerIdNumber")
                .isNotBlank(orderHead.getConsignee(), "Consignee")
                .isNotBlank(orderHead.getConsigneeTelephone(), "ConsigneeTelephone")
                .isNotBlank(orderHead.getConsigneeAddress(), "ConsigneeAddress")
                .throwIfError("字段不能为空");
        List<OrderList> orderList = cEB311Message.getOrder().getOrderList();
        if (CollectionUtils.isEmpty(orderList)) {
            throw new IllegalArgumentException("订单列表不能为空");
        }

        BaseTransfer baseTransfer = cEB311Message.getBaseTransfer();
        SimpleValidator.create().isNotBlank(baseTransfer.getCopCode(), "CopCode")
                .isNotBlank(baseTransfer.getCopName(), "CopName")
                .isNotBlank(baseTransfer.getDxpMode(), "DxpMode")
                .isNotBlank(baseTransfer.getDxpId(), "DxpId")
                .throwIfError("字段不能为空");
    }

}
