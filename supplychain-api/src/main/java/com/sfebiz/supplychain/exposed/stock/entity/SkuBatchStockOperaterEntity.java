package com.sfebiz.supplychain.exposed.stock.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * @author yangh [yangh@ifunq.com]
 * @description: 商品批次库存操作实体
 * @date 2017/7/24 15:36
 */


public class SkuBatchStockOperaterEntity implements Comparable<SkuBatchStockOperaterEntity>, Serializable {

    private static final long serialVersionUID = -1325347075910649085L;
    //批次库存表主键ID
    private Long id;
    //货主ID
    private Long merchantId;
    // 操作数量
    private Integer count;
    // 操作坏品数量
    private Integer wearCount;
    // 商品SKU ID
    private Long skuId;
    // 仓库 ID
    private Long warehouseId;
    // 批次号
    private String batchNo;
    // 供应商
    private Long providerId;
    // 操作后冻结数量
    private Integer freezeCount;
    // 操作后正品数量
    private Integer qualityCount;
    // 生产日期
    private Date productionDate;
    // 过期日期
    private Date expirationDate;
    // 入库日期
    private Date stockinDate;
    // 资金来源 0：自营 1：供应链金融 2：非自营
    public Integer moneySource;
    // 采购单ID
    public Long purchaseOrderId;
    // 入库单ID
    public Long stockinId;

    public SkuBatchStockOperaterEntity() {
    }

    public SkuBatchStockOperaterEntity(Long skuId, Integer count) {
        this.skuId = skuId;
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }


    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getFreezeCount() {
        return freezeCount;
    }

    public void setFreezeCount(Integer freezeCount) {
        this.freezeCount = freezeCount;
    }

    public Integer getQualityCount() {
        return qualityCount;
    }

    public void setQualityCount(Integer qualityCount) {
        this.qualityCount = qualityCount;
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

    public Date getStockinDate() {
        return stockinDate;
    }

    public void setStockinDate(Date stockinDate) {
        this.stockinDate = stockinDate;
    }

    @Override
    public int compareTo(SkuBatchStockOperaterEntity o) {
        // 默认按照skuID升序排列
        if (o.getSkuId().longValue() > this.getSkuId().longValue()) {
            return -1;
        } else if (o.getSkuId().longValue() == this.getSkuId().longValue()) {
            return 0;
        } else {
            return 1;
        }
    }

    public Integer getWearCount() {
        return wearCount;
    }

    public void setWearCount(Integer wearCount) {
        this.wearCount = wearCount;
    }

    public Integer getMoneySource() {
        return moneySource;
    }

    public void setMoneySource(Integer moneySource) {
        this.moneySource = moneySource;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Long getStockinId() {
        return stockinId;
    }

    public void setStockinId(Long stockinId) {
        this.stockinId = stockinId;
    }

    @Override
    public String toString() {
        return "SkuBatchStockOperaterEntity{" +
                ", count=" + count +
                ", wearCount=" + wearCount +
                ", skuId=" + skuId +
                ", warehouseId=" + warehouseId +
                ", id=" + id +
                ", batchNo='" + batchNo + '\'' +
                ", providerId=" + providerId +
                ", freezeCount=" + freezeCount +
                ", qualityCount=" + qualityCount +
                ", productionDate=" + productionDate +
                ", expirationDate=" + expirationDate +
                ", stockinDate=" + stockinDate +
                ", moneySource=" + moneySource +
                ", purchaseOrderId=" + purchaseOrderId +
                ", stockinId=" + stockinId +
                '}';
    }
}
