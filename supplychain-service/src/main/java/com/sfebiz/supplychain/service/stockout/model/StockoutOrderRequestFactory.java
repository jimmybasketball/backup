package com.sfebiz.supplychain.service.stockout.model;

import java.util.Date;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskType;
import com.sfebiz.supplychain.service.statemachine.EngineType;
import com.sfebiz.supplychain.service.statemachine.Operator;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/5/17 下午9:18
 */
public class StockoutOrderRequestFactory {

    /**
     * 构造出库单流程，流程引擎请求对象
     * 根据线路上配置的engineTag，进行出库单请求的流程选用
     *
     * @param actionType
     * @param stockoutOrderBO
     * @param lineEntity
     * @param processDateTime
     * @param operator
     * @return
     */
    public static StockoutOrderRequest generateStockoutOrderRequest(Enumerable4StringValue actionType,
                                                                    StockoutOrderBO stockoutOrderBO,
                                                                    Date processDateTime,
                                                                    Operator operator) {
        if (null != stockoutOrderBO) {
            StockoutOrderRequest stockoutOrderRequest = new StockoutOrderRequest();
            stockoutOrderRequest.setId(stockoutOrderBO.getId());
            stockoutOrderRequest.setBizId(stockoutOrderBO.getBizId());
            stockoutOrderRequest.setStockoutOrderBO(stockoutOrderBO);
            stockoutOrderRequest.setAction(actionType);
            stockoutOrderRequest.setEngineType(EngineType.STOCKOUT_ORDER);
            if (null != processDateTime) {
                stockoutOrderRequest.setProcessDateTime(processDateTime);
            } else {
                stockoutOrderRequest.setProcessDateTime(new Date());
            }
            if (null != operator) {
                stockoutOrderRequest.setOperator(operator);
            }

            return stockoutOrderRequest;
        }
        return null;
    }

    public static StockoutOrderRequest generateStockoutOrderRequestForSendProcess(StockoutOrderBO stockoutOrderBO) {
        StockoutOrderRequest stockoutOrderRequest = new StockoutOrderRequest();
        stockoutOrderRequest.setBizId(stockoutOrderBO.getBizId());
        stockoutOrderRequest.setStockoutOrderBO(stockoutOrderBO);
        stockoutOrderRequest.setExceptionType(TaskType.STOCKOUT_CREATE_EXCEPTION.getValue());
        return stockoutOrderRequest;
    }

    public static StockoutOrderRequest generateStockoutOrderRequestForSendProcess(StockoutOrderBO stockoutOrderBO,
                                                                                  TaskType exceptionType,
                                                                                  String exceptionMsg,
                                                                                  String currentProcssorTag) {
        StockoutOrderRequest stockoutOrderRequest = new StockoutOrderRequest();
        stockoutOrderRequest.setBizId(stockoutOrderBO.getBizId());
        stockoutOrderRequest.setStockoutOrderBO(stockoutOrderBO);
        stockoutOrderRequest.setExceptionType(exceptionType.getValue());
        stockoutOrderRequest.setExceptionMessage(exceptionMsg);
        stockoutOrderRequest.setCurrentProcssorTag(currentProcssorTag);
        return stockoutOrderRequest;
    }
}