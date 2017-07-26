/**
 *
 */
package com.sfebiz.supplychain.service.statemachine;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

import java.util.Date;

/**
 * @author yuweixiang
 * @version $Id: EngineRequest.java, v 0.1 2014-12-24 下午12:01:17 yuweixiang Exp $
 */
public abstract class EngineRequest {

    /**
     * 业务id，可能是出库单ID，也有可能是采购单ID 或 入库单ID 或 调拨单ID
     */
    private Long id;

    /**
     * 动作
     */
    private Enumerable4StringValue action;

    /**
     * 业务处理时间
     */
    private Date processDateTime;

    /**
     * 操作者, 默认是系统
     */
    private Operator operator = Operator.SYSTEM_OPERATOR;

    /**
     * Getter method for property <tt>orderAction</tt>.
     *
     * @return property value of orderAction
     */
    public Enumerable4StringValue getAction() {
        return action;
    }

    /**
     * Setter method for property <tt>orderAction</tt>.
     *
     * @param action value to be assigned to property orderAction
     */
    public void setAction(Enumerable4StringValue action) {
        this.action = action;
    }

    /**
     * Getter method for property <tt>id</tt>.
     *
     * @return property value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     *
     * @param id value to be assigned to property id
     */
    public void setId(Long id) {
        this.id = id;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public abstract String getTraceId();

    public abstract HaitaoTraceLogger getTraceLogger();

    public abstract EngineType getEngineType();

    public Date getProcessDateTime() {
        return processDateTime;
    }

    public void setProcessDateTime(Date processDateTime) {
        this.processDateTime = processDateTime;
    }

    @Override
    public String toString() {
        return "EngineRequest{" +
                "id=" + id +
                ", action=" + action +
                ", processDateTime=" + processDateTime +
                ", operator=" + operator +
                '}';
    }
}
