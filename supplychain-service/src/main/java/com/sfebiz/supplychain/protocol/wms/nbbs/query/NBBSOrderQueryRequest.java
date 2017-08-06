package com.sfebiz.supplychain.protocol.wms.nbbs.query;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;

/**
 * Description: 用于构造宁波保税区查询请求
 * Created by yanghua on 2017/3/21.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
@XmlType(propOrder = {"head", "body"})
public class NBBSOrderQueryRequest {
    @Description("请求头")
    @XmlElement(name = "head",required = true)
    private NBBSOrderQueryRequestHead head;
    @Description("请求体")
    @XmlElement(name = "body",required = true)
    private NBBSOrderQueryRequestBody body;

    public NBBSOrderQueryRequestHead getHead() {
        return head;
    }

    public void setHead(NBBSOrderQueryRequestHead head) {
        this.head = head;
    }

    public NBBSOrderQueryRequestBody getBody() {
        return body;
    }

    public void setBody(NBBSOrderQueryRequestBody body) {
        this.body = body;
    }
}
