package com.sfebiz.supplychain.exposed.stock.entity;

import java.util.Date;

/**
 * @description: 实物库存实体
 * @author yangh [yangh@ifunq.com]
 * @date 2017/7/14 14:32
 */

public class StockPhysicalEntity  {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 商品ID
     */
    private Long skuId;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 货主ID
     */
    private Long merchantId;
    /**
     * 货主供应商ID
     */
    private Long merchantProviderId;
    /**
     * 可用库存
     */
    private Integer availableCount;
    /**
     * 冻结库存
     */
    private Integer freezeCount;
    /**
     * 坏品库存
     */
    private Integer damagedCount;
    /**
     * 创建日期
     */
    private Date gmtCreate;
    /**
     * 修改日期
     */
    private Date gmtModified;

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

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMerchantProviderId() {
        return merchantProviderId;
    }

    public void setMerchantProviderId(Long merchantProviderId) {
        this.merchantProviderId = merchantProviderId;
    }

    public Integer getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    public Integer getFreezeCount() {
        return freezeCount;
    }

    public void setFreezeCount(Integer freezeCount) {
        this.freezeCount = freezeCount;
    }

    public Integer getDamagedCount() {
        return damagedCount;
    }

    public void setDamagedCount(Integer damagedCount) {
        this.damagedCount = damagedCount;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "StockPhysicalEntity{" +
                "id=" + id +
                ", skuId=" + skuId +
                ", warehouseId=" + warehouseId +
                ", merchantId=" + merchantId +
                ", merchantProviderId=" + merchantProviderId +
                ", availableCount=" + availableCount +
                ", freezeCount=" + freezeCount +
                ", damagedCount=" + damagedCount +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
