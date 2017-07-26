package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 付款详情类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paid", propOrder = {"tradeOrderValue", "tradeOrderValueUnit", "totalShippingFee"
        , "discountValue", "discountValueUnit", "goodsValue", "goodsValueUnit"
        , "totalShippingFeeUnit", "payableWeight", "payTime", "payNumber", "payCompanyCode", "payCompanyName", "eCommerceCode", "eCommerceName", "eCommerceDomain"})
public class Paid implements Serializable {

    private static final long serialVersionUID = 4899386189411637539L;
    /**
     * 订单实际交易(支付)价格
     */
    @XmlElement(nillable = false, required = false)
    public int tradeOrderValue = 0;

    /**
     * 订单商品价格单位
     */
    @XmlElement(nillable = false, required = false)
    public String tradeOrderValueUnit;

    /**
     * 订单商品运费价格
     */
    @XmlElement(nillable = false, required = false)
    public int totalShippingFee = 0;

    /**
     * 订单商品运费单位
     */
    @XmlElement(nillable = false, required = false)
    public String totalShippingFeeUnit;

    /**
     * 订单折扣金额
     */
    @XmlElement(nillable = false, required = false)
    public int discountValue;

    /**
     * 订单折扣金额单位
     */
    @XmlElement(nillable = false, required = false)
    public String discountValueUnit;

    /**
     * 订单货款金额
     */
    @XmlElement(nillable = false, required = false)
    public int goodsValue;

    /**
     * 订单货款金额单位
     */
    @XmlElement(nillable = false, required = false)
    public String goodsValueUnit;

    /**
     * 订单商品运费重量
     */
    @XmlElement(nillable = false, required = false)
    public int payableWeight;

    /**
     * 支付时间
     */
    @XmlElement(nillable = false, required = false)
    public String payTime;

    /**
     * 支付流水号
     */
    @XmlElement(nillable = false, required = false)
    public String payNumber;

    /**
     * 支付企业备案编码
     */
    @XmlElement(nillable = false, required = false)
    public String payCompanyCode;

    /**
     * 支付企业备案名称
     */
    @XmlElement(nillable = false, required = false)
    public String payCompanyName;

    /**
     * 电商企业编码
     */
    @XmlElement(nillable = false, required = false)
    public String eCommerceCode;

    /**
     * 电商企业名称
     */
    @XmlElement(nillable = false, required = false)
    public String eCommerceName;

    /**
     * 电商顶级域名
     */
    @XmlElement(nillable = false, required = false)
    public String eCommerceDomain;


    public int getTradeOrderValue() {
        return tradeOrderValue;
    }

    public void setTradeOrderValue(int tradeOrderValue) {
        this.tradeOrderValue = tradeOrderValue;
    }

    public String getTradeOrderValueUnit() {
        return tradeOrderValueUnit;
    }

    public void setTradeOrderValueUnit(String tradeOrderValueUnit) {
        this.tradeOrderValueUnit = tradeOrderValueUnit;
    }

    public int getTotalShippingFee() {
        return totalShippingFee;
    }

    public void setTotalShippingFee(int totalShippingFee) {
        this.totalShippingFee = totalShippingFee;
    }

    public String getTotalShippingFeeUnit() {
        return totalShippingFeeUnit;
    }

    public void setTotalShippingFeeUnit(String totalShippingFeeUnit) {
        this.totalShippingFeeUnit = totalShippingFeeUnit;
    }

    public int getPayableWeight() {
        return payableWeight;
    }

    public void setPayableWeight(int payableWeight) {
        this.payableWeight = payableWeight;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public String getDiscountValueUnit() {
        return discountValueUnit;
    }

    public void setDiscountValueUnit(String discountValueUnit) {
        this.discountValueUnit = discountValueUnit;
    }

    public int getGoodsValue() {
        return goodsValue;
    }

    public void setGoodsValue(int goodsValue) {
        this.goodsValue = goodsValue;
    }

    public String getGoodsValueUnit() {
        return goodsValueUnit;
    }

    public void setGoodsValueUnit(String goodsValueUnit) {
        this.goodsValueUnit = goodsValueUnit;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public String getPayCompanyCode() {
        return payCompanyCode;
    }

    public void setPayCompanyCode(String payCompanyCode) {
        this.payCompanyCode = payCompanyCode;
    }

    public String getPayCompanyName() {
        return payCompanyName;
    }

    public void setPayCompanyName(String payCompanyName) {
        this.payCompanyName = payCompanyName;
    }

    public String geteCommerceCode() {
        return eCommerceCode;
    }

    public void seteCommerceCode(String eCommerceCode) {
        this.eCommerceCode = eCommerceCode;
    }

    public String geteCommerceName() {
        return eCommerceName;
    }

    public void seteCommerceName(String eCommerceName) {
        this.eCommerceName = eCommerceName;
    }

    public String geteCommerceDomain() {
        return eCommerceDomain;
    }

    public void seteCommerceDomain(String eCommerceDomain) {
        this.eCommerceDomain = eCommerceDomain;
    }

    @Override
    public String toString() {
        return "Paid{" +
                "tradeOrderValue=" + tradeOrderValue +
                ", tradeOrderValueUnit='" + tradeOrderValueUnit + '\'' +
                ", totalShippingFee=" + totalShippingFee +
                ", totalShippingFeeUnit='" + totalShippingFeeUnit + '\'' +
                ", discountValue=" + discountValue +
                ", discountValueUnit='" + discountValueUnit + '\'' +
                ", goodsValue=" + goodsValue +
                ", goodsValueUnit='" + goodsValueUnit + '\'' +
                ", payableWeight=" + payableWeight +
                ", payTime='" + payTime + '\'' +
                ", payNumber='" + payNumber + '\'' +
                ", payCompanyCode='" + payCompanyCode + '\'' +
                ", payCompanyName='" + payCompanyName + '\'' +
                ", eCommerceCode='" + eCommerceCode + '\'' +
                ", eCommerceName='" + eCommerceName + '\'' +
                ", eCommerceDomain='" + eCommerceDomain + '\'' +
                '}';
    }
}
