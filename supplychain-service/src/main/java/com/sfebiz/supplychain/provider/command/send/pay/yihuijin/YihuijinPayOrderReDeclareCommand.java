package com.sfebiz.supplychain.provider.command.send.pay.yihuijin;

import com.alibaba.fastjson.JSONObject;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.order.OrderConfig;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.exposed.user.api.RealNameAuthenticationService;
import com.sfebiz.supplychain.protocol.pay.yihuijin.ReDeclareOrderBuilder;
import com.sfebiz.supplychain.protocol.pay.yihuijin.YihuijinSignUtils;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderReDeclareCommand;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * <p>易汇金支付支付单重新申报</p>
 * User: zhangdi
 * Date: 15/09/16
 */
public class YihuijinPayOrderReDeclareCommand extends PayOrderReDeclareCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    public static final String serviceName = "customs/repeatDeclare";

    private RealNameAuthenticationService realNameAuthenticationService;

    @Override
    public boolean execute() {
        boolean result = false;
        String request = null;
        try {

            realNameAuthenticationService = (RealNameAuthenticationService) CommandConfig.getSpringBean("realNameAuthenticationService");
            String buyerName = CommonUtil.getBuyerName(stockoutOrderBO);
            String buyerIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);
            if (!OrderConfig.getIsSkipRealNameAuthentication(stockoutOrderBO.getBizId())
                    && !realNameAuthenticationService.rz(buyerName, buyerIdNo)) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.WARN)
                        .setMsg("[供应链报文-向易汇金支付重新下发支付单申报指令失败]: 购买人的实名认证失败！")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("出库单ID", stockoutOrderBO.getId())
                        .addParm("购买人姓名", buyerName)
                        .log();
                return false;
            } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-向易汇金支付下发支付单申报指令失败]: 购买人的实名认证成功！")
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .addParm("出库单ID", stockoutOrderBO.getId())
                        .addParm("购买人姓名", buyerName)
                        .log();
            }

            request = buildRequest(logisticsLineBO.getWarehouseBO().getId());
            result = sendMessage2ReDeclarePayBill(request);
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
     * 构建易汇金重新申报请求参数信息
     *
     * @return
     * @throws Exception
     */
    private String buildRequest(Long warehouseId) throws Exception {
        String merchantId = getMerchantId(warehouseId);
        ReDeclareOrderBuilder builder = new ReDeclareOrderBuilder(merchantId);
        String joinPayNo = stockoutOrderBO.getDeclarePayNo();
        if (StringUtils.isBlank(joinPayNo)) {
            throw new IllegalArgumentException("联名支付号为空");
        }

        //各个口岸的meta信息中需要填写在连连支付中的海关编码
        String portName = null;
        Map<String, Object> portMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getPortBO().getMeta());
        if (portMeta.containsKey("yihuijin_eport_code") && StringUtils.isNotBlank(portMeta.get("yihuijin_eport_code").toString())) {
            portName = portMeta.get("yihuijin_eport_code").toString();

        } else {
            throw new IllegalArgumentException("易汇金支付在口岸元信息中的 yihuijin_eport_code 值不存在");
        }

        builder.setPaySerialNumber(joinPayNo);
        builder.setCustomsChannels(portName);

        StringBuilder hmacSource = new StringBuilder();
        hmacSource.append(StringUtils.defaultString(merchantId))
                .append(StringUtils.defaultString(joinPayNo))
                .append(StringUtils.defaultString(portName));
        String signKey = getMerchantKey(warehouseId);
        String sign = YihuijinSignUtils.signMd5(hmacSource.toString(), signKey);
        JSONObject request = builder.build(sign);
        return request.toJSONString();
    }

    /**
     * 给易汇金支付发送支付单申报信息
     *
     * @param requestBodyJsonString 请求体JSON字符串
     * @return
     */
    private boolean sendMessage2ReDeclarePayBill(String requestBodyJsonString) {
        try {
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
            if (StringUtils.isNotBlank(url)) {
                url = url + serviceName;
            }

            String response = HttpUtil.postJsonByHttps(url, requestBodyJsonString);
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向易汇金支付下发支付单重新申报指令]")
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .addParm("请求报文", requestBodyJsonString)
                    .addParm("回复报文", response)
                    .log();

            if (StringUtils.isBlank(response)) {
                return false;
            }
//            JSONObject resJson = JSON.parseObject(response);
            return true;
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
