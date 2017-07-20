package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 重庆保税商品备案明细
 * 
 * @author 张雅静
 * @version $Id: ProductDeclareCqBondedDO.java, v 0.1 2016年11月14日 下午2:55:10 张雅静 Exp $
 */
public class ProductDeclareCqBondedDO extends BaseDO {

	/** 序列号 */
	private static final long serialVersionUID = 5883221621183796759L;

	/**
     * 商品备案主表ID
     */
    private Long productDeclareId;

    /**
     * HS编码
     */
    private String hsCode;

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

    public Long getProductDeclareId() {
        return productDeclareId;
    }

    public void setProductDeclareId(Long productDeclareId) {
        this.productDeclareId = productDeclareId;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
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

    public String getCustomsDeclarationElement() {
        return customsDeclarationElement;
    }

    public void setCustomsDeclarationElement(String customsDeclarationElement) {
        this.customsDeclarationElement = customsDeclarationElement;
    }
}
