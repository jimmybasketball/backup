package com.sfebiz.supplychain.service.route.op.impl;

import com.alibaba.fastjson.JSON;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.JedisProxyUtil;
import com.sfebiz.supplychain.exposed.common.enums.ConfigSystemConstants;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsSystemRouteEntity;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.service.route.op.AbstractRouteOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.*;

/**
 * 路由信息Redis操作， 缓存数据会过期
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-28 13:07
 **/
@Component("routeOperationOnRedis")
public class RouteOperationOnRedis extends AbstractRouteOperation {


    private static final Logger LOGGER = LoggerFactory.getLogger(RouteOperationOnOTS.class);


    @Resource
    private JedisProxyUtil jedisProxyUtil;

    /**
     * 追加用户路由 redis实现
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

        Jedis jedis = null;
        String key = ConfigSystemConstants.SCM_REDIS_PREFIX + ConfigSystemConstants.ROUTE_REDIS_DATA_KEY + orderId;
        try {
            jedis = jedisProxyUtil.getJedis();
            //获取缓存中的路由信息
            String routeJson = jedis.hget(key, routeType.getType());

            List<LogisticsUserRouteEntity> logisticsRouteEntities = new ArrayList<LogisticsUserRouteEntity>();
            if (StringUtils.isNotBlank(routeJson)) {
                logisticsRouteEntities = JSON.parseArray(routeJson, LogisticsUserRouteEntity.class);
            }


            Set<LogisticsUserRouteEntity> logisticsUserRouteEntitySet = new TreeSet<LogisticsUserRouteEntity>();
            logisticsUserRouteEntitySet.addAll(logisticsRouteEntities);

            if (checkRouteTime(logisticsUserRouteEntitySet, logisticsUserRouteEntity)) {
                //追加路由信息
                logisticsUserRouteEntitySet.add(logisticsUserRouteEntity);
                jedis.hset(key, routeType.getType(), JSON.toJSONString(logisticsUserRouteEntitySet));
                jedis.expire(key, 60 * 60 * 24 * 7);
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台路由-追加用户路由信息] Redis存储成功")
                        .addParm("新增记录", logisticsUserRouteEntity)
                        .addParm("路由类型", routeType.getType())
                        .addParm("所有记录", logisticsRouteEntities)
                        .log();
            } else {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台路由-追加用户路由信息] Redis存储失败 路由时间小于集合中最大时间")
                        .addParm("新增记录", logisticsUserRouteEntity)
                        .addParm("路由类型", routeType.getType())
                        .addParm("所有记录", logisticsRouteEntities)
                        .log();
            }
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setErrorMsg("[物流平台路由-追加用户路由信息] Redis存储未知异常")
                    .addParm("新增记录", logisticsUserRouteEntity)
                    .addParm("路由类型", routeType.getType())
                    .log();
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 覆盖用户路由信息 redis实现
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

        Jedis jedis = null;
        String key = ConfigSystemConstants.SCM_REDIS_PREFIX + ConfigSystemConstants.ROUTE_REDIS_DATA_KEY + orderId;

        try {
            jedis = jedisProxyUtil.getJedis();
            jedis.hset(key, routeType.getType(), JSON.toJSONString(logisticsUserRouteEntitySet));
            jedis.expire(key, 60 * 60 * 24 * 7);
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台路由-覆盖用户路由信息] Redis存储成功")
                    .addParm("覆盖记录", logisticsUserRouteEntityList)
                    .addParm("路由类型", routeType.getType())
                    .log();
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setErrorMsg("[物流平台路由-覆盖用户路由信息] Redis存储未知异常")
                    .addParm("覆盖集合", logisticsUserRouteEntityList)
                    .addParm("路由类型", routeType)
                    .log();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 根据订单号和路由类型获取路由信息集合 redis实现
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

        Jedis jedis = null;
        String key = ConfigSystemConstants.SCM_REDIS_PREFIX + ConfigSystemConstants.ROUTE_REDIS_DATA_KEY + orderId;
        try {
            jedis = jedisProxyUtil.getJedis();
            String routesJson = jedis.hget(key, routeType.getType());
            Set<LogisticsUserRouteEntity> logisticsUserRouteEntitySet = new TreeSet<LogisticsUserRouteEntity>();
            logisticsUserRouteEntitySet.addAll(JSON.parseArray(routesJson, LogisticsUserRouteEntity.class));
            return logisticsUserRouteEntitySet;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setErrorMsg("[物流平台路由-根据订单号和路由类型获取路由信息集合] Redis读取未知异常")
                    .addParm("覆盖集合", orderId)
                    .addParm("路由类型", routeType)
                    .log();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 追加系统路由信息 Redis实现
     *
     * @param logisticsSystemRouteEntity
     */
    @Override
    public void appandSystemRoute(LogisticsSystemRouteEntity logisticsSystemRouteEntity) {
        //redis不存储系统路由
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台路由-添加系统物流] redis mock")
                .addParm("logisticsSystemRouteEntity", logisticsSystemRouteEntity)
                .log();
    }

    @Override
    public List<LogisticsSystemRouteEntity> getSystemRouteList(String orderId) {
        //redis不存系统路由，直接返回null
        return null;
    }


}
