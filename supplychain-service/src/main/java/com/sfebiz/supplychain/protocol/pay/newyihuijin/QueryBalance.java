package com.sfebiz.supplychain.protocol.pay.newyihuijin;

import java.io.Serializable;

public class QueryBalance implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4530715060872272138L;

	
	private String merchantId;
	
	private String memberId;
	
	private String hmac;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
}
