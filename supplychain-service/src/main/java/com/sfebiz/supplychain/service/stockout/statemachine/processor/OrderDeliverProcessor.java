package com.sfebiz.supplychain.service.stockout.statemachine.processor;

import java.util.Date;
import java.util.List;

import net.pocrd.entity.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.alarm.EffectiveAlarmConfig;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderStateLogDO;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>订单已开始派件</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/17
 * Time: 下午4:38
 */
@Component("orderDeliverProcessor")
public class OrderDeliverProcessor extends TemplateProcessor implements
                                                            EngineProcessor<StockoutOrderRequest> {

    private static final Logger            logger      = LoggerFactory
                                                           .getLogger(OrderSendProcessor.class);
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");

    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {

        StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();

        //更新状态信息
        Date eventTime = request.getProcessDateTime() == null ? new Date() : request
            .getProcessDateTime();

        //派件中的预警时间归到未签收预警
        Date alarmTime = EffectiveAlarmConfig.getAlarmDate(stockoutOrderBO.getLineId(), eventTime,
            StockoutOrderState.DELIVEING.getValue());
        StockoutOrderStateLogDO stockoutOrderStateLogDO = new StockoutOrderStateLogDO();
        stockoutOrderStateLogDO.setStockoutOrderId(stockoutOrderBO.getId());
        stockoutOrderStateLogDO.setFromState(StockoutOrderState.COLLECTED.getValue());
        List<StockoutOrderStateLogDO> stockoutOrderStateLogDOs = stockoutOrderStateLogManager
            .query(BaseQuery.getInstance(stockoutOrderStateLogDO));
        if (CollectionUtils.isNotEmpty(stockoutOrderStateLogDOs)) {
            alarmTime = stockoutOrderStateLogDOs.get(0).getAlarmTime();
        }
        stockoutOrderStateLogManager.updateStockOutOrderStateLog(
            StockoutOrderConvert.convertBOToDO(stockoutOrderBO),
            StockoutOrderState.DELIVEING.getValue(), eventTime, alarmTime,
            SystemUserName.SYSTEM.getValue(), request.getStateChangeRemark());

        //出库单已出库时，作为仓库向商户发送状态信息
        stockoutOrderNoticeBizService.sendMsgStockoutFinish2Merchant(stockoutOrderBO.getBizId(), 0,
            "");

        traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(),
            TraceLog.TraceLevel.INFO, "发货单状态变更为[派件中]"));
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
            .addParm("发货单状态变更为[派件中]订单号：", stockoutOrderBO.getBizId());
        return new BaseResult(Boolean.TRUE);
    }
}
