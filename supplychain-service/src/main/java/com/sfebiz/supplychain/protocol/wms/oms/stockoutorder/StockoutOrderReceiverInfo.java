package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User:tu.jie@ifunq.com
 * Date: 15/1/21
 * Time: 下午9:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SaleOrder")
@XmlType(propOrder = {"receiverCompany","receiverName","receiverEmail","receiverZipCode","receiverMobile","receiverPhone","receiverCountry","receiverProvince"
,"receiverCity","receiverArea","receiverIdType","ReceiverIdCard","receiverAddress"})
public class StockoutOrderReceiverInfo implements Serializable {


    private static final long serialVersionUID = 4121148515674063743L;
    @Description("寄件方公司")
    @XmlElement(name = "ReceiverCompany", required = false)
    private String receiverCompany;

    @Description("收件方名称")
    @XmlElement(name = "ReceiverName", required = false)
    private String receiverName;

    @Description("收件方电子邮箱")
    @XmlElement(name = "ReceiverEmail", required = false)
    private String receiverEmail;

    @Description("收件方邮编")
    @XmlElement(name = "ReceiverZipCode", required = false)
    private String receiverZipCode;

    @Description("收件方手机")
    @XmlElement(name = "ReceiverMobile", required = false)
    private String receiverMobile;

    @Description("收件方电话")
    @XmlElement(name = "ReceiverPhone", required = false)
    private String receiverPhone;

    @Description("收件方国家")
    @XmlElement(name = "ReceiverCountry", required = false)
    private String receiverCountry;

    @Description("收件方省份")
    @XmlElement(name = "ReceiverProvince", required = false)
    private String receiverProvince;

    @Description("收件方城市")
    @XmlElement(name = "ReceiverCity", required = false)
    private String receiverCity;

    @Description("收件方区县")
    @XmlElement(name = "ReceiverArea", required = false)
    private String receiverArea;

    @Description("收件方地址")
    @XmlElement(name = "ReceiverAddress", required = false)
    private String receiverAddress;

    @Description("收件方证件类型")
    @XmlElement(name = "ReceiverIdType", required = false)
    private String receiverIdType;

    @Description("收件方证件号码")
    @XmlElement(name = "ReceiverIdCard", required = false)
    private String ReceiverIdCard;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getReceiverCompany() {
        return receiverCompany;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getReceiverZipCode() {
        return receiverZipCode;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public String getReceiverArea() {
        return receiverArea;
    }

    public String getReceiverIdType() {
        return receiverIdType;
    }

    public String getReceiverIdCard() {
        return ReceiverIdCard;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public void setReceiverCompany(String receiverCompany) {
        this.receiverCompany = receiverCompany;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public void setReceiverZipCode(String receiverZipCode) {
        this.receiverZipCode = receiverZipCode;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public void setReceiverArea(String receiverArea) {
        this.receiverArea = receiverArea;
    }

    public void setReceiverIdType(String receiverIdType) {
        this.receiverIdType = receiverIdType;
    }

    public void setReceiverIdCard(String receiverIdCard) {
        ReceiverIdCard = receiverIdCard;
    }
}
