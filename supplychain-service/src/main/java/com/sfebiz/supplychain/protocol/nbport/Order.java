package com.sfebiz.supplychain.protocol.nbport;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by ztc on 2016/12/28.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"orderShop","otoCode","hgArea","orderFrom","packageFlag","busOrderNo","postFee","insuranceFee",
        "amount","buyerAccount", "phone","email","taxAmount","tariffAmount","addedValueTaxAmount","consumptionDutyAmount",
            "grossWeight","dealDate","disAmount","remark","promotions", "orderGoods","orderPay","orderLogistics","isPayPush"})
@XmlRootElement(name = "order")
public class Order implements Serializable {

    /**
     * 店铺代吗
     */
    @XmlElement(name = "orderShop")
    private  String  orderShop;

    /**
     * oto店铺代码
     */
    @XmlElement(name = "otoCode")
    private  String  otoCode;

    /**
     * 海关关区
     */
    @XmlElement(name = "hgArea")
    private  String  hgArea;

    /**
     * 购物网站代码
     */
    @XmlElement(name = "orderFrom")
    private  String  orderFrom;

    /**
     * 是否组合装标识
     */
    @XmlElement(name = "packageFlag")
    private  String  packageFlag;

    /**
     * 订单号
     */
    @XmlElement(name = "busOrderNo")
    private  String  busOrderNo;

    /**
     * 运费
     */
    @XmlElement(name = "postFee")
    private  String  postFee;

    /**
     * 保价费
     */
    @XmlElement(name = "insuranceFee")
    private  String  insuranceFee;

    /**
     * 买家实付金额
     */
    @XmlElement(name = "amount")
    private  String  amount;

    /**
     * 购物网站买家账号
     */
    @XmlElement(name = "buyerAccount")
    private  String  buyerAccount;

    /**
     * 手机号
     */
    @XmlElement(name = "phone")
    private  String  phone;

    /**
     * 邮箱
     */
    @XmlElement(name = "email")
    private  String  email;

    /**
     * 税额
     */
    @XmlElement(name = "taxAmount")
    private  String  taxAmount;

    /**
     * 关税额
     */
    @XmlElement(name = "tariffAmount")
    private  String  tariffAmount;

    /**
     * 增值税额
     */
    @XmlElement(name = "addedValueTaxAmount")
    private  String  addedValueTaxAmount;

    /**
     * 消费税额
     */
    @XmlElement(name = "consumptionDutyAmount")
    private  String  consumptionDutyAmount;

    /**
     * 毛重
     */
    @XmlElement(name = "grossWeight")
    private  String  grossWeight;

    /**
     * 优惠金额
     */
    @XmlElement(name = "disAmount")
    private  String  disAmount;

    /**
     * 下单时间
     */
    @XmlElement(name = "dealDate")
    private  String  dealDate;

    /**
     * 备注信息
     */
    @XmlElement(name = "remark")
    private  String  remark;

    /**
     * 优惠信息列表
     */
    @XmlElement(name = "promotions")
    private  OrderPromotions  promotions;

    /**
     * 是否待推送支付单
     */
    @XmlElement(name = "isPayPush")
    private  String isPayPush;

    /**
     * 商品明细列表
     */
    @XmlElement(name = "goods")
    private OrderGoods orderGoods;

    /**
     * 支付单信息
     */
    @XmlElement(name = "pay")
    private  OrderPay orderPay;

    /**
     * 物流信息
     */
    @XmlElement(name = "logistics")
    private  OrderLogistics orderLogistics;


    public String getOrderShop() {
        return orderShop;
    }

    public void setOrderShop(String orderShop) {
        this.orderShop = orderShop;
    }

    public String getOtoCode() {
        return otoCode;
    }

    public void setOtoCode(String otoCode) {
        this.otoCode = otoCode;
    }

    public String getHgArea() {
        return hgArea;
    }

    public void setHgArea(String hgArea) {
        this.hgArea = hgArea;
    }

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }

    public String getPackageFlag() {
        return packageFlag;
    }

    public void setPackageFlag(String packageFlag) {
        this.packageFlag = packageFlag;
    }

    public String getBusOrderNo() {
        return busOrderNo;
    }

    public void setBusOrderNo(String busOrderNo) {
        this.busOrderNo = busOrderNo;
    }

    public String getPostFee() {
        return postFee;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }

    public String getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(String insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTariffAmount() {
        return tariffAmount;
    }

    public void setTariffAmount(String tariffAmount) {
        this.tariffAmount = tariffAmount;
    }

    public String getAddedValueTaxAmount() {
        return addedValueTaxAmount;
    }

    public void setAddedValueTaxAmount(String addedValueTaxAmount) {
        this.addedValueTaxAmount = addedValueTaxAmount;
    }

    public String getConsumptionDutyAmount() {
        return consumptionDutyAmount;
    }

    public void setConsumptionDutyAmount(String consumptionDutyAmount) {
        this.consumptionDutyAmount = consumptionDutyAmount;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getDisAmount() {
        return disAmount;
    }

    public void setDisAmount(String disAmount) {
        this.disAmount = disAmount;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public OrderPromotions getPromotions() {
        return promotions;
    }

    public void setPromotions(OrderPromotions promotions) {
        this.promotions = promotions;
    }

    public String getIsPayPush() {
        return isPayPush;
    }

    public void setIsPayPush(String isPayPush) {
        this.isPayPush = isPayPush;
    }

    public OrderGoods getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(OrderGoods orderGoods) {
        this.orderGoods = orderGoods;
    }

    public OrderPay getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(OrderPay orderPay) {
        this.orderPay = orderPay;
    }

    public OrderLogistics getOrderLogistics() {
        return orderLogistics;
    }

    public void setOrderLogistics(OrderLogistics orderLogistics) {
        this.orderLogistics = orderLogistics;
    }
}
