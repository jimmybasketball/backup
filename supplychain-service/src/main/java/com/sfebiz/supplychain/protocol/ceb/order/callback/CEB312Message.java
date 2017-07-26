package com.sfebiz.supplychain.protocol.ceb.order.callback;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/4/7
 * Time: 下午9:15
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"OrderReturn"})
@XmlRootElement(name = "CEB312Message")
public class CEB312Message implements Serializable {

    private static final long serialVersionUID = -8832468580448868112L;

    @XmlElement(name = "OrderReturn")
    private OrderReturn OrderReturn;

    public OrderReturn getOrderReturn() {
        return OrderReturn;
    }

    public void setOrderReturn(OrderReturn orderReturn) {
        OrderReturn = orderReturn;
    }
}
