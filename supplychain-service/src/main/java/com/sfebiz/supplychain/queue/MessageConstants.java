package com.sfebiz.supplychain.queue;

/**
 * @description:  消息队列信息
 * @author yangh [yangh@ifunq.com]
 * @date 2017/7/25 9:32
 */

public class MessageConstants {

    /**
     * 设置消息内容为空，(解决ONS BODY不能为空的问题)
     */
    public static String       EMPTY_BODY                 = " ";

    /**
     * 定义供应链 TOPIC
     */
    public static final String TOPIC_SUPPLY_CHAIN_EVENT   = "SUPPLY_CHAIN_EVENT";

    /**
     * 定义供应链 开放订单路由事件 TOPIC
     */
    public static final String TOPIC_OPEN_WMS_ROUTE_EVENT = "OPEN_WMS_ROUTE_EVENT";

    /**
     * 商品库存信息更新，异步刷新reids
     */
    public static final String TAG_REDIS_SKU_STOCK_INFO   = "TAG_REDIS_SKU_STOCK_INFO";
}
