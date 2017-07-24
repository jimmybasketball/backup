package com.sfebiz.supplychain.exposed.stockinorder.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangyajing on 7/20/17.
 * 入库单理货报告实体
 */

public class StockinOrderDetailCargoResultEntity implements Serializable {
    private static final long serialVersionUID = -2999759704087246060L;
    /**
     * stockin_order表主键Id
     */
    private Long stockinOrderId;
    /**
     * 商品ID
     */
    private Long skuId;
    /**
     * 商品条码
     */
    private String skuBarcode;
    /**
     * 差异描述
     */
    private String diffDesc;
    /**
     * 差异数量
     */
    private Integer realDiffCount;
    /**
     * 实际入库数量(坏品)
     */
    private Integer realCount;
    /**
     * 实际入库数量(坏品)
     */
    public Integer badRealCount;
    /**
     * 生产日期
     */
    public Date productionDate;
    /**
     * 过期日期
     */
    public Date expirationDate;

    public Long getStockinOrderId() {
        return stockinOrderId;
    }

    public void setStockinOrderId(Long stockinOrderId) {
        this.stockinOrderId = stockinOrderId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuBarcode() {
        return skuBarcode;
    }

    public void setSkuBarcode(String skuBarcode) {
        this.skuBarcode = skuBarcode;
    }

    public String getDiffDesc() {
        return diffDesc;
    }

    public void setDiffDesc(String diffDesc) {
        this.diffDesc = diffDesc;
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

    public Integer getRealDiffCount() {
        return realDiffCount;
    }

    public void setRealDiffCount(Integer realDiffCount) {
        this.realDiffCount = realDiffCount;
    }

    @Override
    public String toString() {
        return "StockinOrderDetailCargoResultEntity{" +
                "stockinOrderId=" + stockinOrderId +
                ", skuId=" + skuId +
                ", skuBarcode='" + skuBarcode + '\'' +
                ", diffDesc='" + diffDesc + '\'' +
                ", realDiffCount=" + realDiffCount +
                ", realCount=" + realCount +
                ", badRealCount=" + badRealCount +
                ", productionDate=" + productionDate +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
