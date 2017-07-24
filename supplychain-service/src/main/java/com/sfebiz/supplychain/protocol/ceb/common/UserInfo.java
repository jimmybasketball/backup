package com.sfebiz.supplychain.protocol.ceb.common;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午2:45
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"userId","userName","copCode","copName"})
@XmlRootElement(name = "UserInfo")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 7486584510429607463L;

    /**
     * 签名用户编号*
     */
    @XmlElement(name = "userId")
    private String userId;

    /**
     * 签名用户姓名*
     */
    @XmlElement(name = "userName")
    private String userName;

    /**
     * 签名企业编号*
     */
    @XmlElement(name = "copCode")
    private String copCode;

    /**
     * 签名企业名称*
     */
    @XmlElement(name = "copName")
    private String copName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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
}
