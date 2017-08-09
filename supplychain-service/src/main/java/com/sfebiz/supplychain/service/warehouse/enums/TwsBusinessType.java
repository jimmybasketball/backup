package com.sfebiz.supplychain.service.warehouse.enums;

/**
 * <p>中转业务类型</p>
 *  zhangdi
 */
public enum TwsBusinessType {

    TWS_CREATE("TWS_CREATE", "订单创建");

    /**
     * 类型值
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    TwsBusinessType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return type;
    }
}
