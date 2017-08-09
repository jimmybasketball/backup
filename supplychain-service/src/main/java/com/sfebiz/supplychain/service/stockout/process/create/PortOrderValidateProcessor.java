package com.sfebiz.supplychain.service.stockout.process.create;

import java.util.Date;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskType;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.port.model.LogisticsPortBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.process.create.validate.StockoutOrderValidator;
import com.sfebiz.supplychain.service.stockout.process.exception.ExceptionProcessor;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>口岸避税校验， 口岸一天一人的限额为1000RMB</p>
 * @author matt
 * @Date 2017年7月25日 下午6:37:22
 */
@Component("portOrderValidateProcessor")
public class PortOrderValidateProcessor extends StockoutProcessAction {

    public static final String             TAG               = "PORT_VALIDATE";
    private static final HaitaoTraceLogger traceLogger       = HaitaoTraceLoggerFactory
                                                                 .getTraceLogger("order");
    private static final Logger            logger            = LoggerFactory
                                                                 .getLogger(PortOrderValidateProcessor.class);
    private static final String            VALIDATE_PORT_KEY = "VALIDATE_PORT_KEY";

    @Resource
    StockoutOrderValidator                 stockoutOrderRiskValidator;
    @Resource
    StockoutOrderValidator                 stockoutOrderCustomsValidator;
    @Resource
    StockoutOrderValidator                 stockoutOrderPortValidator;
    @Resource
    private StockoutOrderManager           stockoutOrderManager;
    @Resource
    private StockoutOrderRecordManager     stockoutOrderRecordManager;
    @Resource
    private Lock                           distributedLock;
    @Resource
    private ExceptionProcessor             exceptionProcessor;
    @Resource
    private LogisticsLineManager           logisticsLineManager;

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {
        BaseResult result = new BaseResult();
        String lockKey = VALIDATE_PORT_KEY;
        StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
        LogisticsLineBO lineBO = request.getLineBO();
        if (stockoutOrderBO == null || CollectionUtils.isEmpty(request.getDetailBOs())
            || lineBO == null) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("出库单相关参数实体为 null")
                .addParm("出库单信息", stockoutOrderBO).log();
            request.setExceptionMessage("[供应链-下发出库单]出库单相关参数实体为空");
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.STOCKOUT_DATA_IS_NULL,
                LogisticsReturnCode.STOCKOUT_DATA_IS_NULL.getDesc()));
            return new BaseResult(Boolean.FALSE);
        }

        boolean isMockAutoCreated = MockConfig.isMocked("port", "createCommand");
        if (isMockAutoCreated) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("校验出库单是否满足避税规则限制MOCK")
                .addParm("出库单", stockoutOrderBO.getBizId()).log();
            return new BaseResult(Boolean.TRUE);
        }

        // 如果是测试路线，直接跳过口岸验证
        // TODO matt
        if (stockoutOrderBO.getLineId().intValue() >= 100
            && stockoutOrderBO.getLineId().intValue() < 200) {
            return new BaseResult(Boolean.TRUE);
        }

        //如果是国内仓，不走口岸，则不需要进行口岸验证
        if (lineBO != null && lineBO.getPortBO() == null) {
            return new BaseResult(Boolean.TRUE);
        }

        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("校验出库单是否满足避税规则限制")
            .addParm("出库单", stockoutOrderBO.getBizId()).log();
        // TODO matt
        //Boolean isSkipPortValidate = OrderConfig.getIsSkipPortValidate(stockoutOrderBO.getBizId());
        Boolean isSkipPortValidate = false;
        if (isSkipPortValidate) {
            //是否跳过口岸验证逻辑
            stockoutOrderRecordManager.updatePortValidatePassTime(stockoutOrderBO.getId(),
                new Date());
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-验证避税规则]:人工设置跳过口岸避税验证逻辑。")
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                        SystemConstants.TRACE_APP)).addParm("出库单", stockoutOrderBO.getBizId())
                .log();
            result.setSuccess(Boolean.TRUE);
            return result;
        }

        LogisticsPortBO portEntity = lineBO.getPortBO();
        if (portEntity != null) {
            lockKey = lockKey + portEntity.getPortNid();
        } else {
            lockKey = lockKey + lineBO.getLogisticsLineNid();
        }
        lockKey = lockKey + stockoutOrderBO.getBuyerBO().getBuyerCertNo();

        //需要按照人和口岸或路线的级别锁定
        boolean isGetLock = this.tryToGetLock(lockKey, 3);
        if (!isGetLock) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("验证避税规则失败未获取并发锁")
                .addParm("出库单", request.getStockoutOrderBO().getBizId()).log();
            result.setSuccess(false);
            return result;
        }
        try {
            boolean resault = true;

            if (!stockoutOrderCustomsValidator.validate(request)
                || !stockoutOrderRiskValidator.validate(request)
                || !stockoutOrderPortValidator.validate(request)) {
                resault = false;
            }

            if (resault) {
                //验证通过更新验证通过时间
                stockoutOrderRecordManager.updatePortValidatePassTime(stockoutOrderBO.getId(),
                    new Date());
                result.setSuccess(Boolean.TRUE);
            } else {
                request
                    .setExceptionMessage("[供应链-验证避税规则]出库单避税规则验证未通过"
                                         + (StringUtils.isNoneEmpty(request.getExceptionMessage()) ? ": "
                                                                                                     + request
                                                                                                         .getExceptionMessage()
                                             : ""));
                request.setServiceException(new ServiceException(
                    LogisticsReturnCode.STOCKOUT_ORDER_WMS_PORT_VALIDATE_FAILURE,
                    LogisticsReturnCode.STOCKOUT_ORDER_WMS_PORT_VALIDATE_FAILURE.getDesc()));
                result.setSuccess(Boolean.FALSE);
            }
            return result;
        } catch (Exception e) {
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.WARN)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO().getBizId(),
                        SystemConstants.TRACE_APP)).setErrorMsg("[供应链-验证避税规则]验证失败").setException(e)
                .setParms(request.getStockoutOrderBO().getBizId()).log();
            request.setExceptionMessage("[供应链-验证避税规则]验证失败");
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.STOCKOUT_ORDER_WMS_PORT_VALIDATE_FAILURE,
                LogisticsReturnCode.STOCKOUT_ORDER_WMS_PORT_VALIDATE_FAILURE.getDesc()));
            return new BaseResult(Boolean.FALSE);
        } finally {
            //7. 释放乐观锁
            try {
                distributedLock.realease(lockKey);
            } catch (Exception e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链-验证避税规则]未能释放乐观锁")
                    .addParm("出库单详情", request.getStockoutOrderBO()).setException(e).log();
                request.setExceptionMessage("[供应链-验证避税规则]未能释放乐观锁");
                request.setServiceException(new ServiceException(
                    LogisticsReturnCode.LOGISTICS_CONCURRENT_ERR,
                    LogisticsReturnCode.LOGISTICS_CONCURRENT_ERR.getDesc()));
                return new BaseResult(Boolean.FALSE);
            }
        }
    }

    /**
     * 重写验证是否需要执行的判断 每次执行process必须执行此验证节点
     *
     * @param request
     * @return
     * @throws ServiceException
     */
    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {
        BaseResult result = new BaseResult();
        result = doProcess(request);
        request.setCurrentProcssorTag(getProcessTag());
        return result;
    }

    /**
     * 获取乐观锁
     *
     * @param key
     * @param count
     * @return
     */
    private boolean tryToGetLock(String key, int count) {
        count = count <= 0 ? 3 : count;
        for (; count > 0; count--) {
            try {
                boolean isGetLock = distributedLock.fetch(key);
                if (isGetLock) {
                    return true;
                } else {
                    LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .setErrorMsg("[供应链-验证口岸规则]: 尝试获取乐观锁失败").addParm("次数", count).log();
                    Thread.sleep(1000 * count);
                }
            } catch (Exception e) {
                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setErrorMsg("[供应链-验证口岸规则]: 尝试获取乐观锁失败").addParm("次数", count).setException(e)
                    .log();
            }
        }
        return false;
    }

    /**
     * 获取应该执行的出库单,如果不需要过海关，则直接执行；
     * 背景：由于口岸单数限制、关税限制等，可能会导致一些订单进入轮询队列，当新单到达的时候，优先执行之前由于口岸限制被卡的订单，当前订单进入等待队列
     *
     * @param stockoutOrderDO
     * @return 如果不存在历史被口岸限制的订单或者获取历史被限制的订单失败，都返回当前订单，保证业务的正常执行
     * @throws ServiceException
     */
    public StockoutOrderBO getShouldExecuteStockoutOrderByFirstInFistOut(StockoutOrderBO stockoutOrderBO)
                                                                                                         throws ServiceException {
        LogisticsLineBO lineEntity = stockoutOrderBO.getLineBO();
        if (lineEntity != null && lineEntity.getPortBO() == null) {
            return stockoutOrderBO;
        }

        StockoutOrderDO firstPortValidateOrder = stockoutOrderManager
            .getFirstPortValidateOrderByIdNo(stockoutOrderBO.getBuyerBO().getBuyerCertNo(),
                lineEntity.getPortBO().getId());
        if (firstPortValidateOrder != null) {
            StockoutOrderRequest stockoutOrderRequest = new StockoutOrderRequest();
            stockoutOrderRequest.setBizId(stockoutOrderBO.getBizId());
            stockoutOrderRequest.setStockoutOrderBO(stockoutOrderBO);
            stockoutOrderRequest.setExceptionType(TaskType.STOCKOUT_CREATE_EXCEPTION.getValue());
            stockoutOrderRequest.setExceptionMessage("口岸限制，优先执行先进入的订单："
                                                     + firstPortValidateOrder.getBizId());
            stockoutOrderRequest.setCurrentProcssorTag(getProcessTag());
            stockoutOrderRequest.setNextRetryTime(new Date());
            BaseResult baseResult = exceptionProcessor.doProcess(stockoutOrderRequest);
            if (baseResult.isSuccess()) {
                return StockoutOrderConvert.convertDOToBO(firstPortValidateOrder);
            } else {
                return stockoutOrderBO;
            }
        } else {
            return stockoutOrderBO;
        }
    }

    @Override
    public String getProcessTag() {
        return TAG;
    }
}
