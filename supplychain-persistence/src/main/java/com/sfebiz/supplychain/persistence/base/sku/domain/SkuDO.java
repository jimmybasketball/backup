package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 基础商品DO
 *
 * @author tanzx
 * @create 2017-07-12 18:22
 **/
public class SkuDO extends BaseDO {

    private static final long serialVersionUID = 6337920990298421466L;

    /**
     * id
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 外文名称
     */
    private String foreignName;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 销售单位
     */
    private String measuringUnit;

    /**
     * 保质期，单位天
     */
    private Integer guarantyPeriod;

    /**
     * 规格型号
     */
    private String attributesDesc;

    /**
     * 毛重，单位克
     */
    private Integer grossWeight;

    /**
     * 净重，单位克
     */
    private Integer netWeight;

    /**
     * 长，单位厘米
     */
    private Integer length;

    /**
     * 宽，单位厘米
     */
    private Integer width;

    /**
     * 高，单位厘米
     */
    private Integer height;

    /**
     * 材质
     */
    private String materialQualityDesc;

    /**
     * 商品成分
     */
    private String goodsComponentDesc;

    /**
     * 批次生成方案（EXPIRE_SAME:过期日期相同,STOCKIN_SAME:入库日期相同）
     */
    private String batchGeneratePlan;

    /**
     * 出库方案（EXPIRE_FIRST:先到期先出,STOCKIN_FIRST:先入库先出）
     */
    private String stockoutPlan;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 删除标识
     */
    private int isDelete;

    /**
     * MYSQL更新时间_FOR_BI
     */
    private String updatedTimeForBi;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
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
                '}';
    }
}
