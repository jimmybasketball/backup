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
@XmlType(propOrder = {"copCode","copName","dxpMode","dxpId","note"})
@XmlRootElement(name = "BaseTransfer")
public class BaseTransfer implements Serializable {

    private static final long serialVersionUID = -4845922139375762017L;

    /**
     * 传输企业代码*
     */

    @XmlElement(name = "copCode")
    private String copCode;

    /**
     * 传输企业名称*
     */
    @XmlElement(name = "copName")
    private String copName;

    /**
     * 报文传输模式*
     */
    @XmlElement(name = "dxpMode")
    private String dxpMode;

    /**
     * 报文传输编号*
     */
    @XmlElement(name = "dxpId")
    private String dxpId;

    /**
     * 备注
     */
    @XmlElement(name = "note")
    private String note;

    public String getCopCode() {
        return copCode;
    }

    public void setCopCode(String copCode) {
        this.copCode = copCode;
    }

    public String getCopName() {
        return copName;
    }

    public void setCopName(String copName) {
        this.copName = copName;
    }

    public String getDxpMode() {
        return dxpMode;
    }

    public void setDxpMode(String dxpMode) {
        this.dxpMode = dxpMode;
    }

    public String getDxpId() {
        return dxpId;
    }

    public void setDxpId(String dxpId) {
        this.dxpId = dxpId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
