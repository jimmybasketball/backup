/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.domain/ProductDeclareGzBondedDO.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.math.BigDecimal;

/**
 * 广州保税商品备案明细
 * <p/>
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public class ProductDeclareGzBondedDO extends BaseDO {

    private static final long serialVersionUID = -7092165776992939420L;

    /**
     * 商品备案主表ID
     */
    private Long productDeclareId;

    /**
     * 海关申报要素
     */
    private String customsDeclarationElement;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 商品货号
     */
    private String thirdSkuId;

    /**
     * 进口单价(人民币)
     */
    private BigDecimal importUnitPriceRmb;

    /**
     * 销售零售价(人民币)
     */
    private BigDecimal retailPriceRmb;

    /**
     * 商品备案价格(人民币)
     */
    private BigDecimal declarePriceRmb;

    /**
     * 网站销售渠道
     */
    private String salesChannel;

    /**
     * 条形码图示
     */
    private String barcodeGraphicUrl;

    /**
     * 规格型号图示
     */
    private String attributesGraphicUrl;

    /**
     * 保质期图示
     */
    private String expirationDateGraphicUrl;

    /**
     * 产品图示
     */
    private String productGraphicUrl;

    /**
     * 包装标签中文翻译
     */
    private String chineseLabelGraphicUrl;

    /**
     * 产品三视图
     */
    private String productResearchedGraphicUrl;

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

    public String getCustomsDeclarationElement() {
        return customsDeclarationElement;
    }

    public void setCustomsDeclarationElement(String customsDeclarationElement) {
        this.customsDeclarationElement = customsDeclarationElement;
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

    public BigDecimal getImportUnitPriceRmb() {
        return importUnitPriceRmb;
    }

    public void setImportUnitPriceRmb(BigDecimal importUnitPriceRmb) {
        this.importUnitPriceRmb = importUnitPriceRmb;
    }

    public BigDecimal getRetailPriceRmb() {
        return retailPriceRmb;
    }

    public void setRetailPriceRmb(BigDecimal retailPriceRmb) {
        this.retailPriceRmb = retailPriceRmb;
    }

    public BigDecimal getDeclarePriceRmb() {
        return declarePriceRmb;
    }

    public void setDeclarePriceRmb(BigDecimal declarePriceRmb) {
        this.declarePriceRmb = declarePriceRmb;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getBarcodeGraphicUrl() {
        return barcodeGraphicUrl;
    }

    public void setBarcodeGraphicUrl(String barcodeGraphicUrl) {
        this.barcodeGraphicUrl = barcodeGraphicUrl;
    }

    public String getAttributesGraphicUrl() {
        return attributesGraphicUrl;
    }

    public void setAttributesGraphicUrl(String attributesGraphicUrl) {
        this.attributesGraphicUrl = attributesGraphicUrl;
    }

    public String getExpirationDateGraphicUrl() {
        return expirationDateGraphicUrl;
    }

    public void setExpirationDateGraphicUrl(String expirationDateGraphicUrl) {
        this.expirationDateGraphicUrl = expirationDateGraphicUrl;
    }

    public String getProductGraphicUrl() {
        return productGraphicUrl;
    }

    public void setProductGraphicUrl(String productGraphicUrl) {
        this.productGraphicUrl = productGraphicUrl;
    }

    public String getChineseLabelGraphicUrl() {
        return chineseLabelGraphicUrl;
    }

    public void setChineseLabelGraphicUrl(String chineseLabelGraphicUrl) {
        this.chineseLabelGraphicUrl = chineseLabelGraphicUrl;
    }

    public String getProductResearchedGraphicUrl() {
        return productResearchedGraphicUrl;
    }

    public void setProductResearchedGraphicUrl(String productResearchedGraphicUrl) {
        this.productResearchedGraphicUrl = productResearchedGraphicUrl;
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
