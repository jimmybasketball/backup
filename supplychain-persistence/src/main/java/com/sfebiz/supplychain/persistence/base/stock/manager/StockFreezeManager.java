package com.sfebiz.supplychain.persistence.base.stock.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.exception.DataAccessException;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.stock.enums.StockFreezeOrderType;
import com.sfebiz.supplychain.persistence.base.stock.dao.StockFreezeDao;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockFreezeDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
                "sc_stock_freeze", true);
    }

    public List<StockFreezeDO> getBySkuIdAndwarehouseIdAndStockoutOrderId(Long skuId, Long warehouseId, Long stockoutOrderId) throws DataAccessException {
        if (null == skuId || 0 == skuId || null == warehouseId || 0 == warehouseId || null == stockoutOrderId || 0 == stockoutOrderId) {
            return null;
        }
        StockFreezeDO stockFreezeDO = new StockFreezeDO();
        stockFreezeDO.setSkuId(skuId);
        stockFreezeDO.setOrderType(Integer.parseInt(StockFreezeOrderType.SALE_OUT_ORDER_TYPE.value));
        stockFreezeDO.setStockOrderId(stockoutOrderId);
        stockFreezeDO.setWarehouseId(warehouseId);
        BaseQuery<StockFreezeDO> baseQuery = new BaseQuery<StockFreezeDO>(stockFreezeDO);
        baseQuery.addOrderBy("id", 1);
        List<StockFreezeDO> StockFreezeDOList = stockFreezeDao.query(baseQuery);
        if (null != StockFreezeDOList && StockFreezeDOList.size() > 0) {
            return StockFreezeDOList;
        }
        return null;
    }
}
