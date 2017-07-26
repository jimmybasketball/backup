package com.sfebiz.supplychain.protocol.nbport.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by ztc on 2016/12/30.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"result","code","message","orderNos"})
@XmlRootElement(name = "response")
public class OrderResponse implements Serializable{

    @XmlElement(name = "result")
    private String result;

    @XmlElement(name = "code")
    private String code;

    @XmlElement(name = "message")
    private String message;

    @XmlElement(name = "orderNos")
    private OrderNos orderNos;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderNos getOrderNos() {
        return orderNos;
    }

    public void setOrderNos(OrderNos orderNos) {
        this.orderNos = orderNos;
    }
}
