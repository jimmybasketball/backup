package com.sfebiz.supplychain.provider.biz;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.common.code.StockoutReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.protocol.bsp.BSPRequest;
import com.sfebiz.supplychain.protocol.bsp.BSPResponse;
import com.sfebiz.supplychain.provider.entity.ErrorCode;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsRequest;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsResponse;
import com.sfebiz.supplychain.sdk.protocol.Response;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.MD5Util;
import com.sfebiz.supplychain.util.XMLUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 商品供应商相关业务处理
 */
@Component("providerBizService")
public class ProviderBizService implements ApplicationContextAware {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private static final Logger logger = LoggerFactory.getLogger("CommandLogger");

    protected static final String RESPONSE_SUCCESS = "true";
    protected static final String RESPONSE_FALSE = "false";
    protected static long MSGID = 0;

    private static ApplicationContext applicationContext;


    public static ProviderBizService getInstance() {
        return (ProviderBizService) applicationContext.getBean("providerBizService");
    }

    public BSPResponse sendBSPRequest(String interfaceUrl, String interfaceKey,
                                      BSPRequest req, String orderId) {
        String content = "";

        try {
            content = XMLUtil.convertToXml(req);
            StringBuffer buf = new StringBuffer();
            StringBuffer key = new StringBuffer();
            key.append(content).append(interfaceKey);
            buf.append("xml=")
                    .append(URLEncoder.encode(content, "UTF-8"))
                    .append("&verifyCode=")
                    .append(URLEncoder.encode(
                            MD5Util.md5EncodeToBase64(key.toString()), "UTF-8"));

            String resp = null;
            boolean isMockAutoCreated = MockConfig.isMocked("bsp", "registResult");
            if (isMockAutoCreated) {
                resp = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                        "<Response service=\"OrderService\">" +
                        "<Head>OK</Head>" +
                        "<Body>" +
                        "<OrderResponse orderid=\"运单号\" mailno=\"123456\" " +
                        "origincode=\"123\" destcode=\"456\" filter_result=\"111\" remark=\"Mock\" return_tracking_no=\"987654\"/>" +
                        "</Body>" +
                        "</Response>";
            } else {
                resp = HttpUtil.postFormByHttp(interfaceUrl, buf.toString());
            }


            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向BSP下发指令完成]")
                    .addParm("请求报文", content)
                    .addParm("回复报文", resp)
                    .log();

            if (StringUtils.isNotBlank(orderId)) {
                traceLogger.log(new TraceLog(orderId, "supplychain", new Date(), TraceLog.TraceLevel.INFO,
                        "[供应链报文-向BSP下发指令成功]: "
                                + "[请求报文:" + content
                                + ", 回复报文: " + resp
                                + "]"
                ));
            }

            if (resp != null && resp.length() > 0) {
                return XMLUtil.converyToJavaBean(resp, BSPResponse.class);
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向BSP下发指令异常]")
                    .addParm("原因", e.getMessage())
                    .setException(e)
                    .addParm("请求报文", content)
                    .log();

            if (StringUtils.isNotBlank(orderId)) {
                traceLogger.log(new TraceLog(orderId, "supplychain", new Date(), TraceLog.TraceLevel.ERROR,
                        "[供应链报文-向BSP下发指令异常]: "
                                + "[请求报文:" + content
                                + ", 异常信息: " + e.getMessage()
                                + "]"
                ));
            }

        }
        return null;
    }

    public LogisticsEventsResponse send(LogisticsEventsRequest req,
                                        String msgType, String url, String key, TraceLogEntity traceLogEntity) throws ServiceException {
        return send(req, msgType, url, key, traceLogEntity, null);
    }

    public LogisticsEventsResponse send(LogisticsEventsRequest req,
                                        String msgType, String url, String key, TraceLogEntity traceLogEntity, String extParams) throws ServiceException {
        LogisticsEventsResponse responses = new LogisticsEventsResponse();
        String logistics_interface = "";
        try {
            logistics_interface = XMLUtil.convertToXml(req);
        } catch (Exception e) {
            throw new ServiceException(StockoutReturnCode.STOCKOUT_ORDER_OBJECT_CONVERT_ERROR, e);
        }
        String data_digest = MD5Util.md5EncodeToBase64(logistics_interface + key);

        MSGID++;
        logger.info("url:" + url);
        logger.info("key:" + key);
        StringBuilder content = new StringBuilder();
        try {
            content.append("logistics_interface=").append(URLEncoder.encode(logistics_interface, "utf-8"));
            content.append("&data_digest=").append(URLEncoder.encode(data_digest, "utf-8"));
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }

        content.append("&msg_type=").append(msgType);
        content.append("&msg_source=").append("SF");
        content.append("&msg_id=").append(MSGID);
        content.append("&version=").append("1");
        if (StringUtils.isNotBlank(extParams)) {
            content.append("&").append(extParams);
        }

        try {
            String result = HttpUtil.postFormByHttp(url, content.toString());

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(traceLogEntity)
                    .setMsg("[供应链报文-向仓库下发指令完成]")
                    .addParm("加密值", data_digest)
                    .addParm("消息类型", msgType)
                    .addParm("消息来源", "SF")
                    .addParm("消息ID", MSGID)
                    .addParm("版本", "1")
                    .addParm("请求报文", logistics_interface)
                    .addParm("回复报文", result)
                    .log();

            if (result != null && result.length() > 0) {
                try {
                    responses = XMLUtil.converyToJavaBean(result, LogisticsEventsResponse.class);
                } catch (Exception e) {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.ERROR)
                            .setTraceLogger(traceLogEntity)
                            .setMsg("[供应链报文-向仓库下发指令异常]")
                            .addParm("原因", e.getMessage())
                            .addParm("加密值", data_digest)
                            .addParm("消息类型", msgType)
                            .addParm("消息来源", "SF")
                            .addParm("消息ID", MSGID)
                            .addParm("版本", "1")
                            .addParm("请求报文", logistics_interface)
                            .setException(e)
                            .log();
                    return getResponse(RESPONSE_FALSE, "S01", "非法的XML/JSON");
                }
                return responses;
            }
        } catch (Exception e) {

            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(traceLogEntity)
                    .setMsg("[供应链报文-向仓库下发指令异常]")
                    .addParm("加密值", data_digest)
                    .addParm("消息类型", msgType)
                    .addParm("消息来源", "SF")
                    .addParm("消息ID", MSGID)
                    .addParm("版本", "1")
                    .addParm("请求报文", logistics_interface)
                    .setException(e)
                    .log();

            return getResponse(RESPONSE_FALSE, "S999", "超时异常");
        }
        return responses;
    }

    public LogisticsEventsResponse sendCOERequest(LogisticsEventsRequest req, String url, String token, String tokenKey, String appType) throws ServiceException {
        LogisticsEventsResponse responses = new LogisticsEventsResponse();
        String logistics_interface;
        try {
            logistics_interface = XMLUtil.convertToXml(req);
        } catch (Exception e) {
            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_OBJECT_CONVERT_ERROR, e);
        }
        String data_digest = MD5Util.MD5In32(logistics_interface
                + tokenKey);

        StringBuilder content = new StringBuilder();
        try {
            content.append("content=").append(
                    URLEncoder.encode(logistics_interface, "utf-8"));
            content.append("&sign=").append(
                    URLEncoder.encode(data_digest, "utf-8"));
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1.getMessage(), e1);
        }
        content.append("&apptype=").append(appType);
        content.append("&token=").append(token);

        try {

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向仓库下发指令]")
                    .addParm("url", url)
                    .addParm("内容", content.toString())
                    .log();

            String result = HttpUtil.postFormByHttp(url, content.toString());

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向仓库下发指令完成]")
                    .addParm("加密值", data_digest)
                    .addParm("应用类型", appType)
                    .addParm("请求报文", logistics_interface)
                    .addParm("回复报文", result)
                    .log();

            if (result != null && result.length() > 0) {
                try {
                    responses = XMLUtil.converyToJavaBean(result, LogisticsEventsResponse.class);
                } catch (Exception e) {

                    LogBetter.instance(logger)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[供应链报文-向仓库下发指令异常]")
                            .addParm("原因", e.getMessage())
                            .addParm("加密值", data_digest)
                            .addParm("应用类型", appType)
                            .addParm("请求报文", logistics_interface)
                            .setException(e)
                            .log();

                    return getResponse(RESPONSE_FALSE, "S01", "非法的XML/JSON");
                }
                return responses;
            }
        } catch (Exception e) {

            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向仓库下发指令异常]")
                    .addParm("加密值", data_digest)
                    .addParm("应用类型", appType)
                    .addParm("请求报文", logistics_interface)
                    .setException(e)
                    .log();
            return getResponse(RESPONSE_FALSE, "S999", "超时异常");
        }
        return responses;
    }

    private LogisticsEventsResponse getResponse(String success, String reason,
                                                String reasonDesc) {
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
