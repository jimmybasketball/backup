package com.sfebiz.supplychain.exposed.merchant.entity;

import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 货主申报方式
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-19 09:53
 **/
public class MerchantPayDeclareEntity implements Serializable{


    private static final long serialVersionUID = 6497852477015707541L;

    public Long id;

    /**
     * 货主ID
     */
    public Long merchantId;

    /**
     * 支付方式
     */
    public String payType;

    /**
     * 申报方式
     */
    @NotNull(message = "申报方式不能为空")
    public String declarePayType;

    /**
     * 口岸ID
     */
    @NotNull(message = "口岸ID不能为空")
    public Long portId;

    /**
     * 申报账号
     */
    @NotNull(message = "申报账号不能为空")
    public String declareAccount;

    /**
     * 创建时间
     */
    public Date gmtCreate;

    /**
     * 修改时间
     */
    public Date gmtModified;


    public Long getId() {
        return id;
    }

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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "MerchantPayDeclareEntity{" +
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
