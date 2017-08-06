package com.sfebiz.supplychain.protocol.wms.fse;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/4.
 */
public class FSESkuItem implements Serializable{

    private static final long serialVersionUID = -3213393530667611523L;

    /**
     * 仓库代码
     */
    private String warehouseCode;

    /**
     * 货主编码
     */
    public String CompanyCode;

    /**
     * 商品编码
     */
    private String commodityCode;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 商品规格
     */
    private String models;

    /**
     * 单位编码，对照口岸标识
     */
    private String unit;

    /**
     * 重量，单位kg
     */
    private String weight;

    /**
     * 商品原产地名称，对照口岸标准
     */
    private String tradeCountryName;

    /**
     * 商品HS编码
     */
    private String HScode;

    /**
     * 商品备案编码
     */
    private String parentCode;

    /**
     * 商品法定第一单位代码
     */
    private String firstUnit;

    /**
     * 商品法定第二单位代码
     */
    private String secondUnit;

    public String getSecondUnit() {
        return secondUnit;
    }

    public void setSecondUnit(String secondUnit) {
        this.secondUnit = secondUnit;
    }

    public String getFirstUnit() {
        return firstUnit;
    }

    public void setFirstUnit(String firstUnit) {
        this.firstUnit = firstUnit;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTradeCountryName() {
        return tradeCountryName;
    }

    public void setTradeCountryName(String tradeCountryName) {
        this.tradeCountryName = tradeCountryName;
    }

    public String getHScode() {
        return HScode;
    }

    public void setHScode(String HScode) {
        this.HScode = HScode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    @Override
    public String toString() {
        return "FSESkuItem{" +
                "warehouseCode='" + warehouseCode + '\'' +
                ", CompanyCode='" + CompanyCode + '\'' +
                ", commodityCode='" + commodityCode + '\'' +
                ", commodityName='" + commodityName + '\'' +
                ", models='" + models + '\'' +
                ", unit='" + unit + '\'' +
                ", weight='" + weight + '\'' +
                ", tradeCountryName='" + tradeCountryName + '\'' +
                ", HScode='" + HScode + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", firstUnit='" + firstUnit + '\'' +
                ", secondUnit='" + secondUnit + '\'' +
                '}';
    }
}
