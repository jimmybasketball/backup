package com.sfebiz.supplychain.provider.command.send.pay.yijifu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.pay.yijifu.YijifuGoodsClauses;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderDeclareCommand;
import com.sfebiz.supplychain.provider.entity.PriceUnit;
import com.sfebiz.supplychain.service.port.PortUtil;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.NumberUtil;
import com.yiji.openapi.tool.YijifuGateway;
import com.yiji.openapi.tool.YijipayConstants;
import com.yiji.openapi.tool.util.Ids;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>易极付支付支付单申报</p>
 * User: tanzongxi
 * Date: 15/09/16
 */
public class YijifuPayOrderDeclareCommand extends PayOrderDeclareCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private StockoutOrderManager stockoutOrderManager;
    private RealNameAuthenticationService realNameAuthenticationService;

    @Override
    public boolean execute() throws ServiceException {
        boolean result = false;
        try {
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("===== 易极付-支付单申报 start =====").log();
            if (portBillDeclareDO == null || stockoutOrderBO == null || StringUtils.isBlank(payBillDeclareProviderNid)
                    || logisticsLineBO == null || logisticsLineBO.getPortBO() == null) {
                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .setMsg("易极付支付支付单申报单参数缺失")
                        .addParm("订单号：", stockoutOrderBO.getBizId())
                        .log();
                return false;
            }
            if (PortBillState.SEND_SUCCESS.getValue().equals(portBillDeclareDO.getState())
                    || PortBillState.VERIFY_CALLBACK.getValue().equals(portBillDeclareDO.getState())) {
                return true;
            }

            boolean isMockAutoCreated = MockConfig.isMocked("yijifupay", "createCommand");
            if (isMockAutoCreated) {
                //直接返回
                logger.info("MOCK：易极付支付申报 采用MOCK实现");
                return mockPayBillDeclareSuccess();
            }

            // 实名认证
            realNameAuthenticationService = (RealNameAuthenticationService) CommandConfig.getSpringBean("realNameAuthenticationService");
            String buyerName = CommonUtil.getBuyerName(stockoutOrderBO);
            String buyerIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);
            if (!OrderConfig.getIsSkipRealNameAuthentication(stockoutOrderBO.getBizId())
                    && !realNameAuthenticationService.rz(buyerName, buyerIdNo, "YIJIFU")) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.WARN)
                        .setMsg("[供应链报文-向易极付支付下发支付单申报指令失败]购买人的实名认证失败！")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("出库单ID", stockoutOrderBO.getId())
                        .addParm("购买人姓名", buyerName)
                        .log();
                createFailureMessage = "收货人的实名认证失败,购买人姓名:" + buyerName;
                return false;
            } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-向易极付支付下发支付单申报指令成功] 购买人的实名认证成功！")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("出库单ID", stockoutOrderBO.getId())
                        .addParm("购买人姓名", buyerName)
                        .log();
            }

            // 交易状态 调用易极付【交易单笔查询tradeInfoQuery】获得交易状态
            JSONObject resJson = tradeInfoQuery();

            // tradeOrderCreate和fsTradePayByFriend交易成功时
            if (resJson !=null && ("TRADE_PAY_SUCCESS".equals(resJson.getString("tradeStatus")) || "TRADE_FINISHED".equals(resJson.getString("tradeStatus")))) {
                String tradeNo = resJson.getString("tradeNo");
                // 调用易极付【支付单上传singlePaymentUpload】完成支付单申报
                result = singlePaymentUpload(tradeNo);
            // tradeOrderCreate交易成功时
            } else if (resJson !=null && ("CREATE_TRADE".equals(resJson.getString("tradeStatus")) || "WAIT_BUYER_PAY".equals(resJson.getString("tradeStatus")))) {
                String tradeNo = resJson.getString("tradeNo");
                // 调用易极付【支付接口fsTradePayByFriend】完成订单支付
                fsTradePayByFriend(tradeNo);
                // 调用易极付【支付单上传singlePaymentUpload】完成支付单申报
                result = singlePaymentUpload(tradeNo);
            // 三个接口都没有调用成功或者没有调用过时
            } else {
                // 调用易极付【创建交易订单tradeOrderCreate】获取交易号
                String tradeNo = tradeOrderCreate();
                // 调用易极付【支付接口fsTradePayByFriend】完成订单支付
                fsTradePayByFriend(tradeNo);
                // 调用易极付【支付单上传singlePaymentUpload】完成支付单申报
                result = singlePaymentUpload(tradeNo);
            }
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .addParm("易极付支付支付单申报请求报文", e)
                    .setException(e)
                    .log();
        }
        return result;
    }
    /**
     * 易极付-交易单笔查询tradeInfoQuery
     *
     * @return 交易状态
     * @throws Exception
     */
    public JSONObject tradeInfoQuery() throws ServiceException {
        String tradeStatus = null;
        String request = null;
        String response = null;
        try {
            // 易极付接口调用地址url
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);;
            // 易极付的创建交易的支付是由公司国内的账户向国际的账户付款
            // 查询的商户id
            String partnerIdQuery = getPartnerId("query");
            // 查询的商户对应的KEY
            String key = getPartnerKey("query");
            // 参数Map
            Map<String, String> map = new HashMap<String, String>();
            // 请求号(必填，并且每次请求都必须不同)
            map.put(YijipayConstants.ORDER_NO, Ids.oid());
            // 商户ID
            map.put(YijipayConstants.PARTNER_ID, partnerIdQuery);
            // 业务请求号(是否必填请联系实施人员)
            map.put("merchOrderNo", stockoutOrderBO.getBizId());
            // 服务码
            map.put("service", "tradeInfoQuery");
            // 服务版本
            map.put("version", "1.0");
            // 交易号或者订单号
            map.put("billNo", stockoutOrderBO.getDeclarePayNo());
            // 类型 1:代表传入的是交易号 2:代表传入的是订单号
            map.put("billNoType", "1");
            request = JSONObject.toJSONString(map);
            response = YijifuGateway.getOpenApiClientService().doPost(url, map, key);
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-易极付-交易单笔查询tradeInfoQuery]")
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .log();

            if (response == null) {
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }
            JSONObject resJson = JSON.parseObject(response);
            if (resJson.containsKey("resultCode") && resJson.getString("resultCode").equals("EXECUTE_SUCCESS")) {
                tradeStatus = resJson.getString("tradeStatus");
            } else if (resJson.containsKey("resultCode") && resJson.getString("resultCode").equals("TRADE_NOT_EXIST_EXCEPTION")) {
                return null;
            } else {
                return null;
            }
            return resJson;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-易极付-交易单笔查询tradeInfoQuery异常]")
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION, e.getMessage());
        }
    }

    /**
     * 易极付-创建交易订单tradeOrderCreate
     *
     * @return 交易号
     * @throws Exception
     */
    public String tradeOrderCreate() throws ServiceException {
        String tradeNo = null;
        String request = null;
        String response = null;
        try {
            // 易极付接口调用地址url
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);;
            // 易极付的创建交易的支付是由公司国内的账户向国际的账户付款
            // 国内的商户id
            String partnerIdDomestic = getPartnerId("domestic");
            // 国内的商户对应的KEY
            String key = getPartnerKey("domestic");
            // 国际的商户id
            String partnerIdInternational = getPartnerId("international");
            // 用户支付金额
            Integer payPrice = stockoutOrderDeclarePriceBO.getOrderActualPrice();

            // 参数Map
            Map<String, String> map = new HashMap<String, String>();
            // 请求号(必填，并且每次请求都必须不同)
            map.put(YijipayConstants.ORDER_NO, Ids.oid());
            // 商户ID
            map.put(YijipayConstants.PARTNER_ID, partnerIdDomestic);
            // 业务请求号(是否必填请联系实施人员)
            map.put("merchOrderNo", stockoutOrderBO.getBizId());
            // 服务码
            map.put("service", "tradeOrderCreate");
            // 服务版本
            map.put("version", "1.0");
            // 付款人id,非必填
            map.put("payerUserId", partnerIdInternational);
            // 买家id,非必填
            map.put("buyerUserId", partnerIdInternational);
            // 卖家id,必填
            map.put("sellerUserId", partnerIdDomestic);
            // 交易额,非必填
            map.put("tradeAmount", NumberUtil.defaultParsePriceFeng2Yuan(payPrice, null));
            // 交易名称,非必填
            // mapTradeOrderCreate.put("tradeName", stockoutOrderBO.getBizNo());
            // 交易备注,非必填
            map.put("tradeMemo", stockoutOrderBO.getRemarks());
            // 外部订单号,必填
            map.put("outOrderNo", stockoutOrderBO.getBizId());

            // 商品条款
            JSONArray yijifuClausesArray = new JSONArray();
//            List<StockoutOrderSkuDO> resultAfterFilter = CommonUtil.removeMixedSku(stockoutOrderSkuDOs);
//            List<StockoutOrderSkuDO> resultAfterMerge = CommonUtil.mergeStockoutOrderSku(resultAfterFilter, Boolean.FALSE);
            for (StockoutOrderDetailBO stockoutOrderDetailBO : stockoutOrderDetailBOList) {
                YijifuGoodsClauses goodsClauses = new YijifuGoodsClauses();
                // 商品ID
                goodsClauses.setOutId(String.valueOf(stockoutOrderDetailBO.getSkuId()));
                // 商品名称
                goodsClauses.setName(stockoutOrderDetailBO.getSkuName());
                // 商品详情
                goodsClauses.setMemo(stockoutOrderDetailBO.getRemark());
                // 商品单价
                goodsClauses.setPrice(NumberUtil.defaultParsePriceFeng2Yuan(declarePriceDetailMap.get(stockoutOrderDetailBO.getSkuId()).getSalePrice(), 2));
                // 商品数量
                goodsClauses.setQuantity(stockoutOrderDetailBO.getQuantity().toString());
                // 商品单位
                // goodsClauses.setUnit(stockoutOrderDetailBO.getUnit());
                // 商品类目
                // goodsClauses.setCategory(skuDO.getCategory());
                yijifuClausesArray.add(goodsClauses);
            }
            map.put("goodsClauses", yijifuClausesArray.toJSONString());
            request = JSONObject.toJSONString(map);
            response = YijifuGateway.getOpenApiClientService().doPost(url, map, key);
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-易极付-创建交易订单tradeOrderCreate]")
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .log();

            if (response == null) {
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }
            JSONObject resJson = JSON.parseObject(response);
            if (resJson.containsKey("resultCode") && resJson.getString("resultCode").equals("EXECUTE_SUCCESS")) {
                tradeNo = resJson.getString("tradeNo");
                // 将易极付的支付流水号tradeNo设定进payNo
                stockoutOrderBO.setDeclarePayNo(tradeNo);
                stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
                stockoutOrderManager.updatePayNo(stockoutOrderBO.getId(), tradeNo);
            } else {
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }
            return tradeNo;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-易极付-创建交易订单tradeOrderCreate异常]")
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION, e.getMessage());
        }
    }


    /**
     * 易极付-代付支付fsTradePayByFriend
     *
     * @param tradeNo 交易号
     * @return
     * @throws Exception
     */
    public void fsTradePayByFriend(String tradeNo) throws ServiceException {

        String request = null;
        String response = null;
        try {
            // 易极付接口调用地址url
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);;
            // 易极付的创建交易的支付是由公司国内的账户向国际的账户付款
            // 国内的商户id
            String partnerIdDomestic = getPartnerId("domestic");
            // 国内的商户对应的KEY
            String key = getPartnerKey("domestic");
            // 国际的商户id
            String partnerIdInternational = getPartnerId("international");

            // 参数Map
            Map<String, String> map = new HashMap<String, String>();
            // 请求号
            map.put(YijipayConstants.ORDER_NO, Ids.oid());
            // 商户ID
            map.put(YijipayConstants.PARTNER_ID, partnerIdDomestic);
            // 业务请求号
            map.put("merchOrderNo", stockoutOrderBO.getBizId());
            // 服务码
            map.put("service", "fsTradePayByFriend");
            // 服务版本
            map.put("version", "1.0");
            // 付款人id,必填
            map.put("payerUserId", partnerIdInternational);
            // 易极付交易号,必填
            map.put("tradeNo", tradeNo);
            // 商户订单号
            map.put("merchantOrderNo", stockoutOrderBO.getBizId());

            request = JSONObject.toJSONString(map);
            response = YijifuGateway.getOpenApiClientService().doPost(url, map, key);
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-易极付-代付支付fsTradePayByFriend]")
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .log();

            if (response == null) {
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }
            JSONObject resJson = JSON.parseObject(response);
            if (resJson.containsKey("resultCode") && resJson.getString("resultCode").equals("EXECUTE_SUCCESS")) {
                tradeNo = resJson.getString("tradeNo");
            } else {
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-易极付-代付支付fsTradePayByFriend异常]")
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION, e.getMessage());
        }
    }

    /**
     * 易极付-支付单上传singlePaymentUpload
     *
     * @param tradeNo 交易号
     * @return
     *
     * @throws Exception
     */
    public boolean singlePaymentUpload(String tradeNo) throws ServiceException {
        String request = null;
        String response = null;
        try {
            // 易极付接口调用地址url
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);;
            // 易极付的创建交易的支付是由公司国内的账户向国际的账户付款
            // 申报的商户id
            String partnerIdDeclare = getPartnerId("declare");
            // 申报的商户对应的KEY
            String key = getPartnerKey("declare");

            // 参数Map
            Map<String, String> map = new HashMap<String, String>();
            map.put(YijipayConstants.ORDER_NO, Ids.oid());
            //商户ID
            map.put(YijipayConstants.PARTNER_ID, partnerIdDeclare);
            //业务请求号(是否必填请联系实施人员)
            map.put("merchOrderNo", stockoutOrderBO.getBizId());
            //服务码
            map.put("service", "singlePaymentUpload");
            //服务版本
            map.put("version", "1.0");
            //支付单业务类型编码,非必填
            map.put("orderFlowType", "NORMAL");

            // 海关备案相关
            //电商平台企业名称,非必填
            // map.put("eplatEntName", "易极付");
            //电商平台企业编码,非必填
            // map.put("eplatEntCode", "Q001");
            if(PortNid.HANGZHOU.getNid().equals(logisticsLineBO.getPortBO().getPortNid())){
                //电商商户企业名称,必填 （电商在海关的企业备案名称）
                map.put("eshopEntName", logisticsLineBO.getPortBO().geteCommerceName());
                //电商商户企业代码,必填 （电商在海关的企业备案编码）
                map.put("eshopEntCode", logisticsLineBO.getPortBO().geteCommerceCode());
            }else {
                //电商商户企业名称,必填 （电商在海关的企业备案名称）
                map.put("eshopEntName", PortUtil.getCustomsName(logisticsLineBO));
                //电商商户企业代码,必填 （电商在海关的企业备案编码）
                map.put("eshopEntCode", PortUtil.getCustomsCode(logisticsLineBO));
            }

            //各个口岸的meta信息中需要填写在支付申报企业的海关编码
            Map<String, Object> portMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getPortBO().getMeta());
            if (portMeta.containsKey("yijifu_eport_code") && StringUtils.isNotBlank(portMeta.get("yijifu_eport_code").toString())) {
                //申报海关代码,必填
                map.put("customsCode", portMeta.get("yijifu_eport_code").toString());
            } else {
                throw new IllegalArgumentException("易极付支付在口岸元信息中的 yijifu_eport_code 值不存在");
            }
            //外部订单号,必填
            map.put("outOrderNo", stockoutOrderBO.getBizId());
            //支付交易号,必填
            map.put("tradeNo", "['" + tradeNo + "']");
            //支付方式,非必填
            // map.put("paymentType", "ALIPAY");

            //证件 购买人如果我们传则用我们的报关 否则用其他收集的
            String idType = stockoutOrderBO.getDeclarePayerCertType();
            if (StringUtils.isNotBlank(idType)) {
                idType = stockoutOrderBO.getDeclarePayerCertType();
            }
            String userName = stockoutOrderBO.getDeclarePayerName();
            if (StringUtils.isBlank(userName)) {
                userName = stockoutOrderBO.getBuyerBO().getBuyerName();
            }
            String userIdNo = stockoutOrderBO.getDeclarePayerCertNo();
            if (StringUtils.isBlank(userIdNo)) {
                userIdNo = stockoutOrderBO.getBuyerBO().getBuyerCertNo();
            }
            //支付人证件类型,必填
            // 身份证
            if ("1".equals(idType)) {
                map.put("payerDocType", "Identity_Card");
            // 护照
            } else if ("2".equals(idType)) {
                map.put("payerDocType", "Passport");
            // 其他
            } else if ("3".equals(idType)) {
                map.put("payerDocType", "Other");
            //  默认身份证
            } else {
                map.put("payerDocType", "Identity_Card");
            }
            //支付人证件号码,必填
            map.put("payerId", userIdNo);
            //支付人姓名,必填
            map.put("payerName", userName);

            //货款币种,必填
            map.put("goodsCurrency", PriceUnit.CNY);
            //货款金额,必填
            map.put("goodsAmount", NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getGoodsTotalPrice(), 2));
            //税款币种,必填
            map.put("taxCurrency", PriceUnit.CNY);
            //税款金额,必填
            map.put("taxAmount", NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getTaxFee(), 2));
            //物流币种,必填
            map.put("freightCurrency", PriceUnit.CNY);
            //物流金额,必填
            map.put("freightAmount", NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getFreightFee(), 2));

            //业务类型,深圳海关必填,非必填 STORE:暂存 DECLARE:申报
            map.put("appStatus", "DECLARE");
            //进出口标示，郑州海关必填,非必填 IMPORT:进口 EXPORT:出口
            map.put("ieType", "IMPORT");
            //电商平台互联网域名,非必填
            map.put("eplatDNS", "http://www.fengqu.com");

            // 广州口岸必填
            if (PortNid.GUANGZHOU.getNid().equals(logisticsLineBO.getPortBO().getPortNid())) {
                //电商平台代码(国检平台),非必填
                // map.put("eplatCodeForNgct", "562568412354");
                //电商企业代码（国检平台）,非必填
                // map.put("eEntCodeForNgct", "231461253");
                //国检编码,非必填
                map.put("ngtcCode", "442300");
            }
            //操作类型,非必填 ADD:新增 MODIFY:修改 DELETE:删除
            map.put("operationType", "ADD");

            // 异步通知URL
            String notifyUrl = PayConfig.getPayProviderCallbackUrl(payBillDeclareProviderNid);
            map.put("notifyUrl", notifyUrl);

            request = JSONObject.toJSONString(map);
            response = YijifuGateway.getOpenApiClientService().doPost(url, map, key);
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-易极付-支付单上传singlePaymentUpload]")
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .log();

            if (response == null) {
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }
            JSONObject resJson = JSON.parseObject(response);
            if (resJson.containsKey("resultCode") && resJson.getString("resultCode").equals("EXECUTE_SUCCESS")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-易极付-支付单上传singlePaymentUpload异常]")
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION, e.getMessage());
        }
    }

    /**
     * 获取商户ID
     *
     * @return
     * @throws Exception
     */
    private String getPartnerId(String key) throws Exception {
        String partnerIds = PayConfig.getECodeOnPayProvider(payBillDeclareProviderNid);
        Map<String, Object> partnerIdMap = JSONUtil.parseJSONMessage(partnerIds);
        String partnerId;
        String partnerIdS;
        if(partnerIdMap.containsKey(key)){
            partnerIdS = (String)partnerIdMap.get(key);
        }else{
            partnerIdS = (String)partnerIdMap.get("DEFAULT");
        }
        if(StringUtils.isNotBlank(partnerIdS) && partnerIdS.contains(",")){
            String[] merchantIdArray = partnerIdS.split(",");
            partnerId = merchantIdArray[0].trim();
        }else{
            partnerId = partnerIdS;
        }
        return partnerId;
    }

    /**
     * 获取商户号对应的KEY
     *
     * @return
     * @throws Exception
     */
    private String getPartnerKey(String key) throws Exception {
        String signKeys = PayConfig.getPayProviderInterfaceKey(payBillDeclareProviderNid);
        Map<String, Object> signKeyMap = JSONUtil.parseJSONMessage(signKeys);
        String signKey;
        String signKeyS;
        if(signKeyMap.containsKey(key)){
            signKeyS = (String)signKeyMap.get(key);
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