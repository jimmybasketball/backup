package com.sfebiz.supplychain.protocol.pay.yihuijin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhangdi on 2015/9/16.
 */
public class DeclareQueryBuilder extends BuilderSupport {

    private String merchantId;
    private String paySerialNumber;

    public DeclareQueryBuilder(String merchantId) {
        this.merchantId = merchantId;
    }

    public DeclareQueryBuilder setPaySerialNumber(String paySerialNumber) {
        this.paySerialNumber = paySerialNumber;
        return this;
    }

    public JSONObject build(String sign) {
        JSONObject json = super.build(this.merchantId);
        if(StringUtils.isNotBlank(this.paySerialNumber)) {
            json.put("paySerialNumber", this.paySerialNumber);
        }
        json.put("hmac", sign);
        return json;
    }

}
