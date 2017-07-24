package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"eCommerceCode", "eCommerceName", "ieFlag", "payType",
        "payCompanyCode", "payNumber", "orderTotalAmount", "orderNo",
        "orderTaxAmount", "orderGoodsAmount", "feeAmount", "companyName",
        "companyCode", "tradeTime", "currCode", "totalAmount", "insureAmount",
        "consigneeEmail", "consigneeTel", "consignee", "consigneeAddress",
        "totalCount", "postMode", "senderCountry", "senderName", "purchaserId",
        "logisCompanyName", "logisCompanyCode", "zipCode", "note", "wayBills","userProcotol","discount","payCompanyName"})
@XmlRootElement(name = "jkfSign")
public class JKFOrderImportHead {
    @XmlElement(name = "eCommerceCode")
    private String eCommerceCode;
    @XmlElement(name = "eCommerceName")
    private String eCommerceName;
    @XmlElement(name = "ieFlag")
    private String ieFlag;
    /**
     * 支付类型
     * 01:银行卡支付
     * 02:余额支付
     * 03:其他
     */
    @XmlElement(name = "payType")
    private String payType;

    @XmlElement(name = "payCompanyCode")
    private String payCompanyCode;

    @XmlElement(name = "payCompanyName")
    private String payCompanyName;

    @XmlElement(name = "payNumber")
    private String payNumber;

    /**
     * 订单总金额,货款+订单税款+运费
     */
    @XmlElement(name = "orderTotalAmount")
    private String orderTotalAmount;

    @XmlElement(name = "orderNo")
    private String orderNo;

    /**
     * 订单税款
     */
    @XmlElement(name = "orderTaxAmount")
    private String orderTaxAmount;

    /**
     * 订单货款
     */
    @XmlElement(name = "orderGoodsAmount")
    private String orderGoodsAmount;

    /**
     * 订单运费
     */
    @XmlElement(name = "feeAmount")
    private String feeAmount;

    /**
     * 保费
     */
    @XmlElement(name = "insureAmount")
    private String insureAmount;

    @XmlElement(name = "companyName")
    private String companyName;

    @XmlElement(name = "companyCode")
    private String companyCode;

    @XmlElement(name = "tradeTime")
    private String tradeTime;

    /**
     * 成交币制
     * 人民币：142
     */
    @XmlElement(name = "currCode")
    private String currCode;

    /**
     * 成交总价: “订单总金额”扣除“折扣”之后的金额
     */
    @XmlElement(name = "totalAmount")
    private String totalAmount;

    @XmlElement(name = "consigneeEmail")
    private String consigneeEmail;

    @XmlElement(name = "consigneeTel")
    private String consigneeTel;

    @XmlElement(name = "consignee")
    private String consignee;

    @XmlElement(name = "consigneeAddress")
    private String consigneeAddress;

    @XmlElement(name = "totalCount")
    private int totalCount;

    /**
     * 发货方式（物流方式）
     * 邮政小包: 1
     * 快件: 2
     * EMS: 3
     */
    @XmlElement(name = "postMode")
    private String postMode;

    @XmlElement(name = "senderCountry")
    private String senderCountry;

    @XmlElement(name = "senderName")
    private String senderName;

    @XmlElement(name = "purchaserId")
    private String purchaserId;

    @XmlElement(name = "logisCompanyName")
    private String logisCompanyName;

    @XmlElement(name = "logisCompanyCode")
    private String logisCompanyCode;

    @XmlElement(name = "zipCode")
    private String zipCode;

    @XmlElement(name = "note")
    private String note;

    @XmlElement(name = "wayBills")
    private String wayBills;

    @XmlElement(name = "userProcotol")
    private String userProcotol;

    @XmlElement(name = "discount")
    private String discount;

    public String geteCommerceCode() {
        return eCommerceCode;
    }

    public void seteCommerceCode(String eCommerceCode) {
        this.eCommerceCode = eCommerceCode;
    }

    public String geteCommerceName() {
        return eCommerceName;
    }

    public void seteCommerceName(String eCommerceName) {
        this.eCommerceName = eCommerceName;
    }

    public String getIeFlag() {
        return ieFlag;
    }

    public void setIeFlag(String ieFlag) {
        this.ieFlag = ieFlag;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderTaxAmount() {
        return orderTaxAmount;
    }

    public void setOrderTaxAmount(String orderTaxAmount) {
        this.orderTaxAmount = orderTaxAmount;
    }

    public String getOrderGoodsAmount() {
        return orderGoodsAmount;
    }

    public void setOrderGoodsAmount(String orderGoodsAmount) {
        this.orderGoodsAmount = orderGoodsAmount;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public String getConsigneeEmail() {
        return consigneeEmail;
    }

    public void setConsigneeEmail(String consigneeEmail) {
        this.consigneeEmail = consigneeEmail;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getPostMode() {
        return postMode;
    }

    public void setPostMode(String postMode) {
        this.postMode = postMode;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getPurchaserId() {
        return purchaserId;
    }

    public void setPurchaserId(String purchaserId) {
        this.purchaserId = purchaserId;
    }

    public String getLogisCompanyName() {
        return logisCompanyName;
    }

    public void setLogisCompanyName(String logisCompanyName) {
        this.logisCompanyName = logisCompanyName;
    }

    public String getLogisCompanyCode() {
        return logisCompanyCode;
    }

    public void setLogisCompanyCode(String logisCompanyCode) {
        this.logisCompanyCode = logisCompanyCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getWayBills() {
        return wayBills;
    }

    public void setWayBills(String wayBills) {
        this.wayBills = wayBills;
    }

    public String getUserProcotol() {
        return userProcotol;
    }

    public void setUserProcotol(String userProcotol) {
        this.userProcotol = userProcotol;
    }

    public String getInsureAmount() {
        return insureAmount;
    }

    public void setInsureAmount(String insureAmount) {
        this.insureAmount = insureAmount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPayCompanyName() {
        return payCompanyName;
    }

    public void setPayCompanyName(String payCompanyName) {
        this.payCompanyName = payCompanyName;
    }


}
