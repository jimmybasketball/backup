package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"goodsOrder", "goodsName", "goodsModel", "codeTs",
        "grossWeight", "unitPrice", "goodsUnit", "goodsCount", "originCountry","currency"})
@XmlRootElement(name = "jkfOrderDetail")
public class JKFOrderDetail {
    /**
     * 商品序号
     */
    @XmlElement(name = "goodsOrder")
    private int goodsOrder;

    /**
     * 物品名称
     */
    @XmlElement(name = "goodsName")
    private String goodsName;

    /**
     * 商品规格、型号
     */
    @XmlElement(name = "goodsModel")
    private String goodsModel;

    /**
     * 行邮税号
     */
    @XmlElement(name = "codeTs")
    private String codeTs;

    /**
     * 商品毛重 ，非必填
     */
    @XmlElement(name = "grossWeight")
    private double grossWeight;

    /**
     * 申报单价
     */
    @XmlElement(name = "unitPrice")
    private String unitPrice;
    /**
     * 申报计量单位
     */
    @XmlElement(name = "goodsUnit")
    private String goodsUnit;

    /**
     * 申报数量
     */
    @XmlElement(name = "goodsCount")
    private int goodsCount;


    /**
     * 申报数量
     */
    @XmlElement(name = "currency")
    private String currency;

    /**
     * 产销国
     */
    @XmlElement(name = "originCountry", required = true)
    private String originCountry;

    public int getGoodsOrder() {
        return goodsOrder;
    }

    public void setGoodsOrder(int goodsOrder) {
        this.goodsOrder = goodsOrder;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsModel() {
        return goodsModel;
    }

    public void setGoodsModel(String goodsModel) {
        this.goodsModel = goodsModel;
    }

    public String getCodeTs() {
        return codeTs;
    }

    public void setCodeTs(String codeTs) {
        this.codeTs = codeTs;
    }

    public double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
