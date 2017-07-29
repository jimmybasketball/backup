package com.sfebiz.supplychain.protocol.nbport;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by ztc on 2016/12/28.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"orderHead","orderBody"})
@XmlRootElement(name = "request")
public class OrderRequest implements Serializable {

    @XmlElement(name = "head")
    private OrderHead orderHead;

    @XmlElement(name = "body")
    private OrderBody orderBody;

    public OrderHead getOrderHead() {
        return orderHead;
    }

    public void setOrderHead(OrderHead orderHead) {
        this.orderHead = orderHead;
    }

    public OrderBody getOrderBody() {
        return orderBody;
    }

    public void setOrderBody(OrderBody orderBody) {
        this.orderBody = orderBody;
    }
}
