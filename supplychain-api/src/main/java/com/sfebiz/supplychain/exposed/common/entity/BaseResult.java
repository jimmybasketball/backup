package com.sfebiz.supplychain.exposed.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>业务处理基础结果</p>
 * User: cai zhengjie
 */
public class BaseResult implements Serializable {

    public static final BaseResult SUCCESS_RESULT = new BaseResult(true);
    private static final long serialVersionUID = 7555748230701587455L;

    /**
     * 是否处理成功
     */
    protected boolean isSuccess = false;

    /**
     * 错误代码
     */
    protected String resultCode;

    /**
     * 错误信息
     */
    protected String resultMessage;


    /**
     * 结果对象
     */
    protected Map<String, String> resultMap = new HashMap<String, String>();

    /**
     * 空构造函数
     */
    public BaseResult() {
    }

    /**
     * 构造函数
     *
     * @param isSuccess 处理结果是否成功
     */
    public BaseResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * 构造函数
     *
     * @param isSuccess     操作是否成功
     * @param resultMessage 结果描述信息
     */
    public BaseResult(boolean isSuccess, String resultMessage) {
        this.isSuccess = isSuccess;
        this.resultMessage = resultMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Map<String, String> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, String> resultMap) {
        this.resultMap = resultMap;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "isSuccess=" + isSuccess +
                ", resultCode='" + resultCode + '\'' +
                ", resultMessage='" + resultMessage + '\'' +
                ", resultMap=" + resultMap +
                '}';
    }
}
