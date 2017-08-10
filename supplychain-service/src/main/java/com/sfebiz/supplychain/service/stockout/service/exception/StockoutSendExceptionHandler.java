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
import com.sfebiz.supplychain.exposed.common.code.StockoutReturnCode;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;

import net.pocrd.entity.ServiceException;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/8/24 下午5:29
 * <p/>
 * 下发出库单发货指令失败
 */
@Component("stockoutSendExceptionHandler")
public class StockoutSendExceptionHandler extends AbstractExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(StockoutSendExceptionHandler.class);

    @Override
    public void handle(StockoutOrderTaskDO stockoutOrderTaskDO) throws ServiceException {
        if (!checkExceptionTaskIsAlreadyHandle(stockoutOrderTaskDO)) {
            stockoutOrderTaskDO.setTaskDesc(null);
            JSONObject feature = JSON.parseObject(stockoutOrderTaskDO.getFeatures());
            String currentProcessorTag = feature.getString("currentProcssorTag");
            StockoutOrderBO stockoutOrderBO = null;
            boolean isHandleSuccess = false;
            try {
                if (stockoutOrderTaskDO.getStockoutOrderId() != null && stockoutOrderTaskDO.getId().longValue() > 0) {
                    stockoutOrderBO = stockoutOrderBOFactory.loadStockoutBOById(stockoutOrderTaskDO.getStockoutOrderId());
                }else{
                    throw new ServiceException(StockoutReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL, "[物流平台-重试异常任务]: 出库单stockoutOrderId为空或不合法");
                }
                if (null == stockoutOrderBO) {
                    throw new ServiceException(StockoutReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL, "[物流平台-重试异常任务]: 未查询到出库单信息");
                }

                isHandleSuccess = stockoutOrderBizService.executeStockoutSendProcesses(stockoutOrderBO, "again", currentProcessorTag);
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
//            else {
//                updateTaskExcuteTime(stockoutOrderTaskDO, Calendar.HOUR_OF_DAY);
//            }
        }
    }
}
