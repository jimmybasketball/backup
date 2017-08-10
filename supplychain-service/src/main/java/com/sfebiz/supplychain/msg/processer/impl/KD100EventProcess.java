package com.sfebiz.supplychain.msg.processer.impl;

import com.aliyun.openservices.ons.api.Message;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.msg.processer.MessageProcesser;
import com.sfebiz.supplychain.queue.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: <a href="mailto:zhang.yajing@ifunq.com">张雅静</a>
 * Version: 1.0.0
 * Since: 2016/12/30  16:31
 */
@Component("KD100EventProcess")
public class KD100EventProcess implements MessageProcesser {

    private static final Logger LOGGER = LoggerFactory.getLogger(KD100EventProcess.class);

    @Resource
    private RouteService routeService;

    @Override
    public Boolean process(Message message) {
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台路由-订阅快递100] 接收订阅快递100消息")
                .addParm("消息内容", message)
                .log();
        String bizId = (String) message.getUserProperties().get("bizId");
        String routeType = (String) message.getUserProperties().get("routeType");
        try {
            CommonRet<Void> commonRet = routeService.registKD100Routes(bizId, routeType);

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台路由-订阅快递100] 结束")
                    .addParm("commonRet", commonRet)
                    .addParm("消息内容", message)
                    .log();
            return true;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.WARN)
                    .setErrorMsg("[物流平台路由-订阅快递100] 异常")
                    .addParm("消息内容", message)
                    .addParm("订单ID", bizId)
                    .addParm("routeType", routeType)
                    .setException(e)
                    .log();
           return false;
        }
    }

    @Override
    public String getTag() {
        return MessageConstants.TAG_REGIST_KUAIDI100;
    }
}
