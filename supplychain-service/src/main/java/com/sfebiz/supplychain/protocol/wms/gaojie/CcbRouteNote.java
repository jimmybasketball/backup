package com.sfebiz.supplychain.protocol.wms.gaojie;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/15
 * Time: 下午3:10
 */
public class CcbRouteNote implements Serializable {
    private static final long serialVersionUID = 8371875874078584851L;

    /**
     * 路由发生城市
     */
    private String acceptAddress;

    /**
     * 路由发生时间
     */
    private String acceptTime;

    /**
     * 操作码
     */
    private String opcode;

    /**
     * 路由具体信息
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

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
