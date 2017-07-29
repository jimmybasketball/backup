package com.sfebiz.supplychain.provider.command.send.pay.alipay;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.pay.alipay.AlipayBillDeclareResponse;
import com.sfebiz.supplychain.protocol.pay.alipay.AlipayErrorCode;
import com.sfebiz.supplychain.protocol.pay.alipay.ResponseContent;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderDeclareCommand;
import com.sfebiz.supplychain.service.port.PortUtil;
import com.sfebiz.supplychain.util.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * <p>支付宝支付单申报新接口
 * 支持子单申报
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/27
 * Time: 下午3:26
 */
public class AlipayOrderDeclareCommand extends PayOrderDeclareCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private volatile int messageId = 0;

    @Override
    public boolean execute() {
        if (portBillDeclareDO == null || stockoutOrderBO == null || StringUtils.isBlank(payBillDeclareProviderNid)
                || logisticsLineBO == null || logisticsLineBO.getPortBO() == null) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("支付宝支付支付单申报单参数缺失")
                    .addParm("订单号：", stockoutOrderBO.getBizId())
                    .log();
            return false;
        }


        boolean isMockAutoCreated = MockConfig.isMocked("aliPay", "createCommand");
        if (isMockAutoCreated) {
            //直接返回仓库已发货
            logger.info("MOCK：支付宝支付申报 采用MOCK实现");
            return mockPayBillDeclareSuccess();
        }

        String custom_code = new String();

        //各个口岸的meta信息中需要填写在支付宝支付中的海关编码
        Map<String, Object> portMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getPortBO().getMeta());
        if(logisticsLineBO.getPortBO().getPortNid().equals(PortNid.HANGZHOU.getNid())){
            if (PortBillState.SEND_SUCCESS.getValue().equals(portBillDeclareDO.getState())
                    || PortBillState.VERIFY_CALLBACK.getValue().equals(portBillDeclareDO.getState())) {
                if (portMeta.containsKey("hangzhou_alipay_eport_code")) {
                    custom_code = (String) portMeta.get("hangzhou_alipay_eport_code");
                } else {
                    throw new IllegalArgumentException("支付宝支付在口岸元信息中的alipay_eport_code值不存在");
                }
            }
        }


        try {
            String request = buildRequest(portMeta,custom_code);
            boolean result = sendMessage2DeclarePayBill(request);
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .addParm("支付宝支付支付单申报请求报文", e)
                    .setException(e)
                    .log();
        }
        return false;
    }


    /**
     * 构建待申报请求参数信息
     *
     * @return
     * @throws Exception
     */
    private String buildRequest(Map<String,Object> portMeta,String custom_code) throws Exception {
        StringBuilder request = new StringBuilder();
        //基本参数
        request.append("?service=alipay.acquire.customs");
        request.append("&partner=");//只有支付宝外卡才能支持申报
        request.append(PayConfig.getECodeOnPayProvider(payBillDeclareProviderNid));
        request.append("&_input_charset=utf-8");
        request.append("&sign_type=MD5");

        //业务参数
        request.append("&out_request_no=");
        String requestNo = stockoutOrderBO.getBizId();
        request.append(requestNo);
        request.append("&trade_no=");
        request.append(stockoutOrderBO.getMerchantPayNo());
        request.append("&merchant_customs_code=");
        request.append(PortUtil.getCustomsCode(logisticsLineBO));
        request.append("&merchant_customs_name=");
        request.append(PortUtil.getCustomsName(logisticsLineBO));
        request.append("&customs_place=");
        if(StringUtils.isNotBlank(custom_code)){
            request.append(custom_code);
        }else{
            //各个口岸的meta信息中需要填写在支付宝支付中的海关编码
            if (portMeta.containsKey("alipay_eport_code")) {
                request.append(portMeta.get("alipay_eport_code").toString());
            } else {
                throw new IllegalArgumentException("支付宝支付在口岸元信息中的alipay_eport_code值不存在");
            }
        }
        String userOrderTotal = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getOrderActualPrice(),2);
        request.append("&amount=");
        request.append(userOrderTotal);
        request.append("&is_split=T");
        request.append("&sub_out_biz_no=");
        request.append(stockoutOrderBO.getBizId());
        String env = (String) CommandConfig.getSpringBean("env");
        if ("DEV".equalsIgnoreCase(env) || "AUTOTEST".equalsIgnoreCase(env)) {
            request.append("&provider_hostname=unitradeprod.d15144aqcn.alipay.net");
        }

        request.append("&sign=");
        request.append(generateRequestSign(portMeta, userOrderTotal, requestNo,custom_code));
        return request.toString();
    }

    /**
     * 生成 签名
     *
     * @param portMeta
     * @return
     */
    private String generateRequestSign(Map<String, Object> portMeta, String amount, String requestNo,String custom_code) {
        ArrayList<String> parameters = new ArrayList<String>();
        parameters.add("service=alipay.acquire.customs");
        parameters.add("partner=" + PayConfig.getECodeOnPayProvider(payBillDeclareProviderNid));
        parameters.add("_input_charset=utf-8");
        parameters.add("out_request_no=" + requestNo);
        parameters.add("trade_no=" + stockoutOrderBO.getMerchantAccountId());
        parameters.add("merchant_customs_code=" + PortUtil.getCustomsCode(logisticsLineBO));
        parameters.add("merchant_customs_name=" + PortUtil.getCustomsName(logisticsLineBO));
        if(StringUtils.isNotBlank(custom_code)){
            parameters.add("customs_place=" + portMeta.get("hangzhou_alipay_eport_code").toString());
        }else{
            parameters.add("customs_place=" + portMeta.get("alipay_eport_code").toString());
        }
        parameters.add("amount=" + amount);
        parameters.add("is_split=T");
        parameters.add("sub_out_biz_no=" + stockoutOrderBO.getBizId());
        String env = (String) CommandConfig.getSpringBean("env");
        if ("DEV".equalsIgnoreCase(env)) {
            parameters.add("provider_hostname=unitradeprod.d15144aqcn.alipay.net");
        }

        Collections.sort(parameters);
        StringBuilder signOrgin = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            signOrgin.append(parameters.get(i));
            if (i < parameters.size() - 1) {
                signOrgin.append("&");
            }
        }

        signOrgin.append(PayConfig.getPayProviderInterfaceKey(payBillDeclareProviderNid));//追加 key信息
        String sign = MD5Util.md5Hex(signOrgin.toString());
        return sign;
    }


    /**
     * 给支付宝支付发送支付单申报信息
     *
     * @param requestBody 请求体字符串
     * @return
     */
    private boolean sendMessage2DeclarePayBill(String requestBody) {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .addParm("支付宝支付支付单申报请求报文", requestBody)
                .log();

        try {
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
            String response = HttpUtil.getByHttp(url, requestBody);
            if (response == null) {
                return false;
            }
            AlipayBillDeclareResponse alipayBillDeclareResponse = XMLUtil.converyToJavaBean(response, AlipayBillDeclareResponse.class);
            if (alipayBillDeclareResponse == null) {
                return false;
            }

            PortBillDeclareManager portBillDeclareManager = (PortBillDeclareManager) CommandConfig.getSpringBean("portBillDeclareManager");
            if ("T".equalsIgnoreCase(alipayBillDeclareResponse.getIsSuccess())) {
                if (alipayBillDeclareResponse.getResponseContentWrap() != null
                        && alipayBillDeclareResponse.getResponseContentWrap().getResponseContent() != null) {
                    ResponseContent responseContent = alipayBillDeclareResponse.getResponseContentWrap().getResponseContent();
                    if ("SUCCESS".equalsIgnoreCase(responseContent.getResultCode())) {
                        StockoutOrderManager stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
                        stockoutOrderBO.setDeclarePayNo(responseContent.getTradeNo());
                        stockoutOrderBO.setDeclarePayerName(responseContent.getBuyerName());
                        stockoutOrderBO.setDeclarePayerCertNo(responseContent.getBuyerCertNo());
                        stockoutOrderManager.updatePayBillInfo(stockoutOrderBO.getId(), responseContent.getTradeNo(), responseContent.getBuyerName(), responseContent.getBuyerCertNo());
                        portBillDeclareDO.setState(PortBillState.SEND_SUCCESS.getValue());
                        portBillDeclareDO.setResult("申报成功");
                        portBillDeclareDO.setBillSendTime(new Date());
                        portBillDeclareDO.setPortAcceptTime(new Date());
                        portBillDeclareManager.update(portBillDeclareDO);

                        LogBetter.instance(logger)
                                .setLevel(LogLevel.INFO)
                                .setMsg("[供应链报文-向支付宝支付下发支付单申报指令成功]")
                                .addParm("请求报文", requestBody)
                                .addParm("回复报文", response)
                                .log();

                        traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO,
                                "[供应链报文-向支付宝支付下发支付单申报指令成功]: "
                                        + "[请求报文: " + requestBody
                                        + ", 回复报文: " + response
                                        + "]"
                        ));

                        return true;
                    } else {
                        portBillDeclareDO.setState(PortBillState.SEND_EXCEPTION.getValue());
                        portBillDeclareDO.setResult(responseContent.getDetailErrorDes());
                        portBillDeclareDO.setBillSendTime(new Date());
                        portBillDeclareManager.update(portBillDeclareDO);
                        setCreateFailureMessage("支付宝申报失败：" + responseContent.getDetailErrorDes());
                        LogBetter.instance(logger)
                                .setLevel(LogLevel.INFO)
                                .setMsg("[供应链报文-向支付宝支付下发支付单申报指令失败]")
                                .addParm("请求报文", requestBody)
                                .addParm("回复报文", response)
                                .log();

                        traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO,
                                "[供应链报文-向支付宝支付下发支付单申报指令失败]: "
                                        + "[请求报文: " + requestBody
                                        + ", 回复报文: " + response
                                        + "]"
                        ));
                        return false;
                    }

                }
            } else {
                portBillDeclareDO.setState(PortBillState.SEND_EXCEPTION.getValue());
                portBillDeclareDO.setResult(AlipayErrorCode.getDescriptionByCode(alipayBillDeclareResponse.getError()));
                portBillDeclareDO.setBillSendTime(new Date());
                portBillDeclareManager.update(portBillDeclareDO);


                traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO,
                        "[供应链报文-向支付宝下发申报指令失败]: "
                                + "[请求报文: " + requestBody
                                + ", 回复报文: " + response
                                + "]"
                ));
            }

        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向支付宝支付下发支付单申报指令异常]")
                    .addParm("原因", e.getMessage())
                    .addParm("请求报文", requestBody)
                    .setException(e)
                    .log();

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.ERROR,
                    "[供应链报文-向支付宝支付下发支付单申报指令异常]: "
                            + "[请求报文: " + requestBody
                            + ", 异常信息: " + e.getMessage()
                            + "]"
            ));
        }
        return false;
    }

}
