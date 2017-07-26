package com.sfebiz.supplychain.protocol.nbport;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by ztc on 2016/12/28.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"methodType","busCode","hgCode"})
@XmlRootElement(name = "head")
public class OrderHead implements Serializable {

    /**
     * 操作类型
     */
    @XmlElement(name = "methodType")
    private String methodType;

    /**
     * 客户编码
     */
    @XmlElement(name = "busCode")
    private String busCode;

    /**
     * 海关编码
     */
    @XmlElement(name = "hgCode")
    private String hgCode;

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public String getHgCode() {
        return hgCode;
    }

    public void setHgCode(String hgCode) {
        this.hgCode = hgCode;
    }
}
