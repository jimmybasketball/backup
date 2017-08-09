package com.sfebiz.supplychain.service.stockout.biz;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskType;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.service.statemachine.BpmConstants;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.service.stockout.model.StockoutOrderRequestFactory;
import com.sfebiz.supplychain.service.stockout.process.create.DataPrepareProcessor;
import com.sfebiz.supplychain.service.stockout.process.exception.ExceptionProcessor;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderActionType;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;
import com.sfebiz.supplychain.service.stockout.statemachine.processor.OrderCreateProcessor;
import com.taobao.tbbpm.process.ProcessEngineFactory;

/**
 * 
 * <p>出库单业务服务类</p>
 * 
 * @author matt
 * @Date 2017年7月20日 上午9:56:08
 */
@Service("stockoutOrderBizService")
public class StockoutOrderBizService {

    private static final Logger            logger                        = LoggerFactory
                                                                             .getLogger(StockoutOrderBizService.class);
    private static final Logger            commandLogger                 = LoggerFactory
                                                                             .getLogger("CommandLogger");
    private static final HaitaoTraceLogger traceLogger                   = HaitaoTraceLoggerFactory
                                                                             .getTraceLogger("order");
    private static final String            SC_STOCKOUTORDER_DISPATCH_KEY = "SC_STOCKOUTORDER_DISPATCH_KEY";

    @Resource
    private OrderCreateProcessor           orderCreateProcessor;

    @Resource
    private ExceptionProcessor             exceptionProcessor;

    @Resource
    private Lock                           distributedLock;

    @Resource
    private FeeSplitBizService             feeSplitBizService;

    @Resource
    private StockoutOrderRecordManager     stockoutOrderRecordManager;

    @Resource
    private StockoutOrderStateBizService   stockoutOrderStateBizService;

    /**
     * 创建出库单
     * 
     * @param stockoutOrderBO
     * @throws ServiceException
     */
    public void createOrder(StockoutOrderBO stockoutOrderBO) throws ServiceException {
        orderCreateProcessor.createStockoutOrder(stockoutOrderBO);
    }

    /**
     * 执行出库单下发流程引擎
     *
     * @param stockoutOrderBO  出库单
     * @param exeType          执行类型，如果为 again 表示重试，此时需要传入currentProcssorTag，流程会从指定TAG开始执行
     * @param startProcssorTag 指定下发流程开始执行的TAG，如果为空，则表示从起始位置开始
     */
    public boolean executeStockoutSendProcesses(StockoutOrderBO stockoutOrderBO, String exeType,
                                                String startProcssorTag) throws Exception {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
            .addParm("[出库单下发流程引擎处理开始], 出库单信息", stockoutOrderBO).log();

        if (distributedLock.fetch(SC_STOCKOUTORDER_DISPATCH_KEY + stockoutOrderBO.getBizId())) {
            try {
                // 只有出库单状态为待发货的情况下，才执行出库单下发流程
                if (StockoutOrderState.WAIT_STOCKOUT.getValue().equalsIgnoreCase(
                    stockoutOrderBO.getOrderState())) {

                    StockoutOrderRequest stockoutOrderRequest = StockoutOrderRequestFactory
                        .generateStockoutOrderRequestForSendProcess(stockoutOrderBO);

                    if (StringUtils.isNotBlank(exeType) && "again".equalsIgnoreCase(exeType)
                        && StringUtils.isNotBlank(startProcssorTag)) {
                        // 处理流程从上次处理失败的节点开始继续处理
                        stockoutOrderRequest.setStartProcessorTag(startProcssorTag);
                        // 获取运费、税费拆分后的金额信息
                        StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceEntity = feeSplitBizService
                            .getPriceConfigFromDB(stockoutOrderBO, stockoutOrderBO.getDetailBOs(),
                                stockoutOrderBO.getLineBO());
                    }

                    BaseResult result = new BaseResult(Boolean.FALSE);
                    Map<String, Object> context = buildProcessorParams(stockoutOrderRequest, result);
                    Map<String, Object> processResult = ProcessEngineFactory.getProcessEngine()
                        .start(BpmConstants.STOCKOUTORDER_SEND_PROCESS_BPM_CODE, context);
                    BaseResult baseResult = (BaseResult) processResult.get(SystemConstants.RESULT);
                    LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .addParm("[出库单创建流程引擎处理结束],出库单信息", stockoutOrderBO)
                        .addParm("处理结果", baseResult.isSuccess())
                        .addParm("当前状态", stockoutOrderBO.getOrderState()).log();

                    if (baseResult.isSuccess()) {

                        stockoutOrderRecordManager.updateStockoutCmdsSuccessSendTime(
                            stockoutOrderBO.getId(), new Date());

                        // 更新仓库类型是虚拟仓且仓库未对接系统出库、有中转仓无海海外实仓的订单状态
                        if (LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue() != stockoutOrderBO
                            .getRecordBO().getLogisticsState()) {
                            stockoutOrderRecordManager.updateLogisticsState(
                                stockoutOrderBO.getId(),
                                LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                        }

                        // 若出库方式为INTERFACE，触发出库单状态至出库中
                        if (1 == stockoutOrderBO.getLineBO().getWarehouseBO()
                            .getLogisticsProviderBO().getIntegrationBO().getIsIntegrationStockout()) {
                            return stockoutOrderStateBizService.triggerOrderStateChange(
                                stockoutOrderBO, StockoutOrderActionType.STOCKOUT);
                        }

                    }
                    return baseResult.isSuccess();
                } else {
                    return true;
                }
            } catch (ServiceException e) {
                LogBetter.instance(commandLogger).setLevel(LogLevel.ERROR)
                    .addParm("[出库单创建流程引擎处理异常],出库单信息", stockoutOrderBO).addParm("处理结果", false)
                    .addParm("当前状态", stockoutOrderBO.getOrderState()).setException(e).log();
                return false;
            } finally {
                try {
                    distributedLock.realease(SC_STOCKOUTORDER_DISPATCH_KEY
                                             + stockoutOrderBO.getBizId());
                } catch (Exception e) {
                    LogBetter
                        .instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(
                            TraceLogEntity.instance(traceLogger,
                                stockoutOrderBO.getMerchantOrderNo(), SystemConstants.TRACE_APP))
                        .setErrorMsg("[供应链-出库单创建流程引擎处理异常]: 未能释放乐观锁")
                        .addParm("出库单详情", stockoutOrderBO).setException(e).log();
                }
            }
        } else {
            StockoutOrderRequest stockoutOrderRequest = StockoutOrderRequestFactory
                .generateStockoutOrderRequestForSendProcess(stockoutOrderBO,
                    TaskType.STOCKOUT_CREATE_EXCEPTION, "并发异常，等待系统半小时后自动重试",
                    DataPrepareProcessor.TAG);
            exceptionProcessor.doProcess(stockoutOrderRequest);
            LogBetter
                .instance(commandLogger)
                .setLevel(LogLevel.WARN)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                        SystemConstants.TRACE_APP)).setErrorMsg("[供应链-出库单下发失败]并发异常,等待自动重试")
                .setParms(stockoutOrderBO).log();
            return false;
        }
    }

    /**
     * 构建出库流程引擎所需要的数据
     *
     * @param request 请求参数
     * @param result  返回参数
     * @return 构造map结果
     */
    public Map<String, Object> buildProcessorParams(StockoutOrderRequest request, BaseResult result) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(SystemConstants.REQUEST, request);
        map.put(SystemConstants.RESULT, result);
        return map;
    }
}
