package com.sfebiz.supplychain.exposed.common.enums;

/**
 * 响应码
 */
public enum SupplyChainReturnCode {
    SUCCESS(100, "成功"),
    FAIL(101, "失败");

    public int code;
    public String description;

    SupplyChainReturnCode(int code, String description) {
        this.code = code;
        this.description = description;
    }


    @Override
    public String toString() {
        return "SupplyChainReturnCode{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
