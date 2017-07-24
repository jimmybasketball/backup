package com.sfebiz.supplychain.service.stockout.biz.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * <p>出库单任务实体</p>
 *
 * @author matt
 * @Date 2017年7月18日 下午12:14:14
 */
public class StockoutOrderTaskBO extends BaseBO {

    /** 序号 */
    private static final long serialVersionUID = -4050155311374292545L;

    /** 出库单ID */
    private Long              stockoutOrderId;

    /** 业务订单号 */
    private String            bizId;

    /** 货主ID */
    private Long              merchantId;

    /** 出库单状态 */
    private String            stockoutOrderState;

    /** 任务类型 */
    private String            taskType;

    /** 子任务类型 */
    private String            subTaskType;

    /** 任务状态 */
    private String            taskState;

    /** 任务所有者，创建者 */
    private String            taskOwner;

    /** 任务处理者 */
    private String            taskOperator;

    /** 任务描述 */
    private String            taskDesc;

    /** 任务处理备注 */
    private String            taskMemo;

    /** 处理任务所用的扩展字段 */
    private String            features;

    /** 异常重试时间 */
    private Date              retryExcuteTime;

    /** 重试次数 */
    private Integer           retryTimes;

    public Long getStockoutOrderId() {
        return stockoutOrderId;
    }

    public void setStockoutOrderId(Long stockoutOrderId) {
        this.stockoutOrderId = stockoutOrderId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getStockoutOrderState() {
        return stockoutOrderState;
    }

    public void setStockoutOrderState(String stockoutOrderState) {
        this.stockoutOrderState = stockoutOrderState;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getSubTaskType() {
        return subTaskType;
    }

    public void setSubTaskType(String subTaskType) {
        this.subTaskType = subTaskType;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }

    public String getTaskOperator() {
        return taskOperator;
    }

    public void setTaskOperator(String taskOperator) {
        this.taskOperator = taskOperator;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskMemo() {
        return taskMemo;
    }

    public void setTaskMemo(String taskMemo) {
        this.taskMemo = taskMemo;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Date getRetryExcuteTime() {
        return retryExcuteTime;
    }

    public void setRetryExcuteTime(Date retryExcuteTime) {
        this.retryExcuteTime = retryExcuteTime;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
