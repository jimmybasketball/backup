package com.sfebiz.supplychain.protocol.ceb.pingtanorder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by TT on 2016/8/3.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OrderPaymentLogistics")
@XmlType(propOrder = {"paymentCode","paymentName","paymentType","paymentNo","logisticsCode","logisticsName","logisticsNo","trackNo"})
public class OrderPaymentLogistics implements Serializable{
    private static final long serialVersionUID = -5282837284967223732L;

    /**
     * 支付企业代码
     */
    @XmlElement(name = "paymentCode")
    private String paymentCode;

    /**
     *支付企业名称
     */
    @XmlElement(name = "paymentName")
    private String paymentName;

    /**
     *支付类型
     */
    @XmlElement(name = "paymentType")
    private String paymentType;

    /**
     *支付交易号
     */
    @XmlElement(name = "paymentNo")
    private String paymentNo;

    /**
     *物流企业代码
     */
    @XmlElement(name = "logisticsCode")
    private String logisticsCode;

    /**
     *物流企业名称
     */
    @XmlElement(name = "logisticsName")
    private String logisticsName;

    /**
     *物流运单号
     */
    @XmlElement(name = "logisticsNo")
    private String logisticsNo;

    /**
     *物流跟踪号
     */
    @XmlElement(name = "trackNo")
    private String trackNo;


    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }
}
