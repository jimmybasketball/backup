package com.sfebiz.supplychain.protocol.wms.ptwms;

/**
 * Created by TT on 2016/7/5.
 */
public class StockoutError {

    /**
     * 错误内容
     */
    private String errMessage;

    /**
     * 错误码
     */
    private String errCode;

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}
