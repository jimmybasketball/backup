package com.sfebiz.supplychain.protocol.fse;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/17.
 */
public class Item implements Serializable{
    private static final long serialVersionUID = 2720279994080604187L;

    /**
     * 商品名称
     */
    public String commodityName;

    /**
     * 商品编码
     */
    public String commodityBarcode;

    /**
     * 应入库数量
     */
    public String shouldInQty;

    /**
     * 实际入库数量
     */
    public String RealInQty;

    /**
     * 报损数量
     */
    public String damageNum;

    /**
     * 商品单价
     */
    public String commodityPrice;

    /**
     * 生产日期
     */
    public String manufactureDate;

    /**
     * 过期日期
     */
    public String expirationDate;

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
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

    public String getShouldInQty() {
        return shouldInQty;
    }

    public void setShouldInQty(String shouldInQty) {
        this.shouldInQty = shouldInQty;
    }

    public String getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(String commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public String getRealInQty() {
        return RealInQty;
    }

    public void setRealInQty(String realInQty) {
        RealInQty = realInQty;
    }

    public String getDamageNum() {
        return damageNum;
    }

    public void setDamageNum(String damageNum) {
        this.damageNum = damageNum;
    }

    @Override
    public String toString() {
        return "Item{" +
                "commodityName='" + commodityName + '\'' +
                ", commodityBarcode='" + commodityBarcode + '\'' +
                ", shouldInQty='" + shouldInQty + '\'' +
                ", RealInQty='" + RealInQty + '\'' +
                ", damageNum='" + damageNum + '\'' +
                ", commodityPrice='" + commodityPrice + '\'' +
                ", manufactureDate='" + manufactureDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                '}';
    }
}
