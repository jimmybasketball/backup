package com.sfebiz.supplychain.protocol.ceb.pingtanorder;



import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午2:15
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderHead","orderPaymentLogistics","orderList"})
@XmlRootElement(name = "Order")
public class Order implements Serializable {

    private static final long serialVersionUID = -6895308463165716455L;


    @XmlElement(name = "OrderHead")
    private OrderHead orderHead;

    @XmlElements(@XmlElement(name = "OrderList", type = OrderList.class))
    private List<OrderList> orderList;

    @XmlElement(name = "OrderPaymentLogistics")
    private OrderPaymentLogistics orderPaymentLogistics;

    public OrderHead getOrderHead() {
        return orderHead;
    }

    public void setOrderHead(OrderHead orderHead) {
        this.orderHead = orderHead;
    }

    public List<OrderList> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderList> orderList) {
        this.orderList = orderList;
    }

    public OrderPaymentLogistics getOrderPaymentLogistics() {
        return orderPaymentLogistics;
    }

    public void setOrderPaymentLogistics(OrderPaymentLogistics orderPaymentLogistics) {
        this.orderPaymentLogistics = orderPaymentLogistics;
    }
}
