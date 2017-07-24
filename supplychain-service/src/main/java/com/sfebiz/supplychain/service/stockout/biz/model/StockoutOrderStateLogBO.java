package com.sfebiz.supplychain.service.stockout.biz.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * <p>出库单状态变更日志实体</p>
 *
 * @author matt
 * @Date 2017年7月18日 下午1:18:07
 */
public class StockoutOrderStateLogBO extends BaseBO {

    /** 序号 */
    private static final long serialVersionUID = 7029352017087603544L;

    /** 出库单主键ID */
    private Long              stockoutOrderId;

    /** 业务订单号 */
    private String            bizId;

    /** 线路ID */
    private Long              lineId;

    /** 开始状态 */
    private String            fromState;

    /** 进入状态时间 */
    private Date              fromTime;

    /** 到达状态 */
    private String            toState;

    /** 到达状态时间 */
    private Date              toTime;

    /** 状态滞留时间，单位秒 */
    private Long              stateDuration;

    /** 预警的滞留时间，单位秒 */
    private Long              alarmDuration;

    /** 预警时间 */
    private Date              alarmTime;

    /** 备注 */
    private String            remark;

    /** 事件发生时间 */
    private Date              eventTime;

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

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    public Long getStateDuration() {
        return stateDuration;
    }

    public void setStateDuration(Long stateDuration) {
        this.stateDuration = stateDuration;
    }

    public Long getAlarmDuration() {
        return alarmDuration;
    }

    public void setAlarmDuration(Long alarmDuration) {
        this.alarmDuration = alarmDuration;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
