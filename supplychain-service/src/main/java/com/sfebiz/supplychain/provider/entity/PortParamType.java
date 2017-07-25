package com.sfebiz.supplychain.provider.entity;

/**
 * <p>海关参数类型</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/3
 * Time: 下午3:17
 */
public enum PortParamType {

    UNIT(0, "商品申报单位"),
    ORIGIN(1, "商品产地"),
    ZIPCODE(99,"全国城市邮编");

    private int value;
    private String description;

    PortParamType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
