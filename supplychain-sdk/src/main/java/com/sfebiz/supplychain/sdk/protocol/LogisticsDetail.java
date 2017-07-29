package com.sfebiz.supplychain.sdk.protocol;


import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 物流详情类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "logisticsDetail", propOrder = {"logisticsOrders"})
public class LogisticsDetail implements Serializable {
    private static final long serialVersionUID = -6833101797478620637L;
    /**
     * 物流订单列表
     */
    @XmlElementWrapper(name = "logisticsOrders")
    @XmlElement(name = "logisticsOrder", nillable = false, required = true)
    public List<LogisticsOrder> logisticsOrders;

    public List<LogisticsOrder> getLogisticsOrders() {
        if (logisticsOrders == null) {
            logisticsOrders = new ArrayList<LogisticsOrder>();
        }
        return logisticsOrders;
    }

    public void setLogisticsOrders(List<LogisticsOrder> logisticsOrders) {
        this.logisticsOrders = logisticsOrders;
    }

    @Override
    public String toString() {
        return "LogisticsDetail{" +
                "logisticsOrders=" + logisticsOrders +
                '}';
    }
}
