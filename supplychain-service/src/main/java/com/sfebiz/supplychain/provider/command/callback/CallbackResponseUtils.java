package com.sfebiz.supplychain.provider.command.callback;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.protocol.fse.FSEResponse;
import com.sfebiz.supplychain.protocol.fse.FSESkuSyncResult;
import com.sfebiz.supplychain.provider.entity.ErrorCode;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsResponse;
import com.sfebiz.supplychain.sdk.protocol.Response;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.XMLUtil;
import net.pocrd.define.SerializeType;
import net.pocrd.responseEntity.RawString;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/9/27 上午12:05
 */
public class CallbackResponseUtils {

    public static final String RESPONSE_SUCCESS = "true";
    public static final String RESPONSE_FALSE = "false";
    private static final Logger logger = LoggerFactory.getLogger("CommandLogger");

    public static RawString setResponse(String success, String reason) {
        LogisticsEventsResponse responses = new LogisticsEventsResponse();
        List<Response> responseItems = new LinkedList<Response>();
        responses.setResponseItems(responseItems);
        Response response = new Response();
        response.setSuccess(success);
        response.setReason(reason);
        if (RESPONSE_SUCCESS.equals(success)) {
            response.setReasonDesc("");
        } else {
            response.setReasonDesc(ErrorCode.getErrorCode(reason));
        }
        responseItems.add(response);
        RawString result = new RawString();
        try {
            result.value = XMLUtil.convertToXml(responses);
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("[供应链-回掉对象转换异常]")
                    .setParms("[异常信息]", e.getMessage())
                    .setException(e).log();
        }

        return result;
    }

    public static RawString setResponse(String success, String reason, String desc) {
        LogisticsEventsResponse responses = new LogisticsEventsResponse();
        List<Response> responseItems = new LinkedList<Response>();
        responses.setResponseItems(responseItems);
        Response response = new Response();
        response.setSuccess(success);
        response.setReason(reason);
        if (RESPONSE_SUCCESS.equals(success)) {
            response.setReasonDesc("");
        } else {
            if (StringUtils.isEmpty(desc)) {
                response.setReasonDesc(ErrorCode.getErrorCode(reason));
            } else {
                response.setReasonDesc(ErrorCode.getErrorCode(reason) + desc);
            }

        }
        responseItems.add(response);
        RawString result = new RawString();
        try {
            result.value = XMLUtil.convertToXml(responses);
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("[供应链-回掉对象转换异常]")
                    .setParms("[异常信息]", e.getMessage())
                    .setException(e).log();
        }

        logger.debug(result.value);
        return result;
    }

    public static RawString setResponse(String success, String reason, String desc, SerializeType serializeType) {
        FSEResponse response = new FSEResponse();
        FSESkuSyncResult rowset = new FSESkuSyncResult();
        if (RESPONSE_SUCCESS.equals(success)) {
            rowset.setResultCode("1000");
        } else {
            rowset.setResultCode("1005");
            if (com.alibaba.dubbo.common.utils.StringUtils.isEmpty(desc)) rowset.setResultMsg(ErrorCode.getErrorCode(reason));
            else rowset.setResultMsg(desc);
        }
        response.setROWSET(rowset);
        RawString result = new RawString();
        try {
            if(serializeType.equals(SerializeType.JSON)){
                result.value = JSONUtil.toJson(response);
            }else{
                result.value = XMLUtil.convertToXml(response);
            }

        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setMsg("[供应链-返回对象转换异常]: " + e.getMessage())
                    .addParm("response", response)
                    .log();
        }
        return result;
    }

    public static RawString setHzPortResponse(String success, String reason) {
        StringBuilder responseString = new StringBuilder("<response>");
        if (RESPONSE_SUCCESS.equals(success)) {
            responseString.append("<success>");
            responseString.append("true");
            responseString.append("</success>");
        } else {
            responseString.append("<success>");
            responseString.append("false");
            responseString.append("</success>");
            responseString.append("<errorCode>");
            responseString.append(ErrorCode.getErrorCode(reason));
            responseString.append("</errorCode>");
            responseString.append("<errorMsg>");
            responseString.append(reason);
            responseString.append("</errorMsg>");
        }
        responseString.append("</response>");
        RawString result = new RawString();
        result.value = responseString.toString();
        return result;
    }

}
