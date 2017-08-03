package com.sfebiz.supplychain.service.stockin.statemachine.process;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.StockInReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderState;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderStateLogManager;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequest;
import net.pocrd.entity.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/5/18 下午6:57
 */
@Component("stockinOrderCancelProcessor")
public class StockinOrderCancelProcessor extends StockinAbstractProcessor {

    private static Logger logger = LoggerFactory.getLogger(StockinOrderCancelProcessor.class);

    @Resource
    StockinOrderManager stockinOrderManager;

    @Resource
    StockinOrderStateLogManager stockinOrderStateLogManager;

    @Override
    public BaseResult process(StockinOrderRequest request) throws ServiceException {

        StockinOrderDO stockinOrderDO = request.getStockinOrderDO();

        //1. 验证请求是否正确
        validateRequest(request);

        //2. 如果入库单已提交仓库，需要向仓库下发取消指令
        if (!stockinOrderDO.getState().equals(StockinOrderState.STOCKIN_FINISH.getValue())) {
            cancelStockinOrder(request);
        }

        //3. 更新状态日志

        String state = stockinOrderDO.getState();
        stockinOrderStateLogManager.insertOrUpdate(
                stockinOrderDO.getId(), request.getOperator().getId(), request.getOperator().getName(), StockinOrderState.STOCKIN_CANCLE.getValue());

        return BaseResult.SUCCESS_RESULT;
    }

    protected void cancelStockinOrder(StockinOrderRequest request) throws ServiceException {
        LogBetter.instance(logger)
                .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                .setMsg("[供应链-取消入库单]: 入库单的模式是寄售不入库或仓库的入库方式是手工入库, 直接改状态")
                .addParm("请求参数", request)
                .log();

    }

    @Override
    public void validateRequest(StockinOrderRequest request) throws ServiceException {
        super.validateRequest(request);

        // 取消操作:除了已完成状态下都允许取消
        if (request.getStockinOrderDO().getState().equals(StockinOrderState.STOCKIN_FINISH.getValue())) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_CANCEL,
                    "[供应链-取消入库单异常]: " + StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_CANCEL.getDesc() + " "
                            + "[入库单ID: " + request.getId()
                            + ", 操作者: " + request.getOperator().getName()
                            + "]");
        }
    }
}
