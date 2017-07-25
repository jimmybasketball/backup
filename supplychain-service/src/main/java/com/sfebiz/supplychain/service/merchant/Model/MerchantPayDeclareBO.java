package com.sfebiz.supplychain.service.merchant.Model;

import com.sfebiz.common.dao.domain.BaseDO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 货主申报方式业务实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-25 17:56
 **/
public class MerchantPayDeclareBO extends BaseDO{

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


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
