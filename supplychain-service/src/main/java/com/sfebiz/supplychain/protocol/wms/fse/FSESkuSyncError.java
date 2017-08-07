package com.sfebiz.supplychain.protocol.wms.fse;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/8.
 */
public class FSESkuSyncError implements Serializable{
    private static final long serialVersionUID = -5899253330778318561L;

    /**
     * 商品编码
     */
    public String code;

    /**
     * 错误提示信息
     */
    public String errorMsg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "FSESkuSyncError{" +
                "code='" + code + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
