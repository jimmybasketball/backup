package com.sfebiz.supplychain.persistence.base.merchant.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * 货主申报方式DO
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-19 10:00
 **/
public class MerchantPayDeclareDO extends BaseDO{
    private static final long serialVersionUID = -1904862176544222754L;

    private Long id;

    /**
     * 货主ID
     */
    private Long merchantId;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 申报方式
     */
    private String declarePayType;

    /**
     * 口岸ID
     */
    private Long portId;

    /**
     * 申报账号
     */
    private String declareAccount;

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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getDeclarePayType() {
        return declarePayType;
    }

    public void setDeclarePayType(String declarePayType) {
        this.declarePayType = declarePayType;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
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
        return "MerchantPayDeclareDO{" +
                "id=" + id +
                ", merchantId=" + merchantId +
                ", payType='" + payType + '\'' +
                ", declarePayType='" + declarePayType + '\'' +
                ", portId=" + portId +
                ", declareAccount='" + declareAccount + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
