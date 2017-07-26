package com.sfebiz.supplychain.sdk.protocol;

import java.io.Serializable;

/**
 * 事件类型
 */
public class EventType implements Serializable {

    public static final EventType LOGISTICS_TRADE_PAID = new EventType("LOGISTICS_TRADE_PAID", "");
    public static final EventType LOGISTICS_SKU_PAID = new EventType("LOGISTICS_SKU_PAID", "");
    public static final EventType WMS_CHECK_ORDER = new EventType("WMS_CHECK_ORDER", "");
    public static final EventType WMS_STOCKIN_INFO = new EventType("WMS_STOCKIN_INFO", "");
    public static final EventType WMS_GOODS_WEIGHT = new EventType("WMS_GOODS_WEIGHT", "");
    public static final EventType WMS_INNER_EXCEPTION = new EventType("WMS_INNER_EXCEPTION", "");
    public static final EventType LOGISTICS_SEND_GOODS = new EventType("LOGISTICS_SEND_GOODS", "");
    public static final EventType LOGISTICS_SEND_SKU = new EventType("LOGISTICS_SEND_SKU", "");
    public static final EventType LOGISTICS_SKU_REPAID = new EventType("LOGISTICS_SKU_REPAID", "");
    public static final EventType LOGISTICS_CANCEL = new EventType("LOGISTICS_CANCEL", "");
    public static final EventType LOGISTICS_STOCKIN_CANCEL = new EventType("LOGISTICS_STOCKIN_CANCEL", "");
    public static final EventType LOGISTICS_TMS_CONFIRM_DUTYS = new EventType("LOGISTICS_TMS_CONFIRM_DUTYS", "");
    public static final EventType LOGISTICS_WMS_DISSENT_INFO = new EventType("LOGISTICS_WMS_DISSENT_INFO", "");
    public static final EventType LOGISTICS_BUYER_REFUND = new EventType("LOGISTICS_BUYER_REFUND", "");
    public static final EventType WMS_STOCKOUT_INFO = new EventType("WMS_STOCKOUT_INFO", "");
    public static final EventType TMS_CLEAR_IDENTITY_INFO = new EventType("TMS_CLEAR_IDENTITY_INFO", "");
    public static final EventType LOGISTICS_SKU_STOCKIN_INFO = new EventType("LOGISTICS_SKU_STOCKIN_INFO", "");
    public static final EventType LOGISTICS_SKU_RE_STOCKIN_INFO = new EventType("LOGISTICS_SKU_RE_STOCKIN_INFO", "");
    public static final EventType LOGISTICS_SKU_STOCKIN_CANCEL = new EventType("LOGISTICS_SKU_STOCKIN_CANCEL", "");
    public static final EventType WMS_SKU_STOCKIN_INFO = new EventType("WMS_SKU_STOCKIN_INFO", "");
    public static final EventType WMS_CHECK_GOODS = new EventType("WMS_CHECK_GOODS", "");
    public static final EventType WMS_ASN_CANCEL = new EventType("WMS_ASN_CANCEL", "退货");
    public static final EventType LOGISTICS_SKU_STOCKIN_FINISH = new EventType("LOGISTICS_SKU_STOCKIN_FINISH", "");
    public static final EventType COE_ETK_ORDER_TRACK = new EventType("COE_ETK_ORDER_TRACK", "");
    public static final EventType LOGISTICS_SKU_SYNC = new EventType("LOGISTICS_SKU_SYNC","商品同步");
    public static final EventType LOGISTICS_SUPPLIER_SYNC = new EventType("LOGISTICS_SUPPLIER_SYNC","供应商同步");
    public static final EventType LOGISTICS_STOCK_SEARCH = new EventType("LOGISTICS_STOCK_SEARCH","库存查询");
    public static final EventType WMS_GALORDER_CREATE = new EventType("WMS_GALORDER_CREATE","损溢单创建");
    public static final EventType WMS_GALORDER_CONFIRM = new EventType("WMS_GALORDER_CONFIRM","损溢单确认");
    public static final EventType WMS_ROUTE_INFO = new EventType("WMS_ROUTE_INFO", "仓库回传包裹路由信息");
    public static final EventType WMS_SKU_STOCK_INFO = new EventType("WMS_SKU_STOCK_INFO", "仓库同步商品库存");
    public static final EventType LOGISTICS_QUERY_ORDER = new EventType("LOGISTICS_QUERY_ORDER", "供应商查询出库单");
    public static final EventType LOGISTICS_QUERY_ORDER_PDF = new EventType("LOGISTICS_QUERY_ORDER_PDF", "供应商查询出库单面单");

    private static final long serialVersionUID = -1971335754168256811L;
    public String value;

    private String description;

    public EventType(String eventType, String description) {
        value = eventType;
    }

    public String value() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
