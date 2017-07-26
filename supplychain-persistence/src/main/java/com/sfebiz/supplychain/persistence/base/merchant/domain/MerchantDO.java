package com.sfebiz.supplychain.persistence.base.merchant.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * 物流平台货主DO
 *
 * @author liujc
 * @create 2017-07-04 18:22
 **/
public class MerchantDO extends BaseDO {

    private static final long serialVersionUID = 2624319259581378362L;
    /**
     * id
     */
    private Long id;

    /**
     * 货主名称
     */
    private String name;

    /**
     * 货主账户ID
     */
    private String merchantAccountId;


    /**
     * 账户状态，启用/禁用
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

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 企业地址
     */
    private String enterpriseAddress;

    /**
     * 营业执照号
     */
    private String businessLicenseNo;

    /**
     * 法定代表人
     */
    private String legalRepresentative;

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

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public void setMerchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
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

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }

    public String getBusinessLicenseNo() {
        return businessLicenseNo;
    }

    public void setBusinessLicenseNo(String businessLicenseNo) {
        this.businessLicenseNo = businessLicenseNo;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
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

    @Override
    public String toString() {
        return "MerchantDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", merchantAccountId='" + merchantAccountId + '\'' +
                ", state='" + state + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", createBy='" + createBy + '\'' +
                ", gmtModified=" + gmtModified +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", enterpriseAddress='" + enterpriseAddress + '\'' +
                ", businessLicenseNo='" + businessLicenseNo + '\'' +
                ", legalRepresentative='" + legalRepresentative + '\'' +
                ", linkman='" + linkman + '\'' +
                ", linkmanEmail='" + linkmanEmail + '\'' +
                ", cellPhoneNumber='" + cellPhoneNumber + '\'' +
                ", landlineTelephone='" + landlineTelephone + '\'' +
                '}';
    }
}
