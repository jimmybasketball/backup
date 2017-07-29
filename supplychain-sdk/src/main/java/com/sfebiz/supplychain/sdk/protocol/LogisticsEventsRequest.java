package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * 请求报头
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "logisticsEventsRequest")
@XmlType(propOrder = {"logisticsEvent"})
public class LogisticsEventsRequest implements Serializable {

    private static final long serialVersionUID = 1849756613358346218L;

    /**
     * 事件类结构体
     */
    @XmlElement(nillable = false, required = true)
    public LogisticsEvent logisticsEvent;

    public LogisticsEvent getLogisticsEvent() {
        if (logisticsEvent == null) {
            logisticsEvent = new LogisticsEvent();
        }
        return logisticsEvent;
    }

    public void setLogisticsEvent(LogisticsEvent logisticsEvent) {
        this.logisticsEvent = logisticsEvent;
    }

    @Override
    public String toString() {
        return "LogisticsEventsRequest{" +
                "logisticsEvent=" + logisticsEvent +
                '}';
    }
}
