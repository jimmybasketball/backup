package com.sfebiz.supplychain.service.stockout.statemachine.processor;

import java.util.Date;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.alarm.EffectiveAlarmConfig;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderStateLogManager;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>出库单运营点击审核处理器</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/17
 * Time: 上午10:36
 */
@Component("orderAuditProcessor")
public class OrderAuditProcessor implements EngineProcessor<StockoutOrderRequest> {

    private static final Logger            logger      = LoggerFactory
                                                           .getLogger(OrderAuditProcessor.class);
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");
    @Resource
    private StockoutOrderStateLogManager   stockoutOrderStateLogManager;
    @Resource
    private RouteService                   routeService;

    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链]变更出库单状态为待出库")
            .addParm("出库单相关参数", request).log();
        try {
            if (request == null || request.getStockoutOrderBO() == null) {
                throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL,
                    "出库单相关参数实体为 null");
            }
            StockoutOrderBO stockoutOrderBO = new StockoutOrderBO();

            //记录状态变更日志
            Date eventTime = request.getProcessDateTime() == null ? new Date() : request
                .getProcessDateTime();
            Date waitStockoutAlarmTime = EffectiveAlarmConfig
                .getAlarmDate(stockoutOrderBO.getLineId(), eventTime,
                    StockoutOrderState.WAIT_STOCKOUT.getValue());
            stockoutOrderStateLogManager.updateStockOutOrderStateLog(
                StockoutOrderConvert.convertBOToDO(stockoutOrderBO),
                StockoutOrderState.WAIT_STOCKOUT.getValue(), eventTime, waitStockoutAlarmTime,
                SystemUserName.SYSTEM.getValue());

            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链]状态变更日志记录成功")
                .addParm("出库单ID", stockoutOrderBO.getId())
                .addParm("订单ID", stockoutOrderBO.getBizId())
                .addParm("商户订单ID", stockoutOrderBO.getMerchantOrderNo()).log();

            saveAuditSuccessRoute(stockoutOrderBO);
            BaseResult result = new BaseResult(true);
            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(),
                TraceLog.TraceLevel.INFO, "发货单状态变更为[待出库]"));

            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setErrorMsg("[供应链]变更出库单状态为出库中异常")
                .setException(e).log();
            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_STOCKOUT_EXCEPTION,
                "出库单状态变更异常，当前状态：" + request.getStockoutOrderBO().getOrderState());
        }
    }

    private void saveAuditSuccessRoute(StockoutOrderBO stockoutOrderBO) {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "包裹待出库",
            SystemConstants.INFO_LEVEL, new Date(), SystemUserName.SYSTEM.getValue());
    }
}
