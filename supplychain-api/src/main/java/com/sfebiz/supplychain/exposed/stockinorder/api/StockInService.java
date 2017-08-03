package com.sfebiz.supplychain.exposed.stockinorder.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.stock.entity.StockBatchEntity;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailCargoResultEntity;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailEntity;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderEntity;
import net.pocrd.entity.ServiceException;

import java.util.List;

/**
 * Created by zhangyajing on 2017/7/17.
 */
public interface StockInService {

    /**
     * 创建入库单，保存入库单的基本信息
     * @param stockinOrderEntity
     * @param userId
     * @param userName
     * @return
     * @throws ServiceException
     */
    CommonRet<Long> createStockinOrder (StockinOrderEntity stockinOrderEntity, Long userId, String userName);

    /**
     * 更新入库单明细
     * @param stockinOrderId
     * @param stockinOrderDetailEntities
     * @param userId
     * @param userName
     * @return
     * @throws ServiceException
     */
    CommonRet<Void> updateStockinOrderDetails(Long stockinOrderId, List<StockinOrderDetailEntity> stockinOrderDetailEntities, Long userId, String userName);

    /**
     * 提交入库单给仓库
     * @param stockinOrderId
     * @param userId
     * @param userName
     * @return
     */
    CommonRet<Void> submitStockinOrder(Long stockinOrderId, Long userId, String userName);

    /**
     * 导入理货报告
     *
     * @param stockinOrderId
     * @param stockinOrderSkuCargoResultEntities
     * @param userId
     * @param userName
     * @return
     */
    CommonRet<Void> importStockinOrderTallyReport(Long stockinOrderId, List<StockinOrderDetailCargoResultEntity> stockinOrderSkuCargoResultEntities, Long userId, String userName);

    /**
     * 添加附加文件到入库单
     * @param stockinOrderId
     * @param fileName
     * @param url
     * @param userId
     * @param userName
     * @return
     */
    CommonRet<Long> addFileToStockinOrder(Long stockinOrderId, String fileName, String url, Long userId, String userName);

    /**
     * 保存入库单sku差异
     * @param stockinOrderId
     * @param stockinOrderDetailEntityList
     * @param userId
     * @param userName
     * @return
     */
    CommonRet<Void> saveSkusInfo(Long stockinOrderId, List<StockinOrderDetailEntity> stockinOrderDetailEntityList, Long userId, String userName);

    /**
     * 完成入库单
     * @param stockinOrderId
     * @param warehouseId
     * @param stockinOrderDetailEntities
     * @param userId
     * @param userName
     * @return
     */
    CommonRet<Void> finishStockinOrder(Long stockinOrderId, Long warehouseId, List<StockinOrderDetailEntity> stockinOrderDetailEntities, Long userId, String userName);

    /**
     * 更新入库单的基本信息
     * @param stockinOrderEntity
     * @param userId
     * @param userName
     * @return
     */
    CommonRet<Void> updateStockinOrderBaseInfo(StockinOrderEntity stockinOrderEntity, Long userId, String userName);

    /**
     * 取消入库单
     *
     * @param stockinOrderId
     * @param userId
     * @param userName
     * @return
     * @throws ServiceException
     */
    CommonRet<Void> cancelStockinOrder(Long stockinOrderId, Long userId, String userName) throws ServiceException;
}
