package com.sfebiz.supplychain.protocol.pay.yihuijin;

import com.alibaba.fastjson.JSONObject;

/**
 * 构建易汇金请求支持类
 * Created by zhangdi on 2015/9/10.
 */
public class BuilderSupport {

    public BuilderSupport() {
    }

    public JSONObject build(String merchantId) {
        JSONObject json = new JSONObject(true);
        json.put("merchantId", merchantId);
        return json;
    }
}
