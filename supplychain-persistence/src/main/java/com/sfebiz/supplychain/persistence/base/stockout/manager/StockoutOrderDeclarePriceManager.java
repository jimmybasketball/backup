package com.sfebiz.supplychain.persistence.base.stockout.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderDeclarePriceDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDeclarePriceDO;

/**
 * 
 * <p>出库单申报金额manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderDeclarePriceManager")
public class StockoutOrderDeclarePriceManager extends BaseManager<StockoutOrderDeclarePriceDO> {

    @Resource
    private StockoutOrderDeclarePriceDao stockoutOrderDeclarePriceDao;

    @Override
    public BaseDao<StockoutOrderDeclarePriceDO> getDao() {
        return stockoutOrderDeclarePriceDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_declare_price-sqlmap.xml",
            StockoutOrderDeclarePriceDao.class, StockoutOrderDeclarePriceDO.class,
            "sc_stockout_order_declare_price");
    }
}
