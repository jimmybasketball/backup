package com.sfebiz.supplychain.protocol.zto;

import com.alibaba.fastjson.JSON;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>与ZTO 国际 交互的客户端</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/6/30
 * Time: 下午4:39
 */
public class ZTOInternationClient {

    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    protected static final Logger logger = LoggerFactory.getLogger("CommandLogger");
    protected static final String RESPONSE_SUCCESS = "true";
    protected static final String RESPONSE_FALSE = "false";
    private static final String DATA_KEY = "data";
    private static final String DATA_DIGEST_KEY = "data_digest";
    private static final String MSG_TYPE_KEY = "msg_type";
    private static final String COMPANY_ID_KEY = "company_id";
    private static final String EQUAL = "=";
    private static final String SPLIT = "&";

    public static String sendByPost(ZTORequest request,String bizId, String url, String interfaceKey ,String companyId, String messageType) {
        String responseString = "";
        try {
            String dataJson = JSON.toJSONString(request);
            StringBuilder contentBuilder = new StringBuilder(DATA_KEY);
            contentBuilder.append(EQUAL).append(dataJson).append(SPLIT);
            contentBuilder.append(DATA_DIGEST_KEY).append(EQUAL).append(generateSign(dataJson,interfaceKey)).append(SPLIT);
            contentBuilder.append(MSG_TYPE_KEY).append(EQUAL).append(messageType).append(SPLIT);
            contentBuilder.append(COMPANY_ID_KEY).append(EQUAL).append(companyId);

            responseString = HttpUtil.postFormByHttp(url, contentBuilder.toString());

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger,bizId, SystemConstants.TRACE_APP))
                    .addParm("[供应链报文-向中通发送指令完成]订单ID", bizId)
                    .addParm("请求地址", url)
                    .addParm("请求报文", dataJson)
                    .addParm("回复报文", responseString)
                    .log();

            return responseString;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链报文-向中通发送指令异常]")
                    .setException(e)
                    .addParm("请求地址", url)
                    .addParm("请求报文", responseString)
                    .log();
            return null;
        }
    }

    public static String sendByPost2(ZTORequest request,String bizId, String url, String interfaceKey ,String companyId, String messageType) {
        String responseString = "";
        try {
            String dataJson = JSON.toJSONString(request);
            String dataDigest = ZTOMD5Helper.md5( dataJson +  interfaceKey , "UTF-8", true);
            StringBuilder contentBuilder = new StringBuilder(DATA_KEY);
            contentBuilder.append(EQUAL).append(dataJson).append(SPLIT);
            contentBuilder.append(DATA_DIGEST_KEY).append(EQUAL).append(dataDigest).append(SPLIT);
            contentBuilder.append(MSG_TYPE_KEY).append(EQUAL).append(messageType).append(SPLIT);
            contentBuilder.append(COMPANY_ID_KEY).append(EQUAL).append(companyId);

            responseString = HttpUtil.postFormByHttp(url, contentBuilder.toString());

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger,bizId, SystemConstants.TRACE_APP))
                    .addParm("[供应链报文-向中通发送指令完成]订单ID", bizId)
                    .addParm("请求地址", url)
                    .addParm("请求报文", dataJson)
                    .addParm("回复报文", responseString)
                    .log();

            return responseString;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链报文-向中通发送指令异常]")
                    .setException(e)
                    .addParm("请求地址", url)
                    .addParm("请求报文", responseString)
                    .log();
            return null;
        }
    }


    private static String generateSign(String dataJson, String interfaceKey) {
        return MD5Util.MD5In32ForLowerCase(dataJson + interfaceKey);
    }
}
