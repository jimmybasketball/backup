/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.domain/ProductDeclareNbBondedDO.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 宁波保税商品备案明细
 * <p/>
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public class ProductDeclareNbBondedDO extends BaseDO {

    private static final long serialVersionUID = 5502955373918018119L;

    /**
     * 商品备案主表ID
     */
    private Long productDeclareId;

    /**
     * 海关申报要素
     */
    private String customsDeclarationElement;

    /**
     * 综合平台商品编号
     */
    private Long nbbsSkuId;

    /**
     * 产品外观图示
     */
    private String productGraphicUrl;

    /**
     * 备案任务创建人
     */
    private String createUser;
    /**
     * 资料收集完毕导入人
     */
    private String dataCollectUser;
    /**
     * 备案完成导入人
     */
    private String filingUser;
    /**
     * 供应商名称
     */
    private String providerName;
    /**
     * 采买模式
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

    public Long getNbbsSkuId() {
        return nbbsSkuId;
    }

    public void setNbbsSkuId(Long nbbsSkuId) {
        this.nbbsSkuId = nbbsSkuId;
    }

    public String getProductGraphicUrl() {
        return productGraphicUrl;
    }

    public void setProductGraphicUrl(String productGraphicUrl) {
        this.productGraphicUrl = productGraphicUrl;
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
