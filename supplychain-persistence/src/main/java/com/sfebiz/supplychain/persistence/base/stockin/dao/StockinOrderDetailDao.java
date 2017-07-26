package com.sfebiz.supplychain.persistence.base.stockin.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDetailDO;

/**
 * Created by zhangyajing on 2017/7/12.
 */
public interface StockinOrderDetailDao extends BaseDao<StockinOrderDetailDO>{

    public void updateByBarcodeAndSkuId(StockinOrderDetailDO stockinOrderDetailDO);
}
