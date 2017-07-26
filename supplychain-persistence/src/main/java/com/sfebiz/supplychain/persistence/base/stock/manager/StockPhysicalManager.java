package com.sfebiz.supplychain.persistence.base.stock.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stock.dao.StockPhysicalDao;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockPhysicalDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
                "sc_stock_physical", true);
    }

    public StockPhysicalDO getBySkuIdAndWarehouseId(long skuId, long warehouseId) {
        StockPhysicalDO stockPhysicalDO = new StockPhysicalDO();
        stockPhysicalDO.setSkuId(skuId);
        stockPhysicalDO.setWarehouseId(warehouseId);
        BaseQuery<StockPhysicalDO> baseQuery = new BaseQuery<StockPhysicalDO>(stockPhysicalDO);
        List<StockPhysicalDO> stockPhysicalDOList = stockPhysicalDao.query(baseQuery);

        if (stockPhysicalDOList != null && stockPhysicalDOList.size() > 0) {
            return stockPhysicalDOList.get(0);
        }
        return null;
    }
}
