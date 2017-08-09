package com.sfebiz.supplychain.service.port.model;

/**
 * <p>报关业务类型</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/4
 * Time: 下午5:04
 */
public enum CustomsBusinessType {

    ORDER_CREATE("ORDER_CREATE", "订单创建"), ORDER_CONFIRM("ORDER_CONFIRM", "订单确认");

    /**
     * 类型值
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    CustomsBusinessType(String type, String description) {
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
