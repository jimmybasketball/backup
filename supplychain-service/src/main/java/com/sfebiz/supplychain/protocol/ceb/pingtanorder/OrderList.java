package com.sfebiz.supplychain.protocol.ceb.pingtanorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午2:16
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OrderList")
public class OrderList implements Serializable {

    private static final long serialVersionUID = 3900889924806817054L;

    /**
     *海关备案商品编号
     */
    @XmlElement(name = "itemNo")
    private String itemNo;
    /**
     *商品货号
     */
    @XmlElement(name = "goodsNo")
    private String goodsNo;
    /**
     *商品上架品名
     */
    @XmlElement(name = "shelfGoodsName")
    private String shelfGoodsName;
    /**
     *商品描述
     */
    @XmlElement(name = "describe")
    private String describe;
    /**
     *HS编码
     */
    @XmlElement(name = "codeTs")
    private String codeTs;
    /**
     *申报品名
     */
    @XmlElement(name = "goodsName")
    private String goodsName;
    /**
     *规格型号
     */
    @XmlElement(name = "goodsModel")
    private String goodsModel;
    /**
     *行邮税号
     */
    @XmlElement(name = "taxCode")
    private String taxCode;
    /**
     *成交单价
     */
    @XmlElement(name = "price")
    private String price;
    /**
     *币制
     */
    @XmlElement(name = "currency")
    private String currency;
    /**
     *数量
     */
    @XmlElement(name = "quantity")
    private String quantity;
    /**
     *成交总价
     */
    @XmlElement(name = "priceTotal")
    private String priceTotal;
    /**
     *计量单位
     */
    @XmlElement(name = "unit")
    private String unit;
    /**
     *折扣优惠
     */
    @XmlElement(name = "discount")
    private String discount;
    /**
     *是否赠品
     */
    @XmlElement(name = "flag")
    private String flag;
    /**
     *原产国
     */
    @XmlElement(name = "country")
    private String country;
    /**
     *用途
     */
    @XmlElement(name = "purposeCode")
    private String purposeCode;
    /**
     *废旧物品
     */
    @XmlElement(name = "wasteMaterials")
    private String wasteMaterials;
    /**
     *包装种类
     */
    @XmlElement(name = "wrapType")
    private String wrapType;
    /**
     *件数
     */
    @XmlElement(name = "packNum")
    private String packNum;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getShelfGoodsName() {
        return shelfGoodsName;
    }

    public void setShelfGoodsName(String shelfGoodsName) {
        this.shelfGoodsName = shelfGoodsName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCodeTs() {
        return codeTs;
    }

    public void setCodeTs(String codeTs) {
        this.codeTs = codeTs;
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

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getWasteMaterials() {
        return wasteMaterials;
    }

    public void setWasteMaterials(String wasteMaterials) {
        this.wasteMaterials = wasteMaterials;
    }

    public String getWrapType() {
        return wrapType;
    }

    public void setWrapType(String wrapType) {
        this.wrapType = wrapType;
    }

    public String getPackNum() {
        return packNum;
    }

    public void setPackNum(String packNum) {
        this.packNum = packNum;
    }
}
