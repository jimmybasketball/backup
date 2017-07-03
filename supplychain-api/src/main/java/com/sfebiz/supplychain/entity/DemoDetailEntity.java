package com.sfebiz.supplychain.entity;

import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 测试用例
 *
 * @author liujc
 * @create 2017-07-01 15:38
 **/
public class DemoDetailEntity implements Serializable{
    private static final long serialVersionUID = -3651164018106576502L;

    public Long id;

    public Long demoId;

    @NotNull(message = "name不能为空")
    public String name;

    public Date gmtCreate;

    public Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDemoId() {
        return demoId;
    }

    public void setDemoId(Long demoId) {
        this.demoId = demoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "DemoDetailEntity{" +
                "id=" + id +
                ", demoId=" + demoId +
                ", name='" + name + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
