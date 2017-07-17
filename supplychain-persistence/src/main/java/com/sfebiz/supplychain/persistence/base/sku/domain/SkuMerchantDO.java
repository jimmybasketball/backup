package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * 货主商品DO
 *
 * @author tanzx
 * @create 2017-07-12 18:22
 **/
public class SkuMerchantDO extends BaseDO {

    private static final long serialVersionUID = -8897294901343943155L;
    /**
     * ID
     */
    private Long id;

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
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 删除标识
     */
    private int isDelete;

    /**
     * MYSQL更新时间_FOR_BI
     */
    private String updatedTimeForBi;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    @Override
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getGmtModified() {
        return gmtModified;
    }

    @Override
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "SkuMerchantDO{" +
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
                '}';
    }
}
