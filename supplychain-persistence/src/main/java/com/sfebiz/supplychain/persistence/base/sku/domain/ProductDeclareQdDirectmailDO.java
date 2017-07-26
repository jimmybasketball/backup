/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.domain/ProductDeclareGzDirectmailDO.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 青岛直邮商品备案明细
 * <p/>
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public class ProductDeclareQdDirectmailDO extends BaseDO {

    private static final long serialVersionUID = 4333441613702095722L;

    /**
     * 商品备案主表ID
     */
    private Long productDeclareId;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 第三方商品编号
     */
    private String thirdSkuId;

    /**
     * 第二计量单位
     */
    private String secondaryMeasuringUnit;

    /**
     * 商品品质
     */
    private String skuQuality;

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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getThirdSkuId() {
        return thirdSkuId;
    }

    public void setThirdSkuId(String thirdSkuId) {
        this.thirdSkuId = thirdSkuId;
    }

    public String getSecondaryMeasuringUnit() {
        return secondaryMeasuringUnit;
    }

    public void setSecondaryMeasuringUnit(String secondaryMeasuringUnit) {
        this.secondaryMeasuringUnit = secondaryMeasuringUnit;
    }

    public String getSkuQuality() {
        return skuQuality;
    }

    public void setSkuQuality(String skuQuality) {
        this.skuQuality = skuQuality;
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
