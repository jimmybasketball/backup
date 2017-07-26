package com.sfebiz.supplychain.persistence.base.stockout.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderRecordDO;

/**
 * <p>出库单记录Dao</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:45:41
 */
public interface StockoutOrderRecordDao extends BaseDao<StockoutOrderRecordDO> {

    /**
     * 修改出库单口岸下单状态
     *
     * @param stockoutOrderRecordDO
     */
    int updatePortState(StockoutOrderRecordDO stockoutOrderRecordDO);
}
