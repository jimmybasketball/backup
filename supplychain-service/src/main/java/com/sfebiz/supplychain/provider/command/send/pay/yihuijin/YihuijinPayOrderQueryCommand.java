package com.sfebiz.supplychain.provider.command.send.pay.yihuijin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.protocol.pay.yihuijin.DeclareQueryBuilder;
import com.sfebiz.supplychain.protocol.pay.yihuijin.YihuijinPayBillDeclareState;
import com.sfebiz.supplychain.protocol.pay.yihuijin.YihuijinSignUtils;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderQueryCommand;
import com.sfebiz.supplychain.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 易汇金支付支付单申报状态查询
 * zhangdi 15/09/16
 */
public class YihuijinPayOrderQueryCommand extends PayOrderQueryCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    public static final String serviceName = "customs/query";

    @Override
    public boolean execute() {
        boolean result = false;
        String request = null;
        try {
            request = buildRequest();
            result = sendMessage2QueryPayBillState(request);
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向连连支付下发支付单状态查询指令异常]")
                    .addParm("请求报文", request)
                    .setException(e)
                    .log();
        }
        return true;
    }

    /**
     * 构建易汇金查询请求参数信息
     *
     * @return
     * @throws Exception
     */
    private String buildRequest() throws Exception {
        String merchantId = getMerchantId();
        DeclareQueryBuilder builder = new DeclareQueryBuilder(merchantId);
        String joinPayNo = stockoutOrderBO.getDeclarePayNo();
        if (StringUtils.isBlank(joinPayNo)) {
            throw new IllegalArgumentException("联名支付号为空");
        }
        builder.setPaySerialNumber(stockoutOrderBO.getDeclarePayNo());
        builder.setPaySerialNumber(joinPayNo);
        StringBuilder hmacSource = new StringBuilder();
        hmacSource.append(StringUtils.defaultString(merchantId)).append(StringUtils.defaultString(joinPayNo));
        String signKey = getMerchantKey();
        String sign = YihuijinSignUtils.signMd5(hmacSource.toString(), signKey);
        JSONObject request = builder.build(sign);
        return request.toJSONString();
    }

    /**
     * 给易汇金支付发送支付单申报查询信息
     *
     * @param requestBodyJsonString 请求体JSON字符串
     * @return
     */
    private boolean sendMessage2QueryPayBillState(String requestBodyJsonString) {
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

            if (resJson.containsKey("customsInfos")) {
                JSONArray array = resJson.getJSONArray("customsInfos");
                JSONObject json = (JSONObject) array.get(0);
                String status = json.getString("status");
                YihuijinPayBillDeclareState state = YihuijinPayBillDeclareState.getByState(status);
                if (state != null) {
                    setPayBillState(state.getDescription());
                } else {
                    setPayBillState(json.toString());
                }
                String payDeclareNo = resJson.getString("paySerialNumber");
                if (StringUtils.isNotBlank(payDeclareNo)) {
                    if (payDeclareNo.equals(stockoutOrderBO.getDeclarePayNo())) {
                        payDeclareNo = "Y:" + payDeclareNo;
                    } else {
                        payDeclareNo = "N:" + payDeclareNo;
                    }
                }
                setPayBillTradeNo(payDeclareNo);

                return true;
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向易汇金支付下发支付单查询指令异常]")
                    .addParm("请求报文", requestBodyJsonString)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .log();
        }
        return false;
    }


    /**
     * 获取商户号
     *
     * @return
     * @throws Exception
     */
    private String getMerchantId() throws Exception {
        String merchantIds = PayConfig.getECodeOnPayProvider(payBillDeclareProviderNid);
        String merchantId;
        if (StringUtils.isNotBlank(merchantIds) && merchantIds.contains(",")) {
            String[] merchantIdArray = merchantIds.split(",");
            merchantId = merchantIdArray[0].trim();
        } else {
            merchantId = merchantIds;
        }
        return merchantId;
    }


    /**
     * 获取商户号对应的KEY
     *
     * @return
     * @throws Exception
     */
    private String getMerchantKey() throws Exception {
        String signKeys = PayConfig.getPayProviderInterfaceKey(payBillDeclareProviderNid);
        String signKey;
        if (StringUtils.isNotBlank(signKeys) && signKeys.contains(",")) {
            String[] signKeyArray = signKeys.split(",");
            signKey = signKeyArray[0].trim();
        } else {
            signKey = signKeys;
        }

        return signKey;
    }

}
