package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * 响应报文
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "responses")
@XmlType(name = "responses", propOrder = {"responseItems"})
public class LogisticsEventsResponse implements Serializable {

    private static final long serialVersionUID = 9139562303458635700L;

    /**
     * 响应类列表
     */
    @XmlElementWrapper(name = "responseItems")
    @XmlElement(name = "response", nillable = false, required = false)
    public List<Response> responseItems;

    public List<Response> getResponseItems() {
        return responseItems;
    }

    public void setResponseItems(List<Response> responseItems) {
        this.responseItems = responseItems;
    }

    @Override
    public String toString() {
        return "LogisticsEventsResponse{" +
                "responseItems=" + responseItems +
                '}';
    }
}
