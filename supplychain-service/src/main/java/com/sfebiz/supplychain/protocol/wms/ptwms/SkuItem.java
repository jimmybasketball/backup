package com.sfebiz.supplychain.protocol.wms.ptwms;

/**
 * Created by TT on 2016/7/5.
 */
public class SkuItem {

    /**
     * sku
     */
    private String product_sku;

    /**
     * 数量
     */
    private Integer quantity;

    public String getProduct_sku() {
        return product_sku;
    }

    public void setProduct_sku(String product_sku) {
        this.product_sku = product_sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "SkuItem{" +
                "product_sku='" + product_sku + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
