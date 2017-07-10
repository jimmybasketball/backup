package com.sfebiz.supplychain.persistence.base.merchant.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * 供应商线路DO
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-07 18:03
 **/
public class MerchantProviderLineDO extends BaseDO{

    private static final long serialVersionUID = 881086360484445264L;


    private Long id;

    /**
     * 货主供应商ID
     */
    private Long merchantProviderId;

    /**
     * 线路ID
     */
    private Long lineId;

    /**
     * 状态 ENABLE:启用  DISABLE:禁用
     */
    private String state;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 修改人
     */
    private String modifiedBy;


    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    @Override
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getGmtModified() {
        return gmtModified;
    }

    @Override
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
        return "MerchantProviderLineDO{" +
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
