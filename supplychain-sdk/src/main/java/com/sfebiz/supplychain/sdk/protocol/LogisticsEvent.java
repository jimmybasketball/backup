package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 事件类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"eventHeader", "eventBody"})
public class LogisticsEvent implements Serializable {
    private static final long serialVersionUID = 7193853014682846004L;
    /**
     * 事件基础信息体
     */
    @XmlElement(nillable = false, required = true)
    public EventHeader eventHeader;

    /**
     * 事件消息类结构体
     */
    @XmlElement(nillable = false, required = true)
    public EventBody eventBody;

    public EventHeader getEventHeader() {
        if (eventHeader == null) {
            eventHeader = new EventHeader();
        }
        return eventHeader;
    }

    public void setEventHeader(EventHeader eventHeader) {
        this.eventHeader = eventHeader;
    }

    public EventBody getEventBody() {
        if (eventBody == null) {
            eventBody = new EventBody();
        }
        return eventBody;
    }

    public void setEventBody(EventBody eventBody) {
        this.eventBody = eventBody;
    }

    @Override
    public String toString() {
        return "LogisticsEvent{" +
                "eventHeader=" + eventHeader +
                ", eventBody=" + eventBody +
                '}';
    }
}
