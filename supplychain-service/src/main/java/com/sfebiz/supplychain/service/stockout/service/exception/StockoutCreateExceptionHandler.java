package com.sfebiz.supplychain.service.stockout.service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskStatus;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;

import net.pocrd.entity.ServiceException;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/8/24 下午5:29
 * <p/>
 * 下发出库单创建指令失败
 */
@Component("stockoutCreateExceptionHandler")
public class StockoutCreateExceptionHandler extends AbstractExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(StockoutCreateExceptionHandler.class);

    @Override
    public void handle(StockoutOrderTaskDO stockoutOrderTaskDO) throws ServiceException {
        if (!checkExceptionTaskIsAlreadyHandle(stockoutOrderTaskDO)) {
            StockoutOrderDO stockoutOrderDO = stockoutOrderManager.getById(stockoutOrderTaskDO.getStockoutOrderId());
            if (!StockoutOrderState.WAIT_STOCKOUT.getValue().equalsIgnoreCase(stockoutOrderDO.getOrderState())) {
                //如果此时出库单状态不为待出库，则将任务表置为已处理
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderTaskDO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[物流开放平台-重试异常任务]：该出库单已不是待出库状态，忽略")
                        .addParm("业务ID", stockoutOrderTaskDO.getBizId())
                        .addParm("异常类型", stockoutOrderTaskDO.getTaskType())
                        .addParm("出库单状态", stockoutOrderDO.getOrderState())
                        .log();
                stockoutOrderTaskDO.setTaskState(TaskStatus.HANDLE_SUCCESS.getValue());
                finishTaskHandle(stockoutOrderTaskDO);
            } else {
                JSONObject feature = JSON.parseObject(stockoutOrderTaskDO.getFeatures());
                String currentProcessorTag = feature.getString("currentProcssorTag");
                boolean isHandleSuccess = false;
                try {
                    if (stockoutOrderDO.getOrderType() == StockoutOrderType.ALLOCATION_STOCK_OUT.getValue()) {
//                        isHandleSuccess = stockoutBizService
//                                .executeTransferOutOrderCreateProcesses(stockoutOrderDO, "again", currentProcessorTag);
                    } else {
//                        isHandleSuccess = stockoutBizService
//                                .executeStockoutCreateProcesses(stockoutOrderDO, "again", currentProcessorTag);
                    }
                    //对接出库工作流后干掉
                    throw new ServiceException(SCReturnCode.PARAM_ILLEGAL_ERR, "[物流平台-立即重试异常任务]: ");
                } catch (ServiceException se) {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.WARN)
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderTaskDO.getBizId(), SystemConstants.TRACE_APP))
                            .setErrorMsg("[物流开放平台-重试异常任务]: " + se.getMsg())
                            .addParm("业务ID", stockoutOrderTaskDO.getBizId())
                            .addParm("异常类型", stockoutOrderTaskDO.getTaskType())
                            .setException(se)
                            .log();
                } catch (Exception e) {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.WARN)
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderTaskDO.getBizId(), SystemConstants.TRACE_APP))
                            .setErrorMsg("[物流开放平台-重试异常任务]: " + e.getMessage())
                            .addParm("业务ID", stockoutOrderTaskDO.getBizId())
                            .addParm("异常类型", stockoutOrderTaskDO.getTaskType())
                            .setException(e)
                            .log();
                }
                if (isHandleSuccess) {
                    //处理成功后，需要设置异常Task中任务为已解决
                    finishTaskHandle(stockoutOrderTaskDO);
                }
//                else {
//                    updateTaskExcuteTime(stockoutOrderTaskDO, Calendar.HOUR_OF_DAY);
//                }
            }
        }
    }
}
