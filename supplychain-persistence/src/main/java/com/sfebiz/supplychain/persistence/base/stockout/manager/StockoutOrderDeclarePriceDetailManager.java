package com.sfebiz.supplychain.persistence.base.stockout.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderDeclarePriceDetailDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDeclarePriceDetailDO;

/**
 * 
 * <p>出库单申报金额明细manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderDeclarePriceDetailManager")
public class StockoutOrderDeclarePriceDetailManager extends
                                                   BaseManager<StockoutOrderDeclarePriceDetailDO> {

    @Resource
    private StockoutOrderDeclarePriceDetailDao stockoutOrderDeclarePriceDetailDao;

    @Override
    public BaseDao<StockoutOrderDeclarePriceDetailDO> getDao() {
        return stockoutOrderDeclarePriceDetailDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_declare_price_detail-sqlmap.xml",
            StockoutOrderDeclarePriceDetailDao.class, StockoutOrderDeclarePriceDetailDO.class,
            "sc_stockout_order_declare_price_detail");
    }
}
