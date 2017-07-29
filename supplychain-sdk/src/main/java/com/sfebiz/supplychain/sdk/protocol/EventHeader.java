package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 事件基础信息体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eventHeader", propOrder = {"eventType", "eventTime", "eventSource", "eventTarget", "eventMessageId"})
public class EventHeader implements Serializable {

    private static final long serialVersionUID = 5802794857302680155L;

    /**
     * 事件类型
     */
    @XmlElement
    public String eventType;

    /**
     * 事件发生时间
     */
    @XmlElement
    public String eventTime;

    /**
     * 事件源【仓库编号】
     */
    @XmlElement
    public String eventSource;

    /**
     * 事件接收方【仓库编号】
     */
    @XmlElement
    public String eventTarget;

    /**
     * 消息ID
     */
    @XmlElement
    public String eventMessageId;


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    public String getEventTarget() {
        return eventTarget;
    }

    public void setEventTarget(String eventTarget) {
        this.eventTarget = eventTarget;
    }

    public String getEventMessageId() {
        return eventMessageId;
    }

    public void setEventMessageId(String eventMessageId) {
        this.eventMessageId = eventMessageId;
    }

    @Override
    public String toString() {
        return "EventHeader{" +
                "eventType='" + eventType + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", eventSource='" + eventSource + '\'' +
                ", eventTarget='" + eventTarget + '\'' +
                ", eventMessageId='" + eventMessageId + '\'' +
                '}';
    }
}
