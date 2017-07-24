package com.sfebiz.supplychain.service.stockin.statemachine.process;

import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequest;
import net.pocrd.entity.ServiceException;

/**
 * Created by zhangyajing on 2017/7/19.
 */
public abstract class StockinAbstractProcessor implements EngineProcessor<StockinOrderRequest> {

    public void validateRequest(StockinOrderRequest stockinOrderRequest) throws ServiceException {
        if (null == stockinOrderRequest
                || null == stockinOrderRequest.getStockinOrderDO()
                || null == stockinOrderRequest.getOperator()) {
            throw new ServiceException(
                    SCReturnCode.PARAM_ILLEGAL_ERR,
                    "[物流平台-入库单流程操作失败]: " + SCReturnCode.PARAM_ILLEGAL_ERR.getDesc() + " "
                            + "[请求参数: " + stockinOrderRequest
                            + "]"
            );
        }
    }
}
