package com.sfebiz.supplychain.persistence.base.stock.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 每日批次库存表
 * @date 2017-07-19 11:24
 **/
public class StockBatchDailyDO extends BaseDO {

    private static final Long serialVersionUID = 4016017259912611693L;

    private Long id;
    private String recordDate;
    private Long skuId;
    private String batchNo;
    private Long warehouseId;
    private Long merchantId;
    private Long merchantProviderId;
    private Integer purchaseStockinAvailableCount;
    private Integer transferStockinAvailableCount;
    private Integer transferStockinDamagedCount;
    private Integer saleReturnStockinAvailableCount;
    private Integer saleReturnStockinDamagedCount;
    private Integer customReturnStockinAvailableCount;
    private Integer customReturnStockinDamagedCount;
    private Integer reportLossesAvalibleCount;
    private Integer reportLossesDamagedCount;
    private Integer reportGainsAvalibleCount;
    private Integer reportGainsDamagedCount;
    private Integer saleStockoutAvailableCount;
    private Integer saleCancelStockoutAvailableCount;
    private Integer purchaseStockoutAvailableCount;
    private Integer purchaseStockoutDamagedCount;
    private Integer adjustAvailableCount;
    private Integer adjustDamagedCount;
    private Integer startAvailableCount;
    private Integer endAvailableCount;
    private Integer startDamagedCount;
    private Integer endDamagedCount;
    private Date gmtCreate;
    private Date gmtModified;
    private Integer isDelete;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public Integer getPurchaseStockinAvailableCount() {
        return purchaseStockinAvailableCount;
    }

    public void setPurchaseStockinAvailableCount(Integer purchaseStockinAvailableCount) {
        this.purchaseStockinAvailableCount = purchaseStockinAvailableCount;
    }

    public Integer getTransferStockinAvailableCount() {
        return transferStockinAvailableCount;
    }

    public void setTransferStockinAvailableCount(Integer transferStockinAvailableCount) {
        this.transferStockinAvailableCount = transferStockinAvailableCount;
    }

    public Integer getTransferStockinDamagedCount() {
        return transferStockinDamagedCount;
    }

    public void setTransferStockinDamagedCount(Integer transferStockinDamagedCount) {
        this.transferStockinDamagedCount = transferStockinDamagedCount;
    }

    public Integer getSaleReturnStockinAvailableCount() {
        return saleReturnStockinAvailableCount;
    }

    public void setSaleReturnStockinAvailableCount(Integer saleReturnStockinAvailableCount) {
        this.saleReturnStockinAvailableCount = saleReturnStockinAvailableCount;
    }

    public Integer getSaleReturnStockinDamagedCount() {
        return saleReturnStockinDamagedCount;
    }

    public void setSaleReturnStockinDamagedCount(Integer saleReturnStockinDamagedCount) {
        this.saleReturnStockinDamagedCount = saleReturnStockinDamagedCount;
    }

    public Integer getCustomReturnStockinAvailableCount() {
        return customReturnStockinAvailableCount;
    }

    public void setCustomReturnStockinAvailableCount(Integer customReturnStockinAvailableCount) {
        this.customReturnStockinAvailableCount = customReturnStockinAvailableCount;
    }

    public Integer getCustomReturnStockinDamagedCount() {
        return customReturnStockinDamagedCount;
    }

    public void setCustomReturnStockinDamagedCount(Integer customReturnStockinDamagedCount) {
        this.customReturnStockinDamagedCount = customReturnStockinDamagedCount;
    }

    public Integer getReportLossesAvalibleCount() {
        return reportLossesAvalibleCount;
    }

    public void setReportLossesAvalibleCount(Integer reportLossesAvalibleCount) {
        this.reportLossesAvalibleCount = reportLossesAvalibleCount;
    }

    public Integer getReportLossesDamagedCount() {
        return reportLossesDamagedCount;
    }

    public void setReportLossesDamagedCount(Integer reportLossesDamagedCount) {
        this.reportLossesDamagedCount = reportLossesDamagedCount;
    }

    public Integer getReportGainsAvalibleCount() {
        return reportGainsAvalibleCount;
    }

    public void setReportGainsAvalibleCount(Integer reportGainsAvalibleCount) {
        this.reportGainsAvalibleCount = reportGainsAvalibleCount;
    }

    public Integer getReportGainsDamagedCount() {
        return reportGainsDamagedCount;
    }

    public void setReportGainsDamagedCount(Integer reportGainsDamagedCount) {
        this.reportGainsDamagedCount = reportGainsDamagedCount;
    }

    public Integer getSaleStockoutAvailableCount() {
        return saleStockoutAvailableCount;
    }

    public void setSaleStockoutAvailableCount(Integer saleStockoutAvailableCount) {
        this.saleStockoutAvailableCount = saleStockoutAvailableCount;
    }

    public Integer getSaleCancelStockoutAvailableCount() {
        return saleCancelStockoutAvailableCount;
    }

    public void setSaleCancelStockoutAvailableCount(Integer saleCancelStockoutAvailableCount) {
        this.saleCancelStockoutAvailableCount = saleCancelStockoutAvailableCount;
    }

    public Integer getPurchaseStockoutAvailableCount() {
        return purchaseStockoutAvailableCount;
    }

    public void setPurchaseStockoutAvailableCount(Integer purchaseStockoutAvailableCount) {
        this.purchaseStockoutAvailableCount = purchaseStockoutAvailableCount;
    }

    public Integer getPurchaseStockoutDamagedCount() {
        return purchaseStockoutDamagedCount;
    }

    public void setPurchaseStockoutDamagedCount(Integer purchaseStockoutDamagedCount) {
        this.purchaseStockoutDamagedCount = purchaseStockoutDamagedCount;
    }

    public Integer getAdjustAvailableCount() {
        return adjustAvailableCount;
    }

    public void setAdjustAvailableCount(Integer adjustAvailableCount) {
        this.adjustAvailableCount = adjustAvailableCount;
    }

    public Integer getAdjustDamagedCount() {
        return adjustDamagedCount;
    }

    public void setAdjustDamagedCount(Integer adjustDamagedCount) {
        this.adjustDamagedCount = adjustDamagedCount;
    }

    public Integer getStartAvailableCount() {
        return startAvailableCount;
    }

    public void setStartAvailableCount(Integer startAvailableCount) {
        this.startAvailableCount = startAvailableCount;
    }

    public Integer getEndAvailableCount() {
        return endAvailableCount;
    }

    public void setEndAvailableCount(Integer endAvailableCount) {
        this.endAvailableCount = endAvailableCount;
    }

    public Integer getStartDamagedCount() {
        return startDamagedCount;
    }

    public void setStartDamagedCount(Integer startDamagedCount) {
        this.startDamagedCount = startDamagedCount;
    }

    public Integer getEndDamagedCount() {
        return endDamagedCount;
    }

    public void setEndDamagedCount(Integer endDamagedCount) {
        this.endDamagedCount = endDamagedCount;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "StockBatchDailyDO{" +
                "id=" + id +
                ", recordDate='" + recordDate + '\'' +
                ", skuId=" + skuId +
                ", batchNo='" + batchNo + '\'' +
                ", warehouseId=" + warehouseId +
                ", merchantId=" + merchantId +
                ", merchantProviderId=" + merchantProviderId +
                ", purchaseStockinAvailableCount=" + purchaseStockinAvailableCount +
                ", transferStockinAvailableCount=" + transferStockinAvailableCount +
                ", transferStockinDamagedCount=" + transferStockinDamagedCount +
                ", saleReturnStockinAvailableCount=" + saleReturnStockinAvailableCount +
                ", saleReturnStockinDamagedCount=" + saleReturnStockinDamagedCount +
                ", customReturnStockinAvailableCount=" + customReturnStockinAvailableCount +
                ", customReturnStockinDamagedCount=" + customReturnStockinDamagedCount +
                ", reportLossesAvalibleCount=" + reportLossesAvalibleCount +
                ", reportLossesDamagedCount=" + reportLossesDamagedCount +
                ", reportGainsAvalibleCount=" + reportGainsAvalibleCount +
                ", reportGainsDamagedCount=" + reportGainsDamagedCount +
                ", saleStockoutAvailableCount=" + saleStockoutAvailableCount +
                ", saleCancelStockoutAvailableCount=" + saleCancelStockoutAvailableCount +
                ", purchaseStockoutAvailableCount=" + purchaseStockoutAvailableCount +
                ", purchaseStockoutDamagedCount=" + purchaseStockoutDamagedCount +
                ", adjustAvailableCount=" + adjustAvailableCount +
                ", adjustDamagedCount=" + adjustDamagedCount +
                ", startAvailableCount=" + startAvailableCount +
                ", endAvailableCount=" + endAvailableCount +
                ", startDamagedCount=" + startDamagedCount +
                ", endDamagedCount=" + endDamagedCount +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", isDelete=" + isDelete +
                '}';
    }
}
