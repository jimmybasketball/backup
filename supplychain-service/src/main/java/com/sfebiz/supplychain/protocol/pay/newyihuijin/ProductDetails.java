package com.sfebiz.supplychain.protocol.pay.newyihuijin;

import java.io.Serializable;

public class ProductDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5224341724390978818L;

	private String name;

	private long quantity;

	private long amount;

	private String receiver;

	private String description;

	private String productDetails;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(String productDetails) {
		this.productDetails = productDetails;
	}
}
