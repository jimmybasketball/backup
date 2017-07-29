package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 16/1/21 下午1:01
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orderQueryResult", propOrder = {"queryPageResult", "logisticsOrders"})
public class OrderQueryResult implements Serializable {

    private static final long serialVersionUID = -222575601564601632L;

    /**
     * 分页信息
     */
    @XmlElement(name = "queryPageResult", nillable = false, required = false)
    public QueryPageResult queryPageResult;

    /**
     * 订单列表信息
     */
    @XmlElementWrapper(name = "logisticsOrders")
    @XmlElement(name = "logisticsOrder", nillable = false, required = false)
    public List<LogisticsOrder> logisticsOrders;

    @Override
    public String toString() {
        return "OrderQueryResult{" +
                "queryPageResult=" + queryPageResult +
                ", logisticsOrders=" + logisticsOrders +
                '}';
    }
}
