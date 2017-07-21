package com.sfebiz.supplychain.persistence.base.merchant.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * 货主包材DO
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-20 12:47
 **/
public class MerchantPackageMaterialDO extends BaseDO{

    private static final long serialVersionUID = -5580709275162215145L;

    private Long id;

    /**
     * 货主ID
     */
    private Long merchantId;

    /**
     * 订单来源
     */
    private String orderSource;

    /**
     * 寄件人名
     */
    private String senderName;

    /**
     * 寄件人电话
     */
    private String senderTelephone;

    /**
     * 包材类型
     */
    private String packageMaterialType;

    /**
     * 宁波店铺号
     */
    private String nbShopNumber;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderTelephone() {
        return senderTelephone;
    }

    public void setSenderTelephone(String senderTelephone) {
        this.senderTelephone = senderTelephone;
    }

    public String getPackageMaterialType() {
        return packageMaterialType;
    }

    public void setPackageMaterialType(String packageMaterialType) {
        this.packageMaterialType = packageMaterialType;
    }

    public String getNbShopNumber() {
        return nbShopNumber;
    }

    public void setNbShopNumber(String nbShopNumber) {
        this.nbShopNumber = nbShopNumber;
    }

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    @Override
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public Date getGmtModified() {
        return gmtModified;
    }

    @Override
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "MerchantPackageMaterialDO{" +
                "id=" + id +
                ", merchantId=" + merchantId +
                ", orderSource='" + orderSource + '\'' +
                ", senderName='" + senderName + '\'' +
                ", senderTelephone='" + senderTelephone + '\'' +
                ", packageMaterialType='" + packageMaterialType + '\'' +
                ", nbShopNumber='" + nbShopNumber + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
