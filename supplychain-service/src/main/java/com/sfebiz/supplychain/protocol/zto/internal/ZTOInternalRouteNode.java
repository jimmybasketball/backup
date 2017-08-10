package com.sfebiz.supplychain.protocol.zto.internal;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/17
 * Time: 下午6:50
 */
public class ZTOInternalRouteNode implements Serializable {
    private static final long serialVersionUID = 301458941740356071L;

    /**
     * 快递地址
     */
    private String acceptAddress;

    /**
     * 处理时间
     */
    private String acceptTime;

    /**
     * 备注信息，用于展示给用户浏览
     */
    private String remark;

    public String getAcceptAddress() {
        return acceptAddress;
    }

    public void setAcceptAddress(String acceptAddress) {
        this.acceptAddress = acceptAddress;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
