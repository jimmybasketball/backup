package com.sfebiz.supplychain.protocol.pay.yihuijin;

/**
 * 购买人信息
 * Created by zhangdi on 2015/9/10.
 */
public class Payer {

    private String name;
    private String idType;
    private String idNum;
    private String customerId;
    private String bankCardNum;
    private String phoneNum;
    private String email;
    private String nationality;

    public Payer() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdType() {
        return this.idType;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getIdNum() {
        return this.idNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getBankCardNum() {
        return this.bankCardNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return this.nationality;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
