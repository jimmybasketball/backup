package com.sfebiz.supplychain.protocol.fse;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/23.
 */
public class FSEOrderDtl implements Serializable{
    private static final long serialVersionUID = -2392323458007924311L;

    /**
     * 商品编码
     */
    public String commodityCode;

    /**
     * 商品名称
     */
    public String commodityName;

    /**
     * 商品备案编码
     */
    public String commodityBarcode;

    /**
     * 商品数量
     */
    public int qty;

    /**
     * 商品重量 单位:kg ，商品毛重
     */
    public String weight;

    /**
     * 成交单价
     */
    public String tradePrice;

    /**
     * 成交总价
     */
    public String tradeTotal;

    /**
     * 申报单价
     */
    public String declPrice;

    /**
     * 申报总价
     */
    public String declTotalPrice;

    /**
     * 单品税款
     */
    public String taxDtl;

    /**
     * HS 编码
     */
    public String codeTs;

    /**
     * 法定第二单位代码,没有第二单位的填:无
     */
    public String secondUnit;

    /**
     * 批次号
     */
    public String batch;

    @Override
    public String toString() {
        return "FSEOrderDtl{" +
                "commodityCode='" + commodityCode + '\'' +
                ", commodityName='" + commodityName + '\'' +
                ", commodityBarcode='" + commodityBarcode + '\'' +
                ", qty=" + qty +
                ", weight='" + weight + '\'' +
                ", tradePrice='" + tradePrice + '\'' +
                ", tradeTotal='" + tradeTotal + '\'' +
                ", declPrice='" + declPrice + '\'' +
                ", declTotalPrice='" + declTotalPrice + '\'' +
                ", taxDtl='" + taxDtl + '\'' +
                ", codeTs='" + codeTs + '\'' +
                ", secondUnit='" + secondUnit + '\'' +
                ", batch='" + batch + '\'' +
                '}';
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityBarcode() {
        return commodityBarcode;
    }

    public void setCommodityBarcode(String commodityBarcode) {
        this.commodityBarcode = commodityBarcode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getTradeTotal() {
        return tradeTotal;
    }

    public void setTradeTotal(String tradeTotal) {
        this.tradeTotal = tradeTotal;
    }

    public String getDeclPrice() {
        return declPrice;
    }

    public void setDeclPrice(String declPrice) {
        this.declPrice = declPrice;
    }

    public String getDeclTotalPrice() {
        return declTotalPrice;
    }

    public void setDeclTotalPrice(String declTotalPrice) {
        this.declTotalPrice = declTotalPrice;
    }

    public String getTaxDtl() {
        return taxDtl;
    }

    public void setTaxDtl(String taxDtl) {
        this.taxDtl = taxDtl;
    }

    public String getCodeTs() {
        return codeTs;
    }

    public void setCodeTs(String codeTs) {
        this.codeTs = codeTs;
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public void setSecondUnit(String secondUnit) {
        this.secondUnit = secondUnit;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
}
