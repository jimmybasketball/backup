package com.sfebiz.supplychain.queue;

import com.aliyun.openservices.ons.api.*;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.queue.exception.MessageSendExceptionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a> Version: 1.0.0 Since: 14/11/21
 * 下午7:49
 */
public class MessageProducer implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Resource
    private String env; // 区分测试和线上
    @Resource
    private MessageSendExceptionProcessor messageSendExceptionProcessor;

    private Producer producer;

    private String producerId;
    private String accessKey;
    private String secretKey;

    /**
     * 发送消息
     * @param message 消息
     * @return 发送结果
     */
    public SendResult send(Message message) {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("供应链统一发送消息即将开始发送消息")
                .addParm("消息", message)
                .log();
        //  发送消息，只要不抛异常就是成功
        boolean isMock = MockConfig.isMocked("msg", message.getTopic());
        if (isMock) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("mock the message")
                    .addParm("message", message)
                    .log();
            return new SendResult();
        }
        String oriTopic = message.getTopic();
        SendResult sendResult = null;
        try {
            message.setTopic(message.getTopic() + "_" + env);
            sendResult = producer.send(message);
            return sendResult;
        } catch (Throwable e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("供应链统一发送消息异常")
                    .addParm("消息", message)
                    .setException(e)
                    .log();
            sendResult = null;
            message.setTopic(oriTopic);
            return sendResult;
        } finally {
            if (null != sendResult && sendResult.getMessageId() != null) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("供应链统一发送消息成功")
                        .addParm("消息", message)
                        .addParm("消息ID",sendResult.getMessageId())
                        .log();
            } else {
                //发送失败处理
                messageSendExceptionProcessor.addOrUpdateErrorTaskWhenSendError(message);
            }
        }
    }

    /**
     * 发送消息，异步发送，不需要返回
     *
     * @param message
     */
    public void sendOneway(Message message) {
        producer.sendOneway(message);
    }

    @Override
    public void destroy() throws Exception {
        producer.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.ProducerId, producerId);
            properties.put(PropertyKeyConst.AccessKey, accessKey);
            properties.put(PropertyKeyConst.SecretKey, secretKey);
            producer = ONSFactory.createProducer(properties);
            // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
            if (producer != null) {
                producer.start();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
