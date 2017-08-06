package com.sfebiz.supplychain.protocol.wms.ptwms;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by TT on 2016/7/28.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "callServiceResponse")
@Description("订单信息")
public class PTOrderResponseBody implements Serializable {

    private static final long serialVersionUID = 5136022556496384744L;

    @XmlElement(name = "response")
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "PTOrderResponseBody{" +
                "response='" + response + '\'' +
                '}';
    }
}
