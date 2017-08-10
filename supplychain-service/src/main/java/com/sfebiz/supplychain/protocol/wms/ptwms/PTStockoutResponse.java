package com.sfebiz.supplychain.protocol.wms.ptwms;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


/**
 * Created by TT on 2016/7/5.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Envelope",namespace = "http://schemas.xmlsoap.org/soap/envelope/")
@Description("订单信息")
public class PTStockoutResponse implements Serializable {

    private static final long serialVersionUID = -5992001754664818059L;


    @XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/" )
    private PTResponseBody body;


    public PTResponseBody getBody() {
        return body;
    }

    public void setBody(PTResponseBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "PTStockoutResponse{" +
                ", body=" + body +
                '}';
    }
}
