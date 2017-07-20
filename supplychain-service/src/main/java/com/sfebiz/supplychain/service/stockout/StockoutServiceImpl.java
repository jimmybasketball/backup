/**
 * Copyright (C) 2017 上海牵趣网络科技有限公司 版权所有
 */
package com.sfebiz.supplychain.service.stockout;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.code.StockoutTaskReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.stockout.api.StockoutService;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderTaskManager;
import com.sfebiz.supplychain.service.stockout.service.exception.ExceptionHandlerFactory;

/**
 * <p>
 * 出库
 * </p>
 * 
 * @author wuyun
 * @Date 2017年7月18日 下午2:38:08
 */
@Service("stockoutService")
public class StockoutServiceImpl implements StockoutService {
    
    private static final Logger logger = LoggerFactory.getLogger(StockoutServiceImpl.class);
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    private static final String EXECUTE_TASK = StockoutServiceImpl.class + "EXECUTE_TASK";
    private static final String UPDATE_TASK_TIME = StockoutServiceImpl.class + "UPDATE_TASK_TIME";

    @Resource
    private StockoutOrderTaskManager stockoutOrderTaskManager;
    @Resource
    private Lock distributedLock;
    @Resource
    private ExceptionHandlerFactory exceptionHandlerFactory;
    /*
     * (non-Javadoc)
     * 
     * @see com.sfebiz.supplychain.exposed.stockout.api.StockoutService#
     * executeStockoutExceptionTaskByHandle(java.lang.Long, java.lang.Long, java.lang.String)
     */
    @Override
    @Transactional
    @MethodParamValidate
    public CommonRet<Void> executeStockoutExceptionTaskByHandle(
            @ParamNotBlank("业务订单ID不能为空") Long id, Long userId, String userName) {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
        .setMsg("[物流开放平台-立即重试异常任务]")
        .addParm("异常任务ID", id)
        .addParm("操作人", userName)
        .log();

        CommonRet<Void> commonRet = new CommonRet<Void>();
        if (null == id) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("[物流开放平台-立即重试异常任务]: "+SCReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("异常任务ID", id)
                    .addParm("操作人", userName)
                    .log();
            commonRet.setRetCode(SCReturnCode.PARAM_ILLEGAL_ERR.getCode());
            commonRet.setRetMsg(SCReturnCode.PARAM_ILLEGAL_ERR.getDesc());
            return commonRet;
        }
        String key = EXECUTE_TASK + id;
        if (distributedLock.fetch(key)) {
            try {
                StockoutOrderTaskDO stockoutOrderTaskDO = stockoutOrderTaskManager.getById(id);
                if (null == stockoutOrderTaskDO) {
                    LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                            .setErrorMsg("[物流开放平台-立即重试异常任务]: 此异常任务不存在")
                            .addParm("异常任务ID", id)
                            .addParm("操作人", userName)
                            .log();
                    commonRet.setRetCode(StockoutTaskReturnCode.STOCKOUTTASK_NOT_EXIST.getCode());
                    commonRet.setRetMsg(StockoutTaskReturnCode.STOCKOUTTASK_NOT_EXIST.getDesc());
                }
              //根据异常类型选择不同的处理类
                exceptionHandlerFactory.getExceptionHandlerByExceptionType(stockoutOrderTaskDO.getTaskType()).handle(stockoutOrderTaskDO);
            } catch (Exception e) {
                LogBetter.instance(logger)
                .setLevel(LogLevel.ERROR)
                .setErrorMsg("[物流开放平台-立即重试异常任务] 异常")
                .setException(e)
                .log();

                commonRet.setRetCode(StockoutTaskReturnCode.STOCKOUTTASK_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(e.getMessage());
                //事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }finally {
                distributedLock.realease(key);
            }
        }else{
            LogBetter.instance(logger).
            setLevel(LogLevel.ERROR).
            setMsg("[物流开放平台-立即重试异常任务] 并发异常")
            .log();
            commonRet.setRetCode(StockoutTaskReturnCode.STOCKOUTTASK_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg(StockoutTaskReturnCode.STOCKOUTTASK_CONCURRENT_EXCEPTION.getDesc());
        }
        return commonRet;
    }
    /* (non-Javadoc)
     * @see com.sfebiz.supplychain.exposed.stockout.api.StockoutService#updateStockoutExceptionTaskExecuteTime(java.lang.Long, java.util.Date, java.lang.Long, java.lang.String)
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> updateStockoutExceptionTaskExecuteTime(
            @ParamNotBlank("业务订单ID不能为空") Long id, @ParamNotBlank("task下次执行时间不能为空") Date executeTime, Long userId,
            String userName) {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
        .setMsg("[物流开放平台-更新异常任务的重试时间]")
        .addParm("异常任务ID", id)
        .addParm("操作人", userName)
        .log();

        CommonRet<Void> commonRet = new CommonRet<Void>();
        if (null == id) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("[物流开放平台-更新异常任务的重试时间]: "+SCReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("异常任务ID", id)
                    .addParm("操作人", userName)
                    .log();
            commonRet.setRetCode(SCReturnCode.PARAM_ILLEGAL_ERR.getCode());
            commonRet.setRetMsg(SCReturnCode.PARAM_ILLEGAL_ERR.getDesc());
            return commonRet;
        }
        String key = UPDATE_TASK_TIME + id;
        if (distributedLock.fetch(key)) {
            try {
                StockoutOrderTaskDO stockoutOrderTaskDO = stockoutOrderTaskManager.getById(id);
                if (null == stockoutOrderTaskDO) {
                    LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                            .setErrorMsg("[物流开放平台-更新异常任务的重试时间]: 此异常任务不存在")
                            .addParm("异常任务ID", id)
                            .addParm("操作人", userName)
                            .log();
                    commonRet.setRetCode(StockoutTaskReturnCode.STOCKOUTTASK_NOT_EXIST.getCode());
                    commonRet.setRetMsg(StockoutTaskReturnCode.STOCKOUTTASK_NOT_EXIST.getDesc());
                }
                StockoutOrderTaskDO update = new StockoutOrderTaskDO();
                update.setId(id);
                update.setRetryExcuteTime(executeTime);
                stockoutOrderTaskManager.update(update);
            } catch (Exception e) {
                LogBetter.instance(logger)
                .setLevel(LogLevel.ERROR)
                .setErrorMsg("[物流开放平台-更新异常任务的重试时间] 异常")
                .setException(e)
                .log();

                commonRet.setRetCode(StockoutTaskReturnCode.STOCKOUTTASK_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(e.getMessage());
            }finally {
                distributedLock.realease(key);
            }
        }else{
            LogBetter.instance(logger).
            setLevel(LogLevel.ERROR).
            setMsg("[物流开放平台-更新异常任务的重试时间] 并发异常")
            .log();
            commonRet.setRetCode(StockoutTaskReturnCode.STOCKOUTTASK_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg(StockoutTaskReturnCode.STOCKOUTTASK_CONCURRENT_EXCEPTION.getDesc());
        }
        return commonRet;
        
    }

}
