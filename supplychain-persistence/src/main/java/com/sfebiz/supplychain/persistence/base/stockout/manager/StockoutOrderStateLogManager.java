package com.sfebiz.supplychain.persistence.base.stockout.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderStateLogDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderStateLogDO;

/**
 * 
 * <p>出库单状态变更日志manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderStateLogManager")
public class StockoutOrderStateLogManager extends BaseManager<StockoutOrderStateLogDO> {

    @Resource
    private StockoutOrderStateLogDao stockoutOrderStateLogDao;

    @Override
    public BaseDao<StockoutOrderStateLogDO> getDao() {
        return stockoutOrderStateLogDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_state_log-sqlmap.xml",
            StockoutOrderStateLogDao.class, StockoutOrderStateLogDO.class,
            "sc_stockout_order_state_log");
    }
}
