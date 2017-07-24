package com.sfebiz.supplychain.service.port.model;

import java.io.Serializable;

/**
 * <p>商品申报信息业务实体</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/7/22
 * Time: 下午7:15
 */
public class ProductDeclareBO implements Serializable {

    private static final long serialVersionUID = -6059554987876452812L;

    /**
     * 商品ID
     */
    private Long              skuId;

    /**
     * 口岸ID
     */
    private Long              portId;

    /**
     * 申报名称
     */
    private String            declareName;

    /**
     * 企业备案编号
     */
    private String            productId;

    /**
     * 单个条码
     */
    private String            singleBarCode;

    /**
     * 行邮税号(必填)
     */
    private String            postTaxNo;

    /**
     * HS编码
     */
    private String            hsCode;

    /**
     * 行邮税率，格式为0.1 ,不超过 1
     */
    private String            taxRate;

    /**
     * 关税税率
     */
    private String            tariffRate;

    /**
     * 增值税税率
     */
    private String            addedValueTaxRate;

    /**
     * 消费税税率
     */
    private String            consumptionDutyRate;

    /**
     * 计量单位
     */
    private String            measuringUnit;

    /**
     * 净重（千克）
     */
    private String            netWeight;

    /**
     * 毛重（千克）
     */
    private String            grossWeight;

    /**
     * 原产国
     */
    private String            origin;

    /**
     * 商品规格
     */
    private String            attributes;

    /**
     * 备注
     */
    private String            remark;

    /**
     * 第一计量单位
     */
    private String            firstMeasuringUnit;

    /**
     * 第一计量单位重量
     */
    private String            firstWeight;

    /**
     * 第二计量单位
     */
    private String            secondMeasuringUnit;

    /**
     * 第二计量单位重量
     */
    private String            secondWeight;

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

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

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public String getSingleBarCode() {
        return singleBarCode;
    }

    public void setSingleBarCode(String singleBarCode) {
        this.singleBarCode = singleBarCode;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProductDeclareEntity{");
        sb.append("skuId=").append(skuId);
        sb.append(", portId=").append(portId);
        sb.append(", declareName='").append(declareName).append('\'');
        sb.append(", productId='").append(productId).append('\'');
        sb.append(", singleBarCode='").append(singleBarCode).append('\'');
        sb.append(", postTaxNo='").append(postTaxNo).append('\'');
        sb.append(", hsCode='").append(hsCode).append('\'');
        sb.append(", taxRate='").append(taxRate).append('\'');
        sb.append(", tariffRate='").append(tariffRate).append('\'');
        sb.append(", addedValueTaxRate='").append(addedValueTaxRate).append('\'');
        sb.append(", consumptionDutyRate='").append(consumptionDutyRate).append('\'');
        sb.append(", measuringUnit='").append(measuringUnit).append('\'');
        sb.append(", netWeight='").append(netWeight).append('\'');
        sb.append(", grossWeight='").append(grossWeight).append('\'');
        sb.append(", origin='").append(origin).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
