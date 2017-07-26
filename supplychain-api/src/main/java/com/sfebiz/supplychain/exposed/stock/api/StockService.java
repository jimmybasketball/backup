package com.sfebiz.supplychain.exposed.stock.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.stock.entity.SkuBatchStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.entity.SkuStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.entity.StockBatchEntity;
import com.sfebiz.supplychain.exposed.stock.entity.StockPhysicalEntity;
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
     * 创建批次库存
     *
     * @param stockBatchEntity
     * @return
     */
    public CommonRet<Long> createStockBatch(StockBatchEntity stockBatchEntity);

    /**
     * 批量冻结商品库存
     *
     * @return
     * @throws ServiceException
     */
    Map<String, List<SkuBatchStockOperaterEntity>> freezeStockBatch(List<SkuStockOperaterEntity> skuStockOperaterEntityList) throws ServiceException;


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


    /**
     * 获取指定仓库中sku的库存信息
     *
     * @param skuId
     * @param warehouseId
     * @return
     * @throws ServiceException
     */
    public StockPhysicalEntity getBatchStockBySkuIdAndWarehouseId(long skuId, long warehouseId) throws ServiceException;
} 
