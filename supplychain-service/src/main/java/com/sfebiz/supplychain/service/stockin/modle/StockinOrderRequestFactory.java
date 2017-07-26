package com.sfebiz.supplychain.service.stockin.modle;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailEntity;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import com.sfebiz.supplychain.service.statemachine.Operator;

import java.util.Date;
import java.util.List;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/5/18 下午4:16
 */
public class StockinOrderRequestFactory {

    public static StockinOrderRequest generateStockinOrderRequest(Enumerable4StringValue actionType, StockinOrderDO stockinOrderDO, List<StockinOrderDetailEntity> stockinOrderDetailEntityList, Date processDateTime, Operator operator) {
        if (null != stockinOrderDO) {
            StockinOrderRequest stockinOrderRequest = new StockinOrderRequest();
            stockinOrderRequest.setId(stockinOrderDO.getId());
            stockinOrderRequest.setStockinOrderDO(stockinOrderDO);
            stockinOrderRequest.setAction(actionType);
            if (null != stockinOrderDetailEntityList) {
                stockinOrderRequest.setStockinOrderDetailEntities(stockinOrderDetailEntityList);
            }
            if (null != processDateTime) {
                stockinOrderRequest.setProcessDateTime(processDateTime);
            } else {
                stockinOrderRequest.setProcessDateTime(new Date());
            }
            if (null != operator) {
                stockinOrderRequest.setOperator(operator);
            }

            return stockinOrderRequest;
        }
        return null;
    }

}
