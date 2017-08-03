package com.sfebiz.supplychain.protocol.zto.internation;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/1
 * Time: 下午4:36
 */
public class ZTOInternationOrderEntity2 implements Serializable {


    private static final long serialVersionUID = 141551362905363086L;
    /**
     * 一笔订单的商品价格
     */
    private BigDecimal tradeOrderValue;

    /**
     * 一笔订单的商品价格单位 (建议为人民币)
     */
    private String tradeOrderValueUnit;

    /**
     * 订单中付款商品的重量(当 前发货重量)
     */
    private BigDecimal payableWeight;

    private String clearCode;

    public String getClearCode() {
        return clearCode;
    }

    public void setClearCode(String clearCode) {
        this.clearCode = clearCode;
    }

    public BigDecimal getTradeOrderValue() {
        return tradeOrderValue;
    }

    public void setTradeOrderValue(BigDecimal tradeOrderValue) {
        this.tradeOrderValue = tradeOrderValue;
    }

    public String getTradeOrderValueUnit() {
        return tradeOrderValueUnit;
    }

    public void setTradeOrderValueUnit(String tradeOrderValueUnit) {
        this.tradeOrderValueUnit = tradeOrderValueUnit;
    }

    public BigDecimal getPayableWeight() {
        return payableWeight;
    }

    public void setPayableWeight(BigDecimal payableWeight) {
        this.payableWeight = payableWeight;
    }
}
