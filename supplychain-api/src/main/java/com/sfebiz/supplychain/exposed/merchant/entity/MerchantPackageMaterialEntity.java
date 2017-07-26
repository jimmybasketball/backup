package com.sfebiz.supplychain.exposed.merchant.entity;

import com.sfebiz.supplychain.exposed.common.enums.RegularExpressionConstant;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 货主包材实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-20 12:43
 **/
public class MerchantPackageMaterialEntity implements Serializable{

    private static final long serialVersionUID = 5983393304719133878L;


    public Long id;

    /**
     * 货主ID
     */
    public Long merchantId;

    /**
     * 订单来源
     */
    @NotNull(message = "订单来源不能为空")
    public String orderSource;

    /**
     * 寄件人名
     */
    @NotNull(message = "寄件人姓名不能为空")
    public String senderName;

    /**
     * 寄件人手机号
     */
    @NotNull(message = "寄件人电话不能为空")
    @MatchPattern(pattern = {RegularExpressionConstant.PHONE_CALL_PATTERN}, message = "寄件人电话不合法")
    public String senderTelephone;

    /**
     * 包材类型
     */
    @NotNull(message = "包材类型不能为空")
    public String packageMaterialType;

    /**
     * 宁波店铺号
     */
    public String nbShopNumber;

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
        return "MerchantPackageMaterialEntity{" +
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

