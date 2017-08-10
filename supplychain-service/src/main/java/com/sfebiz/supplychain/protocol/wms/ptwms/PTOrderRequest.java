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
@XmlRootElement(name = "ns1:callService")
public class PTOrderRequest implements Serializable {

    private static final long serialVersionUID = 8817560675402691635L;

    @Description("请求数据")
    @XmlElement(name = "paramsJson", required = true)
    private String paramsJson;

    @Description("密钥")
    @XmlElement(name = "appToken", required = true)
    private String appToken;

    @Description("标识")
    @XmlElement(name = "appKey", required = true)
    private String appKey;

    @Description("接口方法")
    @XmlElement(name = "service", required = true)
    private String service;

    public String getParamsJson() {
        return paramsJson;
    }

    public void setParamsJson(String paramsJson) {
        this.paramsJson = paramsJson;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
