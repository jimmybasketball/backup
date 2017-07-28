package com.sfebiz.supplychain.protocol.pay.newyihuijin;

import java.io.Serializable;

public class CreateAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3456435577764174305L;

	private String merchantId;

	private String mobile;

	private String email;

	private String realname;

	private String idNum;

	private String userType;

	private String bindPayment;

	private String accountType;

	private String customerId;

	private String hmac;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getBindPayment() {
		return bindPayment;
	}

	public void setBindPayment(String bindPayment) {
		this.bindPayment = bindPayment;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
}
