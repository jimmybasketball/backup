package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 16/1/21 上午10:36
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orderQueryRequest", propOrder = {"pageIndex", "pageSize", "orderId", "status", "startTime", "endTime"})
public class OrderQueryRequest implements Serializable {

    private static final long serialVersionUID = -8341733838901771336L;


    /**
     * 分页查询的页码，默认从第一页开始（ 必填）
     */
    @XmlElement(nillable = false, required = false)
    public Integer pageIndex;

    /**
     * 分页查询的每页查询最大数量，默认20，最大100
     */
    @XmlElement(nillable = false, required = false)
    public Integer pageSize;

    /**
     * 出库单号
     */
    @XmlElement(nillable = false, required = false)
    public String orderId;

    /**
     * 出库单状态
     */
    @XmlElement(nillable = false, required = false)
    public String status;

    /**
     * 出路单创建时间 起
     * the number of milliseconds since January 1, 1970, 00:00:00 GMT
     */
    @XmlElement(nillable = false, required = false)
    public Long startTime;

    /**
     * 出路单创建时间 止
     * the number of milliseconds since January 1, 1970, 00:00:00 GMT
     */
    @XmlElement(nillable = false, required = false)
    public Long endTime;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "OrderQueryRequest{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", orderId='" + orderId + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
