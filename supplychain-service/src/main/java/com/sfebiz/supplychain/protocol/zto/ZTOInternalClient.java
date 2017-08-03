package com.sfebiz.supplychain.protocol.zto;

import com.alibaba.fastjson.JSON;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.MD5Util;
import net.pocrd.util.Base64Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.Date;

/**
 * <p>与ZTO交互的客户端</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/6/30
 * Time: 下午4:39
 */
public class ZTOInternalClient {
    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    protected static final Logger logger = LoggerFactory.getLogger("CommandLogger");
    protected static final String RESPONSE_SUCCESS = "true";
    protected static final String RESPONSE_FALSE = "false";
    private static final String CONTENT_KEY = "content";
    private static final String FORMAT_KEY = "style";
    private static final String DEFAULT_FORMAT_VALUE = "json";
    private static final String MSG_TYPE_KEY = "func";
    private static final String PARTNER_KEY = "partner";
    private static final String DATETIME_KEY = "datetime";
    private static final String SIGN_KEY = "verify";
    private static final String EQUAL = "=";
    private static final String SPLIT = "&";

    public static String sendByPost(ZTORequest request, String bizId, String url, String interfaceKey, String partnerCode, String messageType) {
        String responseString = "";
        try {
            String dateTime = DateUtil.defFormatDateStr(new Date());
            String dataJson = JSON.toJSONString(request);
            String dataJsonOnBase64 = Base64Util.encodeToString(dataJson.getBytes());
            StringBuilder contentBuilder = new StringBuilder(FORMAT_KEY);
            contentBuilder.append(EQUAL).append(DEFAULT_FORMAT_VALUE).append(SPLIT);
            contentBuilder.append(MSG_TYPE_KEY).append(EQUAL).append(URLEncoder.encode(messageType)).append(SPLIT);
            contentBuilder.append(PARTNER_KEY).append(EQUAL).append(partnerCode).append(SPLIT);
            contentBuilder.append(DATETIME_KEY).append(EQUAL).append(URLEncoder.encode(dateTime)).append(SPLIT);
            contentBuilder.append(CONTENT_KEY).append(EQUAL).append(URLEncoder.encode(dataJsonOnBase64)).append(SPLIT);
            contentBuilder.append(SIGN_KEY).append(EQUAL).append(URLEncoder.encode(generateSign(partnerCode, dateTime, dataJsonOnBase64, interfaceKey)));

            responseString = HttpUtil.postFormByHttp(url, contentBuilder.toString());

            if (StringUtils.isNotBlank(bizId)) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, bizId, SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-向中通发送指令完成]")
                        .addParm("订单ID",bizId)
                        .addParm("请求地址", url)
                        .addParm("请求报文", dataJson)
                        .addParm("回复报文", responseString)
                        .log();
            }

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

    public static String sendByGet(ZTORequest request, String bizId, String url, String interfaceKey, String partnerCode, String messageType) {
        String responseString = "";
        try {
            String dateTime = DateUtil.defFormatDateStr(new Date());
            String dataJson = JSON.toJSONString(request);
            String dataJsonOnBase64 = Base64Util.encodeToString(dataJson.getBytes());
            StringBuilder contentBuilder = new StringBuilder(FORMAT_KEY);
            contentBuilder.append(EQUAL).append(DEFAULT_FORMAT_VALUE).append(SPLIT);
            contentBuilder.append(MSG_TYPE_KEY).append(EQUAL).append(URLEncoder.encode(messageType)).append(SPLIT);
            contentBuilder.append(PARTNER_KEY).append(EQUAL).append(partnerCode).append(SPLIT);
            contentBuilder.append(DATETIME_KEY).append(EQUAL).append(URLEncoder.encode(dateTime)).append(SPLIT);
            contentBuilder.append(CONTENT_KEY).append(EQUAL).append(URLEncoder.encode(dataJsonOnBase64)).append(SPLIT);
            contentBuilder.append(SIGN_KEY).append(EQUAL).append(URLEncoder.encode(generateSign(partnerCode, dateTime, dataJsonOnBase64, interfaceKey)));

            responseString = HttpUtil.getByHttp(url, contentBuilder.toString());

            if (StringUtils.isNotBlank(bizId)) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, bizId, SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-向中通发送指令完成]")
                        .addParm("订单ID",bizId)
                        .addParm("请求地址", url + "?" + contentBuilder.toString())
                        .addParm("请求报文", dataJson)
                        .addParm("回复报文", responseString)
                        .log();
            }

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

    /**
     * 生成签名信息
     *
     * @param partnerCode
     * @param dateTime
     * @param dataJsonOnBase64
     * @param interfaceKey
     * @return
     */
    private static String generateSign(String partnerCode, String dateTime, String dataJsonOnBase64, String interfaceKey) {
        return MD5Util.MD5In32ForLowerCase(new StringBuilder(partnerCode).append(dateTime).append(dataJsonOnBase64).append(interfaceKey).toString());
    }

}
