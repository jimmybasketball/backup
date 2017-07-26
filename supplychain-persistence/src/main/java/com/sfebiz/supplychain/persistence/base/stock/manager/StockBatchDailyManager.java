package com.sfebiz.supplychain.persistence.base.stock.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stock.dao.StockBatchDailyDao;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDailyDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 每日批次库存Manager
 * @date 2017-07-19 15:21
 **/
@Component("stockBatchDailyManager")
public class StockBatchDailyManager extends BaseManager<StockBatchDailyDO> {
    @Resource
    private StockBatchDailyDao stockBatchDailyDao;

    @Override
    public BaseDao<StockBatchDailyDO> getDao() {
        return stockBatchDailyDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("E:/NewIdeaProject/haitao-b2b-supplychain/supplychain-persistence" +
                        "/src/main/resources/base/sqlmap/stock/sc_stock_batch_daily_sqlmap.xml",
                StockBatchDailyDao.class,
                StockBatchDailyDO.class,
                "sc_stock_batch_daily", true);
    }

    /**
     * 获取某天某个sku某批次的批次信息
     */
    public StockBatchDailyDO getStockBatchDailyByDateStrAndSkuAndBatch(String recordDate, Long skuId, String batchNo) {
        StockBatchDailyDO stockBatchDailyDO = new StockBatchDailyDO();
        stockBatchDailyDO.setRecordDate(recordDate);
        stockBatchDailyDO.setBatchNo(batchNo);
        stockBatchDailyDO.setSkuId(skuId);
        BaseQuery<StockBatchDailyDO> batchDailyDOBaseQuery = new BaseQuery<StockBatchDailyDO>(stockBatchDailyDO);
        List<StockBatchDailyDO> stockBatchDailyDOList = stockBatchDailyDao.query(batchDailyDOBaseQuery);
        if (stockBatchDailyDOList!=null && stockBatchDailyDOList.size()>0){
            return stockBatchDailyDOList.get(0);
        }
        return null;
    }
}
