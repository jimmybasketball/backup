/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.domain/ProductDeclareHzBondedDO.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.math.BigDecimal;

/**
 * 杭州保税商品备案明细
 * <p/>
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public class ProductDeclareHzBondedDO extends BaseDO {

    private static final long serialVersionUID = 8216521418096107249L;

    /**
     * 商品备案主表ID
     */
    private Long productDeclareId;

    /**
     * 规范申报
     */
    private String specificationDeclare;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 成交数量
     */
    private Integer tradeCount;

    /**
     * 功能用途
     */
    private String features;

    /**
     * 第一/第二计量单位
     */
    private String secondaryMeasuringUnit;

    /**
     * 币制
     */
    private String currency;

    /**
     * 备案任务创建人
     */
    private String createUser;
    /**
     *资料收集完毕导入人
     */
    private String dataCollectUser;
    /**
     *备案完成导入人
     */
    private String filingUser;
    /**
     *供应商名称
     */
    private String providerName;
    /**
     *采买模式
     */
    private String purchaseMode;

    public Long getProductDeclareId() {
        return productDeclareId;
    }

    public void setProductDeclareId(Long productDeclareId) {
        this.productDeclareId = productDeclareId;
    }

    public String getSpecificationDeclare() {
        return specificationDeclare;
    }

    public void setSpecificationDeclare(String specificationDeclare) {
        this.specificationDeclare = specificationDeclare;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getSecondaryMeasuringUnit() {
        return secondaryMeasuringUnit;
    }

    public void setSecondaryMeasuringUnit(String secondaryMeasuringUnit) {
        this.secondaryMeasuringUnit = secondaryMeasuringUnit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDataCollectUser() {
        return dataCollectUser;
    }

    public void setDataCollectUser(String dataCollectUser) {
        this.dataCollectUser = dataCollectUser;
    }

    public String getFilingUser() {
        return filingUser;
    }

    public void setFilingUser(String filingUser) {
        this.filingUser = filingUser;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getPurchaseMode() {
        return purchaseMode;
    }

    public void setPurchaseMode(String purchaseMode) {
        this.purchaseMode = purchaseMode;
    }
}
