package com.sfebiz.supplychain.autotest.service;

import com.sfebiz.supplychain.autotest.BaseServiceTest;
import com.sfebiz.supplychain.exposed.stock.api.StockService;
import com.sfebiz.supplychain.exposed.stock.entity.SkuBatchStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.entity.SkuStockOperaterEntity;
import net.pocrd.entity.ServiceException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 批次库存测试类
 * @date 2017-07-28 10:01
 **/
public class StockServiceImplTest extends BaseServiceTest {

    @Autowired
    private StockService stockService;

    /**
     * 新增批次库存
     */
    @Test
    public void incrementStockBatch() throws ServiceException {
        try {
            SkuBatchStockOperaterEntity skuBatchStockOperaterEntity = new SkuBatchStockOperaterEntity();
            skuBatchStockOperaterEntity.setCount(200);
            skuBatchStockOperaterEntity.setWearCount(2);
            skuBatchStockOperaterEntity.setSkuId(1132011L);
            skuBatchStockOperaterEntity.setWarehouseId(2L);
            skuBatchStockOperaterEntity.setBatchNo("SB89757as");
            skuBatchStockOperaterEntity.setProviderId(2L);
            skuBatchStockOperaterEntity.setFreezeCount(2);
            skuBatchStockOperaterEntity.setMerchantId(2L);
            skuBatchStockOperaterEntity.setStockinId(20L);
            skuBatchStockOperaterEntity.setProductionDate(new Date());
            skuBatchStockOperaterEntity.setExpirationDate(new Date());
            stockService.incrementSkuBatchStock(1, 20, 0, skuBatchStockOperaterEntity);
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
    }

    @Test
    public void freezeSkuStockBatch() throws ServiceException {
        try {
            ArrayList<SkuStockOperaterEntity> skuStockOperaterEntities = new ArrayList<SkuStockOperaterEntity>();
            SkuStockOperaterEntity skuStockOperaterEntity = new SkuStockOperaterEntity();
            skuStockOperaterEntity.setBatchNo("SB89757as");
            skuStockOperaterEntity.setWarehouseId(1L);
            skuStockOperaterEntity.setSkuId(1132011L);
            skuStockOperaterEntity.setStockoutOrderId(4L);
            skuStockOperaterEntity.setCount(2);
            skuStockOperaterEntities.add(skuStockOperaterEntity);
            stockService.freezeSkuStockBatch(skuStockOperaterEntities);
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
    }


    @Test
    public void consumeSkuStockInBatch() throws ServiceException {
        try {
            List<SkuStockOperaterEntity> skuStockOperaterEntities = new ArrayList<SkuStockOperaterEntity>();
            SkuStockOperaterEntity skuStockOperaterEntity = new SkuStockOperaterEntity();
            skuStockOperaterEntity.setBatchNo("SB89757as");
            skuStockOperaterEntity.setWarehouseId(1L);
            skuStockOperaterEntity.setSkuId(1132011L);
            skuStockOperaterEntity.setStockoutOrderId(4L);
            skuStockOperaterEntity.setCount(2);
            skuStockOperaterEntities.add(skuStockOperaterEntity);
            //List<SkuStockOperaterEntity> skuStockOperaterEntityList, long warehouseId, long stockoutOrderId
            stockService.consumeSkuStockInBatch(skuStockOperaterEntities, skuStockOperaterEntity.getWarehouseId(), skuStockOperaterEntity.getStockoutOrderId());
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
    }

    @Test
    public void releaseSkuStockInBatch() throws ServiceException {
        try {
            List<SkuStockOperaterEntity> skuStockOperaterEntities = new ArrayList<SkuStockOperaterEntity>();
            SkuStockOperaterEntity skuStockOperaterEntity = new SkuStockOperaterEntity();
            skuStockOperaterEntity.setBatchNo("SB89757as");
            skuStockOperaterEntity.setWarehouseId(1L);
            skuStockOperaterEntity.setSkuId(1132011L);
            skuStockOperaterEntity.setStockoutOrderId(4L);
            skuStockOperaterEntity.setCount(2);
            skuStockOperaterEntities.add(skuStockOperaterEntity);
            //List<SkuStockOperaterEntity> skuStockOperaterEntityList, long warehouseId, long stockoutOrderId
            stockService.releaseSkuStockInBatch(skuStockOperaterEntities, 1L, 4);
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
    }


}
