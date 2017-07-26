package com.sfebiz.supplychain.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 14/11/21 下午8:03
 */
public abstract class MessageConsumer implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    /** 
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        logger.info("停止消费");
        stopConsumer();
    }

    /** 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("开始消费");
        startConsumer();
    }

    /**
     * 开始
     * 
     * @throws Exception
     */
    public abstract void startConsumer() throws Exception;

    /**
     * 停止
     * 
     * @throws Exception
     */
    public abstract void stopConsumer() throws Exception;

}
