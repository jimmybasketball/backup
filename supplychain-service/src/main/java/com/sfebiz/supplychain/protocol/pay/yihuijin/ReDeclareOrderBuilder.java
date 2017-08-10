package com.sfebiz.supplychain.protocol.pay.yihuijin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhangdi on 2015/9/16.
 */
public class ReDeclareOrderBuilder extends BuilderSupport {

    /**
     * 商户编号
     */
    private String merchantId;
    /**
     * 支付流水号
     */
    private String paySerialNumber;
    /**
     * 海关名称
     */
    private String customsChannels;

    public ReDeclareOrderBuilder(String merchantId) {
        this.merchantId = merchantId;
    }

    public ReDeclareOrderBuilder setPaySerialNumber(String paySerialNumber) {
        this.paySerialNumber = paySerialNumber;
        return this;
    }

    public void setCustomsChannels(String customsChannels) {
        this.customsChannels = customsChannels;
    }

    public JSONObject build(String sign) {
        JSONObject json = super.build(this.merchantId);
        if(StringUtils.isNotBlank(this.paySerialNumber)) {
            json.put("paySerialNumber", this.paySerialNumber);
        }
        if(StringUtils.isNotBlank(this.customsChannels)) {
            json.put("customsChannels", this.customsChannels);
        }
        json.put("hmac", sign);
        return json;
    }

}
