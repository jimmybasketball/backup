package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * User:tu.jie@ifunq.com
 * Date: 15/1/21
 * Time: 下午9:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SaleOrder")
@XmlType(propOrder = {"warehouseCode","sfOrderType","erpOrderType","erpOrder","orderNote","packageNote","tradeOrderDateTime","payDateTime","currencyCode",
        "companyNote","shopName","tradePlatform","tradeOrder","buyerId","completeDelivery","fromFlag","deliveryDate","paymentMethod","paymentNumber","freight",
        "orderTotalAmount","orderDiscount","otherCharge","actualAmount","deliveryRequested","priority","deliveryModel","isInvoice","isAllowSplit","orgErpOrder",
        "orgTradeOrder","inProcessWaybillNo","customerNetCode","customerNetName","customerAreaCode","customsDeclarationInfo","orderAddedServices","orderExtendAttribute","orderItems",
        "orderCarrier","orderReceiverInfo","orderSenderInfo","orderInvoice"})
public class StockoutOrderSaleOrder implements Serializable {


    private static final long serialVersionUID = 3117497079631648136L;
    @Description("仓库编码")
    @XmlElement(name = "WarehouseCode", required = false)
    private String warehouseCode;

    @Description("顺丰订单类型")
    @XmlElement(name = "SfOrderType", required = false)
    private String sfOrderType;

    @Description("ERP订单类型")
    @XmlElement(name = "ErpOrderType", required = false)
    private String erpOrderType;

    @Description("订单号")
    @XmlElement(name = "ErpOrder", required = false)
    private String erpOrder;

    @Description("订单备注")
    @XmlElement(name = "OrderNote", required = false)
    private String orderNote;

    @Description("包装备注")
    @XmlElement(name = "PackageNote", required = false)
    private String packageNote;

    @Description("订单日期")
    @XmlElement(name = "TradeOrderDateTime", required = false)
    private String tradeOrderDateTime;

    @Description("付款日期")
    @XmlElement(name = "PayDateTime", required = false)
    private String payDateTime;

    @Description("币种代码")
    @XmlElement(name = "CurrencyCode", required = false)
    private String currencyCode;

    @Description("商家备注")
    @XmlElement(name = "CompanyNote", required = false)
    private String companyNote;

    @Description("店铺名称")
    @XmlElement(name = "ShopName", required = false)
    private String shopName;

    @Description("交易平台")
    @XmlElement(name = "TradePlatform", required = false)
    private String tradePlatform;

    @Description("平台交易号")
    @XmlElement(name = "TradeOrder", required = false)
    private String tradeOrder;

    @Description("买家账号")
    @XmlElement(name = "BuyerId", required = false)
    private String buyerId;

    @Description("是否需要整单发货")
    @XmlElement(name = "CompleteDelivery", required = false)
    private String completeDelivery;

    @Description("运单打印寄件方信息来源")
    @XmlElement(name = "FromFlag", required = false)
    private String fromFlag;

    @Description("发货日期")
    @XmlElement(name = "DeliveryDate", required = false)
    private String deliveryDate;

    @Description("客户支付方式")
    @XmlElement(name = "PaymentMethod", required = false)
    private String paymentMethod;

    @Description("支付号码")
    @XmlElement(name = "PaymentNumber", required = false)
    private String paymentNumber;

    @Description("订单支付运费")
    @XmlElement(name = "Freight", required = false)
    private String freight;

    @Description("订单总金额")
    @XmlElement(name = "OrderTotalAmount", required = false)
    private String orderTotalAmount;

    @Description("订单优惠金额")
    @XmlElement(name = "OrderDiscount", required = false)
    private String orderDiscount;

    @Description("其他金额")
    @XmlElement(name = "OtherCharge", required = false)
    private String otherCharge;

    @Description("实际支付金额")
    @XmlElement(name = "ActualAmount", required = false)
    private String actualAmount;

    @Description("配送要求")
    @XmlElement(name = "DeliveryRequested", required = false)
    private String deliveryRequested;

    @Description("订单优先级")
    @XmlElement(name = "Priority", required = false)
    private String priority;

    @Description("订单发货模式")
    @XmlElement(name = "DeliveryModel", required = false)
    private String deliveryModel;

    @Description("是否需要发票")
    @XmlElement(name = "IsInvoice", required = false)
    private String isInvoice;

    @Description("是否允许拆单")
    @XmlElement(name = "IsAllowSplit", required = false)
    private String isAllowSplit;

    @Description("原始ERP单号")
    @XmlElement(name = "OrgErpOrder", required = false)
    private String orgErpOrder;

    @Description("原始电商单号")
    @XmlElement(name = "OrgTradeOrder", required = false)
    private String orgTradeOrder;

    @Description("头程运单号")
    @XmlElement(name = "InProcessWaybillNo", required = false)
    private String inProcessWaybillNo;

    @Description("客户网络代码")
    @XmlElement(name = "CustomerNetCode", required = false)
    private String customerNetCode;

    @Description("客户网点名称")
    @XmlElement(name = "CustomerNetName", required = false)
    private String customerNetName;

    @Description("客户区域代码")
    @XmlElement(name = "CustomerAreaCode", required = false)
    private String customerAreaCode;

    @XmlElement(name = "CustomsDeclarationInfo", required = false)
     public StockoutOrderCustomsDeclarationInfo customsDeclarationInfo;

    @XmlElement(name = "OrderAddedService", required = false)
    @XmlElementWrapper(name = "orderAddedServices")
    public List<StockoutOrderAddedService> orderAddedServices;

    @XmlElement(name = "OrderCarrier", required = false)
    public StockoutOrderCarrier orderCarrier;

    @XmlElement(name = "OrderReceiverInfo", required = false)
    public StockoutOrderReceiverInfo orderReceiverInfo;

    @XmlElement(name = "OrderSenderInfo", required = false)
    public StockoutOrderSenderInfo orderSenderInfo;

    @XmlElement(name = "OrderExtendAttribute", required = false)
    @XmlElementWrapper(name = "orderExtendAttribute")
    public List<StockoutOrderExtendAttribute> orderExtendAttribute;

    @XmlElement(name = "OrderInvoice", required = false)
    public StockoutOrderInvoice orderInvoice;

    @XmlElement(name = "OrderItem", required = false)
    @XmlElementWrapper(name = "OrderItems")
    public List<StockoutOrderItem> orderItems;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public String getSfOrderType() {
        return sfOrderType;
    }

    public String getErpOrderType() {
        return erpOrderType;
    }

    public String getErpOrder() {
        return erpOrder;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public String getPackageNote() {
        return packageNote;
    }

    public String getTradeOrderDateTime() {
        return tradeOrderDateTime;
    }

    public String getPayDateTime() {
        return payDateTime;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCompanyNote() {
        return companyNote;
    }

    public String getShopName() {
        return shopName;
    }

    public String getTradePlatform() {
        return tradePlatform;
    }

    public String getTradeOrder() {
        return tradeOrder;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getCompleteDelivery() {
        return completeDelivery;
    }

    public String getFromFlag() {
        return fromFlag;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public String getFreight() {
        return freight;
    }

    public String getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public String getOrderDiscount() {
        return orderDiscount;
    }

    public String getOtherCharge() {
        return otherCharge;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public String getDeliveryRequested() {
        return deliveryRequested;
    }

    public String getPriority() {
        return priority;
    }

    public String getDeliveryModel() {
        return deliveryModel;
    }

    public String getIsInvoice() {
        return isInvoice;
    }

    public String getIsAllowSplit() {
        return isAllowSplit;
    }

    public String getOrgErpOrder() {
        return orgErpOrder;
    }

    public String getOrgTradeOrder() {
        return orgTradeOrder;
    }

    public String getInProcessWaybillNo() {
        return inProcessWaybillNo;
    }

    public String getCustomerNetCode() {
        return customerNetCode;
    }

    public String getCustomerNetName() {
        return customerNetName;
    }

    public String getCustomerAreaCode() {
        return customerAreaCode;
    }

    public StockoutOrderCustomsDeclarationInfo getCustomsDeclarationInfo() {
        return customsDeclarationInfo;
    }

    public List<StockoutOrderAddedService> getOrderAddedServices() {
        return orderAddedServices;
    }

    public StockoutOrderCarrier getOrderCarrier() {
        return orderCarrier;
    }

    public StockoutOrderReceiverInfo getOrderReceiverInfo() {
        return orderReceiverInfo;
    }

    public StockoutOrderSenderInfo getOrderSenderInfo() {
        return orderSenderInfo;
    }

    public List<StockoutOrderExtendAttribute> getOrderExtendAttribute() {
        return orderExtendAttribute;
    }

    public StockoutOrderInvoice getOrderInvoice() {
        return orderInvoice;
    }

    public List<StockoutOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public void setSfOrderType(String sfOrderType) {
        this.sfOrderType = sfOrderType;
    }

    public void setErpOrderType(String erpOrderType) {
        this.erpOrderType = erpOrderType;
    }

    public void setErpOrder(String erpOrder) {
        this.erpOrder = erpOrder;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public void setPackageNote(String packageNote) {
        this.packageNote = packageNote;
    }

    public void setTradeOrderDateTime(String tradeOrderDateTime) {
        this.tradeOrderDateTime = tradeOrderDateTime;
    }

    public void setPayDateTime(String payDateTime) {
        this.payDateTime = payDateTime;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setCompanyNote(String companyNote) {
        this.companyNote = companyNote;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setTradePlatform(String tradePlatform) {
        this.tradePlatform = tradePlatform;
    }

    public void setTradeOrder(String tradeOrder) {
        this.tradeOrder = tradeOrder;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public void setCompleteDelivery(String completeDelivery) {
        this.completeDelivery = completeDelivery;
    }

    public void setFromFlag(String fromFlag) {
        this.fromFlag = fromFlag;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public void setOrderTotalAmount(String orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public void setOrderDiscount(String orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public void setOtherCharge(String otherCharge) {
        this.otherCharge = otherCharge;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public void setDeliveryRequested(String deliveryRequested) {
        this.deliveryRequested = deliveryRequested;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setDeliveryModel(String deliveryModel) {
        this.deliveryModel = deliveryModel;
    }

    public void setIsInvoice(String isInvoice) {
        this.isInvoice = isInvoice;
    }

    public void setIsAllowSplit(String isAllowSplit) {
        this.isAllowSplit = isAllowSplit;
    }

    public void setOrgErpOrder(String orgErpOrder) {
        this.orgErpOrder = orgErpOrder;
    }

    public void setOrgTradeOrder(String orgTradeOrder) {
        this.orgTradeOrder = orgTradeOrder;
    }

    public void setInProcessWaybillNo(String inProcessWaybillNo) {
        this.inProcessWaybillNo = inProcessWaybillNo;
    }

    public void setCustomerNetCode(String customerNetCode) {
        this.customerNetCode = customerNetCode;
    }

    public void setCustomerNetName(String customerNetName) {
        this.customerNetName = customerNetName;
    }

    public void setCustomerAreaCode(String customerAreaCode) {
        this.customerAreaCode = customerAreaCode;
    }

    public void setCustomsDeclarationInfo(StockoutOrderCustomsDeclarationInfo customsDeclarationInfo) {
        this.customsDeclarationInfo = customsDeclarationInfo;
    }

    public void setOrderAddedServices(List<StockoutOrderAddedService> orderAddedServices) {
        this.orderAddedServices = orderAddedServices;
    }

    public void setOrderCarrier(StockoutOrderCarrier orderCarrier) {
        this.orderCarrier = orderCarrier;
    }

    public void setOrderReceiverInfo(StockoutOrderReceiverInfo orderReceiverInfo) {
        this.orderReceiverInfo = orderReceiverInfo;
    }

    public void setOrderSenderInfo(StockoutOrderSenderInfo orderSenderInfo) {
        this.orderSenderInfo = orderSenderInfo;
    }

    public void setOrderExtendAttribute(List<StockoutOrderExtendAttribute> orderExtendAttribute) {
        this.orderExtendAttribute = orderExtendAttribute;
    }

    public void setOrderInvoice(StockoutOrderInvoice orderInvoice) {
        this.orderInvoice = orderInvoice;
    }

    public void setOrderItems(List<StockoutOrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
