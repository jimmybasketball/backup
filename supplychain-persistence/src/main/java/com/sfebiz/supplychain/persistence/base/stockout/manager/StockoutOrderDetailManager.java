package com.sfebiz.supplychain.persistence.base.stockout.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderDetailDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDetailDO;

/**
 * 
 * <p>出库单明细manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderDetailManager")
public class StockoutOrderDetailManager extends BaseManager<StockoutOrderDetailDO> {

    @Resource
    private StockoutOrderDetailDao stockoutOrderDetailDao;

    @Override
    public BaseDao<StockoutOrderDetailDO> getDao() {
        return stockoutOrderDetailDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_detail-sqlmap.xml",
            StockoutOrderDetailDao.class, StockoutOrderDetailDO.class, "sc_stockout_order_detail");
    }
}
