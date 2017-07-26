package com.sfebiz.supplychain.persistence.base.stock.dao;


import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;

/**
 * @author yangh [yangh@ifunq.com]
 * @description: 批次库存DAO
 * @date 2017/7/13 16:32
 */
public interface StockBatchDao extends BaseDao<StockBatchDO> {
    StockBatchDO getBySkuIdAndWarehouseIdAndBatchNoForUpdate(StockBatchDO stockBatchDO);
}
