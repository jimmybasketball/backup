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
@XmlType(propOrder = {"senderCompany","senderName","senderEmail","senderPhone","senderZipCode","senderMobile","senderCountry","senderProvince","senderCity","senderArea","senderAddress"})
public class StockoutOrderSenderInfo implements Serializable {


    private static final long serialVersionUID = -1472922680325207474L;
    @Description("寄件方公司")
    @XmlElement(name = "SenderCompany", required = false)
    private String senderCompany;

    @Description("寄件方名称")
    @XmlElement(name = "SenderName", required = false)
    private String senderName;

    @Description("寄件方电子邮箱")
    @XmlElement(name = "SenderEmail", required = false)
    private String senderEmail;

    @Description("寄件方邮编")
    @XmlElement(name = "SenderZipCode", required = false)
    private String senderZipCode;

    @Description("寄件方手机")
    @XmlElement(name = "SenderMobile", required = false)
    private String senderMobile;

    @Description("寄件方电话")
    @XmlElement(name = "SenderPhone", required = false)
    private String senderPhone;

    @Description("寄件方国家")
    @XmlElement(name = "SenderCountry", required = false)
    private String senderCountry;

    @Description("寄件方省份")
    @XmlElement(name = "SenderProvince", required = false)
    private String senderProvince;

    @Description("寄件方城市")
    @XmlElement(name = "SenderCity", required = false)
    private String senderCity;

    @Description("寄件方区县")
    @XmlElement(name = "SenderArea", required = false)
    private String senderArea;

    @Description("寄件方地址")
    @XmlElement(name = "SenderAddress", required = false)
    private String senderAddress;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSenderCompany() {
        return senderCompany;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderZipCode() {
        return senderZipCode;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public String getSenderProvince() {
        return senderProvince;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public String getSenderArea() {
        return senderArea;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderCompany(String senderCompany) {
        this.senderCompany = senderCompany;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public void setSenderZipCode(String senderZipCode) {
        this.senderZipCode = senderZipCode;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public void setSenderProvince(String senderProvince) {
        this.senderProvince = senderProvince;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public void setSenderArea(String senderArea) {
        this.senderArea = senderArea;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
}
