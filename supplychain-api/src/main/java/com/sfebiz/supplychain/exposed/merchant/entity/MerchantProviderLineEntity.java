package com.sfebiz.supplychain.exposed.merchant.entity;

import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商线路实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-07 18:13
 **/
public class MerchantProviderLineEntity implements Serializable{
    public static final long serialVersionUID = 4859553727316102649L;

    public Long id;

    /**
     * 货主供应商ID
     */
    @NotNull(message = "货主供应商ID不能为空")
    public Long merchantProviderId;

    /**
     * 线路ID
     */
    @NotNull(message = "线路ID不能为空")
    public Long lineId;

    /**
     * 状态 ENABLE:启用  DISABLE:禁用
     */
    public String state;

    /**
     * 创建时间
     */
    public Date gmtCreate;

    /**
     * 创建人
     */
    public String createBy;

    /**
     * 修改时间
     */
    public Date gmtModified;

    /**
     * 修改人
     */
    public String modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getMerchantProviderId() {
        return merchantProviderId;
    }

    public void setMerchantProviderId(Long merchantProviderId) {
        this.merchantProviderId = merchantProviderId;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public String toString() {
        return "MerchantProviderLineEntity{" +
                "id=" + id +
                ", merchantProviderId=" + merchantProviderId +
                ", lineId=" + lineId +
                ", state='" + state + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", createBy='" + createBy + '\'' +
                ", gmtModified=" + gmtModified +
                ", modifiedBy='" + modifiedBy + '\'' +
                '}';
    }
}
