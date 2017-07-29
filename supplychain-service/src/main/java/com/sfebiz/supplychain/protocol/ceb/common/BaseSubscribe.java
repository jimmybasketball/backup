package com.sfebiz.supplychain.protocol.ceb.common;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午2:17
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"status","dxpMode","dxpAddress","note"})
@XmlRootElement(name = "BaseSubscribe")
public class BaseSubscribe implements Serializable {

    private static final long serialVersionUID = 7489591533911846008L;

    /**
     * 订阅状态
     */
    @XmlElement(name = "status")
    private String status;

    /**
     * 订阅方传输模式
     */
    @XmlElement(name = "dxpMode")
    private String dxpMode;

    /**
     * 订阅方传输地址
     */
    @XmlElement(name = "dxpAddress")
    private String dxpAddress;

    /**
     * 备注
     */
    @XmlElement(name = "note")
    private String note;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDxpMode() {
        return dxpMode;
    }

    public void setDxpMode(String dxpMode) {
        this.dxpMode = dxpMode;
    }

    public String getDxpAddress() {
        return dxpAddress;
    }

    public void setDxpAddress(String dxpAddress) {
        this.dxpAddress = dxpAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
