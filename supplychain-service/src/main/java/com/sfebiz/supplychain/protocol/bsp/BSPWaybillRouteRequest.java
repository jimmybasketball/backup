package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by wang_cl on 2015/1/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "mailno", "orderid", "acceptTime", "acceptAddress", "remark", "opCode" })
@XmlRootElement(name = "WaybillRoute")
@Description("路由推送请求结构体")
public class BSPWaybillRouteRequest extends BSPBody  implements Serializable {
    private static final long serialVersionUID = 9168847835170121992L;
    @XmlAttribute(name = "id")
    @Description("路由编号，每一个id 代表一条不同的路由")
    public Integer id;
    @XmlAttribute(name = "mailno")
    @Description("运单号")
    public String mailno;
    @XmlAttribute(name = "orderid")
    @Description("订单号")
    public String orderid;
    @XmlAttribute(name = "acceptTime")
    @Description("路由产生时间")
    public String acceptTime;
    @XmlAttribute(name = "acceptAddress")
    @Description("路由发生城市")
    public String acceptAddress;
    @XmlAttribute(name = "remark")
    @Description("路由说明")
    public String remark;
    @XmlAttribute(name = "opCode")
    @Description("操作码")
    public String opCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMailno() {
        return mailno;
    }

    public void setMailno(String mailno) {
        this.mailno = mailno;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

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

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    @Override
    public String toString() {
        return "BSPWaybillRouteRequest{" +
                "id=" + id +
                ", mailno='" + mailno + '\'' +
                ", orderid='" + orderid + '\'' +
                ", acceptTime='" + acceptTime + '\'' +
                ", acceptAddress='" + acceptAddress + '\'' +
                ", remark='" + remark + '\'' +
                ", opCode='" + opCode + '\'' +
                "} " + super.toString();
    }
}
