package com.sfebiz.supplychain.protocol.ceb.order;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午2:16
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"gnum", "itemNo", "itemName", "itemDescribe", "barCode", "unit", "qty", "price", "totalPrice", "currency", "country", "note"})
@XmlRootElement(name = "OrderList")
public class OrderList implements Serializable {

    private static final long serialVersionUID = 1041248930049353283L;

    /**
     * 商品序号
     * 从1开始的递增序号（关联对应清单商品项）
     * 必填
     */
    @XmlElement(name = "gnum")
    private Integer gnum;

    /**
     * 企业商品货号
     * 电商平台自定义的商品货号（SKU）
     * 非必填
     */
    @XmlElement(name = "itemNo")
    private String itemNo;

    /**
     * 企业商品名称
     * 电商平台上架的商品名称
     * 必填
     */
    @XmlElement(name = "itemName")
    private String itemName;

    /**
     * 企业商品描述
     * 电商平台上架的商品描述宣传信息
     * 非必填
     */
    @XmlElement(name = "itemDescribe")
    private String itemDescribe;

    /**
     * 条形码
     * 非必填
     */
    @XmlElement(name = "barCode")
    private String barCode;

    /**
     * 单位
     * 海关标准的参数代码 海关标准的参数代码 《JGS-20 海关业务代码集》- 计量单位代码
     */
    @XmlElement(name = "unit")
    private String unit;

    /**
     * 数量
     */
    @XmlElement(name = "qty")
    private String qty;

    /**
     * 单价
     */
    @XmlElement(name = "price")
    private String price;

    /**
     * 总价
     */
    @XmlElement(name = "totalPrice")
    private String totalPrice;

    /**
     * 币制
     */
    @XmlElement(name = "currency")
    private String currency;

    /**
     * 原产国
     */
    @XmlElement(name = "country")
    private String country;

    /**
     * 备注
     * 非必填
     */
    @XmlElement(name = "note")
    private String note;

    public Integer getGnum() {
        return gnum;
    }

    public void setGnum(Integer gnum) {
        this.gnum = gnum;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescribe() {
        return itemDescribe;
    }

    public void setItemDescribe(String itemDescribe) {
        this.itemDescribe = itemDescribe;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
