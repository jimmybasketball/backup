package com.sfebiz.supplychain.protocol.fse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Shipporder implements Serializable{
    private static final long serialVersionUID = -3814503760371747000L;

    public String orderCode;

    public String companyNo;

    public String expressNo;

    public String shippingTime;

    public String realWeight;

    public List<Detail> details;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getRealWeight() {
        return realWeight;
    }

    public void setRealWeight(String realWeight) {
        this.realWeight = realWeight;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Shipporder{" +
                "orderCode='" + orderCode + '\'' +
                ", companyNo='" + companyNo + '\'' +
                ", expressNo='" + expressNo + '\'' +
                ", shippingTime='" + shippingTime + '\'' +
                ", realWeight='" + realWeight + '\'' +
                ", details=" + details +
                '}';
    }
}
