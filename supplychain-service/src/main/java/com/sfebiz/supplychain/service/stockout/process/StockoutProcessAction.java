package com.sfebiz.supplychain.service.stockout.process;

import net.pocrd.entity.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.process.create.DataPrepareProcessor;
import com.sfebiz.supplychain.service.stockout.process.create.PortOrderValidateProcessor;
import com.sfebiz.supplychain.service.stockout.process.exception.ExceptionProcessor;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>抽象事件处理接口</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/16
 * Time: 下午12:05
 */
public abstract class StockoutProcessAction implements EngineProcessor<StockoutOrderRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockoutProcessAction.class);

    /**
     * 获取此Process的标识
     *
     * @return
     */
    public abstract String getProcessTag();

    public abstract BaseResult doProcess(StockoutOrderRequest request) throws ServiceException;

    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {
        BaseResult result = new BaseResult();
        //如果设置了起始处理器，则从起始处理器开始执行，如果当前节点非起始节点，则跳过
        if (StringUtils.isNotBlank(request.getStartProcessorTag())) {
            if (PortOrderValidateProcessor.TAG.equals(request.getStartProcessorTag())
                || DataPrepareProcessor.TAG.equals(request.getStartProcessorTag())) {
                //这里 是针对需要每个节点都需要执行的Process，且Process需要重载Process方法
                request.setStartProcessorTag(null);
                result = doProcess(request);
                request.setCurrentProcssorTag(getProcessTag());
                return result;
            } else {
                //如果为任务重试，且还没有抵达起始节点，则跳过顶部节点；
                if (!ExceptionProcessor.TAG.equals(getProcessTag())
                    && !request.getStartProcessorTag().equalsIgnoreCase(getProcessTag())) {
                    result.setSuccess(Boolean.TRUE);
                    return result;
                }
            }
        }

        //如果某一个节点执行失败了，需要进入异常节点，且TAG不能变
        if (ExceptionProcessor.TAG.equals(getProcessTag())) {
            request.setStartProcessorTag(null);
            result = doProcess(request);
            return result;
        } else {
            request.setStartProcessorTag(null);
            result = doProcess(request);
            request.setCurrentProcssorTag(getProcessTag());
            return result;
        }
    }

    /**
     * 校验创建命令基本信息
     * 
     * @param request
     * @return
     */
    public boolean checkoutRequestBaseInfo(StockoutOrderRequest request) {
        if (request.getStockoutOrderBO() == null
            || CollectionUtils.isEmpty(request.getStockoutOrderBO().getDetailBOs())
            || request.getStockoutOrderBO().getLineBO() == null) {

            LogBetter.instance(LOGGER).setLevel(LogLevel.WARN).setMsg("出库单相关参数实体为 null")
                .addParm("出库单信息", request.getStockoutOrderBO()).log();
            request.setExceptionMessage("[供应链-下发出库单]出库单相关参数实体为空");
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.DATA_PREPARE_ERROR, LogisticsReturnCode.DATA_PREPARE_ERROR
                    .getDesc()));
            return false;
        }
        return true;
    }
}
