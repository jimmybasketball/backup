package com.sfebiz.supplychain.exposed.stockinorder.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
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
    CommonRet<List<Long>> createStockinOrder (StockinOrderEntity stockinOrderEntity, Long userId, String userName);

    /**
     * 更新入库单明细
     * @param stockinOrderId
     * @param stockinOrderDetailEntities
     * @param userId
     * @param userName
     * @return
     * @throws ServiceException
     */
    CommonRet<List<Long>> updateStockinOrderDetails(Long stockinOrderId, List<StockinOrderDetailEntity> stockinOrderDetailEntities, Long userId, String userName);

    /**
     * 提交入库单给仓库
     * @param stockinOrderId
     * @param userId
     * @param userName
     * @return
     */
    CommonRet<Void> submitStockinOrder(Long stockinOrderId, Long userId, String userName);
}
