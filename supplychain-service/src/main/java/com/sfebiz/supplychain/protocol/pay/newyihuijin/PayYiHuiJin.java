package com.sfebiz.supplychain.protocol.pay.newyihuijin;

import java.io.Serializable;
import java.util.List;

public class PayYiHuiJin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4778887535642608743L;

	private String merchantId;

	private String requestId;

	private String payerMember;

	private long amount;

	private String currency;

	private List<ProductDetails> productDetails;

	private String hmac;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getPayerMember() {
		return payerMember;
	}

	public void setPayerMember(String payerMember) {
		this.payerMember = payerMember;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<ProductDetails> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetails> productDetails) {
		this.productDetails = productDetails;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
	}

}
