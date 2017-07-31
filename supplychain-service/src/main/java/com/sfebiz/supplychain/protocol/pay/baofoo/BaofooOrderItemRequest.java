package com.sfebiz.supplychain.protocol.pay.baofoo;

/**
 * 商品参数
 * User: 不良人 Date:2016/12/14 ProjectName: cbpaygate Version: 1.0
 */
public class BaofooOrderItemRequest {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品单价
     */
    private String goodsPrice;

    /**
     * 商品数量
     */
    private Integer goodsNum;


    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }
}
