package com.sfebiz.supplychain.protocol.wms.fse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangyajing on 2016/11/23.
 * 费舍尔销售订单信息
 */
public class Order implements Serializable{
    private static final long serialVersionUID = 770789697019944532L;

    /**
     * 订单编号
     */
    public String orderCode;

    /**
     * 货主编码 (FQHT)
     */
    public String companyCode;

    /**
     * 下单时间
     */
    public String orderDate;

    /**
     * 订单类型(默认SHB)
     */
    public String outType;

    /**
     * 快递公司代码
     */
    public String expressName;

    /**
     * 寄件人名称
     */
    public String senderName;

    /**
     * 寄件人电话
     */
    public String senderTel;

    /**
     * 寄件人地址
     */
    public String senderAddr;

    /**
     * 收货姓名
     */
    public String receiverName;

    /**
     * 手机号码
     */
    public String mobile;

    /**
     * 仓库代码 FWLD01
     */
    public String warehouseCode;

    /**
     * 起运国代码
     */
    public String originCountry;

    /**
     * 收件人省
     */
    public String province;

    /**
     * 收件人市
     */
    public String city;

    /**
     * 收件人区
     */
    public String district;

    /**
     * 详细地址
     */
    public String receiverAddress;

    /**
     * 个人委托申报协议
     */
    public String holdCode;

    /**
     * 支付类型
     */
    public String payType;

    /**
     * 支付公司编码
     */
    public String payCompanyCode;

    /**
     * 支付单号
     */
    public String payNumber;

    /**
     * 订单总金额
     */
    public String orderTotalAmount;

    /**
     * 订单货款
     */
    public String orderGoodsAmount;

    /**
     * 订单税款
     */
    public String orderTaxAmount;

    /**
     * 优惠金额
     */
    public String discount;


    /**
     * 关税
     */
    public String tariffAmount;

    /**
     * 增值税
     */
    public String addedValueTax;

    /**
     * 消费税
     */
    public String consumptionDutyAmount;

    /**
     * 运费
     */
    public String feeAmount;

    /**
     * 成交时间
     */
    public String tradeTime;

    /**
     * 成交币制
     */
    public String currCode;

    /**
     * 成交总价
     */
    public String totalAmount;

    /**
     * 购买人 ID
     */
    public String purchaserId;

    /**
     * 支付人 ID
     */
    public String id;

    /**
     * 购买人姓名
     */
    public String name;

    /**
     * 联系电话
     */
    public String telNumber;

    /**
     * 证件类型代码
     */
    public String paperType;

    /**
     * 证件号码
     */
    public String paperNumber;

    /**
     * 购买人地址
     */
    public String address;

    /**
     * 渠道区分
     */
    public String isMaster;

    /**
     * 商品信息
     */
    List<FSEOrderDtl> OrderDtls;

    @Override
    public String toString() {
        return "Order{" +
                "orderCode='" + orderCode + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", outType='" + outType + '\'' +
                ", expressName='" + expressName + '\'' +
                ", senderName='" + senderName + '\'' +
                ", senderTel='" + senderTel + '\'' +
                ", senderAddr='" + senderAddr + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", warehouseCode='" + warehouseCode + '\'' +
                ", originCountry='" + originCountry + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", holdCode='" + holdCode + '\'' +
                ", payType='" + payType + '\'' +
                ", payCompanyCode='" + payCompanyCode + '\'' +
                ", payNumber='" + payNumber + '\'' +
                ", orderTotalAmount='" + orderTotalAmount + '\'' +
                ", orderGoodsAmount='" + orderGoodsAmount + '\'' +
                ", orderTaxAmount='" + orderTaxAmount + '\'' +
                ", discount='" + discount + '\'' +
                ", tariffAmount='" + tariffAmount + '\'' +
                ", addedValueTax='" + addedValueTax + '\'' +
                ", consumptionDutyAmount='" + consumptionDutyAmount + '\'' +
                ", feeAmount='" + feeAmount + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                ", currCode='" + currCode + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", purchaserId='" + purchaserId + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", telNumber='" + telNumber + '\'' +
                ", paperType='" + paperType + '\'' +
                ", paperNumber='" + paperNumber + '\'' +
                ", address='" + address + '\'' +
                ", isMaster='" + isMaster + '\'' +
                ", OrderDtls=" + OrderDtls +
                '}';
    }

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderTel() {
        return senderTel;
    }

    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getSenderAddr() {
        return senderAddr;
    }

    public void setSenderAddr(String senderAddr) {
        this.senderAddr = senderAddr;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getHoldCode() {
        return holdCode;
    }

    public void setHoldCode(String holdCode) {
        this.holdCode = holdCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayCompanyCode() {
        return payCompanyCode;
    }

    public void setPayCompanyCode(String payCompanyCode) {
        this.payCompanyCode = payCompanyCode;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public String getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(String orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public String getOrderGoodsAmount() {
        return orderGoodsAmount;
    }

    public void setOrderGoodsAmount(String orderGoodsAmount) {
        this.orderGoodsAmount = orderGoodsAmount;
    }

    public String getOrderTaxAmount() {
        return orderTaxAmount;
    }

    public void setOrderTaxAmount(String orderTaxAmount) {
        this.orderTaxAmount = orderTaxAmount;
    }

    public String getTariffAmount() {
        return tariffAmount;
    }

    public void setTariffAmount(String tariffAmount) {
        this.tariffAmount = tariffAmount;
    }

    public String getAddedValueTax() {
        return addedValueTax;
    }

    public void setAddedValueTax(String addedValueTax) {
        this.addedValueTax = addedValueTax;
    }

    public String getConsumptionDutyAmount() {
        return consumptionDutyAmount;
    }

    public void setConsumptionDutyAmount(String consumptionDutyAmount) {
        this.consumptionDutyAmount = consumptionDutyAmount;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPurchaserId() {
        return purchaserId;
    }

    public void setPurchaserId(String purchaserId) {
        this.purchaserId = purchaserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getPaperNumber() {
        return paperNumber;
    }

    public void setPaperNumber(String paperNumber) {
        this.paperNumber = paperNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<FSEOrderDtl> getOrderDtls() {
        return OrderDtls;
    }

    public void setOrderDtls(List<FSEOrderDtl> orderDtls) {
        OrderDtls = orderDtls;
    }
}
