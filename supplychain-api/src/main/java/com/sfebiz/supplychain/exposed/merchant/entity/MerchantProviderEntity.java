package com.sfebiz.supplychain.exposed.merchant.entity;

import com.sfebiz.supplychain.exposed.common.enums.RegularExpressionConstant;
import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 货主供应商实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-06 19:22
 **/
public class MerchantProviderEntity implements Serializable {
    private static final long serialVersionUID = -6277660733405769897L;

    public Long id;

    /**
     * 货主供应商简称
     */
    @NotNull(message = "货主供应商简称不能为空")
    @Length(max = 64, min = 2, message = "货主供应商简称长度为2-64位")
    public String name;

    /**
     * 货主供应商状态  ENABLE:启用  DISABLE:禁用
     */
    public String state;

    /**
     * 物流平台货主(账户、货主)ID
     */
    @NotNull(message = "货主ID不能为空")
    public Long merchantId;

    /**
     * 货主供应商所属国家
     */
    @NotNull(message = "供应商所属国不能为空")
    public Long nationId;

    /**
     * 货主供应商全称
     */
    @NotNull(message = "货主供应商全称不能为空")
    @Length(max = 128, min = 2, message = "商货主供应商全称长度为2-128位")
    public String fullName;

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

    /**
     * 退货地址
     */
    @Length(max = 256, message = "退货地址长度为0-256")
    public String returnAddress;

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

    public Long getNationId() {
        return nationId;
    }

    public void setNationId(Long nationId) {
        this.nationId = nationId;
    }

    @Override
    public String toString() {
        return "MerchantProviderEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", merchantId=" + merchantId +
                ", nationId='" + nationId + '\'' +
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
