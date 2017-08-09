package com.sfebiz.supplychain.service.sku.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品申报信息(备案)业务实体
 * @author liujc [liujunchi@ifunq.com]
 * @date  2017/7/25 18:23
 */
public class SkuDeclareBO implements Serializable {

    private static final long serialVersionUID = -6059554987876452812L;

    /**
     * 商品ID
     */
    private Long skuId;

    /**
     * 口岸ID
     */
    private Long portId;

    /**
     * 企业备案编号
     */
    private String productId;

    /**
     * 备案状态
     */
    private String state;

    /**
     * 产品国检备案编号
     */
    private String recordNo;

    /**
     * 备案名称
     */
    private String declareName;

    /**
     * 商品规格
     */
    private String attributes;

    /**
     * 行邮税号(必填)
     */
    private String postTaxNo;

    /**
     * 商品类别(必填)
     */
    private String categoryName;

    /**
     * 商品类别码
     */
    private String categoryId;

    /**
     * 条形码
     */
    private String barCode;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 货源地
     */
    private String origin;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 备案人民币价格
     */
    private Long priceRmb;

    /**
     * 净重（千克）
     */
    private String netWeight;

    /**
     * 毛重（千克）
     */
    private String grossWeight;

    /**
     * HS编码
     */
    private String hsCode;

    /**
     * 税则号
     */
    private String hsNumber;

    /**
     * 生产商地址
     */
    private String providerAddress;

    /**
     * 行邮税率，格式为0.1 ,不超过 1
     */
    private String taxRate;

    /**
     * 关税税率
     */
    private String tariffRate;

    /**
     * 增值税税率
     */
    private String addedValueTaxRate;

    /**
     * 消费税税率
     */
    private String consumptionDutyRate;

    /**
     * 备案模式 (DIRECT_MAIL:直邮, BONDED:保税)
     */
    private String declareMode;

    /**
     * 开始收集时间
     */
    private Date startCollectTime;

    /**
     * 收集完毕时间
     */
    private Date finishCollectTime;

    /**
     * 计量单位
     */
    private String measuringUnit;

    /**
     * 备案任务创建人
     */
    private String createUser;
    /**
     *资料收集完毕导入人
     */
    private String dataCollectUser;
    /**
     *备案完成导入人
     */
    private String filingUser;
    /**
     *供应商名称
     */
    private String providerName;
    /**
     *采买模式
     */
    private String purchaseMode;

    /**
     * 第一计量单位
     */
    private String firstMeasuringUnit;

    /**
     * 第一计量单位重量
     */
    private String firstWeight;

    /**
     * 第二计量单位
     */
    private String secondMeasuringUnit;

    /**
     * 第二计量单位重量
     */
    private String secondWeight;


    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getDeclareName() {
        return declareName;
    }

    public void setDeclareName(String declareName) {
        this.declareName = declareName;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getPostTaxNo() {
        return postTaxNo;
    }

    public void setPostTaxNo(String postTaxNo) {
        this.postTaxNo = postTaxNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Long getPriceRmb() {
        return priceRmb;
    }

    public void setPriceRmb(Long priceRmb) {
        this.priceRmb = priceRmb;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getHsNumber() {
        return hsNumber;
    }

    public void setHsNumber(String hsNumber) {
        this.hsNumber = hsNumber;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getTariffRate() {
        return tariffRate;
    }

    public void setTariffRate(String tariffRate) {
        this.tariffRate = tariffRate;
    }

    public String getAddedValueTaxRate() {
        return addedValueTaxRate;
    }

    public void setAddedValueTaxRate(String addedValueTaxRate) {
        this.addedValueTaxRate = addedValueTaxRate;
    }

    public String getConsumptionDutyRate() {
        return consumptionDutyRate;
    }

    public void setConsumptionDutyRate(String consumptionDutyRate) {
        this.consumptionDutyRate = consumptionDutyRate;
    }

    public String getDeclareMode() {
        return declareMode;
    }

    public void setDeclareMode(String declareMode) {
        this.declareMode = declareMode;
    }

    public Date getStartCollectTime() {
        return startCollectTime;
    }

    public void setStartCollectTime(Date startCollectTime) {
        this.startCollectTime = startCollectTime;
    }

    public Date getFinishCollectTime() {
        return finishCollectTime;
    }

    public void setFinishCollectTime(Date finishCollectTime) {
        this.finishCollectTime = finishCollectTime;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDataCollectUser() {
        return dataCollectUser;
    }

    public void setDataCollectUser(String dataCollectUser) {
        this.dataCollectUser = dataCollectUser;
    }

    public String getFilingUser() {
        return filingUser;
    }

    public void setFilingUser(String filingUser) {
        this.filingUser = filingUser;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getPurchaseMode() {
        return purchaseMode;
    }

    public void setPurchaseMode(String purchaseMode) {
        this.purchaseMode = purchaseMode;
    }

    public String getFirstMeasuringUnit() {
        return firstMeasuringUnit;
    }

    public void setFirstMeasuringUnit(String firstMeasuringUnit) {
        this.firstMeasuringUnit = firstMeasuringUnit;
    }

    public String getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(String firstWeight) {
        this.firstWeight = firstWeight;
    }

    public String getSecondMeasuringUnit() {
        return secondMeasuringUnit;
    }

    public void setSecondMeasuringUnit(String secondMeasuringUnit) {
        this.secondMeasuringUnit = secondMeasuringUnit;
    }

    public String getSecondWeight() {
        return secondWeight;
    }

    public void setSecondWeight(String secondWeight) {
        this.secondWeight = secondWeight;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}