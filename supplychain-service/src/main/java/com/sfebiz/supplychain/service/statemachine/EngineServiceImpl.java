package com.sfebiz.supplychain.service.statemachine;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.taobao.tbbpm.statemachine.StateMachineEngineFactory;
import com.taobao.tbbpm.statemachine.persistence.IStateMachineStateService;
import net.pocrd.entity.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/16
 * Time: 下午5:39
 */
public class EngineServiceImpl implements EngineService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(EngineServiceImpl.class);
    /**
     * 状态机引擎服务的映射
     */
    @Resource
    private Map<String, IStateMachineStateService> stateMachineStateServiceMaps;
    /**
     * 状态机引擎模板的映射
     */
    @Resource
    private Map<String, String> stateMachineTemplateCodeMaps;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void startStateMachineEngine(final EngineRequest request) throws ServiceException {

        try {
            if (null == stateMachineStateServiceMaps || !stateMachineStateServiceMaps.containsKey(request.getEngineType().getValue())) {
                throw new ServiceException(SCReturnCode.LOGISTICS_STATE_MACHINE_INTERNAL_EXCEPTION,
                        "[供应链-状态机流程创建异常]: 未找到对应的状态机服务"
                                + "[实例信息: " + request
                                + ", 实例类型: " + request.getEngineType().getDescription()
                                + "]");
            }

            if (null == stateMachineTemplateCodeMaps || !stateMachineTemplateCodeMaps.containsKey(request.getEngineType().getValue())) {
                throw new ServiceException(SCReturnCode.LOGISTICS_STATE_MACHINE_INTERNAL_EXCEPTION,
                        "[供应链-状态机流程创建异常]: 未找到对应的状态机模板"
                                + "[实例信息: " + request
                                + ", 实例类型: " + request.getEngineType().getDescription()
                                + "]");
            }

            long stateId = StateMachineEngineFactory.getInstance(stateMachineStateServiceMaps.get(request.getEngineType().getValue())).createStateMachineInstance(
                    stateMachineTemplateCodeMaps.get(request.getEngineType().getValue()), request.getId().toString(), null, null);

            StateMachineEngineFactory.getInstance(stateMachineStateServiceMaps.get(request.getEngineType().getValue())).start(stateId);

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-状态机流程创建成功]")
                    .addParm("实例信息", request)
                    .addParm("实例类型", request.getEngineType().getDescription())
                    .log();
            
        } catch (ServiceException e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setException(e)
                    .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                    .setErrorMsg(e.getMsg())
                    .log();
            throw e;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-状态机流程创建异常]")
                    .setException(e)
                    .addParm("实例信息", request)
                    .addParm("实例类型", request.getEngineType().getDescription())
                    .log();
            throw new ServiceException(SCReturnCode.STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION,
                    "[供应链-状态机流程创建异常]: " + e.getMessage()
                            + "[实例信息: " + request
                            + ", 实例类型: " + request.getEngineType().getDescription()
                            + "]");
        }
    }

    @Override
    public BaseResult executeStateMachineEngine(final EngineRequest request, Boolean isAsyncExecute) throws ServiceException {

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                .setErrorMsg("[供应链-状态机流程触发执行]")
                .addParm("触发原因", request.getAction().getDescription())
                .addParm("操作者", request.getOperator())
                .addParm("请求参数", request)
                .log();

        try {

            if (request == null || request.getId() == null) {
                throw new ServiceException(SCReturnCode.STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE,
                        "[供应链-状态机流程执行异常]: " + SCReturnCode.STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE.getDesc()
                                + "[触发原因: " + request.getAction().getDescription()
                                + ", 操作者: " + request.getOperator()
                                + ", 请求参数: " + request
                                + "]");
            }
            // 构造流程引擎需要的参数
            BaseResult result = new BaseResult(Boolean.TRUE);
            final Map<String, Object> params = buildEngineParams(request, result);

            //同步调用
            if (isAsyncExecute == null || !isAsyncExecute) {
                StateMachineEngineFactory.getInstance(stateMachineStateServiceMaps.get(request.getEngineType().getValue())).triggerStateTransferWithTransaction(request.getId(), request.getAction().getValue(), params);
            } else {
                //异步调用
                Runnable task = new Runnable() {
                    public void run() {
                        try {
                            StateMachineEngineFactory.getInstance(stateMachineStateServiceMaps.get(request.getEngineType().getValue())).triggerStateTransferWithTransaction(request.getId(), request.getAction().getValue(), params);
                        } catch (Exception e) {
                            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                                    .setErrorMsg(e.getMessage())
                                    .setException(e)
                                    .log();
                        }
                    }
                };
                threadPoolTaskExecutor.execute(task);

            }
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-状态机流程执行成功]")
                    .addParm("触发原因", request.getAction().getDescription())
                    .addParm("操作者", request.getOperator())
                    .addParm("请求参数", request)
                    .addParm("返回结果", result)
                    .log();
            return result;
        } catch (ServiceException e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setException(e)
                    .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                    .setErrorMsg(e.getMsg())
                    .log();
            throw e;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-状态机流程执行异常]: " + e.getMessage())
                    .setException(e)
                    .addParm("触发原因", request.getAction().getDescription())
                    .addParm("操作者", request.getOperator())
                    .addParm("请求参数", request)
                    .log();
            throw new ServiceException(SCReturnCode.STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION,
                    "[供应链-状态机流程执行异常]: " + e.getMessage()
                            + "[触发原因: " + request.getAction().getDescription()
                            + ", 操作者: " + request.getOperator()
                            + ", 请求参数: " + request
                            + "]");
        }
    }

    @Override
    public BaseResult executeStateMachineProcessor(final EngineProcessor processor, final EngineRequest request, Boolean isAsyncExecute) throws ServiceException {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                .setErrorMsg("[供应链-状态机节点触发执行]")
                .addParm("触发原因", request.getAction().getDescription())
                .addParm("操作者", request.getOperator())
                .addParm("请求参数", request)
                .log();

        try {

            if (request == null || request.getId() == null) {
                throw new ServiceException(SCReturnCode.STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE,
                        "[供应链-状态机节点执行异常]: " + SCReturnCode.STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE.getDesc()
                                + "[触发原因: " + request.getAction().getDescription()
                                + ", 操作者: " + request.getOperator()
                                + ", 请求参数: " + request
                                + "]");
            }
            // 构造流程引擎需要的参数
            BaseResult result = new BaseResult(Boolean.TRUE);
            final Map<String, Object> params = buildEngineParams(request, result);

            //同步调用
            if (isAsyncExecute == null || !isAsyncExecute) {
                result = processor.process(request);
            } else {
                //异步调用
                Runnable task = new Runnable() {
                    public void run() {
                        try {
                            processor.process(request);
                        } catch (Exception e) {
                            LogBetter.instance(logger)
                                    .setLevel(LogLevel.INFO)
                                    .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                                    .setErrorMsg("[供应链-状态机节点执行异常]")
                                    .setException(e)
                                    .addParm("触发原因", request.getAction().getDescription())
                                    .addParm("操作者", request.getOperator())
                                    .addParm("请求参数", request)
                                    .log();
                        }
                    }
                };
                threadPoolTaskExecutor.execute(task);

            }
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-状态机节点执行成功]")
                    .addParm("触发原因", request.getAction().getDescription())
                    .addParm("操作者", request.getOperator())
                    .addParm("请求参数", request)
                    .addParm("返回结果", result)
                    .log();
            return result;
        } catch (ServiceException e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setException(e)
                    .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                    .setErrorMsg(e.getMsg())
                    .log();
            throw e;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-状态机节点执行异常]")
                    .setException(e)
                    .addParm("触发原因", request.getAction().getDescription())
                    .addParm("操作者", request.getOperator())
                    .addParm("请求参数", request)
                    .log();
            throw new ServiceException(SCReturnCode.STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION,
                    "[供应链-状态机节点执行异常]: " + e.getMessage()
                            + "[触发原因: " + request.getAction().getDescription()
                            + ", 操作者: " + request.getOperator()
                            + ", 请求参数: " + request
                            + "]");
        }
    }

    /**
     * 构建流程引擎所需要的数据
     *
     * @param request 请求参数
     * @param result  返回参数
     * @return 构造map结果
     */
    private Map<String, Object> buildEngineParams(EngineRequest request, BaseResult result) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(SystemConstants.REQUEST, request);
        map.put(SystemConstants.RESULT, result);
        return map;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (null == stateMachineStateServiceMaps) {
            throw new RuntimeException("stateMachineStateServiceMaps is null!");
        }

        if (null == stateMachineTemplateCodeMaps) {
            throw new RuntimeException("stateMachineTemplateCodeMaps is null!");
        }
    }
}
