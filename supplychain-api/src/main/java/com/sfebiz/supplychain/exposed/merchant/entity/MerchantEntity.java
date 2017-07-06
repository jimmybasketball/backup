package com.sfebiz.supplychain.exposed.merchant.entity;

import com.sfebiz.supplychain.exposed.common.enums.RegularExpressionConstant;
import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 物流平台商户实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017/7/5 10:04
 */
public class MerchantEntity implements Serializable {

    private static final long serialVersionUID = -5876613883600519095L;
    /**
     * id
     */
    public Long id;

    /**
     * 商户名称
     */
    @NotNull(message = "商户名称不能为空")
    @Length(max = 64, min = 2, message = "商户名称长度为2-64位")
    public String name;

    /**
     * 商户账户名称
     */
    @NotNull(message = "商户账户ID不能为空")
    @MatchPattern(pattern = {"^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$"},
            message = "商户账户ID由6-16位数字和字符串组成")
    public String merchantAccountId;


    /**
     * 商户状态
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

    /**
     * 企业名称
     */
    @NotNull(message = "企业名称不能为空")
    @Length(max = 128, min = 2, message = "企业名称长度为2-128位")
    public String enterpriseName;

    /**
     * 企业地址
     */
    @NotNull(message = "企业地址不能为空")
    @Length(max = 256, min = 2, message = "企业地址长度为2-256位")
    public String enterpriseAddress;

    /**
     * 营业执照号
     */
    @Length(max = 50, message = "营业执照号最长为50位")
    public String businessLicenseNo;

    /**
     * 法定代表人
     */
    @NotNull(message = "法定代表人不能为空")
    @Length(max = 64, min = 2, message = "法定代表人长度为2-64位")
    public String legalRepresentative;

    /**
     * 联系人
     */
    @NotNull(message = "联系人不能为空")
    @Length(max = 64, min = 2, message = "联系人长度为2-64位")
    public String linkman;

    /**
     * 联系人邮箱
     */
    @NotNull(message = "联系人不能为空")
    @Email(message = "联系人邮箱不合法")
    public String linkmanEmail;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    @MatchPattern(pattern = {RegularExpressionConstant.SIMPLE_PHONE_CHECK}, message = "手机号不合法")
    public String cellPhoneNumber;

    /**
     * 固定电话
     */
    @NotNull(message = "固定电话不能为空")
    @MatchPattern(pattern = {RegularExpressionConstant.PHONE_CALL_PATTERN}, message = "固定电话不合法")
    public String landlineTelephone;


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

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public void setMerchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
    }

    @Override
    public String toString() {
        return "MerchantEntity{" +
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
