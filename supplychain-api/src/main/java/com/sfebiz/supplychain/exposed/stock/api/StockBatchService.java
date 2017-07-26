package com.sfebiz.supplychain.exposed.stock.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.stock.entity.SkuBatchStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.entity.SkuStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.entity.StockBatchEntity;
import net.pocrd.entity.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 批次库存服务
 * @date 2017-07-14 11:44
 **/
public interface StockBatchService {
    /**
     * 创建批次库存
     *
     * @param stockBatchEntity
     * @return
     */
    public CommonRet<Long> createStockBatch(StockBatchEntity stockBatchEntity);

    /**
     * 批量冻结所有出库单所需的库存
     *
     * @return
     * @throws ServiceException
     */
    Map<String, List<SkuBatchStockOperaterEntity>> freezeStockBatch(List<SkuStockOperaterEntity> skuStockOperaterEntityList) throws ServiceException;


}
