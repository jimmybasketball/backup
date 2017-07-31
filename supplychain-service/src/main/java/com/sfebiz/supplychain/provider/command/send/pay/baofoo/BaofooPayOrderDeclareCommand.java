package com.sfebiz.supplychain.provider.command.send.pay.baofoo;

import com.alibaba.fastjson.JSONObject;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.config.order.OrderConfig;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.user.api.RealNameAuthenticationService;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.pay.baofoo.*;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderDeclareCommand;
import com.sfebiz.supplychain.provider.entity.PriceUnit;
import com.sfebiz.supplychain.service.port.PortUtil;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.NumberUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>宝付支付单申报</p>
 * User: tanzongxi
 * Date: 17/05/16
 */
public class BaofooPayOrderDeclareCommand extends PayOrderDeclareCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private StockoutOrderManager stockoutOrderManager;
    private RealNameAuthenticationService realNameAuthenticationService;

    @Override
    public boolean execute() throws ServiceException {
        boolean result = false;
        try {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setMsg("宝付支付单申报")
                    .addParm("出库单信息：", stockoutOrderBO)
                    .addParm("出库单金额相关数据：", stockoutOrderDeclarePriceBO)
                    .log();

            if (portBillDeclareDO == null || stockoutOrderBO == null || StringUtils.isBlank(payBillDeclareProviderNid)
                    || logisticsLineBO == null || logisticsLineBO.getPortBO() == null) {
                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .setMsg("宝付支付单申报单参数缺失")
                        .addParm("订单号：", stockoutOrderBO.getBizId())
                        .log();
                return false;
            }
            if (PortBillState.SEND_SUCCESS.getValue().equals(portBillDeclareDO.getState())
                    || PortBillState.VERIFY_CALLBACK.getValue().equals(portBillDeclareDO.getState())) {
                return true;
            }

            boolean isMockAutoCreated = MockConfig.isMocked("baofoopay", "createCommand");
            if (isMockAutoCreated) {
                //直接返回
                logger.info("MOCK：宝付支付单申报 采用MOCK实现");
                return mockPayBillDeclareSuccess();
            }

            // 是否跳过订单
            String SKIP_ORDERS = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "skip_orders");
            if (StringUtils.isNotBlank(SKIP_ORDERS)) {
                String[] skipOrders = SKIP_ORDERS.split(",");
                if (Arrays.asList(skipOrders).contains(stockoutOrderBO.getBizId())) {
                    LogBetter.instance(logger).setLevel(LogLevel.INFO)
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                            .setMsg("宝付支付单申报-跳过订单")
                            .addParm("订单号：", stockoutOrderBO.getBizId())
                            .log();
                    result = true;
                    return result;
                }
            }

            // 实名认证
            realNameAuthenticationService = (RealNameAuthenticationService) CommandConfig.getSpringBean("realNameAuthenticationService");
            String buyerName = CommonUtil.getBuyerName(stockoutOrderBO);
            String buyerIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);
            if (!OrderConfig.getIsSkipRealNameAuthentication(stockoutOrderBO.getBizId())
                    && !realNameAuthenticationService.rz(buyerName, buyerIdNo)) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.WARN)
                        .setMsg("[供应链报文-向宝付下发支付单申报指令失败]购买人的实名认证失败！")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("出库单ID", stockoutOrderBO.getId())
                        .addParm("购买人姓名", buyerName)
                        .log();
                createFailureMessage = "收货人的实名认证失败,购买人姓名:" + buyerName;
                return false;
            } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-向宝付下发支付单申报指令成功] 购买人的实名认证成功！")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("出库单ID", stockoutOrderBO.getId())
                        .addParm("购买人姓名", buyerName)
                        .log();
            }

            // 宝付-代理跨境结算订单查询baofooOrderQuery
            if (!baofooOrderQuery()) {
                // 宝付-代理跨境结算订单上传baofooOrderSubmit
                baofooOrderSubmit();
            }
            // 宝付-支付单查询baofooOrderQuery
            if (!baofooDeclareQuery()) {
                // 宝付-支付单报送baofooDeclareSubmit
                baofooDeclareSubmit();
            }
            result = true;
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .addParm("宝付支付单申报请求报文", e)
                    .setException(e)
                    .log();
        }
        return result;
    }

    /**
     * 宝付-代理跨境结算订单上传baofooOrderSubmit
     *
     * @return 交易号
     * @throws Exception
     */
    public void baofooOrderSubmit() throws ServiceException {
        String orderId = null;
        String request = null;
        String response = null;
        try {
            // 宝付接口调用地址url
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);;
            // 商户号：（商户在宝付注册时提供）
            String MEMBER_ID = getPartnerId("DEFAULT");
            // 终端号：（商户在宝付注册时提供）
            String TERMINAL_ID = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "terminal_id");
            // 商户数字证书密码
            String KEY_PWD = getPartnerKey("DEFAULT");
            // 商户密钥文件：（商户在宝付注册时提供）
            String PRIVATE_FILE = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "private_file");
            // 公钥内容：（商户在宝付注册时提供）
            String PUBLIC_FILE = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "public_file");

            // 用户支付金额
            Integer payPrice = stockoutOrderDeclarePriceBO.getOrderActualPrice();

            //证件 购买人如果我们传则用我们的报关 否则用其他收集的
            String userName = CommonUtil.getBuyerName(stockoutOrderBO);
            String userIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);
            String userTele = stockoutOrderBO.getBuyerBO().getBuyerTelephone();

            BaofooOrderRequest requestParams = new BaofooOrderRequest();
            // 商户号
            requestParams.setMemberId(MEMBER_ID);
            // 终端号
            requestParams.setTerminalId(TERMINAL_ID);
            // 商户订单号
            requestParams.setMemberTransId(stockoutOrderBO.getBizId());
            // 订单金额
            requestParams.setOrderAmt(NumberUtil.defaultParsePriceFeng2Yuan(payPrice, 2));
            // 订单币种
            requestParams.setOrderCcy(PriceUnit.CNY);
            // 交易金额
            requestParams.setTransAmt(NumberUtil.defaultParsePriceFeng2Yuan(payPrice, 2));
            // 交易币种
            requestParams.setTransCcy(PriceUnit.CNY);
            // 身份证号码
            requestParams.setIdCardNo(userIdNo);
            // 姓名
            requestParams.setIdName(userName);
            // 手机号码
            requestParams.setMobile(userTele);
            // 银行卡号
            requestParams.setBankCardNo(stockoutOrderBO.getMerchantOrderNo());
            // 商品信息
            List<BaofooOrderItemRequest> orderItems = new ArrayList<BaofooOrderItemRequest>();
//            List<StockoutOrderSkuDO> resultAfterFilter = CommonUtil.removeMixedSku(stockoutOrderSkuDOs);
//            List<StockoutOrderSkuDO> resultAfterMerge = CommonUtil.mergeStockoutOrderSku(resultAfterFilter, Boolean.FALSE);
            for (StockoutOrderDetailBO skuDO : stockoutOrderDetailBOList) {
                BaofooOrderItemRequest orderItem = new BaofooOrderItemRequest();
                orderItem.setGoodsName(skuDO.getSkuName());
                orderItem.setGoodsNum(skuDO.getQuantity());
                orderItem.setGoodsPrice(NumberUtil.defaultParsePriceFeng2Yuan(declarePriceDetailMap.get(skuDO.getSkuId()).getDeclarePrice(), 2));
                orderItems.add(orderItem);
            }
            requestParams.setGoodsInfo(orderItems);

            // 请求内容转换成json数据格式
            request = JSONObject.toJSONString(requestParams);
            String encrypt = BaofooRsaCodingUtil.encryptByPriPfxFile(request, PRIVATE_FILE, KEY_PWD);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            UrlEncodedFormEntity entity = setCommonParams(MEMBER_ID, TERMINAL_ID, encrypt);
            HttpPost post = new HttpPost(url + "/cbpay/order/proxy/submit");
            post.setEntity(entity);
            CloseableHttpResponse res = httpClient.execute(post);
            response = EntityUtils.toString(res.getEntity());

            // 返回为空
            if (response == null) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-宝付-代理跨境结算订单上传baofooOrderSubmit失败]")
                        .addParm("请求报文", request)
                        .addParm("回复报文", "null")
                        .log();
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }

            // 返回结果转换成对象
            BaofooBaseResult baseResult = JSONObject.parseObject(response, BaofooBaseResult.class);
            // 判断返回成功失败
            if (baseResult.getSuccess()) {
                String decrypt = BaofooRsaCodingUtil.decryptByPubCerFile(baseResult.getResult(), PUBLIC_FILE);
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-宝付-代理跨境结算订单上传baofooOrderSubmit成功]")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("请求报文", request)
                        .addParm("回复报文", response)
                        .addParm("解密数据", decrypt)
                        .log();
                JSONObject decryptObj = JSONObject.parseObject(decrypt);
                orderId  = String.valueOf(decryptObj.get("orderId"));
                stockoutOrderBO.setDeclarePayNo(orderId);
                stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
                stockoutOrderManager.updatePayNo(stockoutOrderBO.getId(), orderId);
            } else {
//                if ("商户订单号已创建支付单".equals(baseResult.getErrorMsg())) {
//                    LogBetter.instance(logger)
//                            .setLevel(LogLevel.INFO)
//                            .setMsg("[供应链报文-宝付-代理跨境结算订单上传baofooOrderSubmit成功]")
//                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
//                            .addParm("请求报文", request)
//                            .addParm("回复报文", response)
//                            .log();
//                } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("[供应链报文-宝付-代理跨境结算订单上传baofooOrderSubmit失败]")
                        .addParm("请求报文", request)
                        .addParm("回复报文", response)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .log();
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
//                }

            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-宝付-代理跨境结算订单上传baofooOrderSubmit异常]")
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION, e.getMessage());
        }
    }

    /**
     * 宝付-支付单报送baofooDeclareSubmit
     *
     * @throws Exception
     */
    public void baofooDeclareSubmit() throws ServiceException {
        String functionId = null;
        String request = null;
        String response = null;
        try {
            // 宝付接口调用地址url
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);;
            // 商户号：（商户在宝付注册时提供）
            String MEMBER_ID = getPartnerId("DEFAULT");
            // 终端号：（商户在宝付注册时提供）
            String TERMINAL_ID = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "terminal_id");
            // 商户数字证书密码
            String KEY_PWD = getPartnerKey("DEFAULT");
            // 异步通知URL
            String notifyUrl = PayConfig.getPayProviderCallbackUrl(payBillDeclareProviderNid);
            // 商户密钥文件：（商户在宝付注册时提供）
            String PRIVATE_FILE = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "private_file");
            // 公钥内容：（商户在宝付注册时提供）
            String PUBLIC_FILE = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "public_file");
            //各个口岸的meta信息中需要填写在连连支付中的海关编码
            Map<String, Object> portMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getPortBO().getMeta());
            if (portMeta.containsKey("baofoo_eport_code") && StringUtils.isNotBlank(portMeta.get("baofoo_eport_code").toString())) {
                //申报海关代码,必填
                functionId = portMeta.get("baofoo_eport_code").toString();
            } else {
                throw new IllegalArgumentException("宝付在" + logisticsLineBO.getPortBO().getName() + "口岸元信息中的baofoo_eport_code值不存在");
            }
            //电商在海关的企业备案编码
            String companyCode = null;
            if(PortNid.HANGZHOU.getNid().equals(logisticsLineBO.getPortBO().getPortNid())){
                companyCode = logisticsLineBO.getPortBO().geteCommerceCode();
            }else {
                companyCode = PortUtil.getCustomsCode(logisticsLineBO);
            }

            // 用户支付金额
            Integer payPrice = stockoutOrderDeclarePriceBO.getOrderActualPrice();

            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

            BaofooDeclareRequest requestParams = new BaofooDeclareRequest();
            // 版本号
            requestParams.setVersion("1.0.0");
            // 商户备案申请编号
            requestParams.setMemberApplyNo(stockoutOrderBO.getBizId()+"X");
            // 商户号
            requestParams.setMemberId(MEMBER_ID);
            // 终端号
            requestParams.setTerminalId(TERMINAL_ID);
            // 商户订单号
            requestParams.setMemberTransId(stockoutOrderBO.getBizId());
            // 商户订单日期yyyyMMddHHmmss
            requestParams.setMemberTransDate(format.format(stockoutOrderBO.getGmtCreate()));
            // 海关关区代码
            requestParams.setFunctionId(functionId);
            // 电商订单编号
            requestParams.setCompanyOrderNo(stockoutOrderBO.getBizId());
            // 支付总金额
            requestParams.setPayTotalAmount(NumberUtil.defaultParsePriceFeng2Yuan(payPrice, 2));
            // 支付货款
            requestParams.setPayGoodsAmount(NumberUtil.defaultParsePriceFeng2Yuan(payPrice, 2));
            // 支付税款
            requestParams.setPayTaxAmount("0");
            // 支付运费
            requestParams.setPayFeeAmount("0");
            // 支付保费
            requestParams.setPayPreAmount("0");
            // 币种
            requestParams.setCcy(PriceUnit.CNY);
            // 结果通知地址
            requestParams.setNotifyUrl(notifyUrl);
            // 商户备案号
            requestParams.setCompanyCode(companyCode);
            // 备注
            requestParams.setRemarks("");

            // 请求内容转换成json数据格式
            request = JSONObject.toJSONString(requestParams);
            String encrypt = BaofooRsaCodingUtil.encryptByPriPfxFile(request, PRIVATE_FILE, KEY_PWD);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            UrlEncodedFormEntity entity = setCommonParams(MEMBER_ID, TERMINAL_ID, encrypt);
            HttpPost post = new HttpPost(url + "/declare");
            post.setEntity(entity);
            CloseableHttpResponse res = httpClient.execute(post);
            response = EntityUtils.toString(res.getEntity());

            // 返回为空
            if (response == null) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-宝付-支付单报送baofooDeclareSubmit失败]")
                        .addParm("请求报文", request)
                        .addParm("回复报文", "null")
                        .log();
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }

            // 返回结果转换成对象
            BaofooBaseResult baseResult = JSONObject.parseObject(response, BaofooBaseResult.class);
            // 判断返回成功失败
            if (baseResult.getSuccess()) {
                String decrypt = BaofooRsaCodingUtil.decryptByPubCerFile(baseResult.getResult(), PUBLIC_FILE);
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-宝付-支付单报送baofooDeclareSubmit成功]")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("请求报文", request)
                        .addParm("回复报文", response)
                        .addParm("解密数据", decrypt)
                        .log();
            } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("[供应链报文-宝付-支付单报送baofooDeclareSubmit失败]")
                        .addParm("请求报文", request)
                        .addParm("回复报文", response)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .log();
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-宝付-支付单报送baofooDeclareSubmit异常]")
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION, e.getMessage());
        }
    }

    /**
     * 宝付-代理跨境结算订单查询baofooOrderQuery
     *
     * @return 查询结果
     * @throws Exception
     */
    public boolean baofooOrderQuery() throws ServiceException {
        boolean result = false;
        String request = null;
        String response = null;
        try {
            // 宝付接口调用地址url
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);;
            // 商户号：（商户在宝付注册时提供）
            String MEMBER_ID = getPartnerId("DEFAULT");
            // 终端号：（商户在宝付注册时提供）
            String TERMINAL_ID = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "terminal_id");
            // 商户数字证书密码
            String KEY_PWD = getPartnerKey("DEFAULT");
            // 商户密钥文件：（商户在宝付注册时提供）
            String PRIVATE_FILE = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "private_file");
            // 公钥内容：（商户在宝付注册时提供）
            String PUBLIC_FILE = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "public_file");

            BaofooOrderRequest requestParams = new BaofooOrderRequest();
            // 商户订单号
            requestParams.setMemberTransId(stockoutOrderBO.getBizId());
            // 商户号
            requestParams.setMemberId(MEMBER_ID);

            // 请求内容转换成json数据格式
            request = JSONObject.toJSONString(requestParams);
            String encrypt = BaofooRsaCodingUtil.encryptByPriPfxFile(request, PRIVATE_FILE, KEY_PWD);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            UrlEncodedFormEntity entity = setCommonParams(MEMBER_ID, TERMINAL_ID, encrypt);
            HttpPost post = new HttpPost(url + "/orderQuery");
            post.setEntity(entity);
            CloseableHttpResponse res = httpClient.execute(post);
            response = EntityUtils.toString(res.getEntity());

            // 返回为空
            if (response == null) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-宝付-代理跨境结算订单查询baofooOrderQuery失败]")
                        .addParm("请求报文", request)
                        .addParm("回复报文", "null")
                        .log();
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }

            // 返回结果转换成对象
            BaofooBaseResult baseResult = JSONObject.parseObject(response, BaofooBaseResult.class);
            // 判断返回成功失败
            if (baseResult.getSuccess()) {
                String decrypt = BaofooRsaCodingUtil.decryptByPubCerFile(baseResult.getResult(), PUBLIC_FILE);
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-宝付-代理跨境结算订单查询baofooOrderQuery成功]")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("请求报文", request)
                        .addParm("回复报文", response)
                        .addParm("解密数据", decrypt)
                        .log();
                result = true;
            } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-宝付-代理跨境结算订单查询baofooOrderQuery成功]")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("请求报文", request)
                        .addParm("回复报文", response)
                        .log();
                result = false;
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-宝付-代理跨境结算订单查询baofooOrderQuery异常]")
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION, e.getMessage());
        }
        return result;
    }

    /**
     * 宝付-支付单查询baofooDeclareQuery
     *
     * @return 查询结果
     * @throws Exception
     */
    public boolean baofooDeclareQuery() throws ServiceException {
        boolean result = false;
        String request = null;
        String response = null;
        try {
            // 宝付接口调用地址url
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);;
            // 商户号：（商户在宝付注册时提供）
            String MEMBER_ID = getPartnerId("DEFAULT");
            // 终端号：（商户在宝付注册时提供）
            String TERMINAL_ID = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "terminal_id");
            // 商户数字证书密码
            String KEY_PWD = getPartnerKey("DEFAULT");
            // 商户密钥文件：（商户在宝付注册时提供）
            String PRIVATE_FILE = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "private_file");
            // 公钥内容：（商户在宝付注册时提供）
            String PUBLIC_FILE = LogisticsDynamicConfig.getPay().getRule("provider", payBillDeclareProviderNid, "public_file");

            BaofooDeclareRequest requestParams = new BaofooDeclareRequest();
            // 版本号
            requestParams.setVersion("1.0.0");
            // 商户号
            // requestParams.setMemberId(MEMBER_ID);
            // 终端号
            requestParams.setTerminalId(TERMINAL_ID);
            // 商户订单号
            requestParams.setMemberApplyNo(stockoutOrderBO.getBizId()+"X");
            // 商户号
            requestParams.setMemberId(MEMBER_ID);

            // 请求内容转换成json数据格式
            request = JSONObject.toJSONString(requestParams);
            String encrypt = BaofooRsaCodingUtil.encryptByPriPfxFile(request, PRIVATE_FILE, KEY_PWD);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            UrlEncodedFormEntity entity = setCommonParams(MEMBER_ID, TERMINAL_ID, encrypt);
            HttpPost post = new HttpPost(url + "/declare/query");
            post.setEntity(entity);
            CloseableHttpResponse res = httpClient.execute(post);
            response = EntityUtils.toString(res.getEntity());

            // 返回为空
            if (response == null) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-宝付-支付单查询baofooDeclareQuery失败]")
                        .addParm("请求报文", request)
                        .addParm("回复报文", "null")
                        .log();
                throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION);
            }

            // 返回结果转换成对象
            BaofooBaseResult baseResult = JSONObject.parseObject(response, BaofooBaseResult.class);
            // 判断返回成功失败
            if (baseResult.getSuccess()) {
                String decrypt = BaofooRsaCodingUtil.decryptByPubCerFile(baseResult.getResult(), PUBLIC_FILE);
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-宝付-支付单查询baofooDeclareQuery成功]")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("请求报文", request)
                        .addParm("回复报文", response)
                        .addParm("解密数据", decrypt)
                        .log();
                result = true;
            } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-宝付-支付单查询baofooDeclareQuery成功]")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("请求报文", request)
                        .addParm("回复报文", response)
                        .log();
                result = false;
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-宝付-支付单查询baofooDeclareQuery异常]")
                    .addParm("请求报文", request)
                    .addParm("回复报文", response)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION, e.getMessage());
        }
        return result;
    }

    /**
     * 设定宝付接口调用的公共参数
     *
     * @param MEMBER_ID 商户号
     * @param TERMINAL_ID 终端号
     * @param encrypt 加密请求内容
     * @return
     * @throws UnsupportedEncodingException
     */
    private UrlEncodedFormEntity setCommonParams(String MEMBER_ID, String TERMINAL_ID, String encrypt) throws UnsupportedEncodingException {
        List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
        // 版本号
        valuePairs.add(new BasicNameValuePair("version", "1.0.0"));
        // 商户号
        valuePairs.add(new BasicNameValuePair("memberId", MEMBER_ID));
        // 终端号
        valuePairs.add(new BasicNameValuePair("terminalId", TERMINAL_ID));
        // 数据类型
        valuePairs.add(new BasicNameValuePair("dataType", "JSON"));
        // 加密数据
        valuePairs.add(new BasicNameValuePair("dataContent", encrypt));
        return new UrlEncodedFormEntity(valuePairs, "UTF-8");
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