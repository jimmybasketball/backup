package com.sfebiz.supplychain.service.merchant.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sfebiz.supplychain.service.stockout.biz.model.BaseBO;

/**
 * 货主包材业务实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-25 16:45
 **/
public class MerchantPackageMaterialBO extends BaseBO {
    private static final long serialVersionUID = -5817321257827352619L;
    /**
     * 货主ID
     */
    private Long              merchantId;

    /**
     * 订单来源
     */
    private String            orderSource;

    /**
     * 寄件人名
     */
    private String            senderName;

    /**
     * 寄件人电话
     */
    private String            senderTelephone;

    /**
     * 包材类型
     */
    private String            packageMaterialType;

    /**
     * 宁波店铺号
     */
    private String            nbShopNumber;

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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
