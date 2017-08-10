package com.sfebiz.supplychain.protocol.wms.ptwms;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by TT on 2016/7/4.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SOAP-ENV:Body")
@Description("订单信息")
public class PTOrderBody implements Serializable {

    private static final long serialVersionUID = -4062532876160842784L;

    @XmlElement(name="ns1:callService")
    private PTOrderRequest request;

    public PTOrderRequest getRequest() {
        return request;
    }

    public void setRequest(PTOrderRequest request) {
        this.request = request;
    }
}
