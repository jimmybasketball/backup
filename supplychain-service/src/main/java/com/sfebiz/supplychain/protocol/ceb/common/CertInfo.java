package com.sfebiz.supplychain.protocol.ceb.common;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午2:46
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"certType","certNo","certKey"})
@XmlRootElement(name = "CertInfo")
public class CertInfo implements Serializable {

    private static final long serialVersionUID = 2261459413086995803L;

    /**
     * 证书类型
     */
    @XmlElement(name = "certType")
    private String certType;

    /**
     * 证书编号
     */
    @XmlElement(name = "certNo")
    private String certNo;

    /**
     * 证书内容
     */
    @XmlElement(name = "certKey")
    private String certKey;

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getCertKey() {
        return certKey;
    }

    public void setCertKey(String certKey) {
        this.certKey = certKey;
    }
}
