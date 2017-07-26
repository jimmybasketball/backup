package com.sfebiz.supplychain.persistence.base.stockin.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;

/**
 * Created by zhangyajing on 2017/7/12.
 */
public interface StockinOrderDao extends BaseDao<StockinOrderDO>{
    StockinOrderDO getByStockinId(String stockinId);
}
