package com.sfebiz.supplychain.service.route;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.aliyun.openservices.ons.api.Message;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.config.route.LogisticsRoutes;
import com.sfebiz.supplychain.exposed.common.code.RouteReturnCode;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsSystemRouteEntity;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.bsp.BSPConstants.BSPConstants;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.tpl.TplOrderRegistRoutesCommand;
import com.sfebiz.supplychain.queue.MessageConstants;
import com.sfebiz.supplychain.queue.MessageProducer;
import com.sfebiz.supplychain.service.route.op.RouteOperation;
import com.sfebiz.supplychain.service.stockout.StockoutServiceImpl;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

                //流转出库单状态信息
                List<LogisticsUserRouteEntity> routeEntities = new ArrayList<LogisticsUserRouteEntity>();
                routeEntities.add(logisticsUserRouteEntity);
                triggerStateByKeyWordsOnRoutes(orderId, routeEnum, routeEntities);

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

                //出库单状态流转
                triggerStateByKeyWordsOnRoutes(orderId, routeEnum, logisticsUserRouteEntityList);

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
    public CommonRet<List<LogisticsSystemRouteEntity>> getSystemRouteList(@ParamNotBlank("订单ID不能为空") String orderId) {
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
    @MethodParamValidate
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
    @MethodParamValidate
    public CommonRet<Void> sendRouteFetchMessage(
            @ParamNotBlank("订单ID不能为空") String orderId,
            @ParamNotBlank("延迟时间不能为空") Long delaySecond) {
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

    /**
     * 根据路由中的关键字信息触发出库单状态流转 （只有国内段的会触发）
     *
     * @param orderId
     * @param routeType
     * @param routeEntities
     */
    private void triggerStateByKeyWordsOnRoutes(String orderId, RouteType routeType, List<LogisticsUserRouteEntity> routeEntities) {
        if (routeType == null || routeType != RouteType.INTERNAL) {
            return;
        }
        StockoutOrderDO stockoutOrderDO = stockoutOrderManager.getByBizId(orderId);
        for (LogisticsUserRouteEntity routeEntity : routeEntities) {
            if (isContainEntity(routeEntity.content, LogisticsRoutes.getkeyWordsByCodeAndStatus(stockoutOrderDO.getIntrCarrierCode(), StockoutOrderState.COLLECTED.getValue()))) {
                triggerStockoutOrderState(stockoutOrderDO, BSPConstants.BSPRoute.Collected.getCode(), routeEntity.mailNo, routeEntity.eventTime);
            }
            if (isContainEntity(routeEntity.content, LogisticsRoutes.getkeyWordsByCodeAndStatus(stockoutOrderDO.getIntrCarrierCode(), StockoutOrderState.DELIVEING.getValue()))) {
                triggerStockoutOrderState(stockoutOrderDO, BSPConstants.BSPRoute.Delivering.getCode(), routeEntity.mailNo, routeEntity.eventTime);
            }
            if (isContainEntity(routeEntity.content, LogisticsRoutes.getkeyWordsByCodeAndStatus(stockoutOrderDO.getIntrCarrierCode(), StockoutOrderState.SIGNED.getValue()))) {
                triggerStockoutOrderState(stockoutOrderDO, BSPConstants.BSPRoute.Signed.getCode(), routeEntity.mailNo, routeEntity.eventTime);
            }
        }
    }

    /**
     * 根据路由信息触发订单状态变更
     *
     * @param stockoutOrderDO 出库单对象
     * @param bspRouteCode    路由码
     * @param mailNo          运单号
     * @param eventTime       时间发生时间
     */
    private void triggerStockoutOrderState(StockoutOrderDO stockoutOrderDO, String bspRouteCode, String mailNo, long eventTime) {
        try {
            stockoutOrderDO = stockoutOrderManager.getById(stockoutOrderDO.getId());

            //TODO 注释掉的业务逻辑  后续补上
            //如果收到BSP信息订单是已出库
            if (StringUtils.isNotBlank(bspRouteCode)) {
//                stockoutOrderDO.setMailNo(mailNo);
//                boolean isSuccess = stockoutBizService.triggerStockoutOrderStateCollected(stockoutOrderDO, new Date().getTime());
//                if (!isSuccess) {
//                    return;
//                }
                LOGGER.info("[供应链-根据路由改状态]订单跳转为已收件，订单ID：" + stockoutOrderDO.getBizId());
            }

            //收派件中信息，订单状态跳转到派件中
            if (BSPConstants.BSPRoute.Delivering.getCode().equals(bspRouteCode)
                    && StockoutOrderState.COLLECTED.getValue().equals(stockoutOrderDO.getOrderState())) {
//                boolean isSuccess = callEngine(stockoutOrderDO, StockoutOrderActionType.DELIVER, new Date(eventTime));
//                if (!isSuccess) {
//                    return;
//                }
                stockoutOrderDO.setOrderState(StockoutOrderState.DELIVEING.getValue());
                LOGGER.info("[供应链-根据路由改状态]订单跳转为派件中，订单ID：" + stockoutOrderDO.getBizId());
            }

            //收到已签收路由信息，但是订单状态还是已揽收时，直接跳到已签收（BSP一些订单没有派件中状态）
            if (BSPConstants.BSPRoute.Signed.getCode().equals(bspRouteCode)
                    && StockoutOrderState.COLLECTED.getValue().equals(stockoutOrderDO.getOrderState())) {
//                boolean isSuccess = callEngine(stockoutOrderDO, StockoutOrderActionType.DELIVER, new Date(eventTime));
//                if (!isSuccess) {
//                    return;
//                }
//                stockoutOrderDO.setState(StockoutOrderState.DELIVEING.getValue());
//                isSuccess = callEngine(stockoutOrderDO, StockoutOrderActionType.SIGN, new Date(eventTime));
//                if (!isSuccess) {
//                    return;
//                }
                stockoutOrderDO.setOrderState(StockoutOrderState.SIGNED.getValue());
                LOGGER.info("[供应链-根据路由改状态]订单跳转为已签收，订单ID：" + stockoutOrderDO.getBizId());
            }

            //收到已签收路由信息，订单状态跳转到已签收
            if (BSPConstants.BSPRoute.Signed.getCode().equals(bspRouteCode)
                    && StockoutOrderState.DELIVEING.getValue().equals(stockoutOrderDO.getOrderState())) {
//                boolean isSuccess = callEngine(stockoutOrderDO, StockoutOrderActionType.SIGN, new Date(eventTime));
//                if (!isSuccess) {
//                    return;
//                }
                stockoutOrderDO.setOrderState(StockoutOrderState.SIGNED.getValue());
                LOGGER.info("[供应链-根据路由改状态]订单跳转为已签收，订单ID：" + stockoutOrderDO.getBizId());
            }
        } catch (Exception e) {
            LOGGER.warn("根据路由触发出库单状态流转发生异常，订单ID：" + stockoutOrderDO.getBizId(), e);
        }
    }

    /**
     * 判断列表是否包含指定关键字
     *
     * @param key
     * @param entities
     * @return
     */
    private static boolean isContainEntity(String key, List<String> entities) {
        if (StringUtils.isBlank(key) || CollectionUtils.isEmpty(entities)) {
            return false;
        }
        for (String entity : entities) {
            if (key.contains(entity)) {
                return true;
            }
        }
        return false;
    }
}
