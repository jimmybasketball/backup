package com.sfebiz.supplychain.persistence.base.stockout.dao;

import java.util.List;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.ExportedShipOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;

/**
 * 
 * <p>出库单Dao</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:45:41
 */
public interface StockoutOrderDao extends BaseDao<StockoutOrderDO> {

    List<ExportedShipOrderDO> query4Page4AutoShipOrderByStockoutOrderIds(List<Long> stockoutOrderIdList);
}
