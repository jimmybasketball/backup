package com.sfebiz.supplychain.protocol.zto.common;

/**
 * 发件人
 * Created by zhangdi on 2015/11/17.
 */
public class ZTOSender {

    /**
     * 发件人姓名
     */
    private String name;

    /**
     * 发件人手机号
     */
    private String mobile;

    /**
     * 发件人所在城市，省、市、区逐级制定，逗号分隔
     */
    private String city;

    /**
     * 发件人地址
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
}
