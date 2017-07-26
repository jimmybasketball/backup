package com.sfebiz.supplychain.autotest.service;

import com.sfebiz.supplychain.autotest.BaseServiceTest;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailCargoResultEntity;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailEntity;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderEntity;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderType;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinTransportType;
import org.junit.Test;

import java.text.SimpleDateFormat;
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
        stockinOrderDetailEntity.setCount(100);
        stockinOrderDetailEntityList.add(stockinOrderDetailEntity);
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

        stockInService.createStockinOrder(stockinOrderEntity, 1L, "张雅静");
    }

    @Test
    public void submitWarehouseTest(){
        stockInService.submitStockinOrder(29519L, 1L ,"张雅静");
    }

    @Test
    public void importStockinOrderTallyReportTest() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        List<StockinOrderDetailCargoResultEntity> entityList = new ArrayList<StockinOrderDetailCargoResultEntity>();
        StockinOrderDetailCargoResultEntity entity = new StockinOrderDetailCargoResultEntity();
        entity.setStockinOrderId(29519L);
        entity.setSkuId(1132011L);
        entity.setSkuBarcode("B12037");
        entity.setRealCount(98);
        entity.setBadRealCount(1);
        entity.setRealDiffCount(1);
        entity.setDiffDesc("丢失");
        entity.setProductionDate(sdf.parse("2016-11-24 17:30:26"));
        entity.setExpirationDate(sdf.parse("2018-11-24 17:30:26"));
        entityList.add(entity);
        stockInService.importStockinOrderTallyReport(29519L,entityList,1L,"张雅静");
    }

    @Test
    public void finishStockinOrderTest() {
        List<StockinOrderDetailEntity> stockinOrderDetailEntityList = new ArrayList<StockinOrderDetailEntity>();
        StockinOrderDetailEntity stockinOrderDetailEntity = new StockinOrderDetailEntity();
        stockinOrderDetailEntity.setSkuId(1132011L);
        stockinOrderDetailEntity.setSkuBarcode("B12037");
        stockinOrderDetailEntity.setSkuName("三鹿奶粉");
        stockinOrderDetailEntity.setCount(100);
        stockinOrderDetailEntity.setRealCount(98);
        stockinOrderDetailEntity.setBadRealCount(1);
        stockinOrderDetailEntity.setRealDiffCount(1);
        stockinOrderDetailEntity.setDiffDesc("丢失");
        stockinOrderDetailEntityList.add(stockinOrderDetailEntity);
        stockInService.finishStockinOrder(29519L,11L,stockinOrderDetailEntityList,1L,"张雅静");
    }
}
