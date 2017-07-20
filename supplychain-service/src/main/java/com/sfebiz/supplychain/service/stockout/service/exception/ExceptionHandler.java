package com.sfebiz.supplychain.service.stockout.service.exception;

import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;

import net.pocrd.entity.ServiceException;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/8/24 下午5:04
 */
public interface ExceptionHandler {

    /**
     * 异常任务处理类
     *
     * @param stockoutOrderTaskDO
     * @throws ServiceException
     */
    void handle(StockoutOrderTaskDO stockoutOrderTaskDO) throws ServiceException;

}
