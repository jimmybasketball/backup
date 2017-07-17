package com.sfebiz.supplychain.exposed.stock.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.line.entity.LogisticsLineEntity;
import com.sfebiz.supplychain.exposed.stock.entity.StockBatchEntity;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 批次库存服务
 * @date 2017-07-14 11:44
 **/
public interface StockBatchService {
    /**
     * @param stockBatchEntity
     * @return
     */
    public CommonRet<Long> createStockBatch(StockBatchEntity stockBatchEntity);

} 
