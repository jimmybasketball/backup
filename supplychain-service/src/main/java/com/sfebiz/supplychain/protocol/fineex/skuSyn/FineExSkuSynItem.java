package com.sfebiz.supplychain.protocol.fineex.skuSyn;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;

/**
 * Created by wuyun on 2017/3/14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@XmlType(propOrder = {"actionType","barCode","itemCode","itemName","categoryName","property","batchRule"})
public class FineExSkuSynItem {

    @Description("操作类型：ADD-新增 MODIFY-修改（actionType=ADD，发网已存在的商品进行修改操作）")
    @XmlElement(name = "actionType",required = true)
    private String actionType;

    @Description("商家条码 [唯一确定该商品,可作为分拣依据]")
    @XmlElement(name = "barCode",required = true)
    private String barCode;

    @Description("SKU 编码")
    @XmlElement(name = "itemCode",required = true)
    private  String itemCode;

    @Description("商品名称")
    @XmlElement(name = "itemName",required = true)
    private  String itemName;

    @Description("商品类型（类目名称）")
    @XmlElement(name = "categoryName",required = true)
    private String categoryName;

    @Description("商品属性，如颜色：红色尺寸：M")
    @XmlElement(name = "property",required = true)
    private  String property;

    @Description("批次规则（0生产日期，1失效日期）")
    @XmlElement(name = "batchRule",required = true)
    private Byte batchRule;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Byte getBatchRule() {
        return batchRule;
    }

    public void setBatchRule(Byte batchRule) {
        this.batchRule = batchRule;
    }
}
