package com.sfebiz.supplychain.exposed.route.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsSystemRouteEntity;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;

import java.util.Date;
import java.util.List;

/**
 * 路由服务
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017/7/28 11:56
 */
public interface RouteService {


    /*    用户物流   begin    */

    /**
     * 追加用户路由信息
     *
     * @param orderId              订单ID
     * @param routeType            路由类型 RouteType枚举值
     * @param logisticsRouteEntity 路由信息实体
     * @return void
     */
    public CommonRet<Void> appendUserRoute(String orderId, String routeType, LogisticsUserRouteEntity logisticsRouteEntity);


    /**
     * 覆盖用户路由信息
     *
     * @param orderId                  订单ID
     * @param routeType                路由类型 RouteType枚举值
     * @param logisticsRouteEntityList 路由信息实体集合
     * @return void
     */
    public CommonRet<Void> overrideUserRoute(String orderId, String routeType, List<LogisticsUserRouteEntity> logisticsRouteEntityList);


    /**
     * 获取该订单的所有用户路由信息
     *
     * @param orderId 订单ID
     * @return List<LogisticsUserRouteEntity>
     */
    public CommonRet<List<LogisticsUserRouteEntity>> getUserRoutes(String orderId);

    /*    用户物流   end      */


    /*    系统物流   begin      */

    /**
     * 添加系统路由信息
     *
     * @param logisticsSystemRouteEntity
     * @return
     */
    public CommonRet<Void> appandSystemRoute(LogisticsSystemRouteEntity logisticsSystemRouteEntity);
    public CommonRet<Void> appandSystemRoute(String bizId, String content, String level, Date eventTime, String opreator);


    /**
     * 获取订单系统路由
     *
     * @param orderId
     * @return
     */
    public CommonRet<List<LogisticsSystemRouteEntity>> getSystemRouteList(String orderId);

    /*    系统物流   end      */








    /*    其他服务   begin      */

    /**
     *  注册快递100，只支持国内/国际路由类型
     *
     * @param orderId       订单ID
     * @param routeType     路由类型（国内路由/国际路由）
     * @return
     */
    public CommonRet<Void> registKD100Routes(String orderId, String routeType);


    /**
     * 发送路由获取延迟消息
     * @param orderId       订单ID
     * @param delaySecond   延迟时间 单位秒
     * @return
     */
    public CommonRet<Void> sendRouteFetchMessage(String orderId, Long delaySecond);

    /*    其他服务   end      */


}