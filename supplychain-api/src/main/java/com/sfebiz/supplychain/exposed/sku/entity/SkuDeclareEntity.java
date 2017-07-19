package com.sfebiz.supplychain.exposed.sku.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * User: <a href="mailto:yanmingming@163.com">严明明</a>
 * Version: 1.0.0
 * Since: 15/04/01 下午6:49
 * <p/>
 * 商品申报信息结构体
 */
public class SkuDeclareEntity implements Serializable {

    private static final long serialVersionUID = 2862879127890169948L;

    /**
     * 商品编码
     */
    public Long skuId;

    /**
     * 口岸ID
     */
    public Long portId;

    /**
     * 口岸名称
     */
    public String portName;

    /**
     * 备案名称
     */
    public String declareName;

    /**
     * 国检编号
     */
    public String recordNo;

    /**
     * 企业商品编号
     */
    public String productId;

    /**
     * 行邮税号
     */
    public String postTaxNo;

    /**
     * 商品类别
     */
    public String categoryName;

    /**
     * 商品类别码
     */
    public String categoryId;

    /**
     * 商品规格
     */
    public String attributes;

    /**
     * 品牌名称
     */
    public String brandName;

    /**
     * 商品条形码，多个已“,”分割
     */
    public String barcode;

    /**
     * 货源地
     */
    public String origin;

    /**
     * 备案价格，单位RMB的元
     */
    public Float price;

    /**
     * 备注
     */
    public String remark;

    /**
     * 净重
     */
    public String netWeight;

    /**
     * 毛重
     */
    public String grossWeight;

    /**
     * HS编码
     */
    public String hsCode;

    /**
     * 税率，小于1，比如0.1
     */
    public String taxRate;

    /**
     * 消费税率，小于1，比如0.1
     */
    public String consumptionDutyRate;

    /**
     * 增值税率，小于1，比如0.1
     */
    public String addedValueTaxRate;

    /**
     * 关税，小于1，比如0.1
     */
    public String tariffRate;

    /**
     * 计量单位
     */

    public String measuringUnit;

    /**
     * 备案模式
     */
    public String declareMode;

    /**
     * 供应商名称
     */
    public String providerName;

    // -------- 广州保税 ------- //

    /**
     * 海关申报要素
     */
    public String customsDeclarationElement;

    /**
     * 生产厂家
     */
    public String manufacturer;

    /**
     * 第三方商品编号
     */
    public String thirdSkuId;

    /**
     * 进口单价(人民币)
     */
    public BigDecimal importUnitPriceRmb;

    /**
     * 销售零售价(人民币)
     */
    public BigDecimal retailPriceRmb;

    /**
     * 网站销售渠道
     */
    public String salesChannel;

    // --------广州直邮---------- //

    /**
     * 第二计量单位
     */
    public String secondaryMeasuringUnit;

    /**
     * 商品品质
     */
    public String skuQuality;

    // --------宁波保税---------- //

    /**
     * 综合平台商品编号
     */
    public Long nbbsSkuId;

    // --------杭州保税---------- //

    /**
     * 规范申报
     */
    public String specificationDeclare;

    /**
     * 单价
     */
    public BigDecimal unitPrice;

    /**
     * 总价
     */
    public BigDecimal totalPrice;

    /**
     * 成交数量
     */
    public Integer tradeCount;

    /**
     * 功能用途
     */
    public String features;

    /**
     * 币制
     */
    public String currency;

    //--------杭州直邮---------//


    /**
     * 是否涉证
     */
    public String isRelateCertificate;

    /**
     * 是否有证
     */
    public String isHaveCertificate;

    /**
     * 生产企业地址
     */
    public String providerAddress;

    // -------宁波直邮-------- //
    /**
     * 仓库企业代码
     */
    private String warehouseEnterpriseCode;


    /**
     * 税则号
     */
    public String hsNumber;

    /**
     * 商品英文名称
     */
    private String skuForeignName;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 成分
     */
    public String ingredient;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 图片链接
     */
    private String imgUrl;


    /**
     * 商品备案表ID
     */
    public Long productDeclareId;

    /**
     * 操作类型,finish_collect:备案完成导入, declare_pass:备案通过
     */
    public String operateType;

    /**
     * 商品号 平潭专用
     */
    public  String productCode;

    /**
     * 第一计量值
     */
    public String firstWeight;

    /**
     * 第二计量值
     */
    public String secondWeight;

    /**
     * 第一计量单位
     */
    public String firstMeasuringUnit;

    /**
     * 第二计量单位
     */
    public String secondMeasuringUnit;

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

    public String getDeclareName() {
        return declareName;
    }

    public void setDeclareName(String declareName) {
        this.declareName = declareName;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getDeclareMode() {
        return declareMode;
    }

    public void setDeclareMode(String declareMode) {
        this.declareMode = declareMode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public String getCustomsDeclarationElement() {
        return customsDeclarationElement;
    }

    public void setCustomsDeclarationElement(String customsDeclarationElement) {
        this.customsDeclarationElement = customsDeclarationElement;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getThirdSkuId() {
        return thirdSkuId;
    }

    public void setThirdSkuId(String thirdSkuId) {
        this.thirdSkuId = thirdSkuId;
    }

    public BigDecimal getImportUnitPriceRmb() {
        return importUnitPriceRmb;
    }

    public void setImportUnitPriceRmb(BigDecimal importUnitPriceRmb) {
        this.importUnitPriceRmb = importUnitPriceRmb;
    }

    public BigDecimal getRetailPriceRmb() {
        return retailPriceRmb;
    }

    public void setRetailPriceRmb(BigDecimal retailPriceRmb) {
        this.retailPriceRmb = retailPriceRmb;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getSecondaryMeasuringUnit() {
        return secondaryMeasuringUnit;
    }

    public void setSecondaryMeasuringUnit(String secondaryMeasuringUnit) {
        this.secondaryMeasuringUnit = secondaryMeasuringUnit;
    }

    public String getSkuQuality() {
        return skuQuality;
    }

    public void setSkuQuality(String skuQuality) {
        this.skuQuality = skuQuality;
    }

    public Long getNbbsSkuId() {
        return nbbsSkuId;
    }

    public void setNbbsSkuId(Long nbbsSkuId) {
        this.nbbsSkuId = nbbsSkuId;
    }

    public String getSpecificationDeclare() {
        return specificationDeclare;
    }

    public void setSpecificationDeclare(String specificationDeclare) {
        this.specificationDeclare = specificationDeclare;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getWarehouseEnterpriseCode() {
        return warehouseEnterpriseCode;
    }

    public void setWarehouseEnterpriseCode(String warehouseEnterpriseCode) {
        this.warehouseEnterpriseCode = warehouseEnterpriseCode;
    }

    public String getSkuForeignName() {
        return skuForeignName;
    }

    public void setSkuForeignName(String skuForeignName) {
        this.skuForeignName = skuForeignName;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getProductDeclareId() {
        return productDeclareId;
    }

    public void setProductDeclareId(Long productDeclareId) {
        this.productDeclareId = productDeclareId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getConsumptionDutyRate() {
        return consumptionDutyRate;
    }

    public void setConsumptionDutyRate(String consumptionDutyRate) {
        this.consumptionDutyRate = consumptionDutyRate;
    }

    public String getAddedValueTaxRate() {
        return addedValueTaxRate;
    }

    public void setAddedValueTaxRate(String addedValueTaxRate) {
        this.addedValueTaxRate = addedValueTaxRate;
    }

    public String getTariffRate() {
        return tariffRate;
    }

    public void setTariffRate(String tariffRate) {
        this.tariffRate = tariffRate;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    public String getHsNumber() {
        return hsNumber;
    }

    public void setHsNumber(String hsNumber) {
        this.hsNumber = hsNumber;
    }

    public String getIsRelateCertificate() {
        return isRelateCertificate;
    }

    public void setIsRelateCertificate(String isRelateCertificate) {
        this.isRelateCertificate = isRelateCertificate;
    }

    public String getIsHaveCertificate() {
        return isHaveCertificate;
    }

    public void setIsHaveCertificate(String isHaveCertificate) {
        this.isHaveCertificate = isHaveCertificate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(String firstWeight) {
        this.firstWeight = firstWeight;
    }

    public String getSecondWeight() {
        return secondWeight;
    }

    public void setSecondWeight(String secondWeight) {
        this.secondWeight = secondWeight;
    }

    public String getFirstMeasuringUnit() {
        return firstMeasuringUnit;
    }

    public void setFirstMeasuringUnit(String firstMeasuringUnit) {
        this.firstMeasuringUnit = firstMeasuringUnit;
    }

    public String getSecondMeasuringUnit() {
        return secondMeasuringUnit;
    }

    public void setSecondMeasuringUnit(String secondMeasuringUnit) {
        this.secondMeasuringUnit = secondMeasuringUnit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("skuId", skuId)
                .append("portId", portId)
                .append("portName", portName)
                .append("declareName", declareName)
                .append("recordNo", recordNo)
                .append("productId", productId)
                .append("postTaxNo", postTaxNo)
                .append("categoryName", categoryName)
                .append("categoryId", categoryId)
                .append("attributes", attributes)
                .append("brandName", brandName)
                .append("barcode", barcode)
                .append("origin", origin)
                .append("price", price)
                .append("remark", remark)
                .append("netWeight", netWeight)
                .append("grossWeight", grossWeight)
                .append("hsCode", hsCode)
                .append("consumptionDutyRate", consumptionDutyRate)
                .append("addedValueTaxRate", addedValueTaxRate)
                .append("tariffRate", tariffRate)
                .append("measuringUnit", measuringUnit)
                .append("declareMode", declareMode)
                .append("providerName", providerName)
                .append("customsDeclarationElement", customsDeclarationElement)
                .append("manufacturer", manufacturer)
                .append("thirdSkuId", thirdSkuId)
                .append("importUnitPriceRmb", importUnitPriceRmb)
                .append("retailPriceRmb", retailPriceRmb)
                .append("salesChannel", salesChannel)
                .append("secondaryMeasuringUnit", secondaryMeasuringUnit)
                .append("skuQuality", skuQuality)
                .append("nbbsSkuId", nbbsSkuId)
                .append("specificationDeclare", specificationDeclare)
                .append("unitPrice", unitPrice)
                .append("totalPrice", totalPrice)
                .append("tradeCount", tradeCount)
                .append("features", features)
                .append("currency", currency)
                .append("warehouseEnterpriseCode", warehouseEnterpriseCode)
                .append("skuForeignName", skuForeignName)
                .append("purpose", purpose)
                .append("ingredient", ingredient)
                .append("description", description)
                .append("imgUrl", imgUrl)
                .append("productDeclareId", productDeclareId)
                .append("operateType", operateType)
                .append("providerAddress", providerAddress)
                .append("isRelateCertificate", isRelateCertificate)
                .append("isHaveCertificate", isHaveCertificate)
                .append("hsNumber", hsNumber)
                .toString();
    }
}
