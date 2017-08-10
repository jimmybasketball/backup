package com.sfebiz.supplychain.persistence.base.stockout.domain;

import com.sfebiz.common.dao.domain.BaseDO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 
 * <p>出库单实体</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午2:26:57
 */
public class StockoutOrderDO extends BaseDO {

    /** 序号 */
    private static final long serialVersionUID = 1775963648231189487L;

    /** 业务订单ID */
    private String            bizId;

    /** 货主、商户订单号 */
    private String            merchantOrderNo;

    /** 出库单流程状态 */
    private String            orderState;

    /** 出库单类型 */
    private Integer           orderType;

    /** 订单来源，预留给商户的字段，便于商户跟踪订单 */
    private String            orderSource;

    /** 服务类型 */
    private Integer           serviceType;

    /** 货主ID */
    private Long              merchantId;

    /** 货主供应商ID */
    private Long              merchantProviderId;

    /** 仓库ID */
    private Long              warehouseId;

    /** 线路ID */
    private Long              lineId;

    /** 大头笔 */
    private String            destcode;

    /** 波次号，供应商后台批量导出面单时提供区分 */
    private String            waveNo;

    /** Y-大包，N-小包 */
    // TODO
    private String            needCheck;

    /** 提货方式(TAKE_THEIR:自提, EXPRESS:快递) */
    private String            deliveryMode;

    /** 国际承运商编码 */
    private String            intlCarrierCode;

    /** 国际运单号 */
    private String            intlMailNo;

    /** 国内承运商编码 */
    private String            intrCarrierCode;

    /** 国内运单号 */
    private String            intrMailNo;

    /** 包裹体积，长，单位cm */
    private Integer           pkgLength;

    /** 包裹体积，宽，单位cm */
    private Integer           pkgWidth;

    /** 包裹体积，高，单位cm */
    private Integer           pkgHeight;

    /** 计算重量，单位 g */
    private Integer           calWeight;

    /** 实际重量，单位 g */
    private Integer           actualWeight;

    /** 商户支付方式（默认：merchant_pay） */
    private String            merchantPayType;

    /** 商户支付流水 */
    private String            merchantPayNo;

    /** 支付申报的支付类型 */
    private String            declarePayType;

    /** 支付申报支付流水 */
    private String            declarePayNo;

    /** 支付申报人姓名 */
    private String            declarePayerName;

    /** 支付人申报证件号码 */
    private String            declarePayerCertNo;

    /** 支付人申报类型 */
    private String            declarePayerCertType;

    /** 用户商品总金额 */
    private Integer           userGoodsPrice;

    /** 用户折扣金额 */
    private Integer           userDiscountPrice;

    /** 用户运费金额，单位分 */
    private Integer           userFreightFee;

    /** 结算运费，单位分 */
    private Integer           settleFreightFee;

    /** 包裹根据线路计算所得的运费，单位分 */
    private Integer           calFreightFee;

    /** 预计发货时间 */
    private Date              estimatedShippingTime;

    /** 备注 */
    private String            remarks;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMerchantProviderId() {
        return merchantProviderId;
    }

    public void setMerchantProviderId(Long merchantProviderId) {
        this.merchantProviderId = merchantProviderId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getDestcode() {
        return destcode;
    }

    public void setDestcode(String destcode) {
        this.destcode = destcode;
    }

    public String getWaveNo() {
        return waveNo;
    }

    public void setWaveNo(String waveNo) {
        this.waveNo = waveNo;
    }

    public String getNeedCheck() {
        return needCheck;
    }

    public void setNeedCheck(String needCheck) {
        this.needCheck = needCheck;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getIntlCarrierCode() {
        return intlCarrierCode;
    }

    public void setIntlCarrierCode(String intlCarrierCode) {
        this.intlCarrierCode = intlCarrierCode;
    }

    public String getIntlMailNo() {
        return intlMailNo;
    }

    public void setIntlMailNo(String intlMailNo) {
        this.intlMailNo = intlMailNo;
    }

    public String getIntrCarrierCode() {
        return intrCarrierCode;
    }

    public void setIntrCarrierCode(String intrCarrierCode) {
        this.intrCarrierCode = intrCarrierCode;
    }

    public String getIntrMailNo() {
        return intrMailNo;
    }

    public void setIntrMailNo(String intrMailNo) {
        this.intrMailNo = intrMailNo;
    }

    public Integer getPkgLength() {
        return pkgLength;
    }

    public void setPkgLength(Integer pkgLength) {
        this.pkgLength = pkgLength;
    }

    public Integer getPkgWidth() {
        return pkgWidth;
    }

    public void setPkgWidth(Integer pkgWidth) {
        this.pkgWidth = pkgWidth;
    }

    public Integer getPkgHeight() {
        return pkgHeight;
    }

    public void setPkgHeight(Integer pkgHeight) {
        this.pkgHeight = pkgHeight;
    }

    public Integer getCalWeight() {
        return calWeight;
    }

    public void setCalWeight(Integer calWeight) {
        this.calWeight = calWeight;
    }

    public Integer getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(Integer actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getMerchantPayType() {
        return merchantPayType;
    }

    public void setMerchantPayType(String merchantPayType) {
        this.merchantPayType = merchantPayType;
    }

    public String getMerchantPayNo() {
        return merchantPayNo;
    }

    public void setMerchantPayNo(String merchantPayNo) {
        this.merchantPayNo = merchantPayNo;
    }

    public String getDeclarePayType() {
        return declarePayType;
    }

    public void setDeclarePayType(String declarePayType) {
        this.declarePayType = declarePayType;
    }

    public String getDeclarePayNo() {
        return declarePayNo;
    }

    public void setDeclarePayNo(String declarePayNo) {
        this.declarePayNo = declarePayNo;
    }

    public String getDeclarePayerName() {
        return declarePayerName;
    }

    public void setDeclarePayerName(String declarePayerName) {
        this.declarePayerName = declarePayerName;
    }

    public String getDeclarePayerCertNo() {
        return declarePayerCertNo;
    }

    public void setDeclarePayerCertNo(String declarePayerCertNo) {
        this.declarePayerCertNo = declarePayerCertNo;
    }

    public String getDeclarePayerCertType() {
        return declarePayerCertType;
    }

    public void setDeclarePayerCertType(String declarePayerCertType) {
        this.declarePayerCertType = declarePayerCertType;
    }

    public Integer getUserGoodsPrice() {
        return userGoodsPrice;
    }

    public void setUserGoodsPrice(Integer userGoodsPrice) {
        this.userGoodsPrice = userGoodsPrice;
    }

    public Integer getUserDiscountPrice() {
        return userDiscountPrice;
    }

    public void setUserDiscountPrice(Integer userDiscountPrice) {
        this.userDiscountPrice = userDiscountPrice;
    }

    public Integer getUserFreightFee() {
        return userFreightFee;
    }

    public void setUserFreightFee(Integer userFreightFee) {
        this.userFreightFee = userFreightFee;
    }

    public Integer getSettleFreightFee() {
        return settleFreightFee;
    }

    public void setSettleFreightFee(Integer settleFreightFee) {
        this.settleFreightFee = settleFreightFee;
    }

    public Integer getCalFreightFee() {
        return calFreightFee;
    }

    public void setCalFreightFee(Integer calFreightFee) {
        this.calFreightFee = calFreightFee;
    }

    public Date getEstimatedShippingTime() {
        return estimatedShippingTime;
    }

    public void setEstimatedShippingTime(Date estimatedShippingTime) {
        this.estimatedShippingTime = estimatedShippingTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
