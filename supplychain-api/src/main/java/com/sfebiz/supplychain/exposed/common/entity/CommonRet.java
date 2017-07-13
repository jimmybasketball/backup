package com.sfebiz.supplychain.exposed.common.entity;

import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;

import java.io.Serializable;

/**
 * 通用响应结果
 *
 * @author liujc
 * @create 2017-06-29 16:09
 **/
public class CommonRet<T> implements Serializable{


    private static final long serialVersionUID = -7554908803357258034L;
    private T result;

    private Integer retCode = SCReturnCode.COMMON_SUCCESS.getCode();

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

    public CommonRet(String retMsg) {
        this.retMsg = retMsg;
    }

    public CommonRet(Integer retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public CommonRet(Integer retCode) {
        this.retCode = retCode;
    }

    public CommonRet() {
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
        retCode = SCReturnCode.COMMON_SUCCESS.getCode();
        result = null;
        retMsg = null;
    }
}
