package com.sfebiz.supplychain.persistence.base.stock.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stock.dao.StockFreezeDao;
import com.sfebiz.supplychain.persistence.base.stock.dao.StockPhysicalDao;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockFreezeDO;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockPhysicalDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 实物库存Manager
 * @date 2017-07-13 16:44
 **/
@Component("physicalStockManager")
public class StockPhysicalManager extends BaseManager<StockPhysicalDO> {
    @Resource
    private StockPhysicalDao stockPhysicalDao;

    @Override
    public BaseDao<StockPhysicalDO> getDao() {
        return stockPhysicalDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("E:/NewIdeaProject/haitao-b2b-supplychain/supplychain-persistence" +
                        "/src/main/resources/base/sqlmap/stock/sc_stock_physical_sqlmap.xml",
                StockPhysicalDao.class,
                StockPhysicalDO.class,
                "sc_stock_physical",true);
    }
}
