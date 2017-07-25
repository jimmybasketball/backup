package com.sfebiz.supplychain.protocol.nbport.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by ztc on 2016/12/30.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"busOrderNo","orderNo"})
@XmlRootElement(name = "no")
public class OrderNo implements Serializable{

    @XmlElement(name = "busOrderNo")
    private String busOrderNo;

    @XmlElement(name = "orderNo")
    private String orderNo;


    public String getBusOrderNo() {
        return busOrderNo;
    }

    public void setBusOrderNo(String busOrderNo) {
        this.busOrderNo = busOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
