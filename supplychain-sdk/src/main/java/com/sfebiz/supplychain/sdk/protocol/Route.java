package com.sfebiz.supplychain.sdk.protocol;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 单条路由结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "route", propOrder = {"acceptTime", "acceptAddress", "remark", "opcode"})
public class Route implements Serializable {
    private static final long serialVersionUID = 2501382811415318086L;

    /**
     * 时间
     */
    @XmlElement(name = "acceptTime")
    public String acceptTime;

    /**
     * 接收地址
     */
    @XmlElement(name = "acceptAddress")
    public String acceptAddress;

    /**
     * 备注
     */
    @XmlElement(name = "remark")
    public String remark;

    /**
     * 操作码
     */
    @XmlElement(name = "opcode")
    public String opcode;

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptAddress() {
        return acceptAddress;
    }

    public void setAcceptAddress(String acceptAddress) {
        this.acceptAddress = acceptAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("acceptTime", acceptTime)
                .append("acceptAddress", acceptAddress)
                .append("remark", remark)
                .append("opcode", opcode)
                .toString();
    }
}
