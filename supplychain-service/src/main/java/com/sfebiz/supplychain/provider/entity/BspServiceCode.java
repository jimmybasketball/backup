package com.sfebiz.supplychain.provider.entity;

/**
 * <p>对接BSP时，BSP提供的接口服务名称</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/11
 * Time: 下午5:37
 */
public class BspServiceCode {

    //订单结果查询
    public static final String ORDER_SEARCH_SERVICE = "OrderSearchService";

    //订单创建
    public static final String ORDER_SERVICE = "OrderService";

    //订单发货 或 订单取消（参数不一样）
    public static final String ORDER_SENDER_OR_CANCEL = "OrderConfirmService";

    //获取订单路由信息
    public static final String ORDER_ROUTE = "RouteService";


}
