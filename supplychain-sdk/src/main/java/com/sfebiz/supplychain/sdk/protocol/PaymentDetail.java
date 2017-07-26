package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 付款详情类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paymentDetail", propOrder = {"paid"})
public class PaymentDetail implements Serializable {

    private static final long serialVersionUID = -3576002115140602823L;

    /**
     * 付款详情对象
     */
    @XmlElement(nillable = false, required = false)
    public Paid paid;

    public Paid getPaid() {
        if (paid == null) {
            paid = new Paid();
        }
        return paid;
    }

    public void setPaid(Paid paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "PaymentDetail{" +
                "paid=" + paid +
                '}';
    }
}
