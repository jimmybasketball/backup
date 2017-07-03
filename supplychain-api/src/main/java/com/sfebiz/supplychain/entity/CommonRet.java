package com.sfebiz.supplychain.entity;

import com.sfebiz.supplychain.enums.SupplyChainReturnCode;
import com.sfebiz.supplychain.util.JSONUtil;

/**
 * 通用响应结果
 *
 * @author liujc
 * @create 2017-06-29 16:09
 **/
public class CommonRet<T> {

    private T result;

    private Integer retCode = SupplyChainReturnCode.SUCCESS.code;

    private String retMsg;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    @Override
    public String toString() {
        return "CommonRet{" +
                "result=" + result +
                ", retCode=" + retCode +
                ", retMsg='" + retMsg + '\'' +
                '}';
    }

    public void reSet() {
        retCode = SupplyChainReturnCode.SUCCESS.code;
        result = null;
        retMsg = null;
    }

    public String toJsonString() {
        return JSONUtil.toJson(this);
    }
}
