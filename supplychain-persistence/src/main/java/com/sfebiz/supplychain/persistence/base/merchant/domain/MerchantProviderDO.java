package com.sfebiz.supplychain.persistence.base.merchant.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * 货主供应商持久化对象
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-06 19:30
 **/
public class MerchantProviderDO extends BaseDO{
    private static final long serialVersionUID = 2144284393618395090L;

    private Long id;

    /**
     * 货主供应商简称
     */
    private String name;

    /**
     * 货主供应商状态  ENABLE:启用  DISABLE:禁用
     */
    private String state;

    /**
     * 物流平台货主(账户、货主)ID
     */
    private Long merchantId;

    /**
     * 货主供应商所属国家ID
     */
    private Long nationId;

    /**
     * 货主供应商全称
     */
    private String fullName;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 联系人邮箱
     */
    private String linkmanEmail;

    /**
     * 手机号
     */
    private String cellPhoneNumber;

    /**
     * 固定电话
     */
    private String landlineTelephone;

    /**
     * 退货地址
     */
    private String returnAddress;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkmanEmail() {
        return linkmanEmail;
    }

    public void setLinkmanEmail(String linkmanEmail) {
        this.linkmanEmail = linkmanEmail;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getLandlineTelephone() {
        return landlineTelephone;
    }

    public void setLandlineTelephone(String landlineTelephone) {
        this.landlineTelephone = landlineTelephone;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
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


    public Long getNationId() {
        return nationId;
    }

    public void setNationId(Long nationId) {
        this.nationId = nationId;
    }

    @Override
    public String toString() {
        return "MerchantProviderDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", merchantId=" + merchantId +
                ", nationId=" + nationId +
                ", fullName='" + fullName + '\'' +
                ", linkman='" + linkman + '\'' +
                ", linkmanEmail='" + linkmanEmail + '\'' +
                ", cellPhoneNumber='" + cellPhoneNumber + '\'' +
                ", landlineTelephone='" + landlineTelephone + '\'' +
                ", returnAddress='" + returnAddress + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", createBy='" + createBy + '\'' +
                ", gmtModified=" + gmtModified +
                ", modifiedBy='" + modifiedBy + '\'' +
                '}';
    }
}
