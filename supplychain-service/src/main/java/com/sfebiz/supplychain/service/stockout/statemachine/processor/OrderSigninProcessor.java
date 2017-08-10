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
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderStateLogManager;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.biz.StockoutOrderNoticeBizService;
import com.sfebiz.supplychain.service.stockout.biz.StockoutOrderStateBizService;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>订单已被用户签收</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/17
 * Time: 下午4:39
 */
@Component("orderSigninProcessor")
public class OrderSigninProcessor implements EngineProcessor<StockoutOrderRequest> {
    private static final Logger             logger      = LoggerFactory
                                                            .getLogger(OrderSendProcessor.class);
    private static final HaitaoTraceLogger  traceLogger = HaitaoTraceLoggerFactory
                                                            .getTraceLogger("order");

    @Resource
    protected StockoutOrderStateBizService  stockoutOrderStateBizService;

    @Resource
    protected StockoutOrderNoticeBizService stockoutOrderNoticeBizService;

    @Resource
    private StockoutOrderStateLogManager    stockoutOrderStateLogManager;

    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {

        if (request == null || request.getStockoutOrderBO() == null) {
            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL,
                "出库单相关参数实体为 null");
        }
        StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
        //更新订单为已发货
        //stockoutOrderStateBizService.sendTradeOrder2Signed(request.getStockoutOrderDO(), null);

        //更新状态信息
        Date eventTime = request.getProcessDateTime() == null ? new Date() : request
            .getProcessDateTime();
        stockoutOrderStateLogManager.updateStockOutOrderStateLog(
            StockoutOrderConvert.convertBOToDO(stockoutOrderBO),
            StockoutOrderState.SIGNED.getValue(), eventTime, SystemUserName.SYSTEM.getValue(),
            request.getStateChangeRemark());

        //出库单已出库时，作为仓库向商户发送状态信息
        stockoutOrderNoticeBizService.sendMsgStockoutFinish2Merchant(stockoutOrderBO.getBizId(), 0,
            "");

        traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(),
            TraceLog.TraceLevel.INFO, "发货单状态变更为[已签收]"));
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
            .addParm("发货单状态变更为[已签收] 订单号：", stockoutOrderBO.getBizId());
        return new BaseResult(Boolean.TRUE);
    }
}
