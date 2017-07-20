package com.sfebiz.supplychain.persistence.base.stockin.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockin.dao.StockinOrderStateLogDao;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderStateLogDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by zhangyajing on 2017/7/12.
 */
@Component("stockinOrderStateLogManager")
public class StockinOrderStateLogManager extends BaseManager<StockinOrderStateLogDO>{

    @Resource
    private StockinOrderStateLogDao stockinOrderStateLogDao;

    @Override
    public BaseDao<StockinOrderStateLogDO> getDao() {
        return stockinOrderStateLogDao;
    }

    /**
     * 新增或更新
     * @param stockinOrderId
     * @param userId
     * @param userName
     * @param state
     */
    public void insertOrUpdate(Long stockinOrderId, Long userId, String userName, String state) {
        StockinOrderStateLogDO stockinOrderStateLogDO = new StockinOrderStateLogDO();
        stockinOrderStateLogDO.setStockinOrderId(stockinOrderId);
        stockinOrderStateLogDO.setState(state);
        stockinOrderStateLogDO.setUserId(userId);
        stockinOrderStateLogDO.setUserName(userName);
        insert(stockinOrderStateLogDO);
    }

    public static void main(String[] args){
        DaoHelper.genXMLWithFeature("E:\\work\\cqfqht\\haitao-b2b-supplychain\\supplychain-persistence\\src\\main\\resources\\base\\sqlmap\\stockin\\sc_stockin_order_state_log.xml",
                StockinOrderStateLogDao.class,
                StockinOrderStateLogDO.class,
                "sc_stockin_order_state_log");
    }
}
