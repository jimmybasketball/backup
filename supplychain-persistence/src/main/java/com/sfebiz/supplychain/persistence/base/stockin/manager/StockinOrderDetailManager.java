package com.sfebiz.supplychain.persistence.base.stockin.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockin.dao.StockinOrderDetailDao;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDetailDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by zhangyajing on 2017/7/12.
 */
@Component("stockinOrderDetailManager")
public class StockinOrderDetailManager extends BaseManager<StockinOrderDetailDO> {

    @Resource
    private StockinOrderDetailDao stockinOrderDetailDao;

    @Override
    public BaseDao<StockinOrderDetailDO> getDao() {
        return stockinOrderDetailDao;
    }

    public static void main(String[] args){
        DaoHelper.genXMLWithFeature("E:\\work\\cqfqht\\haitao-b2b-supplychain\\supplychain-persistence\\src\\main\\resources\\base\\sqlmap\\stockin\\sc_stockin_order_detail.xml"
        ,StockinOrderDetailDao.class
        ,StockinOrderDetailDO.class
        ,"sc_stockin_order_detail");
    }
}
