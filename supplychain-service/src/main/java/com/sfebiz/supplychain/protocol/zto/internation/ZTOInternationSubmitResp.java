package com.sfebiz.supplychain.protocol.zto.internation;

/**
 * Created by zhangdi on 2015/11/17.
 */
public class ZTOInternationSubmitResp {

    private String result;

    private ZTOSubmitRespKeys keys;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ZTOSubmitRespKeys getKeys() {
        return keys;
    }

    public void setKeys(ZTOSubmitRespKeys keys) {
        this.keys = keys;
    }
}
