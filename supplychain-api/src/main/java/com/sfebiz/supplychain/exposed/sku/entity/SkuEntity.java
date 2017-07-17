package com.sfebiz.supplychain.exposed.sku.entity;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 基础商品实体
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-12 17:56
 **/
public class SkuEntity implements Serializable {

    private static final long serialVersionUID = -3489151077895355734L;

    /**
     * id
     */
    public Long id;

    /**
     * 商品名称
     */
    @NotNull(message = "商品名称不能为空")
    @Length(max = 64, min = 2, message = "商品名称长度为2-64位")
    public String name;

    /**
     * 外文名称
     */
    @Length(max = 64, min = 2, message = "外文名称长度为2-64位")
    public String foreignName;

    /**
     * 品牌名称
     */
    @NotNull(message = "品牌名称不能为空")
    @Length(max = 64, min = 2, message = "品牌名称长度为2-64位")
    public String brandName;

    /**
     * 销售单位
     */
    public String measuringUnit;

    /**
     * 保质期，单位天
     */
    public Integer guarantyPeriod;

    /**
     * 规格型号
     */
    public String attributesDesc;

    /**
     * 毛重，单位克
     */
    public Integer grossWeight;

    /**
     * 净重，单位克
     */
    public Integer netWeight;

    /**
     * 长，单位厘米
     */
    public Integer length;

    /**
     * 宽，单位厘米
     */
    public Integer width;

    /**
     * 高，单位厘米
     */
    public Integer height;

    /**
     * 材质
     */
    public String materialQualityDesc;

    /**
     * 商品成分
     */
    public String goodsComponentDesc;

    /**
     * 批次生成方案（EXPIRE_SAME:过期日期相同,STOCKIN_SAME:入库日期相同）
     */
    public String batchGeneratePlan;

    /**
     * 出库方案（EXPIRE_FIRST:先到期先出,STOCKIN_FIRST:先入库先出）
     */
    public String stockoutPlan;

    /**
     * 创建时间
     */
    public Date gmtCreate;

    /**
     * 创建人
     */
    public String createBy;

    /**
     * 修改人
     */
    public String modifiedBy;

    /**
     * 修改时间
     */
    public Date gmtModified;

    /**
     * 商品条码
     */
    public List<SkuBarcodeEntity> skuBarcodeList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForeignName() {
        return foreignName;
    }

    public void setForeignName(String foreignName) {
        this.foreignName = foreignName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public Integer getGuarantyPeriod() {
        return guarantyPeriod;
    }

    public void setGuarantyPeriod(Integer guarantyPeriod) {
        this.guarantyPeriod = guarantyPeriod;
    }

    public String getAttributesDesc() {
        return attributesDesc;
    }

    public void setAttributesDesc(String attributesDesc) {
        this.attributesDesc = attributesDesc;
    }

    public Integer getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Integer grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Integer getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Integer netWeight) {
        this.netWeight = netWeight;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getMaterialQualityDesc() {
        return materialQualityDesc;
    }

    public void setMaterialQualityDesc(String materialQualityDesc) {
        this.materialQualityDesc = materialQualityDesc;
    }

    public String getGoodsComponentDesc() {
        return goodsComponentDesc;
    }

    public void setGoodsComponentDesc(String goodsComponentDesc) {
        this.goodsComponentDesc = goodsComponentDesc;
    }

    public String getBatchGeneratePlan() {
        return batchGeneratePlan;
    }

    public void setBatchGeneratePlan(String batchGeneratePlan) {
        this.batchGeneratePlan = batchGeneratePlan;
    }

    public String getStockoutPlan() {
        return stockoutPlan;
    }

    public void setStockoutPlan(String stockoutPlan) {
        this.stockoutPlan = stockoutPlan;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public List<SkuBarcodeEntity> getSkuBarcodeList() {
        return skuBarcodeList;
    }

    public void setSkuBarcodeList(List<SkuBarcodeEntity> skuBarcodeList) {
        this.skuBarcodeList = skuBarcodeList;
    }

    @Override
    public String toString() {
        return "SkuDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", foreignName='" + foreignName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", measuringUnit='" + measuringUnit + '\'' +
                ", guarantyPeriod='" + guarantyPeriod + '\'' +
                ", attributesDesc='" + attributesDesc + '\'' +
                ", grossWeight='" + grossWeight + '\'' +
                ", netWeight='" + netWeight + '\'' +
                ", length='" + length + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", materialQualityDesc='" + materialQualityDesc + '\'' +
                ", goodsComponentDesc='" + goodsComponentDesc + '\'' +
                ", batchGeneratePlan='" + batchGeneratePlan + '\'' +
                ", stockoutPlan='" + stockoutPlan + '\'' +
                ", skuBarcodeList='" + skuBarcodeList + '\'' +
                '}';
    }
}