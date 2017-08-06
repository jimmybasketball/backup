package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p>出库响应</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/22
 * Time: 上午11:37
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Response")
@XmlType(propOrder = {"service", "lang", "head", "responseBody"})
public class StockoutResponse implements Serializable {

    private static final long serialVersionUID = 1681334496701386807L;
    @XmlAttribute
    public String service;
    @XmlAttribute
    public String lang;

    @Description("返回结果")
    @XmlElement(name = "Head", required = true)
    public String head;

    @Description("消息体")
    @XmlElement(name = "Body", required = false)
    public ResponseBody responseBody;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getService() {
        return service;
    }

    public String getLang() {
        return lang;
    }

    public String getHead() {
        return head;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }
}
