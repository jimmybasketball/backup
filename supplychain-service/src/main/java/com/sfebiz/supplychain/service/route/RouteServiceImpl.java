package com.sfebiz.supplychain.service.route;

import com.aliyun.openservices.ons.api.Message;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.exposed.common.code.RouteReturnCode;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsSystemRouteEntity;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.tpl.TplOrderRegistRoutesCommand;
import com.sfebiz.supplychain.queue.MessageConstants;
import com.sfebiz.supplychain.queue.MessageProducer;
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
import java.util.Properties;

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

    private static final String REGIST_KD100_KEY = "REGIST_KD100_KEY";

    @Resource
    private Lock distributedLock;

    @Resource
    private StockoutOrderManager stockoutOrderManager;

    @Resource
    private RouteOperation routeOperation;

    @Resource
    private MessageProducer supplyChainRouteMessageProducer;

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

        commonRet = checkStockOutOrder(orderId);
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
                    .addParm("routeType", routeType)
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
                        .setException(e)
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
        commonRet = checkStockOutOrder(orderId);
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
                        .setException(e)
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
        commonRet = checkStockOutOrder(orderId);
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
        commonRet = checkStockOutOrder(logisticsSystemRouteEntity.orderId);
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
        commonRet = checkStockOutOrder(orderId);
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
     * 注册快递100，只支持国内/国际路由类型
     *
     * @param orderId   订单ID
     * @param routeType 路由类型（国内路由/国际路由）
     * @return
     */
    @Override
    public CommonRet<Void> registKD100Routes(
            @ParamNotBlank("订单ID不能为空") String orderId,
            @ParamNotBlank("路由类型不能为空") String routeType) {
        CommonRet<Void> commonRet = new CommonRet<Void>();

        //检查出库单是否存在
        commonRet = checkStockOutOrder(orderId);
        if (SCReturnCode.COMMON_SUCCESS.getCode() != commonRet.getRetCode()) {
            return commonRet;
        }

        //检查路由类型
        RouteType routeEnums = RouteType.getByType(routeType);
        if (routeEnums == null ||
                (routeEnums != RouteType.INTERNAL && routeEnums != RouteType.INTERNATIONAL)) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[物流平台路由-注册快递100] 路由类型不合法，只支持国内/国际路由类型")
                    .addParm("orderId", orderId)
                    .addParm("routeType", routeType)
                    .log();
            commonRet.setRetMsg("路由类型不合法");
            commonRet.setRetCode(SCReturnCode.PARAM_ILLEGAL_ERR.getCode());
            return commonRet;
        }

        String key = REGIST_KD100_KEY + "orderId";
        if (distributedLock.fetch(key)) {
            try {

                StockoutOrderDO stockoutOrderDO = stockoutOrderManager.getByBizId(orderId);
                ProviderCommand cmd = CommandFactory.createCommand(LogisticsProviderConfig.getKD100CommandVersion(), WmsMessageType.CALLBACK_KD100_DECLARE.getValue());
                TplOrderRegistRoutesCommand tplOrderRegistRoutesCommand = (TplOrderRegistRoutesCommand) cmd;
                tplOrderRegistRoutesCommand.setStockoutOrderDO(stockoutOrderDO);
                if (RouteType.INTERNAL.getType().equals(routeType)) {
                    tplOrderRegistRoutesCommand.setRouteType(RouteType.INTERNAL);
                } else if (RouteType.INTERNATIONAL.getType().equals(routeType)) {
                    tplOrderRegistRoutesCommand.setRouteType(RouteType.INTERNATIONAL);
                }
                boolean isSuccess = tplOrderRegistRoutesCommand.execute();
                if (!isSuccess) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台路由-注册快递100] 执行注册command返回失败")
                            .addParm("orderId", orderId)
                            .addParm("routeType", routeType)
                            .log();

                    commonRet.setRetCode(RouteReturnCode.REGIST_KD100_ROUTES_FAIL.getCode());
                    commonRet.setRetMsg(RouteReturnCode.REGIST_KD100_ROUTES_FAIL.getDesc());
                    return commonRet;
                }

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("[物流平台路由-注册快递100] 注册成功")
                        .addParm("orderId", orderId)
                        .addParm("routeType", routeType)
                        .log();
                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setException(e)
                        .setMsg("[物流平台路由-注册快递100] 未知异常")
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
                    .setMsg("[物流平台路由-注册快递100] 并发异常")
                    .log();
            commonRet.setRetCode(RouteReturnCode.USER_ROUTE_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }

    /**
     * 发送路由获取延迟消息
     *
     * @param orderId     订单ID
     * @param delaySecond 延迟时间 单位秒
     * @return
     */
    @Override
    public CommonRet<Void> sendRouteFetchMessage(String orderId, Long delaySecond) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台路由-发路由获取消息] 开始")
                .addParm("订单ID", orderId)
                .log();
        if (supplyChainRouteMessageProducer != null) {
            try {
                Message msg = new Message();
                msg.setTopic(MessageConstants.TOPIC_SUPPLY_CHAIN_ROUTE_EVENT);
                msg.setTag(MessageConstants.TAG_ROUTE_FETCH);
                msg.setBody(" ".getBytes());
                msg.setStartDeliverTime(System.currentTimeMillis() + 1000 * delaySecond);
                msg.setKey(orderId + "-" + delaySecond + "-" + System.currentTimeMillis());
                Properties properties = new Properties();
                properties.put("orderId", orderId);
                msg.setUserProperties(properties);
                supplyChainRouteMessageProducer.send(msg);

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台路由-发路由获取消息] 成功")
                        .addParm("订单ID", orderId)
                        .addParm("延迟时长(秒)", delaySecond)
                        .log();
                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setException(e)
                        .setErrorMsg("[物流平台路由-发路由获取消息] 未知错误")
                        .addParm("订单ID", orderId)
                        .log();
                commonRet.setRetCode(RouteReturnCode.SEND_FETCH_ROUTE_MSG_ERROR.getCode());
                commonRet.setRetMsg(RouteReturnCode.SEND_FETCH_ROUTE_MSG_ERROR.getDesc());
                return commonRet;
            }
        } else {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[物流平台路由-发路由获取消息] 消息生产者为空！")
                    .log();
            commonRet.setRetCode(RouteReturnCode.SEND_FETCH_ROUTE_MSG_ERROR.getCode());
            commonRet.setRetMsg(RouteReturnCode.SEND_FETCH_ROUTE_MSG_ERROR.getDesc());
            return commonRet;
        }
    }


    /**
     * 校验出库单是否存在
     *
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
