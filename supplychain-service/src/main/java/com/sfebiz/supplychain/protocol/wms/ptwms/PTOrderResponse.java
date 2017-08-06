package com.sfebiz.supplychain.protocol.wms.ptwms;

/**
 * Created by TT on 2016/7/28.
 */
public class PTOrderResponse {
    /**
     * 响应标志
     */
    private String ask;

    /**
     * 消息提示
     */
    private String message;


    private String order_code;

    /**
     * 错误信息
     */
    private StockoutError error;

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StockoutError getError() {
        return error;
    }

    public void setError(StockoutError error) {
        this.error = error;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "ask='" + ask + '\'' +
                ", message='" + message + '\'' +
                ", order_code='" + order_code + '\'' +
                ", error=" + error +
                '}';
    }
}
