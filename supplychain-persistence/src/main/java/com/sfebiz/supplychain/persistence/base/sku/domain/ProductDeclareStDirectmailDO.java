package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 济南直邮商品备案明细
 * 
 * @author matt
 * @version $Id: ProductDeclareStDirectmailDO.java, v 0.1 2016年6月2日 下午2:55:10 matt Exp $
 */
public class ProductDeclareStDirectmailDO  extends BaseDO {


    /** 序列号 */
    private static final long serialVersionUID = -6597494489918665316L;
	/**
     * 商品备案主表ID
     */
    private Long productDeclareId;

    /**
     * 用途
     */
    private String categoryName;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产厂家地址
     */
    private String providerAddress;

    /**
     * 主要成分
     */
    private String ingredient;

    /**
     * 功能介绍
     */
    private String features;

    /**
     * HS编码
     */
    private String hsCode;

    /**
     * 是否涉证
     */
    private String isRelateCertificate;

    /**
     * 是否有证
     */
    private String isHaveCertificate;

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

    /**
     *海关申报要素
     */
    private  String customsDeclarationElement;
    /**
     *产品编号
     */
    private String productCode;

    public String getCustomsDeclarationElement() {
        return customsDeclarationElement;
    }

    public void setCustomsDeclarationElement(String customsDeclarationElement) {
        this.customsDeclarationElement = customsDeclarationElement;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

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

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
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

    public String getIsHaveCertificate() {
        return isHaveCertificate;
    }

    public void setIsHaveCertificate(String isHaveCertificate) {
        this.isHaveCertificate = isHaveCertificate;
    }

    public String getIsRelateCertificate() {
        return isRelateCertificate;
    }

    public void setIsRelateCertificate(String isRelateCertificate) {
        this.isRelateCertificate = isRelateCertificate;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
