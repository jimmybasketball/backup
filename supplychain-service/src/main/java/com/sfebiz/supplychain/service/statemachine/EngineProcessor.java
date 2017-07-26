package com.sfebiz.supplychain.service.statemachine;

import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import net.pocrd.entity.ServiceException;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/5/18 下午5:19
 */
public interface EngineProcessor<T> {

    /**
     * 处理方法
     *
     * @param request 出库单请求信息
     * @return result   处理结果
     * @throws ServiceException
     */
    BaseResult process(T request) throws ServiceException;

}
