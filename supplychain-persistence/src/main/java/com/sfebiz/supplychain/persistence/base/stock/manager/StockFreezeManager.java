package com.sfebiz.supplychain.persistence.base.stock.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stock.dao.StockBatchDao;
import com.sfebiz.supplychain.persistence.base.stock.dao.StockFreezeDao;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockFreezeDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 冻结库存Maneger
 * @date 2017-07-13 16:42
 **/
@Component("freezeStockManager")
public class StockFreezeManager extends BaseManager<StockFreezeDO> {
    @Resource
    private StockFreezeDao stockFreezeDao;

    @Override
    public BaseDao<StockFreezeDO> getDao() {
        return stockFreezeDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("E:/NewIdeaProject/haitao-b2b-supplychain/supplychain-persistence" +
                        "/src/main/resources/base/sqlmap/stock/sc_stock_freeze_sqlmap.xml",
                StockFreezeDao.class,
                StockFreezeDO.class,
                "sc_stock_freeze",true);
    }
}
