package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 联系人基础信息类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"name", "phone", "mobile", "email", "country",
        "province", "city", "district", "streetAddress", "zipCode", "code", "deliveryMode", "identityNumber"})
public class ContactDetail implements Serializable {

    /**
     * 姓名
     */
    @XmlElement(nillable = false, required = true)
    public String name;

    /**
     * 座机
     */
    @XmlElement(nillable = false, required = false)
    public String phone;

    /**
     * 手机
     */
    @XmlElement(nillable = false, required = false)
    public String mobile;

    /**
     * 邮箱
     */
    @XmlElement(nillable = false, required = false)
    public String email;

    /**
     * 国家
     */
    @XmlElement(nillable = false, required = false)
    public String country;

    /**
     * 省
     */
    @XmlElement(nillable = false, required = true)
    public String province;

    /**
     * 城市
     */
    @XmlElement(nillable = false, required = true)
    public String city;

    /**
     * 区
     */
    @XmlElement(nillable = false, required = true)
    public String district;

    /**
     * 地址
     */
    @XmlElement(nillable = false, required = true)
    public String streetAddress;

    /**
     * 邮政编码
     */
    @XmlElement(nillable = false, required = false)
    public String zipCode;

    /**
     * 仓库编号
     */
    @XmlElement(nillable = false, required = true)
    public String code;

    /**
     * 提货方式
     */
    @XmlElement(nillable = false, required = true)
    public String deliveryMode;

    /**
     * 身份证号
     */
    @XmlElement(nillable = false, required = true)
    public String identityNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }
}
