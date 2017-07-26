package com.sfebiz.supplychain.exposed.common.enums;

/**
 * <p>单据类型</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/11
 * Time: 上午12:22
 */
public enum BillType {

    ORDER_BILL("ORDER_BILL", "订单"),
    PAY_BILL("PAY_BILL", "支付单"),
    WAY_BILL("WAY_BILL", "物流运单"),
    TWS_BILL("TWS_BILL","中转单"),
    PERSONAL_GOODS_DECLARE_BILL("PERSONAL_GOODS_DECLARE_BILL", "个人物品申报单");

    private String type;
    private String description;

    BillType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
