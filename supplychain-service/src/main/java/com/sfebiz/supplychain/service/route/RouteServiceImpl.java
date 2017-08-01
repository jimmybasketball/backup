package com.sfebiz.supplychain.service.route;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.exposed.common.code.RouteReturnCode;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsSystemRouteEntity;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.service.route.op.RouteOperation;
import com.sfebiz.supplychain.service.stockout.StockoutServiceImpl;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 订单路由服务
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-28 12:48
 **/
@Service("routeService")
public class RouteServiceImpl implements RouteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockoutServiceImpl.class);

    private static final String APPEND_USER_ROUTE_KEY = "APPEND_USER_ROUTE_KEY";

    private static final String OVERRIDE_USER_ROUTE_KEY = "OVERRIDE_USER_ROUTE_KEY";

    @Resource
    private Lock distributedLock;

    @Resource
    private StockoutOrderManager stockoutOrderManager;

    @Resource
    private RouteOperation routeOperation;

    /**
     * 追加用户路由信息
     *
     * @param orderId                  订单ID
     * @param routeType                路由类型 RouteType枚举值
     * @param logisticsUserRouteEntity 路由信息实体
     * @return void
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> appendUserRoute(
            @ParamNotBlank("订单ID不能为空") String orderId,
            @ParamNotBlank("路由类型不能为空") String routeType,
            @ParamNotBlank("路由信息实体不能为空") LogisticsUserRouteEntity logisticsUserRouteEntity) {

        CommonRet<Void> commonRet = new CommonRet<Void>();

        //commonRet = checkStockOutOrder(orderId);
        if (SCReturnCode.COMMON_SUCCESS.getCode() != commonRet.getRetCode()) {
            return commonRet;
        }

        //检查路由类型是否合法
        RouteType routeEnum = RouteType.getByType(routeType);
        if (routeEnum == null) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[物流平台路由-追加用户路由信息] 路由类型不合法")
                    .addParm("orderId", orderId)
                    .log();
            commonRet.setRetMsg("路由类型不合法");
            commonRet.setRetCode(SCReturnCode.PARAM_ILLEGAL_ERR.getCode());
            return commonRet;
        }


        String key = APPEND_USER_ROUTE_KEY + orderId + "_" + routeType;
        if (distributedLock.fetch(key)) {
            try {

                logisticsUserRouteEntity.orderId = orderId;
                logisticsUserRouteEntity.routeType = routeType;
                //执行追加操作
                routeOperation.appendUserRoute(logisticsUserRouteEntity, routeEnum);

                //TODO 流转出库单状态信息


                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台路由-追加用户路由信息] 成功")
                        .addParm("orderId", orderId)
                        .addParm("routeType", routeEnum)
                        .addParm("logisticsUserRouteEntity", logisticsUserRouteEntity)
                        .log();
                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("[物流平台路由-追加用户路由信息] 未知异常")
                        .log();
                commonRet.setRetCode(RouteReturnCode.USER_ROUTE_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg("未知异常");
                return commonRet;
            } finally {
                distributedLock.realease(key);
            }

        } else {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[物流平台路由-追加用户路由信息] 并发异常")
                    .log();
            commonRet.setRetCode(RouteReturnCode.USER_ROUTE_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }

    /**
     * 覆盖用户路由信息
     *
     * @param orderId                      订单ID
     * @param routeType                    路由类型 RouteType枚举值
     * @param logisticsUserRouteEntityList 路由信息实体集合
     * @return void
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> overrideUserRoute(
            @ParamNotBlank("订单ID不能为空") String orderId,
            @ParamNotBlank("路由类型不能为空") String routeType,
            @ParamNotBlank("路由信息实体集合不能为空") List<LogisticsUserRouteEntity> logisticsUserRouteEntityList) {

        CommonRet<Void> commonRet = new CommonRet<Void>();
        //检查出库单是否存在
//        commonRet = checkStockOutOrder(orderId)
        if (SCReturnCode.COMMON_SUCCESS.getCode() != commonRet.getRetCode()) {
            return commonRet;
        }

        //检查路由类型是否合法
        RouteType routeEnum = RouteType.getByType(routeType);
        if (routeEnum == null) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[物流平台路由-覆盖用户路由信息] 路由类型不合法")
                    .addParm("orderId", orderId)
                    .log();
            commonRet.setRetMsg("路由类型不合法");
            commonRet.setRetCode(SCReturnCode.PARAM_ILLEGAL_ERR.getCode());
            return commonRet;
        }

        //校验每个路由信息实体是否合法
        Validator validator = new Validator();
        for (LogisticsUserRouteEntity logisticsUserRouteEntity : logisticsUserRouteEntityList) {
            List<ConstraintViolation> violations = validator.validate(logisticsUserRouteEntity);
            if (violations != null && violations.size() > 0) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-覆盖用户路由信息] 实体校验失败！")
                        .addParm("失败信息", violations.toString())
                        .addParm("路由实体集合", logisticsUserRouteEntityList)
                        .log();
                commonRet.setRetCode(SCReturnCode.PARAM_ILLEGAL_ERR.getCode());
                commonRet.setRetMsg("实体校验失败");
                return commonRet;
            }
        }

        String key = OVERRIDE_USER_ROUTE_KEY + orderId + "_" + routeType;
        if (distributedLock.fetch(key)) {
            try {
                //执行覆盖操作
                routeOperation.overrideUserRoute(orderId, routeEnum, logisticsUserRouteEntityList);

                //TODO 出库单状态流转


                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台路由-覆盖用户路由信息] 成功")
                        .addParm("orderId", orderId)
                        .addParm("routeType", routeEnum)
                        .addParm("logisticsUserRouteEntityList", logisticsUserRouteEntityList)
                        .log();
                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("[物流平台路由-覆盖用户路由信息] 未知异常")
                        .log();
                commonRet.setRetCode(RouteReturnCode.USER_ROUTE_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg("未知异常");
                return commonRet;
            } finally {
                distributedLock.realease(key);
            }
        } else {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[物流平台路由-覆盖用户路由信息] 并发异常")
                    .log();
            commonRet.setRetCode(RouteReturnCode.USER_ROUTE_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }


    /**
     * 获取该订单的所有用户路由信息
     *
     * @param orderId 订单ID
     * @return List<LogisticsUserRouteEntity>
     */
    @Override
    @MethodParamValidate
    public CommonRet<List<LogisticsUserRouteEntity>> getUserRoutes(@ParamNotBlank("订单ID不能为空") String orderId) {

        CommonRet<List<LogisticsUserRouteEntity>> commonRet = new CommonRet<List<LogisticsUserRouteEntity>>();
        //检查出库单是否存在
//        commonRet = checkStockOutOrder(orderId);
        if (SCReturnCode.COMMON_SUCCESS.getCode() != commonRet.getRetCode()) {
            return commonRet;
        }

        //执行读取操作
        List<LogisticsUserRouteEntity> routeEntityList = routeOperation.getUserRoutes(orderId);
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台路由-获取该订单的所有用户路由信息] 成功")
                .addParm("orderId", orderId)
                .addParm("用户所有路由信息", routeEntityList)
                .log();

        commonRet.setResult(routeEntityList);
        return commonRet;
    }

    /**
     * 添加系统路由信息
     *
     * @param logisticsSystemRouteEntity
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> appandSystemRoute(@ParamNotBlank("系统物流实体不能为空") LogisticsSystemRouteEntity logisticsSystemRouteEntity) {

        CommonRet<Void> commonRet = new CommonRet<Void>();

        //检查出库单是否存在
        //commonRet = checkStockOutOrder(logisticsSystemRouteEntity.orderId);
        if (SCReturnCode.COMMON_SUCCESS.getCode() != commonRet.getRetCode()) {
            return commonRet;
        }

        //追加系统路由
        logisticsSystemRouteEntity.eventTime = new Date().getTime();
        routeOperation.appandSystemRoute(logisticsSystemRouteEntity);

        return commonRet;
    }

    /**
     * 获取订单系统路由
     *
     * @param orderId
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<List<LogisticsSystemRouteEntity>> getSystemRouteList(@ParamNotBlank String orderId) {
        CommonRet<List<LogisticsSystemRouteEntity>> commonRet = new CommonRet<List<LogisticsSystemRouteEntity>>();
        //检查出库单是否存在
        //commonRet = checkStockOutOrder(orderId);
        if (SCReturnCode.COMMON_SUCCESS.getCode() != commonRet.getRetCode()) {
            return commonRet;
        }

        //执行读取操作
        List<LogisticsSystemRouteEntity> systemRouteEntities = routeOperation.getSystemRouteList(orderId);
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台路由-获取系统路由] 成功")
                .addParm("systemRouteEntities", systemRouteEntities)
                .log();

        commonRet.setResult(systemRouteEntities);
        return commonRet;
    }


    /**
     * 校验出库单是否存在
     * @param orderId
     * @return
     */
    private CommonRet checkStockOutOrder(String orderId) {
        CommonRet commonRet = new CommonRet();
        StockoutOrderDO stockoutOrderDO = stockoutOrderManager.getByBizId(orderId);
        if (stockoutOrderDO == null) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[物流平台路由-路由服务] 出库单不存在")
                    .addParm("orderId", orderId)
                    .log();

            commonRet.setRetMsg(RouteReturnCode.USER_ROUTE_STOCKOUT_ORDER_NOT_EXIST.getDesc());
            commonRet.setRetCode(RouteReturnCode.USER_ROUTE_STOCKOUT_ORDER_NOT_EXIST.getCode());
        }
        return commonRet;
    }
}
