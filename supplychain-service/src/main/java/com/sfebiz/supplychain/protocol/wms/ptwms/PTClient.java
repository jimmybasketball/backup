package com.sfebiz.supplychain.protocol.wms.ptwms;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/23
 * Time: 上午11:55
 */
public class PTClient {

    protected static final Logger logger = LoggerFactory.getLogger(PTClient.class);

    public static String send(String interfaceUrl, String request4Xml, TraceLogEntity traceLogEntity) {
        try {
            String response4Xml= HttpUtil.postFormByHttp(interfaceUrl,request4Xml);
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(traceLogEntity)
                    .setMsg("[供应链报文-向平潭发送指令成功]")
                    .addParm("请求地址", interfaceUrl)
                    .addParm("请求报文", request4Xml)
                    .addParm("回复报文", response4Xml)
                    .log();
            return response4Xml;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(traceLogEntity)
                    .setErrorMsg("[供应链报文-向平潭发送指令异常]")
                    .setException(e)
                    .addParm("请求地址", interfaceUrl)
                    .addParm("请求报文", request4Xml)
                    .log();
        }
        return null;
    }
}
