package com.sfebiz.supplychain.protocol.wms.ptwms.createOrder;

import com.sfebiz.supplychain.protocol.wms.ptwms.SkuItem;

import java.util.List;

/**
 * Created by TT on 2016/7/5.
 */
public class StockoutInfo {

    /**
     * 订单号
     */
    private String reference_no;

    /**
     * 平台
     */
    private String platform;

    /**
     * 配送方式
     */
    private String shipping_method;

    /**
     *配送仓库
     */
    private String warehouse_code;

    /**
     * 收件人国家
     */
    private String country_code;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String region;

    /**
     * 地址1
     */
    private String address1;

    /**
     * 地址2
     */
    private String address2;

    /**
     * 地址3
     */
    private String address3;

    /**
     * 邮编
     */
    private String zipcode;

    /**
     * 身份证
     */
    private String identityNo;

    /**
     * 门牌号
     */
    private String doorplate;

    /**
     * 收件人姓名
     */
    private String name;

    /**
     * 收件人电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 支付交易号
     */
    private String transaction_no;

    /**
     * 是否审核
     */
    private Integer verify = 0;

    /**
     * 是否强制审核
     */
    private Integer forceVerify = 0;

    /**
     * 订单明细
     */
    private List<SkuItem> items;

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getShipping_method() {
        return shipping_method;
    }

    public void setShipping_method(String shipping_method) {
        this.shipping_method = shipping_method;
    }

    public String getWarehouse_code() {
        return warehouse_code;
    }

    public void setWarehouse_code(String warehouse_code) {
        this.warehouse_code = warehouse_code;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getDoorplate() {
        return doorplate;
    }

    public void setDoorplate(String doorplate) {
        this.doorplate = doorplate;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }

    public Integer getForceVerify() {
        return forceVerify;
    }

    public void setForceVerify(Integer forceVerify) {
        this.forceVerify = forceVerify;
    }

    public List<SkuItem> getItems() {
        return items;
    }

    public void setItems(List<SkuItem> items) {
        this.items = items;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "StockoutInfo{" +
                "reference_no='" + reference_no + '\'' +
                ", platform='" + platform + '\'' +
                ", shipping_method='" + shipping_method + '\'' +
                ", warehouse_code='" + warehouse_code + '\'' +
                ", country_code='" + country_code + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", identityNo='" + identityNo + '\'' +
                ", doorplate='" + doorplate + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", transaction_no='" + transaction_no + '\'' +
                ", verify=" + verify +
                ", forceVerify=" + forceVerify +
                ", items=" + items +
                '}';
    }
}
