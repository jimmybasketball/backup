package com.sfebiz.supplychain.provider.entity;

/**
 * <p>报文响应状态</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/10
 * Time: 下午2:14
 */
public enum ResponseState {

    ONE("1","成功"),
    SINGLE_TRUE("T", "成功"),
    SUCCESS("SUCCESS", "成功"),
    TRUE("true", "成功"),
    OK("OK", "成功"),
    ORDER_EXIST("B0200", "订单已存在"),
    NET_TIMEOUT("S999", "网络超时");


    private String code;

    private String description;

    ResponseState(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.getCode();
    }
}
