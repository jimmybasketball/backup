package com.sfebiz.supplychain.persistence.base.stock.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stock.dao.StockBatchDao;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 批次库存Manager
 * @date 2017-07-13 16:36
 **/
@Component("batchStockManager")
public class StockBatchManager extends BaseManager<StockBatchDO> {
    @Resource
    private StockBatchDao stockBatchDao;

    @Override
    public BaseDao<StockBatchDO> getDao() {
        return stockBatchDao;
    }

    public StockBatchDO getBySkuIdAndWarehouseIdAndBatchNo(Long skuId, Long warehouseId, String batchNo) {
        if (null == skuId || 0 == skuId || null == warehouseId || 0 == warehouseId) {
            return null;
        }
        StockBatchDO queryDO = new StockBatchDO();
        queryDO.setSkuId(skuId);
        queryDO.setWarehouseId(warehouseId);
        queryDO.setBatchNo(batchNo);
        BaseQuery<StockBatchDO> qy = new BaseQuery<StockBatchDO>(queryDO);
        List<StockBatchDO> result = stockBatchDao.query(qy);
        if (CollectionUtils.isNotEmpty(result)) {
            return result.get(0);
        }
        return null;
    }
    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("E:/NewIdeaProject/haitao-b2b-supplychain/supplychain-persistence" +
                        "/src/main/resources/base/sqlmap/stock/sc_stock_batch_sqlmap.xml",
                StockBatchDao.class,
                StockBatchDO.class,
                "sc_stock_batch", true);
    }

    public List<StockBatchDO> getAllStockBatch() {
        StockBatchDO stockBatchDO = new StockBatchDO();
        stockBatchDO.setIsDelete(0);
        BaseQuery<StockBatchDO> batchDOBaseQuery = new BaseQuery<StockBatchDO>(stockBatchDO);
        return stockBatchDao.query(batchDOBaseQuery);
    }
}
