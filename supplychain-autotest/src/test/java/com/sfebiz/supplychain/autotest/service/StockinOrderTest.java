package com.sfebiz.supplychain.autotest.service;

import com.sfebiz.supplychain.autotest.BaseServiceTest;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailEntity;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderEntity;
import com.sfebiz.supplychain.exposed.stockinorder.enums.BatchMakePlan;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderType;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinTransportType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangyajing on 2017/7/18.
 */
public class StockinOrderTest extends BaseServiceTest{

    @Test
    public void createTest(){
        StockinOrderEntity stockinOrderEntity = new StockinOrderEntity();
        List<StockinOrderDetailEntity> stockinOrderDetailEntityList = new ArrayList<StockinOrderDetailEntity>();
        StockinOrderDetailEntity stockinOrderDetailEntity = new StockinOrderDetailEntity();
        stockinOrderDetailEntity.setSkuId(1132011L);
        stockinOrderDetailEntity.setSkuBarcode("B12037");
        stockinOrderDetailEntity.setSkuName("三鹿奶粉");
        stockinOrderDetailEntity.setBatchMakePlan(BatchMakePlan.EXP_SAME.getValue());
        stockinOrderDetailEntity.setCount(100);
        stockinOrderDetailEntityList.add(stockinOrderDetailEntity);
        stockinOrderEntity.setDetailEntities(stockinOrderDetailEntityList);
        stockinOrderEntity.setMerchantId(5L);
        stockinOrderEntity.setMerchantProviderId(10L);
        stockinOrderEntity.setType(StockinOrderType.SALES_STOCK_IN.getValue());
        stockinOrderEntity.setWarehouseId(11L);
        stockinOrderEntity.setPredictSendTime(new Date());
        stockinOrderEntity.setPredictArrivePort(new Date());
        stockinOrderEntity.setContactName("张三");
        stockinOrderEntity.setContactPhone("17829082876");
        stockinOrderEntity.setMailNo("886323234234");
        stockinOrderEntity.setTransportType(StockinTransportType.OCEAN.getType());
        stockinOrderEntity.setLogisticsCompany("圆通");

//        stockInService.createStockinOrder(stockinOrderEntity, 1L, "张雅静");
    }
}
