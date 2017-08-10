package com.sfebiz.supplychain.protocol.pay.yihuijin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 易汇金查询联名支付号类
 * Created by zhangdi on 2015/9/15.
 */
public class JoinPayQueryBuilder extends BuilderSupport {

    private String merchantId;
    private String requestId;

    public JoinPayQueryBuilder(String merchantId) {
        this.merchantId = merchantId;
    }

    public JoinPayQueryBuilder setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public JSONObject build(String sign) {
        JSONObject json = super.build(this.merchantId);
        if(StringUtils.isNotBlank(this.requestId)) {
            json.put("requestId", this.requestId);
        }

        json.put("hmac", sign);
        return json;
    }

}
