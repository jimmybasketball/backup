package com.sfebiz.supplychain.sdk.protocol;


import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易订单结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"tradeOrderId", "occurTime", "tardeOrderValue", "tardeOrderValueUnit", "tardeOrderTaxValue", "tardeOrderTaxValueUnit", "buyer", "buyerEn", "tradeRemark", "items", "itemsEn"})
public class TradeOrder implements Serializable {
    private static final long serialVersionUID = 4129281029190534466L;
    /**
     * 交易订单ID
     */
    @XmlElement(nillable = false, required = false)
    public Long tradeOrderId;

    /**
     * 业务发生时间
     */
    @XmlElement(nillable = false, required = false)
    public String occurTime;

    /**
     * 订单商品价格
     */
    @XmlElement(nillable = false, required = false)
    public Double tardeOrderValue;

    /**
     * 订单商品价格单位
     */
    @XmlElement(nillable = false, required = false)
    public String tardeOrderValueUnit;

    /**
     * 商品税额
     */
    @XmlElement(nillable = false, required = false)
    public Double tardeOrderTaxValue;

    /**
     * 税额价格单位
     */
    @XmlElement(nillable = false, required = false)
    public String tardeOrderTaxValueUnit;

    /**
     * 买家
     */
    @XmlElement(nillable = false, required = false)
    public Buyer buyer;

    /**
     * 买家英文信息
     */
    @XmlElement(nillable = false, required = false)
    public Buyer buyerEn;

    /**
     * 订单备注
     */
    @XmlElement(nillable = false, required = false)
    public String tradeRemark;

    /**
     * 订单列表
     */
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item", nillable = false, required = false)
    public List<Item> items;

    /**
     * 订单列表英文信息
     */
    @XmlElementWrapper(name = "itemsEn")
    @XmlElement(name = "item", nillable = false, required = false)
    public List<Item> itemsEn;

    public Long getTradeOrderId() {
        return tradeOrderId;
    }

    public void setTradeOrderId(Long tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public Double getTardeOrderValue() {
        return tardeOrderValue;
    }

    public void setTardeOrderValue(Double tardeOrderValue) {
        this.tardeOrderValue = tardeOrderValue;
    }

    public String getTardeOrderValueUnit() {
        return tardeOrderValueUnit;
    }

    public void setTardeOrderValueUnit(String tardeOrderValueUnit) {
        this.tardeOrderValueUnit = tardeOrderValueUnit;
    }

    public Double getTardeOrderTaxValue() {
        return tardeOrderTaxValue;
    }

    public void setTardeOrderTaxValue(Double tardeOrderTaxValue) {
        this.tardeOrderTaxValue = tardeOrderTaxValue;
    }

    public String getTardeOrderTaxValueUnit() {
        return tardeOrderTaxValueUnit;
    }

    public void setTardeOrderTaxValueUnit(String tardeOrderTaxValueUnit) {
        this.tardeOrderTaxValueUnit = tardeOrderTaxValueUnit;
    }

    public Buyer getBuyer() {
        if (buyer == null) {
            buyer = new Buyer();
        }
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public String getTradeRemark() {
        return tradeRemark;
    }

    public void setTradeRemark(String tradeRemark) {
        this.tradeRemark = tradeRemark;
    }

    public List<Item> getItems() {
        if (items == null) {
            items = new ArrayList<Item>();
        }
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Buyer getBuyerEn() {
        return buyerEn;
    }

    public void setBuyerEn(Buyer buyerEn) {
        this.buyerEn = buyerEn;
    }

    public List<Item> getItemsEn() {
        return itemsEn;
    }

    public void setItemsEn(List<Item> itemsEn) {
        this.itemsEn = itemsEn;
    }

    @Override
    public String toString() {
        return "TradeOrder{" +
                "tradeOrderId=" + tradeOrderId +
                ", occurTime='" + occurTime + '\'' +
                ", tardeOrderValue=" + tardeOrderValue +
                ", tardeOrderValueUnit='" + tardeOrderValueUnit + '\'' +
                ", tardeOrderTaxValue=" + tardeOrderTaxValue +
                ", tardeOrderTaxValueUnit='" + tardeOrderTaxValueUnit + '\'' +
                ", buyer=" + buyer +
                ", buyerEn=" + buyerEn +
                ", tradeRemark='" + tradeRemark + '\'' +
                ", items=" + items +
                ", itemsEn=" + itemsEn +
                '}';
    }
}
