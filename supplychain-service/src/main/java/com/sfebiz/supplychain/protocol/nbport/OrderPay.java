package com.sfebiz.supplychain.protocol.nbport;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by ztc on 2016/12/28.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"paytime","paymentNo","orderSeqNo","source","idnum","name","merId"})
@XmlRootElement(name = "pay")
public class OrderPay implements Serializable {

    @XmlElement(name = "paytime")
    private String paytime;

    @XmlElement(name = "paymentNo")
    private String paymentNo;

    @XmlElement(name = "orderSeqNo")
    private String orderSeqNo;

    @XmlElement(name = "source")
    private String source;

    @XmlElement(name = "idnum")
    private String idnum;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "merId")
    private String merId;

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getOrderSeqNo() {
        return orderSeqNo;
    }

    public void setOrderSeqNo(String orderSeqNo) {
        this.orderSeqNo = orderSeqNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }
}
