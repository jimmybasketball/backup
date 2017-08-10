package com.sfebiz.supplychain.service.stockout.statemachine.processor;

import java.util.Date;

import net.pocrd.entity.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.alarm.EffectiveAlarmConfig;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * Created by Zhangyajing on 2016/5/6.
 */
@Component("orderClearanceDoneProcessor")
public class OrderClearanceDoneProcessor extends TemplateProcessor
                                                                  implements
                                                                  EngineProcessor<StockoutOrderRequest> {

    private static final Logger            logger      = LoggerFactory
                                                           .getLogger(OrderAuditProcessor.class);
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");

    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链]变更出库单状态为清关完成")
            .addParm("出库单相关参数", request).log();
        try {
            if (request == null || request.getStockoutOrderBO() == null) {
                throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL,
                    "出库单相关参数实体为 null");
            }
            StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();

            //记录状态变更日志
            Date eventTime = request.getProcessDateTime() == null ? new Date() : request
                .getProcessDateTime();
            Date overseaStockinAlarmTime = EffectiveAlarmConfig.getAlarmDate(
                stockoutOrderBO.getLineId(), eventTime,
                StockoutOrderState.OVERSEA_STOCKIN_FINISH.getValue());
            stockoutOrderStateLogManager.updateStockOutOrderStateLog(
                StockoutOrderConvert.convertBOToDO(stockoutOrderBO),
                StockoutOrderState.CLEARANCE_END.getValue(), eventTime, overseaStockinAlarmTime,
                SystemUserName.SYSTEM.getValue());

            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链]状态变更日志记录成功")
                .addParm("出库单ID", stockoutOrderBO.getId())
                .addParm("订单ID", stockoutOrderBO.getBizId())
                .addParm("主订单ID", stockoutOrderBO.getMerchantOrderNo()).log();

            if (request.getOperator() != null && request.getOperator().getName() != null
                && request.getOperator().getName().equals("processEngine")) {
                BaseResult result = new BaseResult(true);
                traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(),
                    TraceLog.TraceLevel.INFO, "发货单状态变更为[清关完成]"));

                return result;
            }

            routeService.appendUserRoute(stockoutOrderBO.getBizId(), RouteType.CLEARANCE.getType(),
                buildLogisticsUserRoute(stockoutOrderBO.getBizId(), "包裹完成清关", eventTime.getTime()));
            BaseResult result = new BaseResult(true);

            //出库单已出库时，作为仓库向商户发送状态信息
            stockoutOrderNoticeBizService.sendMsgStockoutFinish2Merchant(
                stockoutOrderBO.getBizId(), 0, "");

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(),
                TraceLog.TraceLevel.INFO, "发货单状态变更为[清关完成]"));

            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setErrorMsg("[供应链]变更出库单状态为清关完成异常")
                .setException(e).log();
            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_STOCKOUT_EXCEPTION,
                "出库单状态变更异常，当前状态：" + request.getStockoutOrderBO().getOrderState());
        }
    }
}
