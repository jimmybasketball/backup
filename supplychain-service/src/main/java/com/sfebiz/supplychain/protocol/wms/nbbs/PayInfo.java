package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"PaymentNo", "OrderNo", "OrderSeqNo", "Amount", "CurrCode", "BuyerAccount", "Source"})
@XmlRootElement(name = "Pay")
@Description("进口支付单信息")
public class PayInfo extends NbbsBody implements Serializable {
    private static final long serialVersionUID = 7995835832869082608L;
    @Description("支付订单号")
    @XmlElement(nillable = false, required = false)
    private String PaymentNo;

    @Description("订单号")
    @XmlElement(nillable = false, required = false)
    private String OrderNo;

    @Description("商家订单交易号")
    @XmlElement(nillable = false, required = false)
    private String OrderSeqNo;

    @Description("金额")
    @XmlElement(nillable = false, required = false)
    private double Amount;

    @Description("币种")
    @XmlElement(nillable = false, required = false)
    private String CurrCode;

    @Description("买家账号")
    @XmlElement(nillable = false, required = false)
    private String BuyerAccount;

    @Description("支付方式代码")
    @XmlElement(nillable = false, required = false)
    private String Source;

    public String getPaymentNo() {
        return PaymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        PaymentNo = paymentNo;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getOrderSeqNo() {
        return OrderSeqNo;
    }

    public void setOrderSeqNo(String orderSeqNo) {
        OrderSeqNo = orderSeqNo;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getCurrCode() {
        return CurrCode;
    }

    public void setCurrCode(String currCode) {
        CurrCode = currCode;
    }

    public String getBuyerAccount() {
        return BuyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        BuyerAccount = buyerAccount;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("PaymentNo", PaymentNo)
                .append("OrderNo", OrderNo)
                .append("OrderSeqNo", OrderSeqNo)
                .append("Amount", Amount)
                .append("CurrCode", CurrCode)
                .append("BuyerAccount", BuyerAccount)
                .append("Source", Source)
                .toString();
    }
}
