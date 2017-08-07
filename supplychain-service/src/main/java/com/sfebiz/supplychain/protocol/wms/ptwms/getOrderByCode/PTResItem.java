package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderByCode;

public class PTResItem {

	public String product_sku;

	public int quantity;

	public String getProduct_sku() {
		return product_sku;
	}

	public void setProduct_sku(String product_sku) {
		this.product_sku = product_sku;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
