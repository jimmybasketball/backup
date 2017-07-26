package com.sfebiz.supplychain.protocol.nbport;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by ztc on 2016/12/28.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"logisticsNo","logisticsName","logisticsCode","consignee","province","city","district","consigneeAddr","consigneeTel","mailNo","goodsName"})
@XmlRootElement(name = "OrderLogistics")
public class OrderLogistics implements Serializable {

    @XmlElement(name = "logisticsNo")
    private String logisticsNo;

    @XmlElement(name = "logisticsName")
    private String logisticsName;

    @XmlElement(name = "logisticsCode")
    private String logisticsCode;

    @XmlElement(name = "consignee")
    private String consignee;

    @XmlElement(name = "province")
    private String province;

    @XmlElement(name = "city")
    private String city;

    @XmlElement(name = "district")
    private String district;

    @XmlElement(name = "consigneeAddr")
    private String consigneeAddr;

    @XmlElement(name = "consigneeTel")
    private String consigneeTel;

    @XmlElement(name = "mailNo")
    private String mailNo;

    @XmlElement(name = "goodsName")
    private String goodsName;


    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getConsigneeAddr() {
        return consigneeAddr;
    }

    public void setConsigneeAddr(String consigneeAddr) {
        this.consigneeAddr = consigneeAddr;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
