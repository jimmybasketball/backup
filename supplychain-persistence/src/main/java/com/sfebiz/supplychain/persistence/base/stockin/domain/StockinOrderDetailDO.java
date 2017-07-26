package com.sfebiz.supplychain.persistence.base.stockin.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * <p></p>
 * User: 张雅静
 * Date: 17/07/10
 * Time: 下午3:40
 */
public class StockinOrderDetailDO extends BaseDO {

    private static final long serialVersionUID = -4957662139873379528L;
    //入库单id
    private Long stockinOrderId;
    //skuid
    private Long skuId;
    //商品名称
    private String skuName;
    //商品批次
    private String skuBatch;
     //批次生成规则
    private String batchGeneratePlan;
     //不带流水码的批次号
    private String originBatch;
    //商品条形码
    private String skuBarcode;
    //商品外文名称
    private String skuForeignName;
    //商品规格
    private String skuSpecification;
    //应收数量
    private Integer count;
    //仓库回传数量（正品）
    private Integer returnCount;
    //仓库回传数量（坏品）
    private Integer badReturnCount;
    //实际入库数量（正品）
    private Integer realCount;
    //实际入库数量（坏品）
    private Integer badRealCount;
    //差异数量
    private Integer realDiffCount;
    //差异描述
    private String diffDesc;
    //操作人员
    private String operator;
    //生产日期
    private Date productionDate;
    //过期日期
    private Date expirationDate;
    //入库日期
    private Date stockinDate;

    @Override
    public String toString() {
        return "StockinOrderDetailDO{" +
                "stockinOrderId=" + stockinOrderId +
                ", skuId=" + skuId +
                ", skuName='" + skuName + '\'' +
                ", skuBatch='" + skuBatch + '\'' +
                ", batchGeneratePlan='" + batchGeneratePlan + '\'' +
                ", originBatch='" + originBatch + '\'' +
                ", skuBarcode='" + skuBarcode + '\'' +
                ", skuForeignName='" + skuForeignName + '\'' +
                ", skuSpecification='" + skuSpecification + '\'' +
                ", count=" + count +
                ", returnCount=" + returnCount +
                ", badReturnCount=" + badReturnCount +
                ", realCount=" + realCount +
                ", badRealCount=" + badRealCount +
                ", realDiffCount=" + realDiffCount +
                ", diffDesc='" + diffDesc + '\'' +
                ", operator='" + operator + '\'' +
                ", productionDate=" + productionDate +
                ", expirationDate=" + expirationDate +
                ", stockinDate=" + stockinDate +
                '}';
    }

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

    public String getBatchGeneratePlan() {
        return batchGeneratePlan;
    }

    public void setBatchGeneratePlan(String batchGeneratePlan) {
        this.batchGeneratePlan = batchGeneratePlan;
    }

    public String getOriginBatch() {
        return originBatch;
    }

    public void setOriginBatch(String originBatch) {
        this.originBatch = originBatch;
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

    public Integer getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    public Integer getBadReturnCount() {
        return badReturnCount;
    }

    public void setBadReturnCount(Integer badReturnCount) {
        this.badReturnCount = badReturnCount;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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
