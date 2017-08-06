package com.sfebiz.supplychain.protocol.wms.ptwms;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by TT on 2016/7/4.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SOAP-ENV:Envelope ")
@XmlType(propOrder = { "body" })
@Description("订单信息")
public class PTOrderInfo implements Serializable {

    private static final long serialVersionUID = -8118329363588938261L;

    @XmlElement(name="SOAP-ENV:Body")
    private PTOrderBody body;

    public PTOrderBody getBody() {
        return body;
    }

    public void setBody(PTOrderBody body) {
        this.body = body;
    }
}
