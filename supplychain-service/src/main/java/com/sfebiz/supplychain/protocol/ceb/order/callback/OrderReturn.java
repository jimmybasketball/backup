package com.sfebiz.supplychain.protocol.ceb.order.callback;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/4/7
 * Time: 下午9:16
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"guid", "ebpCode", "ebcCode", "orderNo", "returnStatus", "returnTime", "returnInfo"})
@XmlRootElement(name = "OrderReturn")
public class OrderReturn implements Serializable {

    private static final long serialVersionUID = -2385023769978128840L;
    /**
     * 系统唯一序号
     * 企业系统生成36位唯一序号（英文字母大写）
     */
    @XmlElement(name = "guid")
    private String guid;

    @XmlElement(name = "ebpCode")
    private String ebpCode;

    @XmlElement(name = "ebcCode")
    private String ebcCode;

    @XmlElement(name = "orderNo")
    private String orderNo;

    @XmlElement(name = "returnStatus")
    private String returnStatus;

    @XmlElement(name = "returnTime")
    private String returnTime;

    @XmlElement(name = "returnInfo")
    private String returnInfo;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getEbpCode() {
        return ebpCode;
    }

    public void setEbpCode(String ebpCode) {
        this.ebpCode = ebpCode;
    }

    public String getEbcCode() {
        return ebcCode;
    }

    public void setEbcCode(String ebcCode) {
        this.ebcCode = ebcCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }
}
