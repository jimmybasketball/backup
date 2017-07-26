package com.sfebiz.supplychain.persistence.base.stockout.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 
 * <p>出库单接口调用/服务调用日志信息实体</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:53:33
 */
public class StockoutOrderInvokeLogDO extends BaseDO {

    /** 序号 */
    private static final long serialVersionUID = 8450060621256013366L;

    /** 商户订单号 */
    private String            merchantOrderNo;

    /** 业务订单号 */
    private String            bizId;

    /** 货主ID */
    private Long              merchantId;

    /** 动作类型 */
    private String            actionType;

    /** 动作描述 */
    private String            actionDesc;

    /** 请求信息 */
    private String            reqInfo;

    /** 错误码 */
    private String            errCode;

    /** 错误描述 */
    private String            errDesc;

    /** 调用时间 */
    private Date              gmtInvoke;

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public String getReqInfo() {
        return reqInfo;
    }

    public void setReqInfo(String reqInfo) {
        this.reqInfo = reqInfo;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    public Date getGmtInvoke() {
        return gmtInvoke;
    }

    public void setGmtInvoke(Date gmtInvoke) {
        this.gmtInvoke = gmtInvoke;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
