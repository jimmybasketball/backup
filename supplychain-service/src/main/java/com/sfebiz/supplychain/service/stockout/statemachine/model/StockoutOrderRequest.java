package com.sfebiz.supplychain.service.stockout.statemachine.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.statemachine.EngineRequest;
import com.sfebiz.supplychain.service.statemachine.EngineType;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;

/**
 * 
 * <p>出库单流程相关请求对象</p>
 * 
 * @author matt
 * @Date 2017年7月20日 上午10:45:29
 */
public class StockoutOrderRequest extends EngineRequest {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");

    @Override
    public HaitaoTraceLogger getTraceLogger() {
        return traceLogger;
    }

    @Override
    public String getTraceId() {
        if (null != stockoutOrderBO && StringUtils.isNotEmpty(stockoutOrderBO.getBizId())) {
            return stockoutOrderBO.getMerchantAccountId() + "_" + stockoutOrderBO.getBizId();
        }
        return bizId;
    }

    /**
     * 业务订单号
     */
    private String                  bizId;

    /**
     * 出库单创建实体
     */
    private StockoutOrderBO         stockoutOrderBO;

    /**
     * 起始处理器标识，如果设置了此TAG，则Processor会从指定位置执行
     */
    private String                  startProcessorTag;

    /**
     * 当前处理器标识
     */
    private String                  currentProcssorTag;

    /**
     * 如果在当前节点发生异常，此属性记录异常类型
     */
    private String                  exceptionType;

    /**
     * 此属性记录子异常类型
     */
    private String                  subExceptionType;

    /**
     * 如果在当前节点发生异常，此属性记录异常信息
     */
    private String                  exceptionMessage;

    /**
     * 任务重试时间
     */
    private Date                    nextRetryTime;

    /**
     * 时间发生时间
     */
    private Date                    eventTime;

    /**
     * 路线口岸限制单数截止时间
     */
    private Date[]                  timeLimitRange;

    /**
     * SKU 备案信息映射关系
     */
    // TODO matt
    private Map<Long, SkuDeclareBO> productDeclareEntityMap;

    /**
     * 状态流转备注
     */
    private String                  stateChangeRemark;

    /**
     * 流程引擎标识
     */
    private EngineType              engineType = EngineType.STOCKOUT_ORDER;

    /**
     * 用于后续获取异常code
     */
    private ServiceException        serviceException;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public StockoutOrderBO getStockoutOrderBO() {
        return stockoutOrderBO;
    }

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public String getStartProcessorTag() {
        return startProcessorTag;
    }

    public void setStartProcessorTag(String startProcessorTag) {
        this.startProcessorTag = startProcessorTag;
    }

    public String getCurrentProcssorTag() {
        return currentProcssorTag;
    }

    public void setCurrentProcssorTag(String currentProcssorTag) {
        this.currentProcssorTag = currentProcssorTag;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getSubExceptionType() {
        return subExceptionType;
    }

    public void setSubExceptionType(String subExceptionType) {
        this.subExceptionType = subExceptionType;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public Date getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(Date nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Date[] getTimeLimitRange() {
        return timeLimitRange;
    }

    public void setTimeLimitRange(Date[] timeLimitRange) {
        this.timeLimitRange = timeLimitRange;
    }

    public Map<Long, SkuDeclareBO> getProductDeclareEntityMap() {
        return productDeclareEntityMap;
    }

    public void setProductDeclareEntityMap(Map<Long, SkuDeclareBO> productDeclareEntityMap) {
        this.productDeclareEntityMap = productDeclareEntityMap;
    }

    public String getStateChangeRemark() {
        return stateChangeRemark;
    }

    public void setStateChangeRemark(String stateChangeRemark) {
        this.stateChangeRemark = stateChangeRemark;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public ServiceException getServiceException() {
        return serviceException;
    }

    public void setServiceException(ServiceException serviceException) {
        this.serviceException = serviceException;
    }

    public static HaitaoTraceLogger getTracelogger() {
        return traceLogger;
    }

    public LogisticsLineBO getLineBO() {
        if (null != stockoutOrderBO) {
            return stockoutOrderBO.getLineBO();
        }
        return null;
    }

    public List<StockoutOrderDetailBO> getDetailBOs() {
        if (null != stockoutOrderBO) {
            return stockoutOrderBO.getDetailBOs();
        }
        return null;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
