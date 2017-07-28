package com.sfebiz.supplychain.protocol.pay.yihuijin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 易汇金获取联名支付流水号类
 * Created by zhangdi on 2015/9/10.
 */
public class JoinPayOrderBuilder extends BuilderSupport {

    private String merchantId;
    private String requestId;
    private String orderAmount;
    private String orderCurrency;
    private String notifyUrl;
    private String remark;
    private Payer payer;
    private List<ProductDetail> productDetails = new LinkedList();
    private List<CustomsInfo> customsInfos = new LinkedList();

    public JoinPayOrderBuilder(String merchantId) {
        this.merchantId = merchantId;
    }

    public JoinPayOrderBuilder setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
        return this;
    }

    public JoinPayOrderBuilder setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
        return this;
    }

    public JoinPayOrderBuilder setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public JoinPayOrderBuilder setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    public JoinPayOrderBuilder setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public JoinPayOrderBuilder setPayer(Payer payer) {
        this.payer = payer;
        return this;
    }

    public void setProductDetails(List<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public void setCustomsInfos(List<CustomsInfo> customsInfos) {
        this.customsInfos = customsInfos;
    }

    public JoinPayOrderBuilder addProductDetail(ProductDetail productDetail) {
        this.productDetails.add(productDetail);
        return this;
    }

    public JoinPayOrderBuilder addCustomsInfo(CustomsInfo customsInfo) {
        this.customsInfos.add(customsInfo);
        return this;
    }

    public JSONObject build(String signKey) {
        JSONObject json = super.build(this.merchantId);
        if (StringUtils.isNotBlank(this.requestId)) {
            json.put("requestId", this.requestId);
        }

        if (StringUtils.isNotBlank(this.orderAmount)) {
            json.put("orderAmount", this.orderAmount);
        }

        if (StringUtils.isNotBlank(this.orderCurrency)) {
            json.put("orderCurrency", this.orderCurrency);
        }

        if (StringUtils.isNotBlank(this.notifyUrl)) {
            json.put("notifyUrl", this.notifyUrl);
        }

        if (StringUtils.isNotBlank(this.remark)) {
            json.put("remark", this.remark);
        }

        json.put("productDetails", this.productDetails);
        json.put("customsInfos", this.customsInfos);
        json.put("payer", this.payer);
        json.put("hmac", this.generateHmac(signKey));
        return json;
    }

    private String generateHmac(String signKey) {
        StringBuilder hmacSource = new StringBuilder();
        hmacSource.append(StringUtils.defaultString(this.merchantId))
                .append(StringUtils.defaultString(this.requestId, ""))
                .append(StringUtils.defaultString(this.orderAmount, ""))
                .append(StringUtils.defaultString(this.orderCurrency, ""))
                .append(StringUtils.defaultString(this.notifyUrl, ""))
                .append(StringUtils.defaultString(this.remark, ""));
        Iterator iterator;
        if (this.productDetails != null) {
            iterator = this.productDetails.iterator();

            while (iterator.hasNext()) {
                ProductDetail productDetail = (ProductDetail) iterator.next();
                hmacSource.append(StringUtils.defaultString(productDetail.getName()))
                        .append(ObjectUtils.defaultIfNull(productDetail.getQuantity(), ""))
                        .append(ObjectUtils.defaultIfNull(productDetail.getAmount(), ""))
                        .append(StringUtils.defaultString(productDetail.getReceiver()))
                        .append(StringUtils.defaultString(productDetail.getDescription()));
            }
        }

        if (this.payer != null) {
            hmacSource.append(StringUtils.defaultString(this.payer.getName()))
                    //目前易汇金只支持IDCARD一种方式
                    .append(StringUtils.defaultString(this.payer.getIdType()))
                    .append(StringUtils.defaultString(this.payer.getIdNum()))
                    .append(StringUtils.defaultString(this.payer.getCustomerId()))
                            //后四项为非必填项，若不填需设为空字符
                    .append(StringUtils.defaultString(this.payer.getBankCardNum()))
                    .append(StringUtils.defaultString(this.payer.getPhoneNum()))
                    .append(StringUtils.defaultString(this.payer.getEmail()))
                    .append(StringUtils.defaultString(this.payer.getNationality()));
        }

        if (this.customsInfos != null) {
            iterator = this.customsInfos.iterator();
            while (iterator.hasNext()) {
                CustomsInfo customsInfo = (CustomsInfo) iterator.next();
                hmacSource.append(StringUtils.defaultString(customsInfo.getCustomsChannel()))
                        .append(ObjectUtils.defaultIfNull(customsInfo.getAmount(), ""))
                        .append(ObjectUtils.defaultIfNull(customsInfo.getFreight(), ""))
                        .append(ObjectUtils.defaultIfNull(customsInfo.getGoodsAmount(), ""))
                        .append(ObjectUtils.defaultIfNull(customsInfo.getTax(), ""))
                        .append(ObjectUtils.defaultIfNull(customsInfo.getMerchantCommerceName(), ""))
                        .append(ObjectUtils.defaultIfNull(customsInfo.getMerchantCommerceCode(), ""));

            }
        }

        return YihuijinSignUtils.signMd5(hmacSource.toString(), signKey);
    }
}
