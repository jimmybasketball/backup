package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/25
 * Time: 上午11:50
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Request")
@XmlType(propOrder = {"head","body"})

public class StockoutRequest implements Serializable{
    private static final long serialVersionUID = 6140732450379280145L;

    public void setService(String service) {
        this.service = service;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @XmlAttribute
    private String service;
    @XmlAttribute
    private  String lang;

    @XmlElement(name = "Head")
    private StockoutRequestHead head;

    @XmlElement(name = "Body")
    private StockoutBody body;


    public String getService() {
        return service;
    }

    public String getLang() {
        return lang;
    }

    public StockoutRequestHead getHead() {
        return head;
    }

    public StockoutBody getBody() {
        return body;
    }

    public void setHead(StockoutRequestHead head) {
        this.head = head;
    }

    public void setBody(StockoutBody body) {
        this.body = body;
    }
}
