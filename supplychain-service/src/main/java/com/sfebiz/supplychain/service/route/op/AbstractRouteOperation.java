package com.sfebiz.supplychain.service.route.op;

import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;

import java.util.*;

/**
 * 抽象路由操作
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-28 13:04
 **/
public abstract class AbstractRouteOperation implements RouteOperation {

    /**
     * 整理出用户前段展示的路由信息集合
     *
     * @param routeTypeSetMap 路由类型和路由信息的映射Map
     * @return 订单的用户路由信息
     */
    private List<LogisticsUserRouteEntity> arrangeUserViewRoute(Map<RouteType, Set<LogisticsUserRouteEntity>> routeTypeSetMap) {
        List<LogisticsUserRouteEntity> routeEntityList = new ArrayList<LogisticsUserRouteEntity>();
        //顺序是国际-集货-清关-国内，若后一种路由时间小于前面的路由时间，则覆盖显示
        for (RouteType type : RouteType.values()) {
            if (type == RouteType.USERDEFINED) {
                //自定义路由后续单独插入，因为不涉及到覆盖
                continue;
            }
            Set<LogisticsUserRouteEntity> otherTypeRouteSet = routeTypeSetMap.get(type);
            if (routeEntityList.size() > 0) {
                //上一个类型的最后一条路由信息
                LogisticsUserRouteEntity lastRoute = routeEntityList.get(routeEntityList.size() - 1);
                for (LogisticsUserRouteEntity otherTypeRoute : otherTypeRouteSet) {
                    //下一个路由类型的路由信息不能大于上一个路由类型路由信息 时间最小的一条，否则覆盖
                    if (otherTypeRoute.eventTime < lastRoute.eventTime) {
                        routeEntityList.add(otherTypeRoute);
                    }
                }
            } else {
                if (otherTypeRouteSet != null && otherTypeRouteSet.size() > 0) {
                    routeEntityList.addAll(otherTypeRouteSet);
                }
            }
        }

        Set<LogisticsUserRouteEntity> userDefinedRouteSet = routeTypeSetMap.get(RouteType.USERDEFINED);
        if (userDefinedRouteSet != null && userDefinedRouteSet.size() > 0) {
            //加入自定义路由信息
            routeEntityList.addAll(userDefinedRouteSet);
            //重排序
            Collections.sort(routeEntityList);
        }
        return routeEntityList;
    }

    /**
     * 判断追加的路由信息时间是否小于路由集合中最大时间
     *
     * @param logisticsUserRouteEntitySet 路由集合
     * @param logisticsUserRouteEntity    追加的路由信息
     * @return 是否大于
     */
    protected boolean checkRouteTime(Set<LogisticsUserRouteEntity> logisticsUserRouteEntitySet, LogisticsUserRouteEntity logisticsUserRouteEntity) {
        if (logisticsUserRouteEntitySet == null || logisticsUserRouteEntitySet.size() < 1) {
            return true;
        }
        if (logisticsUserRouteEntity.eventTime > logisticsUserRouteEntitySet.iterator().next().eventTime) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取该订单的所有用户路由信息
     *
     * @param orderId 订单ID
     * @return
     */
    @Override
    public List<LogisticsUserRouteEntity> getUserRoutes(String orderId) {
        Map<RouteType, Set<LogisticsUserRouteEntity>> routeTypeSetMap = new HashMap<RouteType, Set<LogisticsUserRouteEntity>>();
        for (RouteType type : RouteType.values()) {
            Set<LogisticsUserRouteEntity> routeEntitySet = getUserRoutesByType(orderId, type);
            routeTypeSetMap.put(type, routeEntitySet);
        }
        return arrangeUserViewRoute(routeTypeSetMap);
    }
}
