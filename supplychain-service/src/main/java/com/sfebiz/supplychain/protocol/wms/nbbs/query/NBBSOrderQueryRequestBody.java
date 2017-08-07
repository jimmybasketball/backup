package com.sfebiz.supplychain.protocol.wms.nbbs.query;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;

/**
 * Description:宁波保税订单查询报文体
 * Created by yanghua on 2017/3/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "body")
@XmlType(propOrder = {"orderNo", "startTime", "endTime", "page", "pageSize"})
public class NBBSOrderQueryRequestBody {
    @Description("订单号")  //rule=0或1时，必填，rule=0时（订单号=客户方订单号），rule=1时（订单号=能容订单号[EO开头]）
    @XmlElement(name = "orderNo",required = true)
    private String orderNo;

    @Description("查询开始时间")
    @XmlElement(name = "startTime",required = false)
    private String startTime;

    @Description("查询结束时间")
    @XmlElement(name = "endTime",required = false)
    private String endTime;

    @Description("查询指定页码")
    @XmlElement(name = "page",required = false)
    private String page;

    @Description("每页条数")
    @XmlElement(name = "pageSize",required = false)
    private String pageSize;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
