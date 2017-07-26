package com.sfebiz.supplychain.exposed.stockinorder.entity;

import java.io.Serializable;
import java.util.Date;

public class StockinOrderDetailEntity implements Serializable {

    private static final Long serialVersionUID = -3829533729382281753L;
    /**
     * ID
     */
    public Long id;
    /**
     * 商品ID,必填
     */
    public Long skuId;
    /**
     * 商品名称
     */
    public String skuName;
    /**
     * 商品批次
     */
    public String skuBatch;
    /**
     * 商品条码
     */
    public String skuBarcode;
    /**
     * 商品外文名称
     */
    public String skuForeignName;
    /**
     * 商品规格
     */
    public String skuSpecification;
    /**
     * 商品数量
     */
    public Integer count;
    /**
     * 实际入库数量,收货完成后值必填
     */
    public Integer realCount;
    /**
     * 实际入库数量(坏品)
     */
    public Integer badRealCount;
    /**
     * 差异数量
     */
    public Integer realDiffCount = 0;
    /**
     * 差异原因
     */
    public String diffDesc = "";
    /**
     * 生产日期
     */
    public Date productionDate;
    /**
     * 过期日期
     */
    public Date expirationDate;
    /**
     * 入库日期
     */
    public Date stockinDate;

    @Override
    public String toString() {
        return "StockinOrderDetailEntity{" +
                "id=" + id +
                ", skuId=" + skuId +
                ", skuName='" + skuName + '\'' +
                ", skuBatch='" + skuBatch + '\'' +
                ", skuBarcode='" + skuBarcode + '\'' +
                ", skuForeignName='" + skuForeignName + '\'' +
                ", skuSpecification='" + skuSpecification + '\'' +
                ", count=" + count +
                ", realCount=" + realCount +
                ", badRealCount=" + badRealCount +
                ", realDiffCount=" + realDiffCount +
                ", diffDesc='" + diffDesc + '\'' +
                ", productionDate=" + productionDate +
                ", expirationDate=" + expirationDate +
                ", stockinDate=" + stockinDate +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuBatch() {
        return skuBatch;
    }

    public void setSkuBatch(String skuBatch) {
        this.skuBatch = skuBatch;
    }

    public String getSkuBarcode() {
        return skuBarcode;
    }

    public void setSkuBarcode(String skuBarcode) {
        this.skuBarcode = skuBarcode;
    }

    public String getSkuForeignName() {
        return skuForeignName;
    }

    public void setSkuForeignName(String skuForeignName) {
        this.skuForeignName = skuForeignName;
    }

    public String getSkuSpecification() {
        return skuSpecification;
    }

    public void setSkuSpecification(String skuSpecification) {
        this.skuSpecification = skuSpecification;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getRealCount() {
        return realCount;
    }

    public void setRealCount(Integer realCount) {
        this.realCount = realCount;
    }

    public Integer getBadRealCount() {
        return badRealCount;
    }

    public void setBadRealCount(Integer badRealCount) {
        this.badRealCount = badRealCount;
    }

    public Integer getRealDiffCount() {
        return realDiffCount;
    }

    public void setRealDiffCount(Integer realDiffCount) {
        this.realDiffCount = realDiffCount;
    }

    public String getDiffDesc() {
        return diffDesc;
    }

    public void setDiffDesc(String diffDesc) {
        this.diffDesc = diffDesc;
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
}
