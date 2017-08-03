package com.sfebiz.supplychain.protocol.zto.internation;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/2
 * Time: 下午2:31
 */
public class ZTOInternationrderResponse2 implements Serializable {

    private static final long serialVersionUID = -620558217869315995L;
    private String status;
    private String msg;
    private ZTOInternationrderDataResponse2 data;


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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ZTOInternationrderDataResponse2 getData() {
        return data;
    }

    public void setData(ZTOInternationrderDataResponse2 data) {
        this.data = data;
    }
}
