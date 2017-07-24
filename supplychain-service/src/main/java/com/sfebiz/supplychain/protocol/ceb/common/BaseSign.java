package com.sfebiz.supplychain.protocol.ceb.common;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午2:18
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"signData", "signTime", "signResult", "UserInfo", "CertInfo"})
@XmlRootElement(name = "BaseSign")
public class BaseSign implements Serializable {

    private static final long serialVersionUID = -1319463252144818513L;

    /**
     * 签名数据
     */
    @XmlElement(name = "signData")
    private String signData;

    /**
     * 签名时间
     */
    @XmlElement(name = "signTime")
    private String signTime;

    /**
     * 签名结果
     */
    @XmlElement(name = "signResult")
    private String signResult;

    /**
     * 签名用户基本信息节点*
     */
    @XmlElement(name = "UserInfo")
    private UserInfo UserInfo;

    /**
     * 签名用户证书信息节点*
     */
    @XmlElement(name = "CertInfo")
    private CertInfo CertInfo;


    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getSignResult() {
        return signResult;
    }

    public void setSignResult(String signResult) {
        this.signResult = signResult;
    }

    public UserInfo getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        UserInfo = userInfo;
    }

    public CertInfo getCertInfo() {
        return CertInfo;
    }

    public void setCertInfo(CertInfo certInfo) {
        CertInfo = certInfo;
    }
}
