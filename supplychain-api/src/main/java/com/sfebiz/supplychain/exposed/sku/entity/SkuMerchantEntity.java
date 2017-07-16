package com.sfebiz.supplychain.exposed.sku.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 货主商品实体
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-12 17:56
 **/
public class SkuMerchantEntity implements Serializable  {

    private static final long serialVersionUID = -4708115715718988369L;
    /**
     * ID
     */
    public Long id;

    /**
     * 货主ID
     */
    public Long merchantId;

    /**
     * SKU_ID（商品ID）
     */
    public Long skuId;

    /**
     * 货主输入的商品条码
     */
    public String merchantBarcode;

    /**
     * 商品面单名称
     */
    public String billName;

    /**
     * 货源地
     */
    public String supplyLand;

    /**
     * 原产地/囯
     */
    public String originLand;

    /**
     * 成本金额（分）
     */
    public Long costAmount;

    /**
     * 计划销售金额（分）
     */
    public Long plannedSaleAmount;

    /**
     * 生产厂家
     */
    public String manufacturer;

    /**
     * 创建时间
     */
    public Date gmtCreate;

    /**
     * 创建人
     */
    public String createBy;

    /**
     * 修改时间
     */
    public Date gmtModified;

    /**
     * 修改人
     */
    public String modifiedBy;

    /**
     * 基本商品信息
     */
    public SkuEntity skuEntity;

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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public SkuEntity getSkuEntity() {
        return skuEntity;
    }

    public void setSkuEntity(SkuEntity skuEntity) {
        this.skuEntity = skuEntity;
    }

    @Override
    public String toString() {
        return "SkuMerchantEntity{" +
                "id=" + id +
                ", merchantId='" + merchantId + '\'' +
                ", skuId='" + skuId + '\'' +
                ", merchantBarcode='" + merchantBarcode + '\'' +
                ", billName='" + billName + '\'' +
                ", supplyLand='" + supplyLand + '\'' +
                ", originLand='" + originLand + '\'' +
                ", costAmount='" + costAmount + '\'' +
                ", plannedSaleAmount='" + plannedSaleAmount + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", skuEntity='" + skuEntity + '\'' +
                '}';
    }
}