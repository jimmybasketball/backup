package com.sfebiz.supplychain.persistence.base.stockout.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderTaskDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;

/**
 * 
 * <p>出库单任务manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderTaskManager")
public class StockoutOrderTaskManager extends BaseManager<StockoutOrderTaskDO> {

    @Resource
    private StockoutOrderTaskDao stockoutOrderTaskDao;

    @Override
    public BaseDao<StockoutOrderTaskDO> getDao() {
        return stockoutOrderTaskDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_task-sqlmap.xml",
            StockoutOrderTaskDao.class, StockoutOrderTaskDO.class, "sc_stockout_order_task");
    }
}
