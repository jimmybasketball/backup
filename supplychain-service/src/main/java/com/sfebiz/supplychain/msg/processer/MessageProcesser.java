package com.sfebiz.supplychain.msg.processer;

import com.aliyun.openservices.ons.api.Message;


/**
 * 消息处理器
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017/8/2 11:52
 */
public interface MessageProcesser {

    /**
     * 是否处理成功
     *
     * @param message
     * @return
     */
    public abstract Boolean process(Message message);


    /**
     * 消息Tag
     *
     * @return
     */
    public abstract String getTag();

}
