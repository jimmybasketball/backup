package com.sfebiz.supplychain.sdk.protocol;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 买家信息类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buyer", propOrder = {"name", "phone", "mobile", "email", "country",
        "province", "city", "district", "streetAddress", "streetAddressEn", "zipCode",
        "identityNumber", "attachments"})
public class Buyer implements Serializable {
    /**
     * 收件人姓名
     */
    @XmlElement(nillable = false, required = true)
    public String name;

    /**
     * 收件人座机
     */
    @XmlElement(nillable = false, required = false)
    public String phone;

    /**
     * 收件人手机
     */
    @XmlElement(nillable = false, required = false)
    public String mobile;

    /**
     * 收件人邮箱
     */
    @XmlElement(nillable = false, required = false)
    public String email;

    /**
     * 收件人国家
     */
    @XmlElement(nillable = false, required = false)
    public String country;

    /**
     * 收件人省
     */
    @XmlElement(nillable = false, required = true)
    public String province;

    /**
     * 收件人市
     */
    @XmlElement(nillable = false, required = true)
    public String city;

    /**
     * 收件人区
     */
    @XmlElement(nillable = false, required = true)
    public String district;

    /**
     * 收件人街道
     */
    @XmlElement(nillable = false, required = true)
    public String streetAddress;

    /**
     * 收件人街道-英文
     */
    @XmlElement(nillable = false, required = true)
    public String streetAddressEn;

    /**
     * 收件人邮编
     */
    @XmlElement(nillable = false, required = false)
    public String zipCode;

    /**
     * 收件人身份证
     */
    @XmlElement(nillable = false, required = true)
    public String identityNumber;

    /**
     * 附件详情
     */
    @XmlElement(nillable = false, required = true)
    public Attachments attachments;

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

    public String getStreetAddressEn() {
        return streetAddressEn;
    }

    public void setStreetAddressEn(String streetAddressEn) {
        this.streetAddressEn = streetAddressEn;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public Attachments getAttachments() {
        if (attachments == null) {
            attachments = new Attachments();
        }
        return attachments;
    }

    public void setAttachments(Attachments attachments) {
        this.attachments = attachments;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", streetAddressEn='" + streetAddressEn + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", attachments=" + attachments +
                '}';
    }
}
