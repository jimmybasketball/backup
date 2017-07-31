package com.sfebiz.supplychain.provider.command.send.pay.alipay;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.protocol.pay.alipay.AlipayBillDeclareResponse;
import com.sfebiz.supplychain.protocol.pay.alipay.ResponseContent;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderQueryCommand;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.MD5Util;
import com.sfebiz.supplychain.util.XMLUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * <p>支付宝支付单申报状态查询</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/26
 * Time: 下午3:14
 */
public class AlipayPayOrderQueryCommand extends PayOrderQueryCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    @Override
    public boolean execute() {
        try {
            String request = buildRequest();
            boolean result = sendMessage2QueryPayBillState(request);
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .addParm("连连支付支付单申报请求报文", e)
                    .setException(e)
                    .log();
        }
        return true;
    }

    /**
     * 构建请求参数信息
     *
     * @return
     * @throws Exception
     */
    private String buildRequest() throws Exception {
        StringBuilder request = new StringBuilder();
        //基本参数
        request.append("?service=alipay.overseas.acquire.customs.query");
        request.append("&partner=");//只有支付宝外卡才能支持申报
        request.append(PayConfig.getECodeOnPayProvider(payBillDeclareProviderNid));
        request.append("&_input_charset=utf-8");
        request.append("&sign_type=MD5");
        request.append("&sendFormat=normal");

        //业务参数
        request.append("&out_request_no=");
        String requestNo = stockoutOrderBO.getBizId();
        request.append(requestNo);
        request.append("&sign=");
        request.append(generateRequestSign(requestNo));
        return request.toString();
    }

    /**
     * 生成 签名
     *
     * @param requestNo
     * @return
     */
    private String generateRequestSign(String requestNo) {
        ArrayList<String> parameters = new ArrayList<String>();
        parameters.add("service=alipay.acquire.customs");
        parameters.add("partner=" + PayConfig.getECodeOnPayProvider(payBillDeclareProviderNid));
        parameters.add("_input_charset=utf-8");
        parameters.add("out_request_no=" + requestNo);
        parameters.add("sendFormat= normal");

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
     * 给支付宝发送支付单申报查询信息
     *
     * @param requestBodyJsonString 请求体JSON字符串
     * @return
     */
    private boolean sendMessage2QueryPayBillState(String requestBodyJsonString) {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .addParm("支付宝支付申报查询请求报文", requestBodyJsonString)
                .log();

        try {
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
            String response = HttpUtil.getByHttp(url, requestBodyJsonString);
            if (response == null) {
                return false;
            }
            AlipayBillDeclareResponse alipayBillDeclareResponse = XMLUtil.converyToJavaBean(response, AlipayBillDeclareResponse.class);
            if (alipayBillDeclareResponse == null) {
                return false;
            }
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setMsg("[供应链报文-向支付宝支付下发支付单申报指令完成]")
                    .addParm("请求报文", requestBodyJsonString)
                    .addParm("回复报文", response)
                    .log();

            if ("T".equalsIgnoreCase(alipayBillDeclareResponse.getIsSuccess())) {
                if (alipayBillDeclareResponse.getResponseContentWrap() != null
                        && alipayBillDeclareResponse.getResponseContentWrap().getResponseContent() != null) {
                    ResponseContent responseContent = alipayBillDeclareResponse.getResponseContentWrap().getResponseContent();
                    setPayBillState(responseContent.getResultCode());
                }
            }else {
                setPayBillState("请查看轨迹日志");
            }

        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向支付宝支付下发支付单申报指令异常]")
                    .addParm("原因", e.getMessage())
                    .addParm("请求报文", requestBodyJsonString)
                    .setException(e)
                    .log();

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.ERROR,
                    "[供应链报文-向支付宝支付下发支付单申报指令异常]: "
                            + "[请求报文: " + requestBodyJsonString
                            + ", 异常信息: " + e.getMessage()
                            + "]"
            ));
        }
        return false;
    }

}
