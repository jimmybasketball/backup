package com.sfebiz.supplychain.protocol.zto.internation;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/1
 * Time: 下午7:00
 */
public class ZTOInternationDistMarkResponse2 implements Serializable {

    private static final long serialVersionUID = 4887500387242777172L;
    private String status;
    private String msg;
    private String data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
