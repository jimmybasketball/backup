package com.sfebiz.supplychain.service.route.op;

import com.sfebiz.supplychain.exposed.route.entity.LogisticsSystemRouteEntity;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;

import java.util.List;
import java.util.Set;

/**
 * 路由信息操作接口
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017/7/28 12:49
 */
public interface RouteOperation {


    /**
     * 追加用户路由
     *
     * @param logisticsUserRouteEntity 路由实体
     * @param routeType                路由类型
     */
    public void appendUserRoute(LogisticsUserRouteEntity logisticsUserRouteEntity, RouteType routeType);


    /**
     * 覆盖用户路由信息
     *
     * @param orderId                      订单ID
     * @param routeType                    路由类型 RouteType枚举值
     * @param logisticsUserRouteEntityList 路由信息实体集合
     */
    public void overrideUserRoute(String orderId, RouteType routeType, List<LogisticsUserRouteEntity> logisticsUserRouteEntityList);


    /**
     * 根据订单号和路由类型获取路由信息集合
     *
     * @param orderId   订单ID
     * @param routeType 路由类型
     * @return
     */
    public Set<LogisticsUserRouteEntity> getUserRoutesByType(String orderId, RouteType routeType);


    /**
     * 获取该订单的所有用户路由信息
     *
     * @param orderId 订单ID
     * @return 所有路由信息
     */
    public List<LogisticsUserRouteEntity> getUserRoutes(String orderId);


    /**
     * 追加系统物流信息
     *
     * @param logisticsSystemRouteEntity
     */
    public void appandSystemRoute(LogisticsSystemRouteEntity logisticsSystemRouteEntity);


    /**
     * 获取系统路由
     * @param orderId
     * @return
     */
    public List<LogisticsSystemRouteEntity> getSystemRouteList(String orderId);
}
