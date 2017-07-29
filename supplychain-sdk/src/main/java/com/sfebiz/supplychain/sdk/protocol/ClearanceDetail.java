package com.sfebiz.supplychain.sdk.protocol;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * 清关类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clearanceDetail", propOrder = {"carrierCode", "mailNo",
        "orderId", "shipperCode", "deliveryCode", "custId", "payMethod",
        "senderAddress", "logo"})
public class ClearanceDetail implements Serializable {

    private static final long serialVersionUID = -2457383649966833621L;
    /**
     * 清关物流商编号
     */
    @XmlElement(nillable = false, required = true)
    public String carrierCode;
    /**
     * 承运商运单号
     */
    @XmlElement(nillable = false, required = false)
    public String mailNo;
    /**
     * 用户订单号
     */
    @XmlElement(nillable = false, required = false)
    public String orderId;
    /**
     * 寄件方代码，如果是SF，才会有，并且要打印到运单上
     */
    @XmlElement(nillable = false, required = false)
    public String shipperCode;
    /**
     * 目的地代码，如果是SF，才会有，并且要打印到运单上
     */
    @XmlElement(nillable = false, required = false)
    public String deliveryCode;
    /**
     * 月结卡号
     */
    @XmlElement(nillable = false, required = false)
    public String custId;
    /**
     * 付款类型
     */
    @XmlElement(nillable = false, required = false)
    public String payMethod;
    /**
     * 发货地址
     */
    @XmlElement(nillable = false, required = false)
    public String senderAddress;
    /**
     * 清关公司的LOGO图片
     */
    @XmlElement(nillable = false, required = false)
    public String logo;

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "ClearanceDetail{" +
                "carrierCode='" + carrierCode + '\'' +
                ", mailNo='" + mailNo + '\'' +
                ", orderId='" + orderId + '\'' +
                ", shipperCode='" + shipperCode + '\'' +
                ", deliveryCode='" + deliveryCode + '\'' +
                ", custId='" + custId + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", senderAddress='" + senderAddress + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
