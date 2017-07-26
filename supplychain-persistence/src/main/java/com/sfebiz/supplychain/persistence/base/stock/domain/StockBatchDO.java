package com.sfebiz.supplychain.persistence.base.stock.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 批次库存持久化对象s
 * @date 2017-07-13 16:33
 **/
public class StockBatchDO extends BaseDO {
    private static final long serialVersionUID = -2523973264957366137L;
    /**
     * 主键ID
     */
    private Long Id;
    /**
     * 商品ID
     */
    private Long skuId;
    /**
     * 货主ID
     */
    private Long merchantId;
    /**
     * 实物库存ID
     */
    private Long stockPhysicalId;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 货主供应商ID
     */
    private Long merchantProviderId;
    /**
     * 货主批次号
     */
    private String merchantBatchNo;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 正品库存
     */
    private Integer availableCount;
    /**
     * 坏品库存
     */
    private Integer damagedCount;
    /**
     * 冻结库存
     */
    private Integer freezeCount;
    /**
     * 入库单ID
     */
    private Long stockinOrderId;
    /**
     * 入库日期
     */
    private Date stockinDate;
    /**
     * 生产日期
     */
    private Date productionDate;
    /**
     * 过去日期
     */
    private Date expirationDate;
    /**
     * 批次状态 ENABLE 启用 DISABLE禁用
     */
    private String state;
    /**
     * 是否删除 0否, 1是
     */
    private Integer isDelete;
    /**
     * 创建日期
     */
    private Date gmtCreate;
    /**
     * 修改日期
     */
    private Date gmtModified;


    @Override
    public Long getId() {
        return Id;
    }

    @Override
    public void setId(Long id) {
        Id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getStockPhysicalId() {
        return stockPhysicalId;
    }

    public void setStockPhysicalId(Long stockPhysicalId) {
        this.stockPhysicalId = stockPhysicalId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getMerchantProviderId() {
        return merchantProviderId;
    }

    public void setMerchantProviderId(Long merchantProviderId) {
        this.merchantProviderId = merchantProviderId;
    }

    public String getMerchantBatchNo() {
        return merchantBatchNo;
    }

    public void setMerchantBatchNo(String merchantBatchNo) {
        this.merchantBatchNo = merchantBatchNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    public Integer getDamagedCount() {
        return damagedCount;
    }

    public void setDamagedCount(Integer damagedCount) {
        this.damagedCount = damagedCount;
    }

    public Integer getFreezeCount() {
        return freezeCount;
    }

    public void setFreezeCount(Integer freezeCount) {
        this.freezeCount = freezeCount;
    }

    public Long getStockinOrderId() {
        return stockinOrderId;
    }

    public void setStockinOrderId(Long stockinOrderId) {
        this.stockinOrderId = stockinOrderId;
    }

    public Date getStockinDate() {
        return stockinDate;
    }

    public void setStockinDate(Date stockinDate) {
        this.stockinDate = stockinDate;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    @Override
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public Date getGmtModified() {
        return gmtModified;
    }

    @Override
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "StockBatchDO{" +
                "Id=" + Id +
                ", skuId=" + skuId +
                ", merchantId=" + merchantId +
                ", stockPhysicalId=" + stockPhysicalId +
                ", warehouseId=" + warehouseId +
                ", merchantProviderId=" + merchantProviderId +
                ", merchantBatchNo=" + merchantBatchNo +
                ", batchNo='" + batchNo + '\'' +
                ", availableCount=" + availableCount +
                ", damagedCount=" + damagedCount +
                ", freezeCount=" + freezeCount +
                ", stockinOrderId=" + stockinOrderId +
                ", stockinDate=" + stockinDate +
                ", productionDate=" + productionDate +
                ", expirationDate=" + expirationDate +
                ", state='" + state + '\'' +
                ", isDelete=" + isDelete +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
