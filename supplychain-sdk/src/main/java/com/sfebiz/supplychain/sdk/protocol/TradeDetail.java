package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易订单详情类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tradeDetail", propOrder = {"tradeOrders", "tradeType"})
public class TradeDetail implements Serializable {
    private static final long serialVersionUID = 2912126921026570560L;

    /**
     * 交易类型，LiuLian：流连，HaiTao：海淘，HeiKe：嘿客
     */
    @XmlElement
    public String tradeType;

    /**
     * 交易订单列表
     */
    @XmlElementWrapper(name = "tradeOrders")
    @XmlElement(name = "tradeOrder", nillable = false, required = false)
    public List<TradeOrder> tradeOrders;

    public List<TradeOrder> getTradeOrders() {
        if (tradeOrders == null) {
            tradeOrders = new ArrayList<TradeOrder>();
        }
        return tradeOrders;
    }

    public void setTradeOrders(List<TradeOrder> tradeOrders) {
        this.tradeOrders = tradeOrders;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    @Override
    public String toString() {
        return "TradeDetail{" +
                "tradeType='" + tradeType + '\'' +
                ", tradeOrders=" + tradeOrders +
                '}';
    }
}
