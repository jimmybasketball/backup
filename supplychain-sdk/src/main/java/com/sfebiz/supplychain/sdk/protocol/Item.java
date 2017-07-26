package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * 商品详情类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item", propOrder = {"itemId", "itemName", "itemCategoryName",
        "itemUnitPrice", "itemQuantity", "qtyUnit", "usedNew", "brand",
        "specification", "model", "material", "used", "netWeight", "particles",
        "size", "itemRemark"})
public class Item implements Serializable {

    /**
     * 商品编号
     */
    @XmlElement(nillable = false, required = true)
    public Long itemId = 0l;

    /**
     * 商品名称
     */
    @XmlElement(nillable = false, required = true)
    public String itemName;

    /**
     * 商品类目
     */
    @XmlElement(nillable = false, required = true)
    public String itemCategoryName;

    /**
     * 商品单价，单位分
     */
    @XmlElement(nillable = false, required = true)
    public Double itemUnitPrice = 0.0;

    /**
     * 商品购买数量
     */
    @XmlElement(nillable = false, required = true)
    public Integer itemQuantity = 0;

    /**
     * 单位
     */
    @XmlElement(nillable = false, required = false)
    public String qtyUnit;

    /**
     * 二手/全新
     */
    @XmlElement(nillable = false, required = false)
    public String usedNew;

    /**
     * 品牌
     */
    @XmlElement(nillable = false, required = false)
    public String brand;

    /**
     * 规格
     */
    @XmlElement(nillable = false, required = false)
    public String specification;

    /**
     * 型号
     */
    @XmlElement(nillable = false, required = false)
    public String model;

    /**
     * 材质
     */
    @XmlElement(nillable = false, required = false)
    public String material;

    /**
     * 用途
     */
    @XmlElement(nillable = false, required = false)
    public String used;

    /**
     * 净重（G）
     */
    @XmlElement(nillable = false, required = false)
    public int netWeight;

    /**
     * 颗粒数
     */
    @XmlElement(nillable = false, required = false)
    public String particles;

    /**
     * 尺码
     */
    @XmlElement(nillable = false, required = false)
    public String size;

    /**
     * 商品备注
     */
    @XmlElement(nillable = false, required = false)
    public String itemRemark;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public Double getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(Double itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public String getUsedNew() {
        return usedNew;
    }

    public void setUsedNew(String usedNew) {
        this.usedNew = usedNew;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public Integer getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Integer netWeight) {
        this.netWeight = netWeight;
    }

    public String getParticles() {
        return particles;
    }

    public void setParticles(String particles) {
        this.particles = particles;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getItemRemark() {
        return itemRemark;
    }

    public void setItemRemark(String itemRemark) {
        this.itemRemark = itemRemark;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemCategoryName='" + itemCategoryName + '\'' +
                ", itemUnitPrice=" + itemUnitPrice +
                ", itemQuantity=" + itemQuantity +
                ", qtyUnit='" + qtyUnit + '\'' +
                ", usedNew='" + usedNew + '\'' +
                ", brand='" + brand + '\'' +
                ", specification='" + specification + '\'' +
                ", model='" + model + '\'' +
                ", material='" + material + '\'' +
                ", used='" + used + '\'' +
                ", netWeight=" + netWeight +
                ", particles='" + particles + '\'' +
                ", size='" + size + '\'' +
                ", itemRemark='" + itemRemark + '\'' +
                '}';
    }
}
