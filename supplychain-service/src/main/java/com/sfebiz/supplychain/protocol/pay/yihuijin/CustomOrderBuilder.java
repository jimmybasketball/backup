package com.sfebiz.supplychain.protocol.pay.yihuijin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhangdi on 2015/9/14.
 */
public class CustomOrderBuilder extends BuilderSupport {
    private String merchantId;
    private String paySerialNumber;
    private String notifyUrl;
    private List<CustomsInfo> customsInfos = new LinkedList();

    public CustomOrderBuilder(String merchantId) {
        this.merchantId = merchantId;
    }

    public CustomOrderBuilder setPaySerialNumber(String paySerialNumber) {
        this.paySerialNumber = paySerialNumber;
        return this;
    }

    public CustomOrderBuilder setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    public void setCustomsInfos(List<CustomsInfo> customsInfos) {
        this.customsInfos = customsInfos;
    }

    public CustomOrderBuilder addCustomsInfo(CustomsInfo customsInfo) {
        this.customsInfos.add(customsInfo);
        return this;
    }

    public JSONObject build(String sign) {
        JSONObject json = super.build(this.merchantId);
        if (StringUtils.isNotBlank(this.paySerialNumber)) {
            json.put("paySerialNumber", this.paySerialNumber);
        }

        if (StringUtils.isNotBlank(this.notifyUrl)) {
            json.put("notifyUrl", this.notifyUrl);
        }

        json.put("customsInfos", this.customsInfos);
        json.put("hmac", sign);
        return json;
    }

}
