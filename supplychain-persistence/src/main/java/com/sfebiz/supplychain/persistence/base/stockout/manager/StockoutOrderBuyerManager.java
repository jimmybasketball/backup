package com.sfebiz.supplychain.persistence.base.stockout.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderBuyerDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderBuyerDO;

/**
 * 
 * <p>出库单manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderBuyerManager")
public class StockoutOrderBuyerManager extends BaseManager<StockoutOrderBuyerDO> {

    @Resource
    private StockoutOrderBuyerDao stockoutOrderBuyerDao;

    @Override
    public BaseDao<StockoutOrderBuyerDO> getDao() {
        return stockoutOrderBuyerDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_buyer-sqlmap.xml",
            StockoutOrderBuyerDao.class, StockoutOrderBuyerDO.class, "sc_stockout_order_buyer");
    }
}
