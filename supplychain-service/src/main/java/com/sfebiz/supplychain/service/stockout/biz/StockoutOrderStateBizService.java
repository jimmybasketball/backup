package com.sfebiz.supplychain.service.stockout.biz;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.service.statemachine.EngineService;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.model.StockoutOrderRequestFactory;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderActionType;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * 
 * <p>出库单状态服务</p>
 * @author matt
 * @Date 2017年7月26日 下午6:57:29
 */
@Service("stockoutOrderStateBizService")
public class StockoutOrderStateBizService {

    private static final Logger           LOGGER = LoggerFactory
                                                     .getLogger(StockoutOrderStateBizService.class);

    @Resource
    private EngineService                 engineService;

    @Resource
    private StockoutOrderNoticeBizService stockoutOrderNoticeBizService;

    private boolean triggerOrderStateChange(StockoutOrderRequest stockoutOrderRequest)
                                                                                      throws ServiceException {
        StockoutOrderBO orderBO = stockoutOrderRequest.getStockoutOrderBO();
        try {
            BaseResult baseResult = engineService.executeStateMachineEngine(stockoutOrderRequest,
                Boolean.FALSE);
            if (!baseResult.isSuccess()) {
                throw new ServiceException(
                    LogisticsReturnCode.STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION,
                    LogisticsReturnCode.STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION.getDesc());
            }
            return baseResult.isSuccess();
        } catch (ServiceException e) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR).setErrorMsg("[供应链-出库单状态失败] 异常信息：")
                .addParm("出库单ID", orderBO.getId()).setException(e).log();
            throw e;
        }
    }

    public boolean triggerOrderStateChange(StockoutOrderBO stockoutOrderBO,
                                           StockoutOrderActionType actionType)
                                                                              throws ServiceException {
        StockoutOrderRequest stockoutOrderRequest = StockoutOrderRequestFactory
            .generateStockoutOrderRequest(actionType, stockoutOrderBO, null, null);
        return triggerOrderStateChange(stockoutOrderRequest);
    }

}
