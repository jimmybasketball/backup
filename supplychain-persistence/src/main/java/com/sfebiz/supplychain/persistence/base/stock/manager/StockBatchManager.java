package com.sfebiz.supplychain.persistence.base.stock.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.stock.enums.StockBatchStateType;
import com.sfebiz.supplychain.persistence.base.stock.dao.StockBatchDao;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 批次库存Manager
 * @date 2017-07-13 16:36
 **/
@Component("batchStockManager")
public class StockBatchManager extends BaseManager<StockBatchDO> {
    @Resource
    private StockBatchDao stockBatchDao;

    @Override
    public BaseDao<StockBatchDO> getDao() {
        return stockBatchDao;
    }

    public List<StockBatchDO> getAllStockBatch() {
        StockBatchDO stockBatchDO = new StockBatchDO();
        stockBatchDO.setIsDelete(0);
        BaseQuery<StockBatchDO> batchDOBaseQuery = new BaseQuery<StockBatchDO>(stockBatchDO);
        return stockBatchDao.query(batchDOBaseQuery);
    }

    /**
     * 通过商品ID和仓库ID查询所有可用批次库存 并且可以用库存> 0
     *
     * @param skuId       商品ID
     * @param warehouseId 仓库ID
     * @return 所有批次库存信息
     */
    public List<StockBatchDO> getAvailableStockBatchDOListBySkuIdAndWarehouseId(Long skuId, Long warehouseId) {
        if (null == skuId || 0 == skuId || null == warehouseId || 0 == warehouseId) {
            return null;
        }
        StockBatchDO stockBatchDO = new StockBatchDO();
        stockBatchDO.setSkuId(skuId);
        stockBatchDO.setWarehouseId(warehouseId);
        stockBatchDO.setState(StockBatchStateType.ENABLE.value);
        BaseQuery<StockBatchDO> batchDOBaseQuery = new BaseQuery<StockBatchDO>(stockBatchDO);
        batchDOBaseQuery.addGt("available_count", 0);
        List<StockBatchDO> stockBatchDOList = stockBatchDao.query(batchDOBaseQuery);

        if (stockBatchDOList != null && stockBatchDOList.size() > 0) {
            return stockBatchDOList;
        }
        return null;
    }

    /**
     * 通过skuId 和 warehouse 查询可用所有批次库存信息
     *
     * @param skuId       商品ID
     * @param warehouseId 仓库ID
     * @return 所有批次库存信息
     */
    public List<StockBatchDO> getBySkuIdAndWarehouseId(Long skuId, Long warehouseId) {
        if (null == skuId || 0 == skuId || null == warehouseId || 0 == warehouseId) {
            return null;
        }
        StockBatchDO stockBatchDO = new StockBatchDO();
        stockBatchDO.setSkuId(skuId);
        stockBatchDO.setWarehouseId(warehouseId);
        stockBatchDO.setState(StockBatchStateType.ENABLE.value);
        BaseQuery<StockBatchDO> batchDOBaseQuery = new BaseQuery<StockBatchDO>(stockBatchDO);
        List<StockBatchDO> stockBatchDOList = stockBatchDao.query(batchDOBaseQuery);

        if (stockBatchDOList != null && stockBatchDOList.size() > 0) {
            return stockBatchDOList;
        }
        return null;
    }

    /**
     * 通过skuId 和仓库ID查询所有批次库存 （包括禁用的）
     *
     * @param skuId       skuId
     * @param warehouseId 仓库ID
     * @return 含禁用批次库存信息
     */
    public List<StockBatchDO> getAllStockBatchDOListBySkuIdAndWarehouseId(Long skuId, Long warehouseId) {
        if (null == skuId || 0 == skuId || null == warehouseId || 0 == warehouseId) {
            return null;
        }
        StockBatchDO stockBatchDO = new StockBatchDO();
        stockBatchDO.setSkuId(skuId);
        stockBatchDO.setWarehouseId(warehouseId);
        BaseQuery<StockBatchDO> batchDOBaseQuery = new BaseQuery<StockBatchDO>(stockBatchDO);
        List<StockBatchDO> stockBatchDOList = stockBatchDao.query(batchDOBaseQuery);

        if (stockBatchDOList != null && stockBatchDOList.size() > 0) {
            return stockBatchDOList;
        }
        return null;
    }

    /**
     * 通过skuId、仓库Id和批次号查询对应批次库存信息
     *
     * @param skuId       skuID
     * @param warehouseId 仓库Id
     * @param batchNo     批次号
     * @return 指定批次库存
     */
    public StockBatchDO getBySkuIdAndWarehouseIdAndBatchNo(Long skuId, Long warehouseId, String batchNo) {
        if (StringUtils.isBlank(batchNo) || null == warehouseId || 0 == warehouseId) {
            return null;
        }
        StockBatchDO stockBatchDO = new StockBatchDO();
        stockBatchDO.setSkuId(skuId);
        stockBatchDO.setWarehouseId(warehouseId);
        stockBatchDO.setBatchNo(batchNo);
        BaseQuery<StockBatchDO> batchDOBaseQuery = new BaseQuery<StockBatchDO>(stockBatchDO);
        List<StockBatchDO> stockBatchDOList = stockBatchDao.query(batchDOBaseQuery);

        if (stockBatchDOList != null && stockBatchDOList.size() > 0) {
            return stockBatchDOList.get(0);
        }
        return null;
    }

    /**
     * 通过skuId、仓库Id、批次号 查询批次库存（for update）
     *
     * @param skuId       skuId
     * @param warehouseId 仓库id
     * @param batchNo     批次号
     * @return 指定for update 批次
     */
    public StockBatchDO getBySkuIdAndWarehouseIdAndBatchNoForUpdate(Long skuId, Long warehouseId, String batchNo) {
        if (null == skuId || 0 == skuId || null == warehouseId || 0 == warehouseId) {
            return null;
        }
        StockBatchDO stockBatchDO = new StockBatchDO();
        stockBatchDO.setSkuId(skuId);
        stockBatchDO.setWarehouseId(warehouseId);
        stockBatchDO.setBatchNo(batchNo);
        StockBatchDO StockBatchDO = stockBatchDao.getBySkuIdAndWarehouseIdAndBatchNoForUpdate(stockBatchDO);
        return StockBatchDO;
    }

    /**
     * ;
     * 通过skuId 查询所有可用批次库存信息
     *
     * @param skuId skuId
     * @return sku所有批次信息
     */
    public List<StockBatchDO> getBySkuId(Long skuId) {
        if (null == skuId || 0 == skuId) {
            return null;
        }
        StockBatchDO stockBatchDO = new StockBatchDO();
        stockBatchDO.setSkuId(skuId);
        stockBatchDO.setState(StockBatchStateType.ENABLE.value);
        BaseQuery<StockBatchDO> batchDOBaseQuery = new BaseQuery<StockBatchDO>(stockBatchDO);
        batchDOBaseQuery.addGt("available_count", 0);
        List<StockBatchDO> stockBatchDOList = stockBatchDao.query(batchDOBaseQuery);
        if (stockBatchDOList != null && stockBatchDOList.size() > 0) {
            return stockBatchDOList;
        }
        return null;
    }

    /**
     * 按照商品和批次号查询批次
     *
     * @param skuId
     * @param batchNo
     * @return
     */
    public List<StockBatchDO> getBySkuIdAndBatchNo(Long skuId, String batchNo) {
        if (null == skuId || org.apache.commons.lang.StringUtils.isBlank(batchNo)) {
            return null;
        }
        StockBatchDO stockBatchDO = new StockBatchDO();
        stockBatchDO.setSkuId(skuId);
        stockBatchDO.setBatchNo(batchNo);
        BaseQuery<StockBatchDO> qy = new BaseQuery<StockBatchDO>(stockBatchDO);
        return stockBatchDao.query(qy);
    }

    /**
     * 根据sku 和货主供应商查询批次库存
     *
     * @param skuId
     * @param merchantProviderId
     * @return
     */
    public List<StockBatchDO> getBySkuIdAndMerchantProviderId(Long skuId, Long merchantProviderId) {
        if (null == skuId || null == merchantProviderId) {
            return null;
        }
        StockBatchDO stockBatchDO = new StockBatchDO();
        stockBatchDO.setSkuId(skuId);
        stockBatchDO.setMerchantProviderId(merchantProviderId);
        BaseQuery<StockBatchDO> baseQuery = new BaseQuery<StockBatchDO>(stockBatchDO);
        List<StockBatchDO> stockBatchDOList = stockBatchDao.query(baseQuery);

        if (stockBatchDOList != null && stockBatchDOList.size() > 0) {
            return stockBatchDOList;
        }
        return null;
    }
}
