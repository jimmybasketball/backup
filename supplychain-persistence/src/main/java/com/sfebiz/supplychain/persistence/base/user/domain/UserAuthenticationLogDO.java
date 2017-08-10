package com.sfebiz.supplychain.persistence.base.user.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * 实名认证三方服务请求记录实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-27 09:31
 **/
public class UserAuthenticationLogDO extends BaseDO{
    private static final long serialVersionUID = -5924908697289068299L;

    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 扩展信息
     */
    private String features;


    private Date gmtCreate;


    private Date gmtModified;

    /**
     * 第三方服务渠道
     */
    private String channel;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    @Override
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public Date getGmtModified() {
        return gmtModified;
    }

    @Override
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "UserAuthenticationLogDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idNo='" + idNo + '\'' +
                ", features='" + features + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", channel='" + channel + '\'' +
                '}';
    }
}
