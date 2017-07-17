/**
 *
 */
package com.sfebiz.supplychain.service.statemachine;

import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import net.pocrd.entity.ServiceException;

/**
 *
 *
 * 流程引擎服务
 */
public interface EngineService {



    /**
     * 启动状态机引擎
     *
     * @param request 流程引擎请求参数
     * @throws ServiceException
     */
    void startStateMachineEngine(EngineRequest request) throws ServiceException;

    /**
     * 触发状态机引擎
     *
     * @param request        流程引擎请求参数
     * @param isAsyncExecute 是否异步执行命令，默认为false；
     * @return result 返回参数
     * @throws Exception
     */
    BaseResult executeStateMachineEngine(EngineRequest request, Boolean isAsyncExecute) throws ServiceException;

    /**
     * 执行状态机引擎的处理任务
     *
     * @param request        流程引擎请求参数
     * @param processor      处理节点
     * @param isAsyncExecute 是否异步执行命令，默认为false；
     * @return result 返回参数
     * @throws Exception
     */
    BaseResult executeStateMachineProcessor(EngineProcessor processor, EngineRequest request, Boolean isAsyncExecute) throws ServiceException;


}
