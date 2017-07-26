package com.sfebiz.supplychain.persistence.base.stockin.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockin.dao.StockinOrderDao;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by zhangyajing on 2017/7/12.
 */
@Component("stockinOrderManager")
public class StockinOrderManager extends BaseManager<StockinOrderDO>{

    @Resource
    private StockinOrderDao stockinOrderDao;

    @Override
    public BaseDao<StockinOrderDO> getDao() {
        return stockinOrderDao;
    }

    public StockinOrderDO getByStockinId(String stockinId) {
        return stockinOrderDao.getByStockinId(stockinId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("E:\\work\\cqcode\\haitao-b2b-supplychain\\supplychain-persistence\\src\\main\\resources\\base\\sqlmap\\stockin\\sc_stockin_order_sqlmap.xml",
                StockinOrderDao.class,
                StockinOrderDO.class,
                "sc_stockin_order");
    }
}
