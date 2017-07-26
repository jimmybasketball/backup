package com.sfebiz.supplychain.exposed.stock.api;

import com.sfebiz.supplychain.exposed.stock.entity.SkuBatchStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.entity.SkuStockOperaterEntity;
import net.pocrd.entity.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 库存相关服务
 * @date 2017-07-25 10:01
 **/
public interface StockService {

    /**
     * 入库新增库存
     *
     * @param storehouseId
     * @param stockinOrderId
     * @param stockinType
     * @param skuBatchStockOperaterEntity
     * @return
     * @throws ServiceException
     */
    boolean incrementSkuBatchStock(long storehouseId, long stockinOrderId, int stockinType, SkuBatchStockOperaterEntity skuBatchStockOperaterEntity) throws ServiceException;

    /**
     * 批量冻结商品库存
     *
     * @return
     * @throws ServiceException
     */
    Map<String, List<SkuBatchStockOperaterEntity>> freezeSkuStockBatch(List<SkuStockOperaterEntity> skuStockOperaterEntityList) throws ServiceException;


    /**
     * 批量消费商品库存
     *
     * @param skuStockOperaterEntityList
     * @param warehouseId
     * @param stockoutOrderId
     * @return
     * @throws ServiceException
     */
    boolean consumeSkuStockInBatch(List<SkuStockOperaterEntity> skuStockOperaterEntityList, long warehouseId, long stockoutOrderId) throws ServiceException;


    /**
     * 批量释放商品库存
     *
     * @param skuStockOperaterEntityList
     * @param warehouseId
     * @param stockoutOrderId
     * @return
     * @throws ServiceException
     */
    boolean releaseSkuStockInBatch(List<SkuStockOperaterEntity> skuStockOperaterEntityList, long warehouseId, long stockoutOrderId) throws ServiceException;


}
