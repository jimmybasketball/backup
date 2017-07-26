package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"orderNo", "eCommerceCode", "payTransactionNo", "payAmount",
        "payGoodsAmount", "payTaxAmount", "payFeeAmount", "payTimeStr",
        "currCode", "payEnterpriseName", "payCompanyCode", "bankName",
        "bankNo", "payAccount", "payerName", "paperType",
        "paperNumber", "payerPhoneNumber", "payMerchantCode"})
@XmlRootElement(name = "jkfImportPay")
public class JKFImportPay {

    /**
     * 订单编号 ,必填
     */
    @XmlElement(name = "orderNo")
    private String orderNo;

    /**
     * 电商平台备案编号 ,必填
     */
    @XmlElement(name = "eCommerceCode")
    private String eCommerceCode;

    /**
     * 支付交易号 ,必填
     */
    @XmlElement(name = "payTransactionNo")
    private String payTransactionNo;

    /**
     * 支付金额 ,必填
     */
    @XmlElement(name = "payAmount")
    private String payAmount;

    /**
     * 支付货款 ,必填
     */
    @XmlElement(name = "payGoodsAmount")
    private String payGoodsAmount;

    /**
     * 支付税款 ,必填
     */
    @XmlElement(name = "payTaxAmount")
    private String payTaxAmount;


    /**
     * 支付运费 ,必填
     */
    @XmlElement(name = "payFeeAmount")
    private String payFeeAmount;


    /**
     * 付款时间 ,必填
     */
    @XmlElement(name = "payTimeStr")
    private String payTimeStr;


    /**
     * 币制 ,必填
     */
    @XmlElement(name = "currCode")
    private String currCode;

    /**
     * 支付企业名称 ,必填
     */
    @XmlElement(name = "payEnterpriseName")
    private String payEnterpriseName;


    /**
     * 支付企业代码 ,必填
     */
    @XmlElement(name = "payCompanyCode")
    private String payCompanyCode;

    /**
     * 发卡行 ,选填
     */
    @XmlElement(name = "bankName")
    private String bankName;

    /**
     * 银行卡号 ,选填
     */
    @XmlElement(name = "bankNo")
    private String bankNo;

    /**
     * 支付ID ,选填
     */
    @XmlElement(name = "payAccount")
    private String payAccount;

    /**
     * 支付人姓名 ,必填
     */
    @XmlElement(name = "payerName")
    private String payerName;

    /**
     * 支付人证件类型 ,必填
     */
    @XmlElement(name = "paperType")
    private String paperType;

    /**
     * 支付人证件号码 ,必填
     */
    @XmlElement(name = "paperNumber")
    private String paperNumber;

    /**
     * 支付人手机号 ,选填
     */
    @XmlElement(name = "payerPhoneNumber")
    private String payerPhoneNumber;

    /**
     * 支付商家编号 ,选填
     */
    @XmlElement(name = "payMerchantCode")
    private String payMerchantCode;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String geteCommerceCode() {
        return eCommerceCode;
    }

    public void seteCommerceCode(String eCommerceCode) {
        this.eCommerceCode = eCommerceCode;
    }

    public String getPayTransactionNo() {
        return payTransactionNo;
    }

    public void setPayTransactionNo(String payTransactionNo) {
        this.payTransactionNo = payTransactionNo;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayGoodsAmount() {
        return payGoodsAmount;
    }

    public void setPayGoodsAmount(String payGoodsAmount) {
        this.payGoodsAmount = payGoodsAmount;
    }

    public String getPayTaxAmount() {
        return payTaxAmount;
    }

    public void setPayTaxAmount(String payTaxAmount) {
        this.payTaxAmount = payTaxAmount;
    }

    public String getPayFeeAmount() {
        return payFeeAmount;
    }

    public void setPayFeeAmount(String payFeeAmount) {
        this.payFeeAmount = payFeeAmount;
    }

    public String getPayTimeStr() {
        return payTimeStr;
    }

    public void setPayTimeStr(String payTimeStr) {
        this.payTimeStr = payTimeStr;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public String getPayEnterpriseName() {
        return payEnterpriseName;
    }

    public void setPayEnterpriseName(String payEnterpriseName) {
        this.payEnterpriseName = payEnterpriseName;
    }

    public String getPayCompanyCode() {
        return payCompanyCode;
    }

    public void setPayCompanyCode(String payCompanyCode) {
        this.payCompanyCode = payCompanyCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
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

    public String getPayerPhoneNumber() {
        return payerPhoneNumber;
    }

    public void setPayerPhoneNumber(String payerPhoneNumber) {
        this.payerPhoneNumber = payerPhoneNumber;
    }

    public String getPayMerchantCode() {
        return payMerchantCode;
    }

    public void setPayMerchantCode(String payMerchantCode) {
        this.payMerchantCode = payMerchantCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JKFImportPay{");
        sb.append("orderNo='").append(orderNo).append('\'');
        sb.append(", eCommerceCode='").append(eCommerceCode).append('\'');
        sb.append(", payTransactionNo='").append(payTransactionNo).append('\'');
        sb.append(", payAmount='").append(payAmount).append('\'');
        sb.append(", payGoodsAmount='").append(payGoodsAmount).append('\'');
        sb.append(", payTaxAmount='").append(payTaxAmount).append('\'');
        sb.append(", payFeeAmount='").append(payFeeAmount).append('\'');
        sb.append(", payTimeStr='").append(payTimeStr).append('\'');
        sb.append(", currCode='").append(currCode).append('\'');
        sb.append(", payEnterpriseName='").append(payEnterpriseName).append('\'');
        sb.append(", payCompanyCode='").append(payCompanyCode).append('\'');
        sb.append(", bankName='").append(bankName).append('\'');
        sb.append(", bankNo='").append(bankNo).append('\'');
        sb.append(", payAccount='").append(payAccount).append('\'');
        sb.append(", payerName='").append(payerName).append('\'');
        sb.append(", paperType='").append(paperType).append('\'');
        sb.append(", paperNumber='").append(paperNumber).append('\'');
        sb.append(", payerPhoneNumber='").append(payerPhoneNumber).append('\'');
        sb.append(", payMerchantCode='").append(payMerchantCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
