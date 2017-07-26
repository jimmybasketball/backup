package com.sfebiz.supplychain.sdk.protocol;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 事件消息类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eventBody", propOrder = { "clearanceDetail", "paymentDetail", "tradeDetail", "logisticsDetail","routeDetail","orderQueryRequest",
        "trackingNo", "noType", "customerNo", "skuDetail", "supplierDetail", "orderType", "orderNo", "remarks","creator","referenceNo",
         "operator","confirmTime", "stockoutOrderType","StockinId","mailNo"})
public class EventBody implements Serializable {

    private static final long serialVersionUID = -8171535429934039630L;


    /**
     * 入库单Id
     */
    @XmlElement(nillable = false,required = false)
    private String StockinId;

    /**
     * 清关详情
     */
    @XmlElement(nillable = false, required = false)
    public ClearanceDetail clearanceDetail;

    /**
     * 付款详情
     */
    @XmlElement(nillable = false, required = true)
    public PaymentDetail paymentDetail;

    /**
     * 交易详情
     */
    @XmlElement(nillable = false, required = true)
    public TradeDetail tradeDetail;

    /**
     * 物流详情
     */
    @XmlElement(nillable = false, required = true)
    public LogisticsDetail logisticsDetail;

    /**
     * 路由详情
     */
    @XmlElement(nillable = false, required = true)
    public RouteDetail routeDetail;

    /**
     * 出库单查询
     */
    @XmlElement(nillable = false, required = false)
    public OrderQueryRequest orderQueryRequest;

    /**
     * 追踪运单号或者参考号
     */
    @XmlElement(nillable = false)
    public String trackingNo;

    /**
     * track:为追踪号,ref:参考号
     */
    @XmlElement(nillable = false)
    public String noType;

    /**
     * 用户号
     */
    @XmlElement(nillable = false)
    public String customerNo;

    /**
     * 商品详情
     */
    @XmlElement(nillable = false)
    public SkuDetail skuDetail;

    /**
     * 供应商详情
     */
    @XmlElement(nillable = false, required = false)
    public SupplierDetail supplierDetail;

    /**
     * 关联单类型
     */
    @XmlElement(nillable = false)
    public Integer orderType;

    /**
     * 关联单号
     */
    @XmlElement(nillable = false)
    public String orderNo;

    /**
     * 备注
     */
    @XmlElement(nillable = false)
    public String remarks;

    /**
     * 创建人
     */
    @XmlElement(nillable = false)
    public String creator;

    /**
     * 损益单号
     */
    @XmlElement(nillable = false)
    public String referenceNo;

    /**
     * 操作员
     */
    @XmlElement(nillable = false)
    public String operator;

    /**
     * 操作完成时间
     */
    @XmlElement(nillable = false)
    public String confirmTime;

    /**
     * 操作完成时间
     */
    @XmlElement(name = "MailNo")
    public String mailNo;

    /**
     * 出库单类型
     */
    @XmlElement(nillable = false)
    public Integer stockoutOrderType;

    public SkuDetail getSkuDetail() {
        return skuDetail;
    }

    public void setSkuDetail(SkuDetail skuDetail) {
        this.skuDetail = skuDetail;
    }

    public ClearanceDetail getClearanceDetail() {
        if (clearanceDetail == null) {
            clearanceDetail = new ClearanceDetail();
        }
        return clearanceDetail;
    }

    public void setClearanceDetail(ClearanceDetail clearanceDetail) {
        this.clearanceDetail = clearanceDetail;
    }

    public PaymentDetail getPaymentDetail() {
        if (paymentDetail == null) {
            paymentDetail = new PaymentDetail();
        }
        return paymentDetail;
    }

    public void setPaymentDetail(PaymentDetail paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public TradeDetail getTradeDetail() {
        if (tradeDetail == null) {
            tradeDetail = new TradeDetail();
        }
        return tradeDetail;
    }

    public void setTradeDetail(TradeDetail tradeDetail) {
        this.tradeDetail = tradeDetail;
    }

    public LogisticsDetail getLogisticsDetail() {
        if (logisticsDetail == null) {
            logisticsDetail = new LogisticsDetail();
        }
        return logisticsDetail;
    }

    public void setLogisticsDetail(LogisticsDetail logisticsDetail) {
        this.logisticsDetail = logisticsDetail;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getNoType() {
        return noType;
    }

    public void setNoType(String noType) {
        this.noType = noType;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public SupplierDetail getSupplierDetail() {
        return supplierDetail;
    }

    public void setSupplierDetail(SupplierDetail supplierDetail) {
        this.supplierDetail = supplierDetail;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Integer getStockoutOrderType() {
        return stockoutOrderType;
    }

    public void setStockoutOrderType(Integer stockoutOrderType) {
        this.stockoutOrderType = stockoutOrderType;
    }

    public RouteDetail getRouteDetail() {
        return routeDetail;
    }

    public void setRouteDetail(RouteDetail routeDetail) {
        this.routeDetail = routeDetail;
    }

    public OrderQueryRequest getOrderQueryRequest() {
        return orderQueryRequest;
    }

    public void setOrderQueryRequest(OrderQueryRequest orderQueryRequest) {
        this.orderQueryRequest = orderQueryRequest;
    }

    public String getStockinId() {
        return StockinId;
    }

    public void setStockinId(String stockinId) {
        StockinId = stockinId;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    @Override
    public String toString() {
        return "EventBody{" +
                "clearanceDetail=" + clearanceDetail +
                ", paymentDetail=" + paymentDetail +
                ", tradeDetail=" + tradeDetail +
                ", logisticsDetail=" + logisticsDetail +
                ", routeDetail=" + routeDetail +
                ", orderQueryRequest=" + orderQueryRequest +
                ", trackingNo='" + trackingNo + '\'' +
                ", noType='" + noType + '\'' +
                ", customerNo='" + customerNo + '\'' +
                ", skuDetail=" + skuDetail +
                ", supplierDetail=" + supplierDetail +
                ", orderType=" + orderType +
                ", orderNo='" + orderNo + '\'' +
                ", remarks='" + remarks + '\'' +
                ", creator='" + creator + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", operator='" + operator + '\'' +
                ", confirmTime='" + confirmTime + '\'' +
                ", stockoutOrderType=" + stockoutOrderType +
                '}';
    }
}
