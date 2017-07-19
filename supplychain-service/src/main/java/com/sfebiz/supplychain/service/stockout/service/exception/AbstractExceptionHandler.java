package com.sfebiz.supplychain.service.stockout.service.exception;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskStatus;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderTaskManager;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/8/24 下午5:53
 */
public abstract class AbstractExceptionHandler implements ExceptionHandler {

    public final static HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private final static Logger logger = LoggerFactory.getLogger(AbstractExceptionHandler.class);
    @Resource
    StockoutOrderManager stockoutOrderManager;

    @Resource
    StockoutOrderTaskManager stockoutOrderTaskManager;

//    @Resource
//    OrderCreateProcessor orderCreateProcessor;
//
//    @Resource
//    StockoutBizService stockoutBizService;
//
//    @Resource
//    OuterService outerService;

    /**
     * 判断异常类是否已处理结束
     *
     * @param stockoutOrderTaskDO
     * @return
     */
    public Boolean checkExceptionTaskIsAlreadyHandle(StockoutOrderTaskDO stockoutOrderTaskDO) {
        if (StringUtils.equalsIgnoreCase(stockoutOrderTaskDO.getTaskState(), TaskStatus.HANDLE_SUCCESS.getValue())) {
            //如果已经处理成功，则直接返回
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderTaskDO.getBizId(), SystemConstants.TRACE_APP))
                    .setMsg("[物流开放平台-重试异常任务]: 异常任务已经执行成功，忽略")
                    .addParm("业务ID", stockoutOrderTaskDO.getBizId())
                    .addParm("异常类型", stockoutOrderTaskDO.getTaskType())
                    .log();
            return true;
        }
        return false;
    }

    /**
     * 完成异常任务单
     *
     * @param stockoutOrderTaskDO
     */
    public void finishTaskHandle(StockoutOrderTaskDO stockoutOrderTaskDO) {
        if (stockoutOrderTaskDO == null || stockoutOrderTaskDO.getId() == null) {
            throw new IllegalArgumentException("参与不能为空");
        }
        StockoutOrderTaskDO newStockoutOrderTaskDO = new StockoutOrderTaskDO();
        newStockoutOrderTaskDO.setId(stockoutOrderTaskDO.getId());
        newStockoutOrderTaskDO.setStockoutOrderId(stockoutOrderTaskDO.getStockoutOrderId());
        newStockoutOrderTaskDO.setTaskState(TaskStatus.HANDLE_SUCCESS.getValue());
        newStockoutOrderTaskDO.setGmtModified(new Date());
        newStockoutOrderTaskDO.setMerchantId(stockoutOrderTaskDO.getMerchantId());
        stockoutOrderTaskManager.update(newStockoutOrderTaskDO);

    }

    /**
     * 更新异常任务下次执行时间
     *
     * @param stockoutOrderTaskDO
     */
    public void updateTaskExcuteTime(StockoutOrderTaskDO stockoutOrderTaskDO,int calendarType) {
        if (stockoutOrderTaskDO == null || stockoutOrderTaskDO.getId() == null) {
            throw new IllegalArgumentException("参与不能为空");
        }
        StockoutOrderTaskDO newStockoutOrderTaskDO = new StockoutOrderTaskDO();
        newStockoutOrderTaskDO.setId(stockoutOrderTaskDO.getId());
        newStockoutOrderTaskDO.setGmtModified(new Date());
        int retryTimes = stockoutOrderTaskDO.getRetryTimes() == null ? 0 :stockoutOrderTaskDO.getRetryTimes();
        newStockoutOrderTaskDO.setRetryTimes(retryTimes + 1);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        //让下次执行时间加上重试次数
        calendar.set(calendarType,calendar.get(calendarType) + retryTimes);
        newStockoutOrderTaskDO.setRetryExcuteTime(calendar.getTime());

        stockoutOrderTaskManager.update(newStockoutOrderTaskDO);
    }

    /**
     * 更新异常任务下次执行时间和异常信息
     *
     * @param stockoutOrderTaskDO
     */
    public void updateTaskExcuteTimeAndMemo(StockoutOrderTaskDO stockoutOrderTaskDO,int calendarType) {
        if (stockoutOrderTaskDO == null || stockoutOrderTaskDO.getId() == null) {
            throw new IllegalArgumentException("参与不能为空");
        }
        StockoutOrderTaskDO newStockoutOrderTaskDO = new StockoutOrderTaskDO();
        newStockoutOrderTaskDO.setId(stockoutOrderTaskDO.getId());
        newStockoutOrderTaskDO.setGmtModified(new Date());
        int retryTimes = stockoutOrderTaskDO.getRetryTimes() == null ? 0 :stockoutOrderTaskDO.getRetryTimes();
        newStockoutOrderTaskDO.setRetryTimes(retryTimes + 1);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        //让下次执行时间加上重试次数
        calendar.set(calendarType,calendar.get(calendarType) + retryTimes);
        newStockoutOrderTaskDO.setRetryExcuteTime(calendar.getTime());
        newStockoutOrderTaskDO.setTaskDesc(stockoutOrderTaskDO.getTaskDesc());

        stockoutOrderTaskManager.update(newStockoutOrderTaskDO);
    }

}
