package com.sfebiz.supplychain.protocol.wms.nbbs.query;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;

/**
 * Description:宁波保税订单查询报文头
 * Created by yanghua on 2017/3/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "head")
@XmlType(propOrder = {"methodType", "rule", "busCode"})
public class NBBSOrderQueryRequestHead {
    @Description("操做类型--find")
    @XmlElement(name =  "methodType",required = true)
    private String methodType;

    @Description("查询规则--0:客户订单号查询 1:能容订单号查询 2:日期查询")
    @XmlElement(name =  "rule",required = true)
    private String rule;

    @Description("客户编码--客户信息，由EDI提供")
    @XmlElement(name =  "busCode",required = true)
    private String busCode;

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }
}
