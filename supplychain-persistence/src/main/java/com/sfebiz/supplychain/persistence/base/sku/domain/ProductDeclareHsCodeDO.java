/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.domain/ProductDeclareNbDirectmailDO.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 宁波直邮商品备案明细
 * <p/>
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
public class ProductDeclareHsCodeDO extends BaseDO {

    private static final long serialVersionUID = -6021365017836447598L;
    /**
     * HS编码/行邮税号
     */
    private String hsCode;

    /**
     * s申报类型（1，跨境BC  2.快件EMS）
     */
    private String type;

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
     * 第一计量单位
     */
    private String measuringUnit;

    /**
     * 第二计量单位
     */
    private String secondMeasuringUnit;

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public String getSecondMeasuringUnit() {
        return secondMeasuringUnit;
    }

    public void setSecondMeasuringUnit(String secondMeasuringUnit) {
        this.secondMeasuringUnit = secondMeasuringUnit;
    }
}
