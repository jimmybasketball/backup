/**
 * Copyright (C) 2017 上海牵趣网络科技有限公司 版权所有
 */
package com.sfebiz.supplychain.exposed.stockout.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;

import net.pocrd.entity.ServiceException;

/**
 * <p>
 * 出库服务
 * </p>
 * 
 * @author wuyun
 * @Date 2017年7月18日 下午2:28:58
 */
public interface StockoutService {

    /**
     * 手工触发出库单异常任务的执行
     *
     * @param id
     * @param userId
     * @param userName
     * @throws ServiceException
     */
    CommonRet<Void> executeStockoutExceptionTaskByHandle(Long id, Long userId, String userName);
}
