package com.sfebiz.supplychain.persistence.base.stockout.domain;

/**
 * 创建日期: 2015-04-27
 *
 * @author: jackiehff
 */
public class ExportedShipOrderDO extends StockoutOrderDO {

    private static final long serialVersionUID = 8739152906246915532L;

    /**
     * 出库单金额
     */
    private String price;

    /**
     * 商品编码列表，以特殊字符串分隔
     */
    private String skuIdStr;

    /**
     * 商品名称列表，以特殊字符串分隔
     */
    private String skuNameStr;

    /**
     * 商品重量列表，以特殊字符串分隔
     */
    private String skuWeightStr;

    /**
     * 商品数量列表，以特殊字符串分隔
     */
    private String skuCountStr;
    /**
     * 商品外文名称
     */
    private String skuForeignNameStr;
    /**
     * 条码
     */
    private String remarks;

    /**
     * 购买人姓名
     */
    private String buyerName;
    /**
     * 购买人电话
     */
    private String buyerTelephone;
    /**
     * 购买人地址
     */
    private String buyerAddress;
    /**
     * 发货人姓名
     */
    private String senderName;
    /**
     * 发货人电话
     */
    private String senderTelephone;
    /**
     * 发货人地址
     */
    private String senderAddress;
    /**
     * 原寄地地址
     */
    private String origincode;
    /**
     * 第三方skuId
     */
    private String thirdSkuId;
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getSkuIdStr() {
        return skuIdStr;
    }
    public void setSkuIdStr(String skuIdStr) {
        this.skuIdStr = skuIdStr;
    }
    public String getSkuNameStr() {
        return skuNameStr;
    }
    public void setSkuNameStr(String skuNameStr) {
        this.skuNameStr = skuNameStr;
    }
    public String getSkuWeightStr() {
        return skuWeightStr;
    }
    public void setSkuWeightStr(String skuWeightStr) {
        this.skuWeightStr = skuWeightStr;
    }
    public String getSkuCountStr() {
        return skuCountStr;
    }
    public void setSkuCountStr(String skuCountStr) {
        this.skuCountStr = skuCountStr;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getBuyerName() {
        return buyerName;
    }
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
    public String getBuyerTelephone() {
        return buyerTelephone;
    }
    public void setBuyerTelephone(String buyerTelephone) {
        this.buyerTelephone = buyerTelephone;
    }
    public String getBuyerAddress() {
        return buyerAddress;
    }
    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }
    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getSenderTelephone() {
        return senderTelephone;
    }
    public void setSenderTelephone(String senderTelephone) {
        this.senderTelephone = senderTelephone;
    }
    public String getSenderAddress() {
        return senderAddress;
    }
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
    public String getOrigincode() {
        return origincode;
    }
    public void setOrigincode(String origincode) {
        this.origincode = origincode;
    }
    public String getThirdSkuId() {
        return thirdSkuId;
    }
    public void setThirdSkuId(String thirdSkuId) {
        this.thirdSkuId = thirdSkuId;
    }
    public String getSkuForeignNameStr() {
        return skuForeignNameStr;
    }
    public void setSkuForeignNameStr(String skuForeignNameStr) {
        this.skuForeignNameStr = skuForeignNameStr;
    }
    
}
