package com.sfebiz.supplychain.service.route.op.impl;

import com.alibaba.fastjson.JSON;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.tracelog.TraceLogNoAutoIncreaseTime;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsSystemRouteEntity;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.service.route.op.AbstractRouteOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 路由信息OTS操作， 数据将持久化到OTS中
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-28 13:07
 **/
@Component("routeOperationOnOTS")
public class RouteOperationOnOTS extends AbstractRouteOperation {

    private static final HaitaoTraceLogger routeLogger = HaitaoTraceLoggerFactory.getTraceLogger("routelog");

    /**
     * 订单用户路由数据 在OTS的key
     */
    public static final String ROUTE_OTS_DATA_KEY = "ROUTE_OTS_DATA_KEY_";

    /**
     * 订单系统内部路由数据 在OTS的key
     */
    public static final String SYSTEM_ROUTE_OTS_DATA_KEY = "SYSTEM_ROUTE_OTS_DATA_KEY_";

    /**
     * 默认时间格式
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteOperationOnOTS.class);

    /**
     * 追加用户路由 OTS实现
     *
     * @param logisticsUserRouteEntity
     * @param routeType
     */
    @Override
    public void appendUserRoute(LogisticsUserRouteEntity logisticsUserRouteEntity, RouteType routeType) {
        String orderId = logisticsUserRouteEntity.orderId;
        if (StringUtils.isBlank(orderId) || routeType == null) {
            return;
        }

        Set<LogisticsUserRouteEntity> logisticsRouteEntities = new TreeSet<LogisticsUserRouteEntity>();
        List<TraceLog> routes = routeLogger.queryAppointAppName(ROUTE_OTS_DATA_KEY + routeType.getType() + orderId, routeType.getType());
        fillRoutesSet(logisticsRouteEntities, routes);

        if (checkRouteTime(logisticsRouteEntities, logisticsUserRouteEntity)) {
            logisticsRouteEntities.add(logisticsUserRouteEntity);
            //更新路由信息
            routeLogger.log(new TraceLogNoAutoIncreaseTime(ROUTE_OTS_DATA_KEY + routeType.getType() + orderId,
                    routeType.getType(),
                    new Date(),
                    TraceLog.TraceLevel.INFO,
                    JSON.toJSONString(logisticsRouteEntities)));

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台路由-追加用户路由信息] OTS存储成功")
                    .addParm("新增记录", logisticsUserRouteEntity)
                    .addParm("路由类型", routeType.getType())
                    .addParm("所有记录", logisticsRouteEntities)
                    .log();
        } else {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台路由-追加用户路由信息] OTS存储失败 路由时间小于集合中最大时间")
                    .addParm("新增记录", logisticsUserRouteEntity)
                    .addParm("路由类型", routeType.getType())
                    .addParm("所有记录", logisticsRouteEntities)
                    .log();
        }

    }

    /**
     * 覆盖用户路由信息 OTS实现
     *
     * @param orderId                      订单ID
     * @param routeType                    路由类型 RouteType枚举值
     * @param logisticsUserRouteEntityList 路由信息实体集合
     */
    @Override
    public void overrideUserRoute(String orderId, RouteType routeType, List<LogisticsUserRouteEntity> logisticsUserRouteEntityList) {
        Set<LogisticsUserRouteEntity> logisticsUserRouteEntitySet = new TreeSet<LogisticsUserRouteEntity>();

        for (LogisticsUserRouteEntity routeEntity : logisticsUserRouteEntityList) {
            routeEntity.orderId = orderId;
            routeEntity.routeType = routeType.getType();
            logisticsUserRouteEntitySet.add(routeEntity);
        }

        routeLogger.log(new TraceLogNoAutoIncreaseTime(ROUTE_OTS_DATA_KEY + routeType.getType() + orderId,
                routeType.getType(),
                new Date(),
                TraceLog.TraceLevel.INFO,
                JSON.toJSONString(logisticsUserRouteEntitySet)));

        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台路由-覆盖用户路由信息] OTS存储成功")
                .addParm("覆盖记录", logisticsUserRouteEntityList)
                .addParm("路由类型", routeType.getType())
                .log();
    }

    /**
     * 根据订单号和路由类型获取路由信息集合 OTS实现
     *
     * @param orderId   订单ID
     * @param routeType 路由类型
     * @return
     */
    @Override
    public Set<LogisticsUserRouteEntity> getUserRoutesByType(String orderId, RouteType routeType) {
        if (StringUtils.isBlank(orderId) || routeType == null) {
            return null;
        }

        Set<LogisticsUserRouteEntity> logisticsRouteEntities = new TreeSet<LogisticsUserRouteEntity>();

        List<TraceLog> routes = routeLogger.queryAppointAppName(ROUTE_OTS_DATA_KEY + routeType.getType() + orderId, routeType.getType());
        fillRoutesSet(logisticsRouteEntities, routes);
        return logisticsRouteEntities;
    }

    /**
     * 追加系统物流信息 OTS实现
     *
     * @param logisticsSystemRouteEntity
     */
    @Override
    public void appandSystemRoute(LogisticsSystemRouteEntity logisticsSystemRouteEntity) {
        routeLogger.log(new TraceLog(SYSTEM_ROUTE_OTS_DATA_KEY + logisticsSystemRouteEntity.orderId,
                logisticsSystemRouteEntity.opreator,
                new Date(),
                TraceLog.TraceLevel.INFO,
                JSON.toJSONString(logisticsSystemRouteEntity)));

        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台路由-追加系统物流信息] OTS存储成功")
                .addParm("新增记录", logisticsSystemRouteEntity)
                .log();
    }

    /**
     * 获取系统路由
     *
     * @param orderId
     * @return
     */
    @Override
    public List<LogisticsSystemRouteEntity> getSystemRouteList(String orderId) {
        List<TraceLog> traceLogs = routeLogger.query(SYSTEM_ROUTE_OTS_DATA_KEY + orderId);
        List<LogisticsSystemRouteEntity> systemRouteEntities = new ArrayList<LogisticsSystemRouteEntity>();
        for (TraceLog log : traceLogs) {
            LogisticsSystemRouteEntity logisticsSystemRouteEntity = JSON.parseObject(log.getContent(), LogisticsSystemRouteEntity.class);
            logisticsSystemRouteEntity.opreator = log.getAppName();
            systemRouteEntities.add(logisticsSystemRouteEntity);
        }
        Collections.sort(systemRouteEntities);
        return systemRouteEntities;
    }


    private static Date parse(String date, String DateFormat) {
        try {
            java.text.DateFormat format = new SimpleDateFormat(DateFormat);
            return format.parse(date);
        } catch (ParseException e) {
            LOGGER.error("[供应链-查询路由]时间解析异常", e);
        }
        return new Date();
    }

    //获取对应路由类型的路由信息
    private void fillRoutesSet(Set<LogisticsUserRouteEntity> logisticsRouteEntities, List<TraceLog> routes) {
        if (CollectionUtils.isNotEmpty(routes)) {
            //取时间最大的一条
            TraceLog traceLog = routes.get(routes.size() - 1);
            String routeData = traceLog.getContent();
            List<LogisticsUserRouteEntity> subList = JSON.parseArray(routeData, LogisticsUserRouteEntity.class);
            logisticsRouteEntities.addAll(subList);
        }
    }
}
