package com.sfebiz.supplychain.service.stockout.service.exception;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskStatus;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;

import net.pocrd.entity.ServiceException;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/8/24 下午5:22
 * <p/>
 * 仓库实际出库时，库存有异常
 */
@Component("stockoutStockExceptionHandler")
public class StockoutStockExceptionHandler extends AbstractExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(StockoutStockExceptionHandler.class);

    @Override
    public void handle(StockoutOrderTaskDO stockoutOrderTaskDO) throws ServiceException {
        if (!checkExceptionTaskIsAlreadyHandle(stockoutOrderTaskDO)) {
            StockoutOrderDO stockoutOrderDO = stockoutOrderManager.getById(stockoutOrderTaskDO.getStockoutOrderId());
            // 如果出库单已经被删除，则将异常认为置为已处理
            if (stockoutOrderDO == null) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderTaskDO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[物流开放平台-重试异常任务]：该订单已被删除，取消出库异常任务。")
                        .addParm("业务ID", stockoutOrderTaskDO.getBizId())
                        .addParm("异常类型", stockoutOrderTaskDO.getTaskType())
                        .log();
                stockoutOrderTaskDO.setTaskState(TaskStatus.HANDLE_SUCCESS.getValue());
                stockoutOrderTaskManager.update(stockoutOrderTaskDO);
                return;
            }
            // 如果当前出库单状态不为出库中，则将异常认为置为已处理
            if (stockoutOrderDO != null && !StockoutOrderState.STOCKOUTING.getValue().equals(stockoutOrderDO.getOrderState())) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderTaskDO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[物流开放平台-重试异常任务]：该订单状态不为出库中，取消出库异常任务。")
                        .addParm("业务ID", stockoutOrderTaskDO.getBizId())
                        .addParm("异常类型", stockoutOrderTaskDO.getTaskType())
                        .log();
                stockoutOrderTaskDO.setTaskState(TaskStatus.HANDLE_SUCCESS.getValue());
                stockoutOrderTaskManager.update(stockoutOrderTaskDO);
            } else {
                try {
//                    orderCreateProcessor.reCreateStockoutOrder(stockoutOrderDO, stockoutOrderTaskDO.getId());
                    //对接出库工作流后干掉
                    throw new ServiceException(SCReturnCode.PARAM_ILLEGAL_ERR, "[供应链-立即重试异常任务]: ");
                } catch (ServiceException e) {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.WARN)
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderTaskDO.getBizId(), SystemConstants.TRACE_APP))
                            .setErrorMsg("[物流开放平台-重试异常任务]: " + e.getMsg())
                            .addParm("业务ID", stockoutOrderTaskDO.getBizId())
                            .addParm("异常类型", stockoutOrderTaskDO.getTaskType())
                            .setException(e)
                            .log();
                    //更新异常信息
                    stockoutOrderTaskDO.setTaskDesc(e.getMsg());
                    updateTaskExcuteTime(stockoutOrderTaskDO, Calendar.DAY_OF_MONTH);
                }
            }
        }
    }
}
