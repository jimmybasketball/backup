package com.sfebiz.supplychain.protocol.pay.yihuijin;

/**
 * 易汇金报关信息类
 * Created by zhangdi on 2015/9/10.
 */
public class CustomsInfo {
    /**
     * 报关通道,GUANGZHOU
     */
    private String customsChannel;

    /**
     * 报关金额
     */
    private Long amount;

    /**
     * 支付货款
     */
    private Long goodsAmount;

    /**
     * 支付税款
     */
    private Long tax;

    /**
     * 运费
     */
    private Long freight;

    /**
     * 企业备案名称
     */
    private String merchantCommerceName;

    /**
     * 企业备案号
     */
    private String merchantCommerceCode;


    private String dxpid;


    public CustomsInfo() {
    }

    public String getCustomsChannel() {
        return this.customsChannel;
    }

    public void setCustomsChannel(String customsChannel) {
        this.customsChannel = customsChannel;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getGoodsAmount() {
        return this.goodsAmount;
    }

    public void setGoodsAmount(Long goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public Long getTax() {
        return this.tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public Long getFreight() {
        return this.freight;
    }

    public void setFreight(Long freight) {
        this.freight = freight;
    }

    public String getMerchantCommerceName() {
        return merchantCommerceName;
    }

    public void setMerchantCommerceName(String merchantCommerceName) {
        this.merchantCommerceName = merchantCommerceName;
    }

    public String getMerchantCommerceCode() {
        return merchantCommerceCode;
    }

    public void setMerchantCommerceCode(String merchantCommerceCode) {
        this.merchantCommerceCode = merchantCommerceCode;
    }

    public String getDxpid() {
        return dxpid;
    }

    public void setDxpid(String dxpid) {
        this.dxpid = dxpid;
    }
}
