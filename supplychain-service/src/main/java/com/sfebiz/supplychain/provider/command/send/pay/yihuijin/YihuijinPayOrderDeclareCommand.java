package com.sfebiz.supplychain.provider.command.send.pay.yihuijin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.config.order.OrderConfig;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.user.api.RealNameAuthenticationService;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.pay.yihuijin.*;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderDeclareCommand;
import com.sfebiz.supplychain.provider.entity.PriceUnit;
import com.sfebiz.supplychain.service.port.PortUtil;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * <p>易汇金支付支付单申报</p>
 * User: zhangdi
 * Date: 15/09/16
 */
public class YihuijinPayOrderDeclareCommand extends PayOrderDeclareCommand {

    public static final String serviceName = "customs/order";

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private StockoutOrderManager stockoutOrderManager;
    private RealNameAuthenticationService realNameAuthenticationService;

    @Override
    public boolean execute() {
        try {

            if (portBillDeclareDO == null || stockoutOrderBO == null || StringUtils.isBlank(payBillDeclareProviderNid)
                    || logisticsLineBO == null || logisticsLineBO.getPortBO() == null) {
                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .setMsg("易汇金支付支付单申报单参数缺失")
                        .addParm("订单号：", stockoutOrderBO.getBizId())
                        .log();
                return false;
            }
            if (PortBillState.SEND_SUCCESS.getValue().equals(portBillDeclareDO.getState())
                    || PortBillState.VERIFY_CALLBACK.getValue().equals(portBillDeclareDO.getState())) {
                return true;
            }

            boolean isMockAutoCreated = MockConfig.isMocked("yihuijinpay", "createCommand");
            if (isMockAutoCreated) {
                //直接返回
                logger.info("MOCK：易汇金支付申报 采用MOCK实现");
                return mockPayBillDeclareSuccess();
            }

            realNameAuthenticationService = (RealNameAuthenticationService) CommandConfig.getSpringBean("realNameAuthenticationService");
            String buyerName = CommonUtil.getBuyerName(stockoutOrderBO);
            String buyerIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);

            if (!OrderConfig.getIsSkipRealNameAuthentication(stockoutOrderBO.getBizId())
                    && !realNameAuthenticationService.rz(buyerName, buyerIdNo)) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.WARN)
                        .setMsg("[供应链报文-向易汇金支付下发支付单申报指令失败]购买人的实名认证失败！")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("出库单ID", stockoutOrderBO.getId())
                        .addParm("购买人姓名", buyerName)
                        .log();
                createFailureMessage = "收货人的实名认证失败,购买人姓名:" + buyerName;
                return false;
            } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-向易汇金支付下发支付单申报指令成功] 购买人的实名认证成功！")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("出库单ID", stockoutOrderBO.getId())
                        .addParm("购买人姓名", buyerName)
                        .log();
            }

            //调用易汇金查询接口，判断是否已经存在该订单对应的联名支付号
            String joinPayNo = queryJoinPayNo(logisticsLineBO.getWarehouseBO().getId());

            //若不存在则调用联名支付号生成接口
            if (StringUtils.isBlank(joinPayNo)) {
                joinPayNo = generateJoinPayNo(logisticsLineBO.getWarehouseBO().getId());
            }

            if (StringUtils.isBlank(joinPayNo)) {
                return false;
            }

            stockoutOrderBO.setDeclarePayNo(joinPayNo);
            stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
            stockoutOrderManager.updatePayNo(stockoutOrderBO.getId(), joinPayNo);
            String request = buildDeclareRequest(joinPayNo,logisticsLineBO.getWarehouseBO().getId());
            boolean result = sendMessage2DeclarePayBill(request);
            return result;
        } catch (ServiceException e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
	            	.addParm("易汇金支付支付单申报请求报文", e)
	            	.setException(e)
	            	.log();
            createFailureMessage = e.getMsg();
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .addParm("易汇金支付支付单申报请求报文", e)
                    .setException(e)
                    .log();
        }
        return false;
    }


    /**
     * 查询易汇金联名支付流水号
     *
     * @return
     * @throws Exception
     */
    public String queryJoinPayNo(Long warehouseId) {
        String joinPayNo = null;
        try {
            String request = buildJoinPayQueryRequest(warehouseId);
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .addParm("易汇金支付查询联名支付号请求报文", request)
                    .log();

            try {
                String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
                if (StringUtils.isNotBlank(url)) {
                    url = url + "hg/query";
                }

                String response = HttpUtil.postJsonByHttps(url, request);
                if (response == null) {
                    return null;
                }
                JSONObject resJson = JSON.parseObject(response);

                if (resJson.containsKey("serialNumber")) {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.INFO)
                            .setMsg("[供应链报文-向易汇金支付下发查询联名支付号指令成功] 出库单已经在易汇金生成过支付流水")
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                            .addParm("请求报文", request)
                            .addParm("回复报文", response)
                            .log();

                    joinPayNo = resJson.getString("serialNumber");
                } else {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.INFO)
                            .setMsg("[供应链报文-向易汇金支付下发查询联名支付号指令成功] 出库单尚未在易汇金生成支付流水")
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                            .addParm("订单号：", stockoutOrderBO.getBizId())
                            .addParm("请求报文",request)
                            .addParm("回复报文",response)
                            .log();
                    return null;
                }

            } catch (Exception e) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("[供应链报文-向易支付下发查询联名支付号指令异常]")
                        .addParm("请求报文", request)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setException(e)
                        .log();
            }

        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .addParm("易汇金查询联名支付流水号请求报文", e)
                    .setException(e)
                    .log();
        }
        return joinPayNo;
    }


    /**
     * 获取易汇金联名支付流水号
     *
     * @return
     * @throws Exception
     */
    public String generateJoinPayNo(Long warehouseId) throws ServiceException {
        String joinPayNo = null;
        String request = null;
        try {
            request = buildJoinPayRequest(warehouseId);

            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
            if (StringUtils.isNotBlank(url)) {
                url = url + "hg/order";
            }

            String response = HttpUtil.postJsonByHttps(url, request);
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("易汇金支付获取联名支付号")
                    .addParm("请求报文", request)
                    .addParm("响应报文", response)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .log();

            if (response == null) {
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }
            JSONObject resJson = JSON.parseObject(response);

            if (resJson.containsKey("status") && resJson.getString("status").equals("SUCCESS")) {
                joinPayNo = resJson.getString("serialNumber");
            }
            return joinPayNo;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向易支付下发获取联名支付号指令异常]")
                    .addParm("请求报文", request)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION, e.getMessage());
        }
    }


    /**
     * 给易汇金支付发送支付单申报信息
     *
     * @param requestBodyJsonString 请求体JSON字符串
     * @return
     */
    private boolean sendMessage2DeclarePayBill(String requestBodyJsonString) {
        try {
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
            if (StringUtils.isNotBlank(url)) {
                url = url + serviceName;
            }

            String response = HttpUtil.postJsonByHttps(url, requestBodyJsonString);

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向易汇金支付下发支付单申报指令]")
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .addParm("请求报文", requestBodyJsonString)
                    .addParm("回复报文", response)
                    .log();

            if (StringUtils.isBlank(response)) {
                return false;
            }
            JSONObject resJson = JSON.parseObject(response);
            if (resJson.containsKey("status")) {
                String returnStatus = resJson.getString("status");
                String errorMessage = resJson.getString("error");
                PortBillDeclareManager portBillDeclareManager = (PortBillDeclareManager) CommandConfig.getSpringBean("portBillDeclareManager");
                if ("SUCCESS".equalsIgnoreCase(returnStatus)||errorMessage.contains("exception.record.exists")) {
                    portBillDeclareDO.setState(PortBillState.SEND_SUCCESS.getValue());
                    portBillDeclareDO.setResult(returnStatus);
                    portBillDeclareDO.setBillSendTime(new Date());
                    portBillDeclareManager.update(portBillDeclareDO);
                    return true;
                } else {
                    portBillDeclareDO.setState(PortBillState.PARAMS_EXCEPTION.getValue());
                    portBillDeclareDO.setResult(returnStatus);
                    portBillDeclareDO.setBillSendTime(new Date());
                    portBillDeclareManager.update(portBillDeclareDO);
                    setCreateFailureMessage(response);
                }
            }

        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向易汇金支付下发支付单申报指令异常]")
                    .addParm("请求报文", requestBodyJsonString)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
        }
        return false;
    }


    /**
     * 构建查询易汇金联名支付请求参数信息
     *
     * @return
     * @throws Exception
     */
    private String buildJoinPayQueryRequest(Long warehouseId) throws Exception {
        String merchantId = getMerchantId(warehouseId);
        JoinPayQueryBuilder builder = new JoinPayQueryBuilder(merchantId);
        builder.setRequestId(stockoutOrderBO.getBizId());
        StringBuilder hmacSource = new StringBuilder();
        hmacSource.append(StringUtils.defaultString(merchantId)).append(StringUtils.defaultString(stockoutOrderBO.getBizId()));
        String signKey = getMerchantKey(warehouseId);
        String sign = YihuijinSignUtils.signMd5(hmacSource.toString(), signKey);
        JSONObject request = builder.build(sign);
        return request.toJSONString();
    }


    /**
     * 构建易汇金联名支付请求参数信息
     *
     * @return
     * @throws Exception
     */
    private String buildJoinPayRequest(Long warehouseId) throws Exception {
        String merchantId = getMerchantId(warehouseId);

        Integer payPrice = stockoutOrderDeclarePriceBO.getOrderActualPrice();

        JoinPayOrderBuilder builder = new JoinPayOrderBuilder(merchantId);
        builder.setRequestId(stockoutOrderBO.getBizId())
                .setOrderAmount(payPrice.toString())
                .setOrderCurrency(PriceUnit.CNY)
                .setNotifyUrl(PayConfig.getPayProviderCallbackUrl(payBillDeclareProviderNid))
                //备注不能为空！
                .setRemark(stockoutOrderBO.getRemarks() != null ? stockoutOrderBO.getRemarks() : "无备注");

//        List<StockoutOrderSkuDO> resultAfterFilter = CommonUtil.removeMixedSku(stockoutOrderSkuDOs);
//        List<StockoutOrderSkuDO> resultAfterMerge = CommonUtil.mergeStockoutOrderSku(resultAfterFilter, Boolean.FALSE);
        for (StockoutOrderDetailBO skuDO : stockoutOrderDetailBOList) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setName(skuDO.getSkuName());
            productDetail.setQuantity((long) skuDO.getQuantity());
            productDetail.setAmount(declarePriceDetailMap.get(skuDO.getSkuId()).getDeclarePrice().longValue());
            productDetail.setReceiver("上海牵趣网络科技有限公司");
            productDetail.setDescription(skuDO.getSkuName());
            builder.addProductDetail(productDetail);
        }

        String buyerName = CommonUtil.getBuyerName(stockoutOrderBO);
        String buyerIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);

        Payer payer = new Payer();
        payer.setName(buyerName);
        //目前易汇金只支持"IDCARD"(身份证)
        payer.setIdType("IDCARD");
        payer.setIdNum(buyerIdNo);
        payer.setCustomerId(stockoutOrderBO.getUserId().toString());
        //后四项为非必填项，若不填需设为空字符
        payer.setBankCardNum("");
        payer.setPhoneNum(stockoutOrderBO.getBuyerBO().getBuyerTelephone());
        payer.setEmail("");
        payer.setNationality("");

        builder.setPayer(payer);

        JSONObject request = builder.build(getMerchantKey(warehouseId));
        return request.toJSONString();
    }


    /**
     * 构建易汇金待申报请求参数信息
     *
     * @return
     * @throws Exception
     */
    private String buildDeclareRequest(String joinPayNo,Long warehouseId) throws Exception {

        String merchantId = getMerchantId(warehouseId);
        CustomOrderBuilder builder = new CustomOrderBuilder(merchantId);
        builder.setPaySerialNumber(joinPayNo);
        CustomsInfo customsInfo = new CustomsInfo();

        //各个口岸的meta信息中需要填写在连连支付中的海关编码
        Map<String, Object> portMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getPortBO().getMeta());
        if (portMeta.containsKey("yihuijin_eport_code") && StringUtils.isNotBlank(portMeta.get("yihuijin_eport_code").toString())) {
            customsInfo.setCustomsChannel(portMeta.get("yihuijin_eport_code").toString());
        } else {
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PARAMS_ILLEGAL, "易汇金支付在口岸元信息中的 yihuijin_eport_code 值不存在");
        }

        //各个口岸的meta信息中需要填写在连连支付中的海关编码
        if (portMeta.containsKey("dxpId") && StringUtils.isNotBlank(portMeta.get("dxpId").toString())) {
            customsInfo.setDxpid(portMeta.get("dxpId").toString());
        } else {
        	throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PARAMS_ILLEGAL, "易汇金支付在口岸元信息中的 dxpId 值不存在");
        }

        //报关金额 分
        customsInfo.setAmount((long) stockoutOrderDeclarePriceBO.getOrderActualPrice());
        //货款
        customsInfo.setGoodsAmount((long) stockoutOrderDeclarePriceBO.getGoodsTotalPrice());
        //税款
        customsInfo.setTax((long) stockoutOrderDeclarePriceBO.getTaxFee());
        //运费
        customsInfo.setFreight((long) stockoutOrderDeclarePriceBO.getFreightFee());
        if(PortNid.HANGZHOU.getNid().equals(logisticsLineBO.getPortBO().getPortNid())){
            //电商在海关的企业备案编码
            customsInfo.setMerchantCommerceCode(logisticsLineBO.getPortBO().geteCommerceCode());
            //电商在海关的企业备案名称
            customsInfo.setMerchantCommerceName(logisticsLineBO.getPortBO().geteCommerceName());
        }else {
            //电商在海关的企业备案编码
            customsInfo.setMerchantCommerceCode(PortUtil.getCustomsCode(logisticsLineBO));
            //电商在海关的企业备案名称
            customsInfo.setMerchantCommerceName(PortUtil.getCustomsName(logisticsLineBO));
        }


        builder.addCustomsInfo(customsInfo);
        String notifyUrl = PayConfig.getPayProviderCallbackUrl(payBillDeclareProviderNid);
        builder.setNotifyUrl(notifyUrl);

        JSONObject request = builder.build(generateDeclareSign(merchantId, joinPayNo, notifyUrl, customsInfo,logisticsLineBO.getWarehouseBO().getId()));
        return request.toJSONString();
    }

    /**
     * 生产易汇金支付申报签名key
     *
     * @param merchantId  商户ID
     * @param joinPayNo   联名支付流水号
     * @param notifyUrl   回调地址
     * @param customsInfo 支付订单相关信息
     * @return
     */
    private String generateDeclareSign(String merchantId, String joinPayNo, String notifyUrl, CustomsInfo customsInfo,Long warehouseId) throws Exception {
        StringBuilder hmacSource = new StringBuilder();
        hmacSource.append(StringUtils.defaultString(merchantId)).append(StringUtils.defaultString(joinPayNo, "")).append(StringUtils.defaultString(notifyUrl, ""));
        hmacSource.append(StringUtils.defaultString(customsInfo.getCustomsChannel()))
                .append(ObjectUtils.defaultIfNull(customsInfo.getAmount(), ""))
                .append(ObjectUtils.defaultIfNull(customsInfo.getFreight(), ""))
                .append(ObjectUtils.defaultIfNull(customsInfo.getGoodsAmount(), ""))
                .append(ObjectUtils.defaultIfNull(customsInfo.getTax(), ""))
                .append(ObjectUtils.defaultIfNull(customsInfo.getMerchantCommerceName(), ""))
                .append(ObjectUtils.defaultIfNull(customsInfo.getMerchantCommerceCode(), ""))
                .append(ObjectUtils.defaultIfNull(customsInfo.getDxpid(), ""));

        String signKey = getMerchantKey(warehouseId);
        return YihuijinSignUtils.signMd5(hmacSource.toString(), signKey);
    }


    /**
     * 获取商户号
     *
     * @return
     * @throws Exception
     */
    private String getMerchantId(Long warehouseId) throws Exception {
        String merchantIds = PayConfig.getECodeOnPayProvider(payBillDeclareProviderNid);
        Map<String, Object> merchantIdMap = JSONUtil.parseJSONMessage(merchantIds);
        String merchantId;
        String merchantIdS;
        if(merchantIdMap.containsKey(warehouseId.toString())){
            merchantIdS = (String)merchantIdMap.get(warehouseId.toString());
        }else{
            merchantIdS = (String)merchantIdMap.get("DEFAULT");
        }
        if(StringUtils.isNotBlank(merchantIdS) && merchantIdS.contains(",")){
            String[] merchantIdArray = merchantIdS.split(",");
            merchantId = merchantIdArray[0].trim();
        }else{
            merchantId = merchantIdS;
        }
        return merchantId;
    }


    /**
     * 获取商户号对应的KEY
     *
     * @return
     * @throws Exception
     */
    private String getMerchantKey(Long warehouseId) throws Exception {
        String signKeys = PayConfig.getPayProviderInterfaceKey(payBillDeclareProviderNid);
        Map<String, Object> signKeyMap = JSONUtil.parseJSONMessage(signKeys);
        String signKey;
        String signKeyS;
        if(signKeyMap.containsKey(warehouseId.toString())){
            signKeyS = (String)signKeyMap.get(warehouseId.toString());
        }else{
            signKeyS = (String)signKeyMap.get("DEFAULT");
        }
        if (StringUtils.isNotBlank(signKeys) && signKeys.contains(",")) {
            String[] signKeyArray = signKeyS.split(",");
            signKey = signKeyArray[0].trim();
        } else {
            signKey = signKeyS;
        }

        return signKey;
    }
}
