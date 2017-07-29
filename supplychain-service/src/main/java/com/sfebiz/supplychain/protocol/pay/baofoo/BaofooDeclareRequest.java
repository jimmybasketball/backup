package com.sfebiz.supplychain.protocol.pay.baofoo;

public class BaofooDeclareRequest {
    /**
     * 版本号
     */
    private String version;
    
    /**
     * 商户备案申请编号
     */
    private String memberApplyNo;
	
    /**
     * 商户编号
     */
    private String memberId;

    /**
     * 终端编号
     */
    private String terminalId;

    /**
     * 商户在宝付支付时的订单号
     */
    private String memberTransId;
    
    /**
     * 商户订单日期
     */
    private String memberTransDate;
    
    /**
     * 海关关区代码
     */
    private String functionId;
    
    /**
     * 电商订单编号
     */
    private String companyOrderNo;
    
    /**
     * 支付总金额
     */
    private String payTotalAmount;
    
    /**
     * 支付货款
     */
    private String payGoodsAmount;
    
    /**
     * 支付税款
     */
    private String payTaxAmount;
    
    /**
     * 支付运费
     */
    private String payFeeAmount;
    
    /**
     * 支付保费
     */
    private String payPreAmount;
    
    /**
     * 币种
     */
    private String ccy;
    
    /**
     * 结果通知地址
     */
    private String notifyUrl;

    /**
     * 商户备案号
     */
    private String companyCode;

    /**
     * 备注
     */
    private String remarks;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMemberApplyNo() {
		return memberApplyNo;
	}

	public void setMemberApplyNo(String memberApplyNo) {
		this.memberApplyNo = memberApplyNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMemberTransId() {
		return memberTransId;
	}

	public void setMemberTransId(String memberTransId) {
		this.memberTransId = memberTransId;
	}

	public String getMemberTransDate() {
		return memberTransDate;
	}

	public void setMemberTransDate(String memberTransDate) {
		this.memberTransDate = memberTransDate;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getCompanyOrderNo() {
		return companyOrderNo;
	}

	public void setCompanyOrderNo(String companyOrderNo) {
		this.companyOrderNo = companyOrderNo;
	}

	public String getPayTotalAmount() {
		return payTotalAmount;
	}

	public void setPayTotalAmount(String payTotalAmount) {
		this.payTotalAmount = payTotalAmount;
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

	public String getPayPreAmount() {
		return payPreAmount;
	}

	public void setPayPreAmount(String payPreAmount) {
		this.payPreAmount = payPreAmount;
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
