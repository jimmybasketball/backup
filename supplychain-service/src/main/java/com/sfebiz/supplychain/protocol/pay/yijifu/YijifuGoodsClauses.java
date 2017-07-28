package com.sfebiz.supplychain.protocol.pay.yijifu;

/**
 * 易极付商品信息类
 *
 * Created by tanzx on 2017/4/24.
 */
public class YijifuGoodsClauses {

    /** 商品的外部ID */
    private String outId;
    /** 商品名称 */
    private String name;
    /** 商品详情 */
    private String memo;
    /** 商品单价 */
    private String price;
    /** 商品数量 */
    private String quantity;
    /** 商品其它费用 */
    private String otherFee;
    /** 商品单位 */
    private String unit;
    /** 商品描述网址 */
    private String detailUrl;
    /** 商品来源网址 */
    private String referUrl;
    /** 商品类目 */
    private String category;
    public String getOutId() {
        return outId;
    }
    public void setOutId(String outId) {
        this.outId = outId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getOtherFee() {
        return otherFee;
    }
    public void setOtherFee(String otherFee) {
        this.otherFee = otherFee;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getDetailUrl() {
        return detailUrl;
    }
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
    public String getReferUrl() {
        return referUrl;
    }
    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
