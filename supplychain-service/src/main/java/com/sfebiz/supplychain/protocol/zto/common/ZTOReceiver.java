package com.sfebiz.supplychain.protocol.zto.common;

/**
 * Created by zhangdi on 2015/11/17.
 */
public class ZTOReceiver {

    /**
     * 收件人姓名
     */
    private String name;

    /**
     * 收件人手机号
     */
    private String mobile;

    /**
     * 收件人电话号码
     */
    private String phone;

    /**
     * 收件人城市，省、市、区逐级制定，逗号分隔
     */
    private String city;

    /**
     * 收件人地址
     */
    private String address;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
