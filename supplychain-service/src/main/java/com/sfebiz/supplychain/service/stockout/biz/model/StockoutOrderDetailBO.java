package com.sfebiz.supplychain.service.stockout.biz.model;

import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.sku.model.SkuMerchantBO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * <p>出库单明细实体</p>
 *
 * @author matt
 * @Date 2017年7月18日 上午10:39:12
 */
public class StockoutOrderDetailBO extends BaseBO {

    /** 序号 */
    private static final long serialVersionUID = -5402329124067594489L;

    /** 出库单主键ID */
    private Long              stockoutOrderId;

    /** 货主供应商ID */
    private Long              merchantProviderId;

    /** 商品ID */
    private Long              skuId;

    /** 商户侧skuId */
    private Long              merchantSkuId;

    /** 商品条码 */
    private String            skuBarcode;

    /** 批次号 */
    private String            skuBatch;

    /** 数量 */
    private Integer           quantity;

    /** 商户商品费用，单位分 */
    private Integer           merchantPrice;

    /** 商户折扣费用，单位分 */
    private Integer           merchantDiscountPrice;

    /** 运费，单位分 */
    private Integer           freightFee;

    /** 币种 */
    private String            currency;

    /** 商品名称 */
    private String            skuName;

    /** 商品面单名称 */
    private String            skuBillName;

    /** 商品外文名称 */
    private String            skuForeignName;

    /** 商品 */
    private String            brandName;

    /** 商品净重，单位g */
    // TODO
    private Long              weight;

    /** 商品出库状态 */
    private Long              outState;

    /** 入库单ID */
    private Long              stockinOrderId;

    /** 备注 */
    private String            remark;

    /** 商品备案信息业务实体 */
    private SkuDeclareBO skuDeclareBO;

    /** 货主商品信息业务实体 */
    private SkuMerchantBO     skuMerchantBO;

    public Long getStockoutOrderId() {
        return stockoutOrderId;
    }

    public void setStockoutOrderId(Long stockoutOrderId) {
        this.stockoutOrderId = stockoutOrderId;
    }

    public Long getMerchantProviderId() {
        return merchantProviderId;
    }

    public void setMerchantProviderId(Long merchantProviderId) {
        this.merchantProviderId = merchantProviderId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getMerchantSkuId() {
        return merchantSkuId;
    }

    public void setMerchantSkuId(Long merchantSkuId) {
        this.merchantSkuId = merchantSkuId;
    }

    public String getSkuBarcode() {
        return skuBarcode;
    }

    public void setSkuBarcode(String skuBarcode) {
        this.skuBarcode = skuBarcode;
    }

    public String getSkuBatch() {
        return skuBatch;
    }

    public void setSkuBatch(String skuBatch) {
        this.skuBatch = skuBatch;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMerchantPrice() {
        return merchantPrice;
    }

    public void setMerchantPrice(Integer merchantPrice) {
        this.merchantPrice = merchantPrice;
    }

    public Integer getMerchantDiscountPrice() {
        return merchantDiscountPrice;
    }

    public void setMerchantDiscountPrice(Integer merchantDiscountPrice) {
        this.merchantDiscountPrice = merchantDiscountPrice;
    }

    public Integer getFreightFee() {
        return freightFee;
    }

    public void setFreightFee(Integer freightFee) {
        this.freightFee = freightFee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuBillName() {
        return skuBillName;
    }

    public void setSkuBillName(String skuBillName) {
        this.skuBillName = skuBillName;
    }

    public String getSkuForeignName() {
        return skuForeignName;
    }

    public void setSkuForeignName(String skuForeignName) {
        this.skuForeignName = skuForeignName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getOutState() {
        return outState;
    }

    public void setOutState(Long outState) {
        this.outState = outState;
    }

    public Long getStockinOrderId() {
        return stockinOrderId;
    }

    public void setStockinOrderId(Long stockinOrderId) {
        this.stockinOrderId = stockinOrderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SkuDeclareBO getSkuDeclareBO() {
        return skuDeclareBO;
    }

    public void setSkuDeclareBO(SkuDeclareBO skuDeclareBO) {
        this.skuDeclareBO = skuDeclareBO;
    }

    public SkuMerchantBO getSkuMerchantBO() {
        return skuMerchantBO;
    }

    public void setSkuMerchantBO(SkuMerchantBO skuMerchantBO) {
        this.skuMerchantBO = skuMerchantBO;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
