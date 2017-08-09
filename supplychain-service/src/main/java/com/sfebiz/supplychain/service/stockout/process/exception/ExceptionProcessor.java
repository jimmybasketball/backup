package com.sfebiz.supplychain.service.stockout.process.exception;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.port.PortConfig;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskStatus;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskType;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderTaskManager;
import com.sfebiz.supplychain.service.stockout.biz.StockoutOrderNoticeBizService;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.process.create.CcbOrderCreateProcessor;
import com.sfebiz.supplychain.service.stockout.process.create.OrderFeeSplitProcessor;
import com.sfebiz.supplychain.service.stockout.process.create.PayOrderCreateProcessor;
import com.sfebiz.supplychain.service.stockout.process.create.PortOrderCreateProcessor;
import com.sfebiz.supplychain.service.stockout.process.create.PortOrderValidateProcessor;
import com.sfebiz.supplychain.service.stockout.process.create.TplOrderCreateProcessor;
import com.sfebiz.supplychain.service.stockout.process.create.WmsOrderCreateProcessor;
import com.sfebiz.supplychain.service.stockout.process.send.CcbOrderConfirmProcessor;
import com.sfebiz.supplychain.service.stockout.process.send.TplOrderSendProcessor;
import com.sfebiz.supplychain.service.stockout.process.send.WmsOrderSendProcessor;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/27
 * Time: 下午6:26
 */
@Component("exceptionProcessor")
public class ExceptionProcessor extends StockoutProcessAction {

    public static final String            TAG    = "EXCEPTION";
    private static final Logger           logger = LoggerFactory
                                                     .getLogger(ExceptionProcessor.class);
    @Resource
    private StockoutOrderTaskManager      stockoutOrderTaskManager;
    @Resource
    private StockoutOrderNoticeBizService stockoutOrderNoticeBizService;

    @Override
    public String getProcessTag() {
        return TAG;
    }

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {
        try {
            BaseResult result = new BaseResult();

            if (request.getStockoutOrderBO() == null
                || StringUtils.isBlank(request.getCurrentProcssorTag())
                || StringUtils.isBlank(request.getExceptionType())) {
                LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("出库单相关参数实体为 null")
                    .addParm("出库单信息", request.getStockoutOrderBO()).log();
                return new BaseResult(Boolean.FALSE);
            }
            String exceptionMessage = request.getExceptionMessage();
            if (StringUtils.isBlank(exceptionMessage)) {
                exceptionMessage = buildExceptionMessage(request.getExceptionType(),
                    request.getCurrentProcssorTag());
            }
            logger.info("[供应链-出库单下发异常信息]业务订单号：" + request.getStockoutOrderBO().getBizId()
                        + "，异常信息：" + exceptionMessage);
            StockoutOrderTaskDO stockoutOrderTaskDO;
            if (StringUtils.isNotBlank(request.getSubExceptionType())) {
                stockoutOrderTaskDO = stockoutOrderTaskManager
                    .getByStockoutOrderIdAndTaskTypeAndSubTaskType(request.getStockoutOrderBO()
                        .getId(), request.getExceptionType(), request.getSubExceptionType());
            } else {
                stockoutOrderTaskDO = stockoutOrderTaskManager.getByStockoutOrderIdAndTaskType(
                    request.getStockoutOrderBO().getId(), request.getExceptionType());
            }
            if (stockoutOrderTaskDO == null) {
                stockoutOrderTaskDO = new StockoutOrderTaskDO();
                stockoutOrderTaskDO.setStockoutOrderId(request.getStockoutOrderBO().getId());
                stockoutOrderTaskDO.setBizId(request.getStockoutOrderBO().getBizId());
                stockoutOrderTaskDO.setStockoutOrderState(request.getStockoutOrderBO()
                    .getOrderState());
                stockoutOrderTaskDO.setTaskType(request.getExceptionType());
                stockoutOrderTaskDO.setSubTaskType(request.getSubExceptionType());
                stockoutOrderTaskDO.setTaskState(TaskStatus.WAIT_HANDLE.getValue());
                stockoutOrderTaskDO.setTaskDesc(exceptionMessage);
                stockoutOrderTaskDO.setMerchantId(request.getStockoutOrderBO().getMerchantId());
                Date nextExeTime = getNextExecuteTime(request);
                stockoutOrderTaskDO.setRetryExcuteTime(nextExeTime);
                JSONObject feature = new JSONObject();
                feature.put("currentProcssorTag", request.getCurrentProcssorTag());
                stockoutOrderTaskDO.setFeatures(feature.toJSONString());
                stockoutOrderTaskManager.insert(stockoutOrderTaskDO);

            } else {
                stockoutOrderTaskDO.setTaskState(TaskStatus.WAIT_HANDLE.getValue());
                stockoutOrderTaskDO.setStockoutOrderState(request.getStockoutOrderBO()
                    .getOrderState());
                JSONObject feature = new JSONObject();
                feature.put("currentProcssorTag", request.getCurrentProcssorTag());
                stockoutOrderTaskDO.setTaskDesc(exceptionMessage);
                stockoutOrderTaskDO.setFeatures(feature.toJSONString());
                Date lastExeTime = stockoutOrderTaskDO.getRetryExcuteTime();
                Date nextExeTime = getNextExecuteTime(request);
                if (nextExeTime.after(lastExeTime)) {
                    stockoutOrderTaskDO.setRetryExcuteTime(nextExeTime);
                } else {
                    stockoutOrderTaskDO.setRetryExcuteTime(lastExeTime);
                }
                stockoutOrderTaskManager.update(stockoutOrderTaskDO);

            }

            // 发消息给供应链，作为仓库通知商户订单异常
            if (request.getServiceException() != null) {
                if (request.getServiceException().getCode() == LogisticsReturnCode.CONFIRM_REAL_NAME_ERROR
                    .getCode()) {
                    stockoutOrderNoticeBizService.sendMsgStockoutFinish2Merchant(request
                        .getStockoutOrderBO().getBizId(), request.getServiceException().getCode(),
                        request.getServiceException().getDescription());
                }
            }

            result.setSuccess(Boolean.TRUE);
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("处理处理器异常逻辑异常")
                .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
            return new BaseResult(Boolean.FALSE);
        }
    }

    /**
     * 根据异常类型&当前节点构造异常信息
     *
     * @param exceptionType
     * @param currentProcssorTag
     * @return
     */
    private String buildExceptionMessage(String exceptionType, String currentProcssorTag) {
        if (TaskType.STOCKOUT_CREATE_EXCEPTION.getValue().equalsIgnoreCase(exceptionType)) {
            if (TplOrderCreateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return "[供应链-TPL]国内物流商-下单失败";
            } else if (PayOrderCreateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return "[供应链-PAY]支付企业-下单失败";
            } else if (PortOrderCreateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return "[供应链-PORT]口岸-下单失败";
            } else if (WmsOrderCreateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return "[供应链-WMS]仓库-下单失败";
            } else if (PortOrderValidateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return "[供应链-WMS]验证口岸避税 验证失败";
            } else if (CcbOrderCreateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return "[供应链-CCB]清关企业-下单失败";
            } else {
                return "[供应链]下单失败";
            }
        } else if (TaskType.STOCKOUT_SEND_EXCEPTION.getValue().equalsIgnoreCase(exceptionType)) {
            if (TplOrderSendProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return "[供应链-TPL]国内物流商-确认订单失败";
            } else if (WmsOrderSendProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return "[供应链-WMS]仓库-发货失败";
            } else if (CcbOrderConfirmProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return "[供应链-WMS]清关企业-确认订单失败";
            } else {
                return "[供应链]发货失败";
            }
        } else {
            return "";
        }
    }

    /**
     * 用于自动创建工单
     * @param exceptionType
     * @param currentProcssorTag
     * @return
     */
    private int transCodeByCurrentProcssorTag(String exceptionType, String currentProcssorTag) {
        if (TaskType.STOCKOUT_CREATE_EXCEPTION.getValue().equalsIgnoreCase(exceptionType)) {
            if (TplOrderCreateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return 24000100;
            } else if (PayOrderCreateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return 24000101;
            } else if (PortOrderCreateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return 24000102;
            } else if (WmsOrderCreateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return 24000103;
            } else if (PortOrderValidateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return 24000104;
            } else if (CcbOrderCreateProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return 24000105;
            } else if (OrderFeeSplitProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return 24000106;
            } else {
                return 24000107;
            }
        } else if (TaskType.STOCKOUT_SEND_EXCEPTION.getValue().equalsIgnoreCase(exceptionType)) {
            if (TplOrderSendProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return 24000200;
            } else if (WmsOrderSendProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return 24000201;
            } else if (CcbOrderConfirmProcessor.TAG.equalsIgnoreCase(currentProcssorTag)) {
                return 24000202;
            } else {
                return 24000203;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取异常任务下一次执行时间
     *
     * @param request
     * @return
     * @throws ServiceException
     */
    private Date getNextExecuteTime(StockoutOrderRequest request) throws ServiceException {
        if (request.getNextRetryTime() != null) {
            return request.getNextRetryTime();
        } else {
            try {
                if (PortOrderValidateProcessor.TAG.equals(request.getCurrentProcssorTag())
                    && request.getStockoutOrderBO().getLineBO() != null
                    && request.getStockoutOrderBO().getLineBO().getPortBO() != null) {
                    Date[] dates = request.getTimeLimitRange();
                    if (null == dates || dates.length < 2) {
                        Map<String, Object> portMeta = JSONUtil.parseJSONMessage(request
                            .getStockoutOrderBO().getLineBO().getPortBO().getMeta());
                        String timeFm = PortConfig.getPortDecalredTimeFm(portMeta);
                        dates = DateUtil.getPortTimeBw(new Date(), timeFm);
                    }
                    return dates[1];
                }
            } catch (Exception e) {
                logger.error("计算任务下次执行时间异常", e);
            }
            return new Date();
        }
    }

}
