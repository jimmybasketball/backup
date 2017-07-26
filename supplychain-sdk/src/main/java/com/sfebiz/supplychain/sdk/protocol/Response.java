package com.sfebiz.supplychain.sdk.protocol;


import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * 响应类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "response", propOrder = {"success", "reason", "reasonDesc", "trackingNo", "shipOrderPdfLink", "traces", "skus", "orderQueryResult"})
public class Response implements Serializable {

    private static final long serialVersionUID = 4250928952064308343L;

    /**
     * 响应结果 true/false
     */
    @XmlElement(nillable = false, required = true)
    public String success;

    /**
     * 错误返回码
     */
    @XmlElement(nillable = false, required = true)
    public String reason;

    /**
     * 错误描述信息
     */
    @XmlElement(nillable = false, required = true)
    public String reasonDesc;

    /**
     * 追踪运单号或者参考号
     */
    @XmlElement(nillable = false, required = true)
    public String trackingNo;

    /**
     * 面单链接
     */
    @XmlElement(nillable = false, required = false)
    public String shipOrderPdfLink;

    /**
     * 路由信息列表
     */
    @XmlElementWrapper(name = "traces")
    @XmlElement(name = "trace", nillable = false, required = false)
    public List<Trace> traces;

    /**
     * sku库存信息列表
     */
    @XmlElementWrapper(name = "skus")
    @XmlElement(name = "sku", nillable = false, required = false)
    private List<Sku> skus;

    /**
     * 物流详情
     */
    @XmlElement(nillable = false, required = false)
    public OrderQueryResult orderQueryResult;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public List<Trace> getTraces() {
        return traces;
    }

    public void setTraces(List<Trace> traces) {
        this.traces = traces;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public OrderQueryResult getOrderQueryResult() {
        return orderQueryResult;
    }

    public void setOrderQueryResult(OrderQueryResult orderQueryResult) {
        this.orderQueryResult = orderQueryResult;
    }

    public String getShipOrderPdfLink() {
        return shipOrderPdfLink;
    }

    public void setShipOrderPdfLink(String shipOrderPdfLink) {
        this.shipOrderPdfLink = shipOrderPdfLink;
    }

    @Override
    public String toString() {
        return "Response{" +
                "success='" + success + '\'' +
                ", reason='" + reason + '\'' +
                ", reasonDesc='" + reasonDesc + '\'' +
                ", trackingNo='" + trackingNo + '\'' +
                ", shipOrderPdfLink='" + shipOrderPdfLink + '\'' +
                ", traces=" + traces +
                ", skus=" + skus +
                ", orderQueryResult=" + orderQueryResult +
                '}';
    }
}
