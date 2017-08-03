package com.sfebiz.supplychain.protocol.zto.internal;

import com.sfebiz.supplychain.protocol.zto.ZTORequest;
import com.sfebiz.supplychain.protocol.zto.common.ZTOReceiver;
import com.sfebiz.supplychain.protocol.zto.common.ZTOSender;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/17
 * Time: 下午2:32
 */
public class ZTOInternalOrderRequest implements ZTORequest {

    private static final long serialVersionUID = 2763713529972957976L;

    /**
     * 订单号
     */
    private String id;

    /**
     * 发货人信息
     */
    private ZTOSender sender;

    /**
     * 收件人信息
     */
    private ZTOReceiver receiver;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZTOSender getSender() {
        return sender;
    }

    public void setSender(ZTOSender sender) {
        this.sender = sender;
    }

    public ZTOReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(ZTOReceiver receiver) {
        this.receiver = receiver;
    }
}
