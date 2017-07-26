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
    // 采买模式
    public String purchaseMode;
    // 操作数量
    private Integer count;
    // 操作坏品数量
    private Integer wearCount;
    // 商品SKU ID
    private Long skuId;
    // 仓库 ID
    private Long warehouseId;
    // 主键ID
    private Long id;
    // 批次号
    private String batchNo;
    // 成本价（港币）
    private Integer price;
    // 成本价（人民币）
    private Integer priceRmb;
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
    // 原币价格
    private Integer originPrice;
    // 币种
    private String currency;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPriceRmb() {
        return priceRmb;
    }

    public void setPriceRmb(Integer priceRmb) {
        this.priceRmb = priceRmb;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
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

    public String getPurchaseMode() {
        return purchaseMode;
    }

    public void setPurchaseMode(String purchaseMode) {
        this.purchaseMode = purchaseMode;
    }

    public Integer getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Integer originPrice) {
        this.originPrice = originPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
                "purchaseMode='" + purchaseMode + '\'' +
                ", count=" + count +
                ", wearCount=" + wearCount +
                ", skuId=" + skuId +
                ", warehouseId=" + warehouseId +
                ", id=" + id +
                ", batchNo='" + batchNo + '\'' +
                ", price=" + price +
                ", priceRmb=" + priceRmb +
                ", providerId=" + providerId +
                ", freezeCount=" + freezeCount +
                ", qualityCount=" + qualityCount +
                ", productionDate=" + productionDate +
                ", expirationDate=" + expirationDate +
                ", stockinDate=" + stockinDate +
                ", originPrice=" + originPrice +
                ", currency='" + currency + '\'' +
                ", moneySource=" + moneySource +
                ", purchaseOrderId=" + purchaseOrderId +
                ", stockinId=" + stockinId +
                '}';
    }
}
