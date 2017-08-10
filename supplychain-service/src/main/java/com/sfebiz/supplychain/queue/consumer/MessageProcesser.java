package com.sfebiz.supplychain.queue.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;

/**
 * Created by sam on 3/25/15.
 * Email: sambean@126.com
    消息处理器
 */
public interface MessageProcesser {

    /**
     * 消息主处理方法
     * 
     * @param message
     * @return
     */
    public abstract Action process(Message message);

    /**
     * 消息标签
     * 
     * @return
     */
    public abstract String getTag();

}
