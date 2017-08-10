package com.sfebiz.supplychain.service.route.op.impl;

import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsSystemRouteEntity;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.service.route.op.RouteOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 路由信息操作实现，可以同时包含对多种存储服务的操作
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-28 16:04
 **/
public class RouteOperationImpl implements RouteOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteOperationImpl.class);

    /**
     * 写操作集合，按照集合中元素顺序执行写操作(例如先执行OTS存储，如果OTS存储失败，Redis就不需要执行存储了)
     */
    private List<RouteOperation> writeOperations;

    /**
     * 读操作集合，按照集合中元素顺序执行读操作(例如先读取Redis，如果Redis不命中，再从OTS中读取数据)
     */
    private List<RouteOperation> readOperations;


    public List<RouteOperation> getWriteOperations() {
        return writeOperations;
    }

    public void setWriteOperations(List<RouteOperation> writeOperations) {
        this.writeOperations = writeOperations;
    }

    public List<RouteOperation> getReadOperations() {
        return readOperations;
    }

    public void setReadOperations(List<RouteOperation> readOperations) {
        this.readOperations = readOperations;
    }

    /**
     * 追加用户路由 委托具体存储服务
     *
     * @param logisticsUserRouteEntity
     * @param routeType
     */
    @Override
    public void appendUserRoute(LogisticsUserRouteEntity logisticsUserRouteEntity, RouteType routeType) {
        for (RouteOperation routeOperation : writeOperations) {
            routeOperation.appendUserRoute(logisticsUserRouteEntity, routeType);
        }
    }

    /**
     * 覆盖用户路由信息 委托具体存储服务
     *
     * @param orderId                      订单ID
     * @param routeType                    路由类型 RouteType枚举值
     * @param logisticsUserRouteEntityList 路由信息实体集合
     */
    @Override
    public void overrideUserRoute(String orderId, RouteType routeType, List<LogisticsUserRouteEntity> logisticsUserRouteEntityList) {
        for (RouteOperation routeOperation : writeOperations) {
            routeOperation.overrideUserRoute(orderId, routeType, logisticsUserRouteEntityList);
        }
    }

    /**
     * 根据订单号和路由类型获取路由信息集合 委托具体存储服务
     *
     * @param orderId   订单ID
     * @param routeType 路由类型
     * @return
     */
    @Override
    public Set<LogisticsUserRouteEntity> getUserRoutesByType(String orderId, RouteType routeType) {
        Set<LogisticsUserRouteEntity> logisticsUserRouteEntitySet = null;
        for (RouteOperation routeOperation : readOperations) {
            logisticsUserRouteEntitySet = routeOperation.getUserRoutesByType(orderId, routeType);
            if (logisticsUserRouteEntitySet != null && logisticsUserRouteEntitySet.size() > 0) {
                break;
            }
        }
        return logisticsUserRouteEntitySet;
    }

    /**
     * 获取该订单的所有用户路由信息 委托具体存储服务
     *
     * @param orderId 订单ID
     * @return
     */
    @Override
    public List<LogisticsUserRouteEntity> getUserRoutes(String orderId) {
        List<LogisticsUserRouteEntity> logisticsRouteEntities = null;
        boolean isMockAutoGetRoutes = MockConfig.isMocked("routes", "getRoutes");
        if (isMockAutoGetRoutes) {
            //直接返回仓库已发货
            LOGGER.info("MOCK：获取用户路由信息 采用MOCK实现");
            return mockGetUserRoutesSuccess();
        }
        for (RouteOperation routeOperation : readOperations) {
            logisticsRouteEntities = routeOperation.getUserRoutes(orderId);
            if (logisticsRouteEntities != null && logisticsRouteEntities.size() > 0) {
                break;
            }
        }
        return logisticsRouteEntities;
    }

    /**
     * 追加系统路由
     *
     * @param logisticsSystemRouteEntity
     */
    @Override
    public void appandSystemRoute(LogisticsSystemRouteEntity logisticsSystemRouteEntity) {
        for (RouteOperation routeOperation : writeOperations) {
            routeOperation.appandSystemRoute(logisticsSystemRouteEntity);
        }
    }

    /**
     * 获取系统路由信息
     * @param orderId
     * @return
     */
    @Override
    public List<LogisticsSystemRouteEntity> getSystemRouteList(String orderId) {
        List<LogisticsSystemRouteEntity> systemRouteEntities = new ArrayList<LogisticsSystemRouteEntity>();
        for (RouteOperation routeOperation : readOperations) {
            systemRouteEntities = routeOperation.getSystemRouteList(orderId);
            if (systemRouteEntities != null && systemRouteEntities.size() > 0) {
                break;
            }
        }
        return systemRouteEntities;
    }


    /**
     * Mock 获取用户路由信息
     *
     * @return
     */
    private List<LogisticsUserRouteEntity> mockGetUserRoutesSuccess() {
        List<LogisticsUserRouteEntity> routeEntityList = new ArrayList<LogisticsUserRouteEntity>();
        LogisticsUserRouteEntity routeEntity = new LogisticsUserRouteEntity();
        routeEntity.eventTime = new Date().getTime();
        routeEntity.mailNo = "589857707412";
        routeEntity.content = "[测试]顺丰速运 已收取快件";
        routeEntity.routeType = RouteType.TRANSIT.getType();

        routeEntityList.add(routeEntity);

        routeEntity = new LogisticsUserRouteEntity();
        routeEntity.eventTime = new Date().getTime();
        routeEntity.mailNo = "589857707412";
        routeEntity.content = "[测试]包裹完成清关";
        routeEntity.routeType = RouteType.CLEARANCE.getType();

        routeEntityList.add(routeEntity);

        routeEntity = new LogisticsUserRouteEntity();
        routeEntity.eventTime = new Date().getTime();
        routeEntity.mailNo = "589857707412";
        routeEntity.carrierCode = "SF";
        routeEntity.content = "[测试]快件到达 正在派送途中,请您准备签收(派件人:吴兵,电话:18302116294)";
        routeEntity.routeType = RouteType.INTERNAL.getType();
        routeEntityList.add(routeEntity);

        return routeEntityList;
    }
}
