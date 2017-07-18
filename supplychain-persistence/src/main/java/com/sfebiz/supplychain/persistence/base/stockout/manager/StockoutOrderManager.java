package com.sfebiz.supplychain.persistence.base.stockout.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;

/**
 * 
 * <p>出库单manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderManager")
public class StockoutOrderManager extends BaseManager<StockoutOrderDO> {

    @Resource
    private StockoutOrderDao stockoutOrderDao;

    @Override
    public BaseDao<StockoutOrderDO> getDao() {
        return stockoutOrderDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order-sqlmap.xml", StockoutOrderDao.class,
            StockoutOrderDO.class, "sc_stockout_order");
    }
}
