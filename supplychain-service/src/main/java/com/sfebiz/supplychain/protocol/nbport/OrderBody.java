package com.sfebiz.supplychain.protocol.nbport;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by ztc on 2016/12/28.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"order"})
@XmlRootElement(name = "body")
public class OrderBody implements Serializable {

    @XmlElement(name = "order")
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
