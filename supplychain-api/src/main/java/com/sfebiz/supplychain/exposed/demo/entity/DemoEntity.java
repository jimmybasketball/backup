package com.sfebiz.supplychain.exposed.demo.entity;

import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 测试用的
 *
 * @author liujc
 * @create 2017-06-29 16:02
 **/
public class DemoEntity implements Serializable{

    private static final long serialVersionUID = 2676786202616481974L;

    public Long id;


    @NotNull(message = "name不能为空")
    public String name;

    public Date gmtCreate;


    public Date gmtModified;


//    @NotNull(message = "明细不能为空")
//    @ValidateWithMethod(methodName = "checkDetail", message = "明细参数不合法", parameterType = List.class)
    public List<DemoDetailEntity> demoDetailEntities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "DemoEntity{" +
                "id=" + id +
                ", name=" + name +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }

    public boolean checkDetail(List<DemoDetailEntity> demoDetailEntities) {
        System.out.println("验证明细了");
        if (!(demoDetailEntities.size() > 0)) {
            return false;
        }
        return true;
    }
}
