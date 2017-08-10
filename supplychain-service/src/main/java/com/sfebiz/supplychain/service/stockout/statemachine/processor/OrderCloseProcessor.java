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
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>订单已被人工关闭</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/17
 * Time: 下午4:39
 */
@Component("orderCloseProcessor")
public class OrderCloseProcessor extends TemplateProcessor implements
                                                          EngineProcessor<StockoutOrderRequest> {
    private static final Logger            logger      = LoggerFactory
                                                           .getLogger(OrderSendProcessor.class);
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");

    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {
        //更新状态信息
        Date eventTime = request.getProcessDateTime() == null ? new Date() : request
            .getProcessDateTime();
        stockoutOrderStateLogManager.updateStockOutOrderStateLog(
            StockoutOrderConvert.convertBOToDO(request.getStockoutOrderBO()),
            StockoutOrderState.COLLECTED.getValue(), eventTime, SystemUserName.SYSTEM.getValue());

        traceLogger.log(new TraceLog(request.getStockoutOrderBO().getBizId(), "supplychain",
            new Date(), TraceLog.TraceLevel.INFO, "发货单状态变更为[已关闭]"));
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
            .addParm("发货单状态变更为[已关闭] 订单号：", request.getStockoutOrderBO().getBizId());
        return new BaseResult(Boolean.TRUE);
    }
}
