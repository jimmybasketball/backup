package com.sfebiz.supplychain.exposed.stock.api;

import com.sfebiz.supplychain.exposed.stock.entity.StockPhysicalEntity;
import net.pocrd.entity.ServiceException;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 实物库存服务
 * @date 2017-07-14 11:48
 **/
public interface StockPhysicalSerivce {
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
