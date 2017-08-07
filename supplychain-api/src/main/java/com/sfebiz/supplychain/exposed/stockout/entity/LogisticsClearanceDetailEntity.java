package com.sfebiz.supplychain.exposed.stockout.entity;

import java.io.Serializable;

/**
 * 清关信息
 */
public class LogisticsClearanceDetailEntity implements Serializable {
    private static final long serialVersionUID = -4192237884965828632L;
    /**
     * 清关承运商，SF 或 EMS"
     */
    public String carrierCode;

    /**
     * 承运单号，如果是SF，才会有，并且需要打印到运单上
     */
    public String mailNo;

    /**
     * 订单ID，如果是SF，才会有，并且需要打印到运单上
     */
    public String orderId;

    /**
     * 寄件方代码，如果是SF，才会有，并且要打印到运单上
     */
    public String shipperCode;

    /**
     * 目的地代码，如果是SF，才会有，并且要打印到运单上
     */
    public String deliveryCode;

    /**
     * 月结卡号
     */
    public String custId;

    /**
     * 付款类型
     */
    public String payMethod;

    /**
     * 发货地址
     */
    public String senderAddress;

    /**
     * logo地址
     */
    public String logo;

    @Override
    public String toString() {
        return "LogisticsClearanceDetailEntity{" +
                "carrierCode='" + carrierCode + '\'' +
                ", mailNo='" + mailNo + '\'' +
                ", orderId='" + orderId + '\'' +
                ", shipperCode='" + shipperCode + '\'' +
                ", deliveryCode='" + deliveryCode + '\'' +
                ", custId='" + custId + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", senderAddress='" + senderAddress + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
