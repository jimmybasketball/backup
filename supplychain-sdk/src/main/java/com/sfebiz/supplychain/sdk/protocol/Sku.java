package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * sku详情类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sku", propOrder = {"skuId", "skuCode", "skuName", "skuNameEn", "origin", "specification", "skuUnitPrice",
        "skuPriceCurrency", "skuQty", "skuRemark",
        "skuNetWeight", "skuCheckQty", "skuCheckTime", "skuBoundTime", "batchNo", "type", "warehouseRemainStock",
        "isEnough", "batchMakePlan", "batchMix", "qualityTestRate"
        , "createTime", "stockinPeriodPlanValue", "stockinPeriodPlan", "stockoutPeriodPlan", "stockoutPeriodPlanValue",
        "customerCode"
        , "grossWeight", "stockoutPlan", "length", "height", "width", "stockAlarmLimit", "maxStockAlarmLimit", "status",
        "guarantyPeriod", "skuInInferiorQty",
        "operator", "totalQyCount", "totalFreezeCount", "totalWearCount", "batchs", "galQty", "isQuality", "isOverFlow", "skuBillName", "lastSendBatchNo"})
public class Sku implements Serializable {

    private static final long serialVersionUID = 3220597097403130186L;

    /**
     * skuId
     */
    @XmlElement(nillable = false, required = true)
    public String skuId;

    /**
     * sku编号
     */
    @XmlElement(nillable = false, required = true)
    public String skuCode;

    /**
     * sku名称
     */
    @XmlElement(nillable = false, required = true)
    public String skuName;

    /**
     * sku英文名称
     */
    @XmlElement(nillable = false, required = true)
    public String skuNameEn;

    /**
     * 货源地
     */
    @XmlElement(nillable = false, required = true)
    public String origin;

    /**
     * 规格
     */
    @XmlElement(nillable = false, required = false)
    public String specification;

    /**
     * sku价格
     */
    @XmlElement(nillable = false, required = true)
    public Double skuUnitPrice;

    /**
     * sku价格单位
     */
    @XmlElement(nillable = false, required = true)
    public String skuPriceCurrency;

    /**
     * sku购买数量
     */
    @XmlElement(nillable = false, required = true)
    public Integer skuQty = 0;

    /**
     * 入库时间
     */
    @XmlElement(nillable = false, required = true)
    public String skuBoundTime;

    /**
     * 清点入库数量
     */
    @XmlElement(nillable = false, required = true)
    public Integer skuCheckQty = 0;

    /**
     * 清点时间
     */
    @XmlElement(nillable = false, required = true)
    public String skuCheckTime;

    /**
     * sku备注
     */
    @XmlElement(nillable = false, required = false)
    public String skuRemark;

    /**
     * sku净重
     */
    @XmlElement(nillable = false, required = false)
    public Integer skuNetWeight;

    /**
     * 批次号
     */
    @XmlElement(nillable = false, required = false)
    public String batchNo;

    /**
     * 上次发送批次号
     */
    @XmlElement(nillable = false, required = false)
    public String lastSendBatchNo;

    /**
     * 是否是组合商品 0:否 1：是
     */
    @XmlElement(nillable = false, required = false)
    public Integer type;

    /**
     * 仓库剩余库存
     */
    @XmlElement(nillable = false, required = false)
    public Integer warehouseRemainStock;

    /**
     * 库存是否足够0:不足 1：足够
     */
    @XmlElement(nillable = false, required = false)
    public Integer isEnough;

    /**
     * 批次生成规则
     */
    @XmlElement(nillable = false, required = false)
    public String batchMakePlan;

    /**
     * 是否允许混放 1-允许 0-不允许
     */
    @XmlElement(nillable = false, required = false)
    public Integer batchMix;

    /**
     * 质检率
     */
    @XmlElement(nillable = false, required = false)
    public Integer qualityTestRate;

    /**
     * 创建日期
     */
    @XmlElement(nillable = false, required = false)
    public String createTime;

    /**
     * 货主
     */
    @XmlElement(nillable = false, required = false)
    public String customerCode;

    /**
     * 出库效期方案
     */
    @XmlElement(nillable = false, required = false)
    public String stockoutPeriodPlan;

    /**
     * 出库效期值
     */
    @XmlElement(nillable = false, required = false)
    public Integer stockoutPeriodPlanValue;

    /**
     * 入库效期方案
     */
    @XmlElement(nillable = false, required = false)
    public String stockinPeriodPlan;

    /**
     * 入库效期方案值
     */
    @XmlElement(nillable = false, required = false)
    public Integer stockinPeriodPlanValue;

    /**
     * 商品毛重,单位:g
     */
    @XmlElement(nillable = false, required = false)
    public Integer grossWeight;

    /**
     * 出库规则,单位:g
     */
    @XmlElement(nillable = false, required = false)
    public String stockoutPlan;

    /**
     * 长度,单位:cm
     */
    @XmlElement(nillable = false, required = false)
    public Integer length;

    /**
     * 高度,单位:cm
     */
    @XmlElement(nillable = false, required = false)
    public Integer height;

    /**
     * 宽度,单位:cm
     */
    @XmlElement(nillable = false, required = false)
    public Integer width;

    /**
     * 库存预警下限
     */
    @XmlElement(nillable = false, required = false)
    public Integer stockAlarmLimit;

    /**
     * 库存预警上限
     */
    @XmlElement(nillable = false, required = false)
    public Integer maxStockAlarmLimit;

    /**
     * 状态 0:禁用 1:启用
     */
    @XmlElement(nillable = false, required = false)
    public Integer status;

    /**
     * 保质期,单位:天
     */
    @XmlElement(nillable = false, required = false)
    public Integer guarantyPeriod;

    /**
     * 次品入库数量
     */
    @XmlElement(nillable = false, required = false)
    public Integer skuInInferiorQty;

    /**
     * 操作人员
     */
    @XmlElement(nillable = false, required = false)
    public String operator;

    /**
     * 损溢数量
     */
    @XmlElement(nillable = false, required = false)
    public Integer galQty;

    /**
     * 是否正品, 0:否 1:是
     */
    @XmlElement(nillable = false, required = false)
    public Integer isQuality;

    /**
     * 是否报溢, 0:否 1:是
     */
    @XmlElement(nillable = false, required = false)
    public Integer isOverFlow;

    /**
     * 库存总数
     */
    @XmlElement(nillable = false, required = false)
    private Integer totalQyCount;

    /**
     * 冻结总数
     */
    @XmlElement(nillable = false, required = false)
    private Integer totalFreezeCount;

    /**
     * 坏品总数
     */
    @XmlElement(nillable = false, required = false)
    private Integer totalWearCount;

    /**
     * 批次库存详情
     */
    @XmlElementWrapper(name = "batchs")
    @XmlElement(name = "batch" ,nillable = false, required = false)
    private List<Batch> batchs;

    /**
     * 面单名称
     */
    @XmlElement(nillable = false, required = false)
    public String skuBillName;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuNameEn() {
        return skuNameEn;
    }

    public void setSkuNameEn(String skuNameEn) {
        this.skuNameEn = skuNameEn;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Double getSkuUnitPrice() {
        return skuUnitPrice;
    }

    public void setSkuUnitPrice(Double skuUnitPrice) {
        this.skuUnitPrice = skuUnitPrice;
    }

    public String getSkuPriceCurrency() {
        return skuPriceCurrency;
    }

    public void setSkuPriceCurrency(String skuPriceCurrency) {
        this.skuPriceCurrency = skuPriceCurrency;
    }

    public Integer getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(Integer skuQty) {
        this.skuQty = skuQty;
    }

    public String getSkuBoundTime() {
        return skuBoundTime;
    }

    public void setSkuBoundTime(String skuBoundTime) {
        this.skuBoundTime = skuBoundTime;
    }

    public Integer getSkuCheckQty() {
        return skuCheckQty;
    }

    public void setSkuCheckQty(Integer skuCheckQty) {
        this.skuCheckQty = skuCheckQty;
    }

    public String getSkuCheckTime() {
        return skuCheckTime;
    }

    public void setSkuCheckTime(String skuCheckTime) {
        this.skuCheckTime = skuCheckTime;
    }

    public String getSkuRemark() {
        return skuRemark;
    }

    public void setSkuRemark(String skuRemark) {
        this.skuRemark = skuRemark;
    }

    public Integer getSkuNetWeight() {
        return skuNetWeight;
    }

    public void setSkuNetWeight(Integer skuNetWeight) {
        this.skuNetWeight = skuNetWeight;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWarehouseRemainStock() {
        return warehouseRemainStock;
    }

    public void setWarehouseRemainStock(Integer warehouseRemainStock) {
        this.warehouseRemainStock = warehouseRemainStock;
    }

    public Integer getIsEnough() {
        return isEnough;
    }

    public void setIsEnough(Integer isEnough) {
        this.isEnough = isEnough;
    }

    public String getBatchMakePlan() {
        return batchMakePlan;
    }

    public void setBatchMakePlan(String batchMakePlan) {
        this.batchMakePlan = batchMakePlan;
    }

    public Integer getBatchMix() {
        return batchMix;
    }

    public void setBatchMix(Integer batchMix) {
        this.batchMix = batchMix;
    }

    public Integer getQualityTestRate() {
        return qualityTestRate;
    }

    public void setQualityTestRate(Integer qualityTestRate) {
        this.qualityTestRate = qualityTestRate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getStockoutPeriodPlan() {
        return stockoutPeriodPlan;
    }

    public void setStockoutPeriodPlan(String stockoutPeriodPlan) {
        this.stockoutPeriodPlan = stockoutPeriodPlan;
    }

    public Integer getStockoutPeriodPlanValue() {
        return stockoutPeriodPlanValue;
    }

    public void setStockoutPeriodPlanValue(Integer stockoutPeriodPlanValue) {
        this.stockoutPeriodPlanValue = stockoutPeriodPlanValue;
    }

    public String getStockinPeriodPlan() {
        return stockinPeriodPlan;
    }

    public void setStockinPeriodPlan(String stockinPeriodPlan) {
        this.stockinPeriodPlan = stockinPeriodPlan;
    }

    public Integer getStockinPeriodPlanValue() {
        return stockinPeriodPlanValue;
    }

    public void setStockinPeriodPlanValue(Integer stockinPeriodPlanValue) {
        this.stockinPeriodPlanValue = stockinPeriodPlanValue;
    }

    public Integer getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Integer grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getStockoutPlan() {
        return stockoutPlan;
    }

    public void setStockoutPlan(String stockoutPlan) {
        this.stockoutPlan = stockoutPlan;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getStockAlarmLimit() {
        return stockAlarmLimit;
    }

    public void setStockAlarmLimit(Integer stockAlarmLimit) {
        this.stockAlarmLimit = stockAlarmLimit;
    }

    public Integer getMaxStockAlarmLimit() {
        return maxStockAlarmLimit;
    }

    public void setMaxStockAlarmLimit(Integer maxStockAlarmLimit) {
        this.maxStockAlarmLimit = maxStockAlarmLimit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGuarantyPeriod() {
        return guarantyPeriod;
    }

    public void setGuarantyPeriod(Integer guarantyPeriod) {
        this.guarantyPeriod = guarantyPeriod;
    }

    public Integer getSkuInInferiorQty() {
        return skuInInferiorQty;
    }

    public void setSkuInInferiorQty(Integer skuInInferiorQty) {
        this.skuInInferiorQty = skuInInferiorQty;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getGalQty() {
        return galQty;
    }

    public void setGalQty(Integer galQty) {
        this.galQty = galQty;
    }

    public Integer getIsQuality() {
        return isQuality;
    }

    public void setIsQuality(Integer isQuality) {
        this.isQuality = isQuality;
    }

    public Integer getIsOverFlow() {
        return isOverFlow;
    }

    public void setIsOverFlow(Integer isOverFlow) {
        this.isOverFlow = isOverFlow;
    }

    public Integer getTotalQyCount() {
        return totalQyCount;
    }

    public void setTotalQyCount(Integer totalQyCount) {
        this.totalQyCount = totalQyCount;
    }

    public Integer getTotalFreezeCount() {
        return totalFreezeCount;
    }

    public void setTotalFreezeCount(Integer totalFreezeCount) {
        this.totalFreezeCount = totalFreezeCount;
    }

    public Integer getTotalWearCount() {
        return totalWearCount;
    }

    public void setTotalWearCount(Integer totalWearCount) {
        this.totalWearCount = totalWearCount;
    }

    public List<Batch> getBatchs() {
        return batchs;
    }

    public void setBatchs(List<Batch> batchs) {
        this.batchs = batchs;
    }

    public String getSkuBillName() {
        return skuBillName;
    }

    public void setSkuBillName(String skuBillName) {
        this.skuBillName = skuBillName;
    }

    public String getLastSendBatchNo() {
        return lastSendBatchNo;
    }

    public void setLastSendBatchNo(String lastSendBatchNo) {
        this.lastSendBatchNo = lastSendBatchNo;
    }

    @Override
    public String toString() {
        return "Sku{" +
                "skuId='" + skuId + '\'' +
                ", skuCode='" + skuCode + '\'' +
                ", skuName='" + skuName + '\'' +
                ", skuNameEn='" + skuNameEn + '\'' +
                ", origin='" + origin + '\'' +
                ", specification='" + specification + '\'' +
                ", skuUnitPrice=" + skuUnitPrice +
                ", skuPriceCurrency='" + skuPriceCurrency + '\'' +
                ", skuQty=" + skuQty +
                ", skuBoundTime='" + skuBoundTime + '\'' +
                ", skuCheckQty=" + skuCheckQty +
                ", skuCheckTime='" + skuCheckTime + '\'' +
                ", skuRemark='" + skuRemark + '\'' +
                ", skuNetWeight=" + skuNetWeight +
                ", batchNo='" + batchNo + '\'' +
                ", lastSendBatchNo='" + lastSendBatchNo + '\'' +
                ", type=" + type +
                ", warehouseRemainStock=" + warehouseRemainStock +
                ", isEnough=" + isEnough +
                ", batchMakePlan='" + batchMakePlan + '\'' +
                ", batchMix=" + batchMix +
                ", qualityTestRate=" + qualityTestRate +
                ", createTime='" + createTime + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", stockoutPeriodPlan='" + stockoutPeriodPlan + '\'' +
                ", stockoutPeriodPlanValue=" + stockoutPeriodPlanValue +
                ", stockinPeriodPlan='" + stockinPeriodPlan + '\'' +
                ", stockinPeriodPlanValue=" + stockinPeriodPlanValue +
                ", grossWeight=" + grossWeight +
                ", stockoutPlan='" + stockoutPlan + '\'' +
                ", length=" + length +
                ", height=" + height +
                ", width=" + width +
                ", stockAlarmLimit=" + stockAlarmLimit +
                ", maxStockAlarmLimit=" + maxStockAlarmLimit +
                ", status=" + status +
                ", guarantyPeriod=" + guarantyPeriod +
                ", skuInInferiorQty=" + skuInInferiorQty +
                ", operator='" + operator + '\'' +
                ", galQty=" + galQty +
                ", isQuality=" + isQuality +
                ", isOverFlow=" + isOverFlow +
                ", totalQyCount=" + totalQyCount +
                ", totalFreezeCount=" + totalFreezeCount +
                ", totalWearCount=" + totalWearCount +
                ", batchs=" + batchs +
                ", skuBillName='" + skuBillName + '\'' +
                ", skuBillName='" + skuBillName + '\'' +
                '}';
    }
}
