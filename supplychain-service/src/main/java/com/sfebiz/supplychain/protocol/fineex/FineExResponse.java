package com.sfebiz.supplychain.protocol.fineex;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;

/**
 * Created by liujunc on 2017/3/13.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
@XmlType(propOrder = {"flag", "code", "message"})
public class FineExResponse {

    @Description("响应标识")
    @XmlElement(name = "flag")
    private String flag;

    @Description("响应编码")
    @XmlElement(name = "code")
    private String code;

    @Description("响应消息")
    @XmlElement(name = "message")
    private String message;


    public String getFlag() {
        return flag;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "FineExResponse{" +
                "flag=" + flag +
                ", code=" + code +
                ", message=" + message +
                '}';
    }
}
