package com.sfebiz.supplychain.persistence.base.stockout.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 
 * <p>出库单申报实体</p>
 *
 * @author matt
 * @Date 2017年7月18日 上午11:11:20
 */
public class StockoutOrderDeclarePriceDO extends BaseDO {

    /** 序号 */
    private static final long serialVersionUID = -6658382463635245147L;

    /** 出库单ID */
    private Long              stockoutOrderId;

    /** 子订单ID */
    private String            bizId;

    /** 用户实际支付总价，单位分 */
    private Integer           orderActualPrice;

    /** 订单总金额，单位分 */
    private Integer           orderTotalPrice;

    /** 货款总金额，单位分 */
    private Integer           goodsTotalPrice;

    /** 订单申报总价，单位分  */
    private Integer           declareTotalPrice;

    /** 折扣总金额，单位分 */
    private Integer           discountTotalPrice;

    /** 运费，单位分 */
    private Integer           freightFee;

    /** 税费(中税费)，单位分 */
    // TODO
    private Integer           taxFee;

    /** 是否支付了税费，单位分 */
    private Integer           isPayTax;

    /** 保费，单位分 */
    private Integer           insuranceFee;

    /** 消费税，单位分 */
    private Integer           consumptionDutyTax;

    /** 增值税，单位分 */
    private Integer           addedValueTax;

    /** 关税，单位分 */
    private Integer           tariffFee;

    /** 备注信息 */
    private String            memo;

    public Long getStockoutOrderId() {
        return stockoutOrderId;
    }

    public void setStockoutOrderId(Long stockoutOrderId) {
        this.stockoutOrderId = stockoutOrderId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Integer getOrderActualPrice() {
        return orderActualPrice;
    }

    public void setOrderActualPrice(Integer orderActualPrice) {
        this.orderActualPrice = orderActualPrice;
    }

    public Integer getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(Integer orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public Integer getGoodsTotalPrice() {
        return goodsTotalPrice;
    }

    public void setGoodsTotalPrice(Integer goodsTotalPrice) {
        this.goodsTotalPrice = goodsTotalPrice;
    }

    public Integer getDeclareTotalPrice() {
        return declareTotalPrice;
    }

    public void setDeclareTotalPrice(Integer declareTotalPrice) {
        this.declareTotalPrice = declareTotalPrice;
    }

    public Integer getDiscountTotalPrice() {
        return discountTotalPrice;
    }

    public void setDiscountTotalPrice(Integer discountTotalPrice) {
        this.discountTotalPrice = discountTotalPrice;
    }

    public Integer getFreightFee() {
        return freightFee;
    }

    public void setFreightFee(Integer freightFee) {
        this.freightFee = freightFee;
    }

    public Integer getTaxFee() {
        return taxFee;
    }

    public void setTaxFee(Integer taxFee) {
        this.taxFee = taxFee;
    }

    public Integer getIsPayTax() {
        return isPayTax;
    }

    public void setIsPayTax(Integer isPayTax) {
        this.isPayTax = isPayTax;
    }

    public Integer getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(Integer insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public Integer getConsumptionDutyTax() {
        return consumptionDutyTax;
    }

    public void setConsumptionDutyTax(Integer consumptionDutyTax) {
        this.consumptionDutyTax = consumptionDutyTax;
    }

    public Integer getAddedValueTax() {
        return addedValueTax;
    }

    public void setAddedValueTax(Integer addedValueTax) {
        this.addedValueTax = addedValueTax;
    }

    public Integer getTariffFee() {
        return tariffFee;
    }

    public void setTariffFee(Integer tariffFee) {
        this.tariffFee = tariffFee;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
