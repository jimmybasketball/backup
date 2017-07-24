package com.sfebiz.supplychain.service.stockout.biz.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * <p>出库单支付申报明细实体</p>
 *
 * @author matt
 * @Date 2017年7月18日 上午11:49:37
 */
public class StockoutOrderDeclarePriceDetailBO extends BaseBO {

    /** 序号 */
    private static final long serialVersionUID = 7120405610515889068L;

    /** 出库单ID */
    private Long              stockoutOrderId;

    /** 子订单ID */
    private String            bizId;

    /** 商品ID */
    private Long              skuId;

    /** 商品数量 */
    private Integer           quantity;

    /** 申报单价，单位分 */
    private Integer           declarePrice;

    /** 售卖单价，单位分 */
    private Integer           salePrice;

    /** 折扣金额，单位分 */
    private Integer           discountPrice;

    /** 总综合税 */
    private Integer           totalTax;

    /** 消费税 */
    private Integer           consumptionDutyTax;

    /** 增值税 */
    private Integer           addedValueTax;

    /** 关税 */
    private Integer           tariffTax;

    /** 备注 */
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

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getDeclarePrice() {
        return declarePrice;
    }

    public void setDeclarePrice(Integer declarePrice) {
        this.declarePrice = declarePrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Integer totalTax) {
        this.totalTax = totalTax;
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

    public Integer getTariffTax() {
        return tariffTax;
    }

    public void setTariffTax(Integer tariffTax) {
        this.tariffTax = tariffTax;
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
