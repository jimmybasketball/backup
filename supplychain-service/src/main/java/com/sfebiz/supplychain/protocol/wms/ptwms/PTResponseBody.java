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
@XmlRootElement(name = "Body")
@Description("订单信息")
public class PTResponseBody implements Serializable {

    private static final long serialVersionUID = 5210242487824366656L;

    @XmlElement(name="callServiceResponse" ,namespace = "http://www.example.org/Ec/")
    private PTOrderResponseBody responseBody;

    public PTOrderResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(PTOrderResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "PTResponseBody{" +
                "responseBody=" + responseBody +
                '}';
    }
}
