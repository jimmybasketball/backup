package com.sfebiz.supplychain.protocol.zto.internation;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/1
 * Time: 下午7:00
 */
public class ZTOInternationDistMarkResponse implements Serializable {

    private static final long serialVersionUID = 4887500387242777172L;

    private String message;

    private ZTOInternationDistMarkResponseResult result;

    private String status;

    private String statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZTOInternationDistMarkResponseResult getResult() {
        return result;
    }

    public void setResult(ZTOInternationDistMarkResponseResult result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
