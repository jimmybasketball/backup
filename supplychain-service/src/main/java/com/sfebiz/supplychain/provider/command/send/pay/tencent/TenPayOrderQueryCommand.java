package com.sfebiz.supplychain.provider.command.send.pay.tencent;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.protocol.pay.tenpay.RequestHandler;
import com.sfebiz.supplychain.protocol.pay.tenpay.TencentPayConstants;
import com.sfebiz.supplychain.protocol.pay.tenpay.client.TenpayHttpClient;
import com.sfebiz.supplychain.protocol.pay.tenpay.client.XMLParseResponseHandler;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderQueryCommand;
import com.sfebiz.supplychain.util.JSONUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * 财付通申报查询命令
 * 我们请求设置的什么字符编码 返回xml编码按照请求设置的返回
 * Created by zhaojingyang on 2015/5/11.
 */
public class TenPayOrderQueryCommand extends PayOrderQueryCommand {
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    @Override
    public boolean execute() {
        String requestUrl = "";
        String resContent = "";
        try {
            //创建查询请求对象
            RequestHandler reqHandler = new RequestHandler(null, null);
            //设置参数
            buildReqHeader(reqHandler);

            //通信对象
            TenpayHttpClient httpClient = new TenpayHttpClient();
            //设置请求返回的等待时间
            httpClient.setTimeOut(30);
            //设置发送类型POST
            httpClient.setMethod("POST");


            Map<String, Object> payMeta = PayConfig.getPayProviderMeta(payBillDeclareProviderNid);
            if (null == payMeta) {
                throw new Exception("未设置" + payBillDeclareProviderNid + "支付的meta信息");
            }
            String caInfo = (String) payMeta.get("caInfo");
            String certInfo = (String) payMeta.get("certInfo");
            String certPwd = (String) payMeta.get("certPwd");
            if (StringUtils.isEmpty(caInfo) || StringUtils.isEmpty(certInfo) || StringUtils.isEmpty(certPwd)) {
                throw new Exception(payBillDeclareProviderNid + "未设置证书信息");
            }

            //设置ca证书
            httpClient.setCaInfo(new File(caInfo));
            //设置个人(商户)证书
            httpClient.setCertInfo(new File(certInfo), certPwd);
            httpClient.setCharset(TencentPayConstants.CharSet.UTF8.getValue());
            //设置请求内容
            requestUrl = reqHandler.getRequestURL();
            httpClient.setReqContent(requestUrl);

            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .addParm("财付通支付支付单查询请求报文", requestUrl)
                    .log();

            boolean callResault = httpClient.call();
            if (!callResault) {
                throw new Exception("财付通支付查询异常" + httpClient.getResponseCode() + httpClient.getErrInfo());
            }
            resContent = httpClient.getResContent();
            //应答处理
            XMLParseResponseHandler resHandler = new XMLParseResponseHandler();
            resHandler.setContent(resContent);
            resHandler.setKey(reqHandler.getKey());
            resHandler.setCharset(reqHandler.getParameter("input_charset"));
            String retcode = resHandler.getParameter("retcode");

            if (!TencentPayConstants.REC_SUCCESS.equalsIgnoreCase(retcode)) {
                throw new Exception("财付通申报查询异常，异常原因" + resHandler.getParameter("retmsg"));
            }
            if (!resHandler.isTenpaySign()) {
                throw new Exception("返回报文签名不相等");
            }

            String countStr = resHandler.getParameter("count");
            if (!"1".equals(countStr)) {
                //count指示返回的记录条数 获取记录的需要为0到count-1 暂时没有子单只有1个
                throw new Exception("反馈结果单数异常,反馈单数" + countStr);

            }

            String expNo = stockoutOrderBO.getBizId();
            String actNo = "";
            if (StringUtils.isNotBlank(stockoutOrderBO.getMerchantOrderNo())
                    && (!stockoutOrderBO.getMerchantOrderNo().equals(stockoutOrderBO.getBizId()))) {
                actNo = resHandler.getParameter("sub_order_no_0");
            } else {
                actNo = resHandler.getParameter("out_trade_no");
            }

            if (!actNo.equals(expNo)) {
                throw new Exception("财付通返回单据信息异常, 期望单号:" + expNo + ",反回单号:" + actNo);
            }


            String declareStat = resHandler.getParameter("state_0");
            TencentPayConstants.TencentPayState state = TencentPayConstants.TencentPayState.getByState(declareStat);
            if (state != null) {
                setPayBillState(state.getDescription());
            } else {
                setPayBillState(resHandler.getParameter("retmsg"));
            }

            String payDeclareNo = resHandler.getParameter("sub_order_id_0");
            if (StringUtils.isBlank(resHandler.getParameter("sub_order_id_0"))) {
                payDeclareNo = resHandler.getParameter("transaction_id");
            }

            if (payDeclareNo.equals(stockoutOrderBO.getMerchantPayNo())) {
                payDeclareNo = "流水号Y:" + payDeclareNo;
            } else {
                payDeclareNo = "流水号N:" + payDeclareNo;
            }

            payDeclareNo = payDeclareNo + ",申报订单号:" + resHandler.getParameter("out_trade_no");

            setPayBillTradeNo(payDeclareNo);

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向财付通支付下发支付单查询指令成功]")
                    .addParm("出库单号", stockoutOrderBO.getBizId())
                    .addParm("请求报文", requestUrl)
                    .addParm("回复报文", resContent)
                    .log();

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO,
                    "[供应链报文-向财付通支付下发支付单查询指令成功]: "
                            + "[请求报文: " + requestUrl
                            + ", 回复报文: " + resContent
                            + "]"));
            return true;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向财付通支付下发支付单状态查询指令异常]")
                    .addParm("出库单号", stockoutOrderBO.getBizId())
                    .addParm("请求报文", requestUrl)
                    .addParm("回复报文", resContent)
                    .setException(e)
                    .log();

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.ERROR,
                    "[供应链报文-向财付通支付下发支付单状态查询指令异常]: "
                            + "[请求报文: " + requestUrl
                            + "[回复报文: " + resContent
                            + ", 异常信息: " + e.getMessage()
                            + "]"
            ));
            return false;
        }
    }

    /**
     * 构建请求对象
     *
     * @param reqHandler
     */
    private void buildReqHeader(RequestHandler reqHandler) {
        reqHandler.init();

        //支付企业 系统交互的接入码
        String key = PayConfig.getPayProviderInterfaceKey(payBillDeclareProviderNid);
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("财付通支付KEY[interface_key]未配置");
        }

        //各个口岸的meta信息中需要填写在财付通支付中的海关编码
        Map<String, Object> portMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getPortBO().getMeta());
        String ePortCode = (String) portMeta.get("tencent_eport_code");
        if (StringUtils.isBlank(ePortCode)) {
            throw new IllegalArgumentException("财付通在口岸" + logisticsLineBO.getPortBO().getPortNid() + "的海关编码[tencent_eport_code]未配置");
        }

        String interFaceBaseUrl = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
        if (StringUtils.isBlank(interFaceBaseUrl)) {
            throw new IllegalArgumentException("与财付通交互的URL[interface_url]未配置");
        }

        //电商企业 在 支付企业 中的备案编码
        String ecp = PayConfig.getECodeOnPayProvider(payBillDeclareProviderNid);
        if (StringUtils.isBlank(ecp)) {
            throw new IllegalArgumentException("财付通企业备案号[e_code_on_pay]未配置");
        }

        reqHandler.setKey(key); //设置key
        reqHandler.setGateUrl(interFaceBaseUrl + TencentPayConstants.TencentPayApiUrl.PAY_BILL_QUERY_URL.getUrl()); //请求地址

        reqHandler.setParameter("sign_type", TencentPayConstants.SignType.MD5.getValue());
        reqHandler.setParameter("service_version", TencentPayConstants.TENCENT_PAY_VERSION);
        reqHandler.setParameter("input_charset", TencentPayConstants.CharSet.UTF8.getValue());
        reqHandler.setParameter("sign_key_index", "1");

        reqHandler.setParameter("partner", ecp);// 商户号,由财付通统一分配的10位正整数

        reqHandler.setParameter("transaction_id", stockoutOrderBO.getMerchantPayNo()); //财付通订单号

        //如果有masterNo说明是有拆单按照子单查询
        String masterNo = stockoutOrderBO.getMerchantOrderNo();
        if (StringUtils.isNotBlank(masterNo) && (!masterNo.equals(stockoutOrderBO.getBizId()))) {
            reqHandler.setParameter("sub_order_no", stockoutOrderBO.getBizId()); //商户子订单号
            if (StringUtils.isNotEmpty(stockoutOrderBO.getDeclarePayType())) {
                reqHandler.setParameter("sub_order_id", stockoutOrderBO.getDeclarePayNo()); //财付通子订单号
            }
        }
        //reqHandler.setParameter("out_trade_no", stockoutOrderBO.getBizNo());//商户订单号

        reqHandler.setParameter("customs", ePortCode);//海关
    }
}
