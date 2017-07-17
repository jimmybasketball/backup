package com.sfebiz.supplychain.persistence.base.stock.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.stock.entity.StockBatchEntity;
import com.sfebiz.supplychain.persistence.base.stock.dao.StockBatchDao;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("E:/NewIdeaProject/haitao-b2b-supplychain/supplychain-persistence" +
                        "/src/main/resources/base/sqlmap/stock/sc_stock_batch_sqlmap.xml",
                StockBatchDao.class,
                StockBatchDO.class,
                "sc_stock_batch",true);
    }


}
