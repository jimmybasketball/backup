package com.sfebiz.supplychain.msg;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.msg.processer.MessageProcesser;
import com.sfebiz.supplychain.msg.processer.MessageProcesserFactory;
import com.sfebiz.supplychain.queue.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 供应链路由消息
 * @author liujc [liujunchi@ifunq.com]
 * @date  2017/8/2 11:59
 */
public class SupplychainEventRouteMessageConsumer extends AbstractMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SupplychainEventRouteMessageConsumer.class);

    @Resource
    private MessageProcesserFactory messageProcesserFactory;

    @Override
    public String getTopic() {
        return MessageConstants.TOPIC_SUPPLY_CHAIN_ROUTE_EVENT;
    }

    @Override
    public MessageListener getMessageListener() {
        return new MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链路由消息]--开始处理统一事件")
                        .addParm("message", message)
                        .log();

                boolean handleSuccess = false;
                try {
                    String msgTag = message.getTag();
                    MessageProcesser messageProcesser = messageProcesserFactory.getMessageProcesser(msgTag);
                    if (messageProcesser == null) {
                        LogBetter.instance(logger)
                                .setLevel(LogLevel.WARN)
                                .setMsg("[供应链路由消息]--未找到消息TAG处理器，默认为完成")
                                .addParm("tag", message.getTag())
                                .addParm("message", message)
                                .log();
                        return Action.CommitMessage;
                    }
                    handleSuccess = messageProcesser.process(message);

                    LogBetter.instance(logger)
                            .setLevel(LogLevel.INFO)
                            .setMsg("[供应链路由消息]--完成处理统一事件")
                            .addParm("message", message)
                            .addParm("处理器实现", messageProcesser.getClass().getSimpleName())
                            .addParm("是否处理成功", handleSuccess)
                            .log();
                } catch (Exception e) {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.ERROR)
                            .setErrorMsg("[订单系统统一事件]-调用事件处理器出现异常")
                            .setException(e)
                            .log();
                    return Action.ReconsumeLater;
                }

                if (!handleSuccess) {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.WARN)
                            .setMsg("[供应链路由消息]--事件处理失败")
                            .addParm("tag", message.getTag())
                            .addParm("message", message)
                            .log();
                    return Action.ReconsumeLater;
                }

                return Action.CommitMessage;
            }
        };
    }
}
