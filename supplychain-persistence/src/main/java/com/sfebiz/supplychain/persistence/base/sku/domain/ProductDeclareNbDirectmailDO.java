/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.domain/ProductDeclareNbDirectmailDO.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 宁波直邮商品备案明细
 * <p/>
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public class ProductDeclareNbDirectmailDO extends BaseDO {

    private static final long serialVersionUID = 7869522207937251957L;

    /**
     * 商品备案主表ID
     */
    private Long productDeclareId;

    /**
     * 税则号
     */
    private String hsNumber;


    /**
     * hs编码
     */
    private String hsCode;

    /**
     * 仓库企业代码
     */
    private String warehouseEnterpriseCode;

    /**
     * 商品英文名称
     */
    private String skuForeignName;

    /**
     * 商品货号
     */
    private String thirdSkuId;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 成分
     */
    private String ingredient;
    /**
     * 功能
     */
    private String features;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 图片链接
     */
    private String imgUrl;

    public Long getProductDeclareId() {
        return productDeclareId;
    }

    public void setProductDeclareId(Long productDeclareId) {
        this.productDeclareId = productDeclareId;
    }

    public String getWarehouseEnterpriseCode() {
        return warehouseEnterpriseCode;
    }

    public void setWarehouseEnterpriseCode(String warehouseEnterpriseCode) {
        this.warehouseEnterpriseCode = warehouseEnterpriseCode;
    }

    public String getSkuForeignName() {
        return skuForeignName;
    }

    public void setSkuForeignName(String skuForeignName) {
        this.skuForeignName = skuForeignName;
    }

    public String getThirdSkuId() {
        return thirdSkuId;
    }

    public void setThirdSkuId(String thirdSkuId) {
        this.thirdSkuId = thirdSkuId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHsNumber() {
        return hsNumber;
    }

    public void setHsNumber(String hsNumber) {
        this.hsNumber = hsNumber;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }
}
