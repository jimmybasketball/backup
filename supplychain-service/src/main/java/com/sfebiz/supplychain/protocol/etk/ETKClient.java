package com.sfebiz.supplychain.protocol.etk;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.provider.entity.ErrorCode;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsRequest;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsResponse;
import com.sfebiz.supplychain.sdk.protocol.Response;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.MD5Util;
import com.sfebiz.supplychain.util.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>与E特快交互的客户端</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/6/30
 * Time: 下午4:39
 */
public class ETKClient {

    protected static final Logger logger = LoggerFactory.getLogger("CommandLogger");
    protected static final String RESPONSE_SUCCESS = "true";
    protected static final String RESPONSE_FALSE = "false";
    private static final String APP_TYPE_KEY = "apptype";
    private static final String APP_TYPE_VALUE = "etk";
    private static final String CONTENT_KEY = "content";
    private static final String TOKEN_KEY = "token";
    private static final String SIGN_KEY = "sign";
    private static final String EQUAL = "=";
    private static final String SPLIT = "&";

    public static LogisticsEventsResponse send(LogisticsEventsRequest request, String url, String interfaceKey, String token) {
        String responseString = "";
        LogisticsEventsResponse responses;
        try {
            String requestXml = XMLUtil.convertToXml(request);
            StringBuilder contentBuilder = new StringBuilder(CONTENT_KEY);
            contentBuilder.append(EQUAL).append(requestXml).append(SPLIT);
            contentBuilder.append(APP_TYPE_KEY).append(EQUAL).append(APP_TYPE_VALUE).append(SPLIT);
            contentBuilder.append(TOKEN_KEY).append(EQUAL).append(token).append(SPLIT);
            contentBuilder.append(SIGN_KEY).append(EQUAL).append(generateSign(requestXml, interfaceKey));

            responseString = HttpUtil.postFormByHttp(url, contentBuilder.toString());

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向E特快发送指令完成]")
                    .addParm("请求地址", url)
                    .addParm("请求报文", requestXml)
                    .addParm("回复报文", responseString)
                    .log();


            if (responseString != null && responseString.length() > 0) {
                try {
                    responses = XMLUtil.converyToJavaBean(responseString, LogisticsEventsResponse.class);
                } catch (Exception e) {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[供应链报文-向E特快发送指令异常]")
                            .addParm("请求报文", contentBuilder.toString())
                            .addParm("原因", e.getMessage())
                            .setException(e)
                            .log();
                    return getResponse(RESPONSE_FALSE, "S01", "非法的XML/JSON");
                }
                return responses;
            }

            return null;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链报文-向E特快发送指令异常]")
                    .setException(e)
                    .addParm("请求地址", url)
                    .addParm("请求报文", responseString)
                    .log();
            return null;
        }
    }

    private static String generateSign(String requestXml, String interfaceKey) {
        return MD5Util.MD5In32(requestXml + interfaceKey);
    }

    private static LogisticsEventsResponse getResponse(String success, String reason, String reasonDesc) {
        LogisticsEventsResponse responses = new LogisticsEventsResponse();
        List<Response> responseItems = new LinkedList<Response>();
        responses.setResponseItems(responseItems);
        Response response = new Response();
        response.setSuccess(success);
        response.setReason(reason);
        response.setReasonDesc(ErrorCode.getErrorCode(reason) + reasonDesc == null ? "" : reasonDesc);
        responseItems.add(response);
        return responses;
    }

}
