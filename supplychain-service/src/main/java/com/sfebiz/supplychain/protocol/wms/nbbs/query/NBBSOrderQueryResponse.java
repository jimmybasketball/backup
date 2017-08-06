package com.sfebiz.supplychain.protocol.wms.nbbs.query;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;

/**
 * Description:查询回执报文
 * Created by yanghua on 2017/3/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
@XmlType(propOrder = {"result", "code", "message", "orderInfo","serialno", "extend"})
public class NBBSOrderQueryResponse {
    @Description("响应结果")
    @XmlElement(name = "result", required = false)
    private String result;

    @Description("结果码")
    @XmlElement(name = "code", required = false)
    private String code;

    @Description("结果码")
    @XmlElement(name = "message", required = false)
    private String message;


    @Description("订单信息")
    @XmlElement(name = "orderInfo", required = false)
    private NBBSOrderQueryResponseOrderInfo orderInfo;


    @Description("流水号")
    @XmlElement(name = "serialno", required = false)
    private String serialno;

    @Description("备注")
    @XmlElement(name = "extend", required = false)
    private String extend;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NBBSOrderQueryResponseOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(NBBSOrderQueryResponseOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
