package com.sfebiz.supplychain.persistence.base.stock.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 冻结库存持久化对象
 * @date 2017-07-13 16:41
 **/
public class StockFreezeDO extends BaseDO {
    private static final long serialVersionUID = -2917213317132888181L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 商品ID
     */
    private Long skuId;
    /**
     * 货主ID
     */
    private Long merchantId;
    /**
     * 批次库存ID
     */
    private Long stockBatchId;
    /**
     * 仓库ID
     */
    private Long warehouseId;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 冻结库存
     */
    private Integer freezeCount;
    /**
     * 实际消费/释放库存
     */
    private Integer realCount;
    /**
     * 库存相关单据ID
     */
    private Long stockOrderId;
    /**
     * 单据类型
     */
    private Integer orderType;
    /**
     * 冻结状态
     */
    private String freezeState;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 修改时间
     */
    private Date gmtModified;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public Long getStockBatchId() {
        return stockBatchId;
    }

    public void setStockBatchId(Long stockBatchId) {
        this.stockBatchId = stockBatchId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getFreezeCount() {
        return freezeCount;
    }

    public void setFreezeCount(Integer freezeCount) {
        this.freezeCount = freezeCount;
    }

    public Integer getRealCount() {
        return realCount;
    }

    public void setRealCount(Integer realCount) {
        this.realCount = realCount;
    }

    public Long getStockOrderId() {
        return stockOrderId;
    }

    public void setStockOrderId(Long stockOrderId) {
        this.stockOrderId = stockOrderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getFreezeState() {
        return freezeState;
    }

    public void setFreezeState(String freezeState) {
        this.freezeState = freezeState;
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
        return "StockFreezeDO{" +
                "id=" + id +
                ", skuId=" + skuId +
                ", merchantId=" + merchantId +
                ", stockBatchId=" + stockBatchId +
                ", warehouseId=" + warehouseId +
                ", batchNo='" + batchNo + '\'' +
                ", freezeCount=" + freezeCount +
                ", realCount=" + realCount +
                ", stockOrderId=" + stockOrderId +
                ", orderType=" + orderType +
                ", freezeState='" + freezeState + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
