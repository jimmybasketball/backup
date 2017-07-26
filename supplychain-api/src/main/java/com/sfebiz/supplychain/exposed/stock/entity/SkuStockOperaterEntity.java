package com.sfebiz.supplychain.exposed.stock.entity;

import java.io.Serializable;

/**
 * @description: 商品库存操作实体
 * @author yangh [yangh@ifunq.com]
 * @date 2017/7/24 15:37
 */

public class SkuStockOperaterEntity implements Comparable<SkuStockOperaterEntity>,Serializable {

    private static final long serialVersionUID = 5990880363176607606L;
    //商品SKU ID
    public Long skuId;
    //商品报损正品数量
    public Integer count = 0;
    //商品报损坏品数量
    public Integer wearCount = 0;
    //商品报溢正品数量
    public Integer overFlowCount= 0;
    //商品报溢坏品数量
    public Integer overFlowWearCount = 0;
    //仓库 ID
    public Long warehouseId;
    //出库单 ID
    public Long stockoutOrderId;
    //商品类型
    public Integer skuType;
    // 商品单价 港币
    public Integer price;
    // 商品单价 人民币
    public Integer priceRmb;
    /**
     * 预售申请单ID
     */
    public Long presellId;
    /**
     * 预售库存
     */
    public Integer presellCount = 0;
    /**
     * 预售库存（可售）
     */
    public Integer salePresellCount = 0;
    /**
     * 预售库存(在途)
     */
    public Integer onroadAdvanceCount = 0;
    /**
     * 预售库存(无PO)
     */
    public Integer nopoAdvanceCount = 0;

    public SkuStockOperaterEntity(){}

    public SkuStockOperaterEntity(Long skuId, Long warehouseId, Long stockoutOrderId, Integer count) {
        this.skuId = skuId;
        this.warehouseId = warehouseId;
        this.stockoutOrderId = stockoutOrderId;
        this.count = count;
    }

    public SkuStockOperaterEntity(Long skuId, Integer count) {
        this.skuId = skuId;
        this.count = count;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getWearCount() {
        return wearCount;
    }

    public void setWearCount(Integer wearCount) {
        this.wearCount = wearCount;
    }

    public Integer getOverFlowCount() {
        return overFlowCount;
    }

    public void setOverFlowCount(Integer overFlowCount) {
        this.overFlowCount = overFlowCount;
    }

    public Integer getOverFlowWearCount() {
        return overFlowWearCount;
    }

    public void setOverFlowWearCount(Integer overFlowWearCount) {
        this.overFlowWearCount = overFlowWearCount;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getStockoutOrderId() {
        return stockoutOrderId;
    }

    public void setStockoutOrderId(Long stockoutOrderId) {
        this.stockoutOrderId = stockoutOrderId;
    }

    public Integer getSkuType() {
        return skuType;
    }

    public void setSkuType(Integer skuType) {
        this.skuType = skuType;
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

    public Integer getPresellCount() {
        return presellCount;
    }

    public void setPresellCount(Integer presellCount) {
        this.presellCount = presellCount;
    }

    public Integer getOnroadAdvanceCount() {
        return onroadAdvanceCount;
    }

    public void setOnroadAdvanceCount(Integer onroadAdvanceCount) {
        this.onroadAdvanceCount = onroadAdvanceCount;
    }

    public Integer getNopoAdvanceCount() {
        return nopoAdvanceCount;
    }

    public void setNopoAdvanceCount(Integer nopoAdvanceCount) {
        this.nopoAdvanceCount = nopoAdvanceCount;
    }

    public Integer getSalePresellCount() {
        return salePresellCount;
    }

    public void setSalePresellCount(Integer salePresellCount) {
        this.salePresellCount = salePresellCount;
    }

    public Long getPresellId() {
        return presellId;
    }

    public void setPresellId(Long presellId) {
        this.presellId = presellId;
    }

    @Override
    public int compareTo(SkuStockOperaterEntity o) {
        // 默认按照skuId升序排列
        if (o.getSkuId().longValue() > this.getSkuId().longValue()) {
            return -1;
        } else if (o.getSkuId().longValue() == this.getSkuId().longValue()) {
            if (o.getStockoutOrderId().longValue() > this.getStockoutOrderId().longValue()) {
                return -1;
            } else if (o.getStockoutOrderId().longValue() < this.getStockoutOrderId().longValue()){
                return 1;
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "SkuStockOperaterEntity{" +
                "skuId=" + skuId +
                ", count=" + count +
                ", wearCount=" + wearCount +
                ", overFlowCount=" + overFlowCount +
                ", overFlowWearCount=" + overFlowWearCount +
                ", warehouseId=" + warehouseId +
                ", stockoutOrderId=" + stockoutOrderId +
                ", skuType=" + skuType +
                ", price=" + price +
                ", priceRmb=" + priceRmb +
                ", presellId=" + presellId +
                ", presellCount=" + presellCount +
                ", salePresellCount=" + salePresellCount +
                ", onroadAdvanceCount=" + onroadAdvanceCount +
                ", nopoAdvanceCount=" + nopoAdvanceCount +
                ", batchNo=" + batchNo +
                '}';
    }


    // 商品批次
    public String batchNo;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
