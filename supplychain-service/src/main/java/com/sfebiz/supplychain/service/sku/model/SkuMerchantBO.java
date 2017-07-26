package com.sfebiz.supplychain.service.sku.model;

import com.sfebiz.supplychain.service.stockout.biz.model.BaseBO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 货主商品业务实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-25 11:44
 **/
public class SkuMerchantBO extends BaseBO{
    private static final long serialVersionUID = -231407538534495644L;
    /**
     * 货主ID
     */
    private Long merchantId;

    /**
     * SKU_ID（商品ID）
     */
    private Long skuId;

    /**
     * 货主输入的商品条码
     */
    private String merchantBarcode;

    /**
     * 商品面单名称
     */
    private String billName;

    /**
     * 货源地
     */
    private String supplyLand;

    /**
     * 原产地/囯
     */
    private String originLand;

    /**
     * 成本金额（分）
     */
    private Long costAmount;

    /**
     * 计划销售金额（分）
     */
    private Long plannedSaleAmount;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 商品业务实体
     */
    private SkuBO skuBO;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getMerchantBarcode() {
        return merchantBarcode;
    }

    public void setMerchantBarcode(String merchantBarcode) {
        this.merchantBarcode = merchantBarcode;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getSupplyLand() {
        return supplyLand;
    }

    public void setSupplyLand(String supplyLand) {
        this.supplyLand = supplyLand;
    }

    public String getOriginLand() {
        return originLand;
    }

    public void setOriginLand(String originLand) {
        this.originLand = originLand;
    }

    public Long getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(Long costAmount) {
        this.costAmount = costAmount;
    }

    public Long getPlannedSaleAmount() {
        return plannedSaleAmount;
    }

    public void setPlannedSaleAmount(Long plannedSaleAmount) {
        this.plannedSaleAmount = plannedSaleAmount;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public SkuBO getSkuBO() {
        return skuBO;
    }

    public void setSkuBO(SkuBO skuBO) {
        this.skuBO = skuBO;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
