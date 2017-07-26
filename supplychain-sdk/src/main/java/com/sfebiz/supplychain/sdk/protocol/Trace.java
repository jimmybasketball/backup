package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by wang_cl on 2015/2/9.
 */

/**
 * 路由信息消息体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "trace", propOrder = {"time", "event", "location", "eventCode"})
public class Trace {
    /**
     * 事件发生事件
     */
    @XmlElement(nillable = false, required = false)
    public String time;

    /**
     * 事件发生描述
     */
    @XmlElement(nillable = false, required = false)
    public String event;

    /**
     * 事件发生地点
     */
    @XmlElement(nillable = false, required = false)
    public String location;

    /**
     * 事件发生状态
     */
    @XmlElement(nillable = false, required = false)
    public String eventCode;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    @Override
    public String toString() {
        return "Trace{" +
                "time='" + time + '\'' +
                ", event='" + event + '\'' +
                ", location='" + location + '\'' +
                ", eventCode='" + eventCode + '\'' +
                '}';
    }
}
