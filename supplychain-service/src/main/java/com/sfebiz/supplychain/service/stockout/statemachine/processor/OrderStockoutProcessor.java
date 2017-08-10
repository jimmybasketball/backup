package com.sfebiz.supplychain.service.stockout.statemachine.processor;

import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.alarm.EffectiveAlarmConfig;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderDetailManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderStateLogManager;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.biz.StockoutOrderNoticeBizService;
import com.sfebiz.supplychain.service.stockout.biz.StockoutOrderStateBizService;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>出库单下发TPL、PAY、PORT、WMS等仓库，订单状态流转为出库中</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/17
 * Time: 上午10:54
 */
@Component("orderStockoutProcessor")
public class OrderStockoutProcessor implements EngineProcessor<StockoutOrderRequest> {
    protected static final Logger           logger      = LoggerFactory
                                                            .getLogger(OrderStockoutProcessor.class);
    private static final HaitaoTraceLogger  traceLogger = HaitaoTraceLoggerFactory
                                                            .getTraceLogger("order");

    @Resource
    protected StockoutOrderStateBizService  stockoutOrderStateBizService;

    @Resource
    protected StockoutOrderNoticeBizService stockoutOrderNoticeBizService;

    @Resource
    protected StockoutOrderManager          stockoutOrderManager;

    @Resource
    protected StockoutOrderDetailManager    stockoutOrderSkuManager;

    @Resource
    protected LogisticsLineManager          lineManager;

    @Resource
    private StockoutOrderStateLogManager    stockoutOrderStateLogManager;

    @Resource
    private RouteService                    routeService;

    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {

        if (request.getStockoutOrderBO() == null) {
            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL,
                "出库单对象为null");
        }
        StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
        try {

            //设置出库单中创建命令成功发送时间
            Date eventDateTime = new Date();
            if (request.getEventTime() != null) {
                eventDateTime = request.getEventTime();
            }
            //更新状态表
            Date stockouAlarmTime = EffectiveAlarmConfig.getAlarmDate(stockoutOrderBO.getLineId(),
                eventDateTime, StockoutOrderState.STOCKOUTING.getValue());
            stockoutOrderStateLogManager
                .updateStockOutOrderStateLog(
                    StockoutOrderConvert.convertBOToDO(stockoutOrderBO),
                    StockoutOrderState.STOCKOUTING.getValue(),
                    null == request.getProcessDateTime() ? eventDateTime : request
                        .getProcessDateTime(), stockouAlarmTime, SystemUserName.SYSTEM.getValue(),
                    request.getStateChangeRemark());

            if (StockoutOrderType.SALES_STOCK_OUT.getValue() == stockoutOrderBO.getOrderType()) {
                //销售出库单 给订单发消息，设置订单为发货中
                //stockoutBizService.sendTradeOrder2Shipping(stockoutOrderBO.getBizId());

                if (stockoutOrderBO.getDeclarePayType().equalsIgnoreCase("ALIPAY")) {
                    //routeBizService.sendCustomsPayManager(stockoutOrderBO.getBizId(), 30 * 60);
                }

                //只有当国内物流供应商为SF的时候才需要进行路由轮询，其他国内物流供应商通过注册快递100完成路由的收集
                if ("SF".equalsIgnoreCase(stockoutOrderBO.getIntrCarrierCode())
                    || "ZTO".equalsIgnoreCase(stockoutOrderBO.getIntrCarrierCode())
                    || stockoutOrderBO.getLineId() == 204 || stockoutOrderBO.getWarehouseId() == 99
                    || stockoutOrderBO.getWarehouseId() == 124
                    || stockoutOrderBO.getWarehouseId() == 132
                    || stockoutOrderBO.getWarehouseId() == 11) {
                    //设置延迟18小时消息，18小时后尝试获取一次路由信息
                    //[Forest]根据物流同事的要求，将尝试获取路由信息缩短为6-12小时
                    Random random = new Random(new Date().getTime());

                    // TODO matt
                    if (stockoutOrderBO.getLineId() == 401 || stockoutOrderBO.getLineId() == 403) {
                        //                        routeBizService.sendFetchMailNoMessage(stockoutOrderBO.getBizId(),
                        //                            (6 + random.nextInt(6)) * 60 * 60);
                    } else {
                        routeService.sendRouteFetchMessage(stockoutOrderBO.getBizId(), 30 * 60L);
                    }
                }

            }

            //出库单已出库时，作为仓库向商户发送状态信息
            stockoutOrderNoticeBizService.sendMsgStockoutFinish2Merchant(
                stockoutOrderBO.getBizId(), 0, "");

            LogBetter
                .instance(logger)
                .setLevel(LogLevel.INFO)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, request.getTraceId(),
                        SystemConstants.TRACE_APP)).setMsg("[供应链-订单出库中]：状态变更为发货中").log();
            return new BaseResult(Boolean.TRUE);
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setErrorMsg("[供应链]变更出库单状态为出库中异常")
                .setException(e).log();
            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_STOCKOUT_EXCEPTION,
                "出库单状态变更异常，当前状态：" + stockoutOrderBO.getOrderState());
        }
    }

}
