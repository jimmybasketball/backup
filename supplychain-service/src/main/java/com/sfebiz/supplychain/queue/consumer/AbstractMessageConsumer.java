package com.sfebiz.supplychain.queue.consumer;

import java.util.Properties;

import javax.annotation.Resource;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.sfebiz.supplychain.queue.MessageConsumer;

/**
 * Created by sam on 3/10/15.
 * Email: sambean@126.com
 */

public abstract class AbstractMessageConsumer extends MessageConsumer {

    @Resource
    private String   env;
    /**  */
    private String   consumerId;
    /**  */
    private String   accessKey;
    /**  */
    private String   secretKey;

    /**
     *
     */
    private Consumer consumer;

    public abstract String getTopic();

    public abstract MessageListener getMessageListener();

    @Override
    public void stopConsumer() throws Exception {
        if (null != consumer) {
            consumer.shutdown();
        }
    }

    @Override
    public void startConsumer() throws Exception {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, consumerId);
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe(getTopic() + "_" + env, "*", getMessageListener());
        consumer.start();
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
