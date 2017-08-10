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
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>物流快递仓库收件</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/17
 * Time: 下午2:55
 */
@Component("orderPostProcessor")
public class OrderPostProcessor extends TemplateProcessor implements
                                                         EngineProcessor<StockoutOrderRequest> {
    private static final Logger            logger      = LoggerFactory
                                                           .getLogger(OrderSendProcessor.class);
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");

    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {
        try {
            StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
            if (stockoutOrderBO == null) {
                throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL,
                    "出库单对象为null");
            }

            //更新状态信息
            Date eventTime = null == request.getProcessDateTime() ? new Date() : request
                .getProcessDateTime();
            Date postAlarmTime = EffectiveAlarmConfig.getAlarmDate(stockoutOrderBO.getLineId(),
                eventTime, StockoutOrderState.COLLECTED.getValue());
            stockoutOrderStateLogManager.updateStockOutOrderStateLog(
                StockoutOrderConvert.convertBOToDO(stockoutOrderBO),
                StockoutOrderState.COLLECTED.getValue(), eventTime, postAlarmTime,
                SystemUserName.SYSTEM.getValue(), request.getStateChangeRemark());

            //给订单发送消息，mailno及carriercode
            //            stockoutBizService.sendTradeMailNo(request.getLineEntity(), stockoutOrderDO,
            //                DateUtil.defFormatDateStr(eventTime));
            // TODO matt 通知外部商户运单号

            //出库单已出库时，作为仓库向商户发送状态信息
            stockoutOrderNoticeBizService.sendMsgStockoutFinish2Merchant(
                stockoutOrderBO.getBizId(), 0, "");

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(),
                TraceLog.TraceLevel.INFO, "发货单状态变更为[物流已收货]"));
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .addParm("发货单状态变更为[物流已收货]订单号：", stockoutOrderBO.getBizId());
            return new BaseResult(Boolean.TRUE);
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                .addParm("出库处理异常,订单ID", request.getStockoutOrderBO().getBizId())
                .addParm("订单DO", request.getStockoutOrderBO())
                .addParm("线路实体", request.getStockoutOrderBO().getLineBO())
                .addParm("线路mamager", logisticsLineManager).setException(e).log();
            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_SEND_EXCEPTION, "发货处理异常");
        }
    }
}
