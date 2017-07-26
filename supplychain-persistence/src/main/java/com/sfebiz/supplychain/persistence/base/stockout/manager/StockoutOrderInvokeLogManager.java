package com.sfebiz.supplychain.persistence.base.stockout.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderInvokeLogDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderInvokeLogDO;

/**
 * 
 * <p>出库单接口/服务调用manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderInvokeLogManager")
public class StockoutOrderInvokeLogManager extends BaseManager<StockoutOrderInvokeLogDO> {

    @Resource
    private StockoutOrderInvokeLogDao stockoutOrderInvokeLogDao;

    @Override
    public BaseDao<StockoutOrderInvokeLogDO> getDao() {
        return stockoutOrderInvokeLogDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_invoke_log-sqlmap.xml",
            StockoutOrderInvokeLogDao.class, StockoutOrderInvokeLogDO.class,
            "sc_stockout_order_invoke_log");
    }


}
