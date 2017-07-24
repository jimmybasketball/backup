package com.sfebiz.supplychain.persistence.base.warehouse.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 
 * <p>
 * 仓库实体
 * </p>
 * 
 * @author matt
 * @Date 2017年7月6日 下午5:01:58
 */
public class WarehouseDO extends BaseDO {

    /** 序号 */
    private static final long serialVersionUID = -6742783139781724481L;

    /** 仓库id */
    private Long              id;

    /** 仓库名称 */
    private String            name;

    /** 仓库内部系统使用业务id */
    private String            warehouseNid;

    /** 仓库对接由真实仓库方提供的仓库编码 */
    private String            warehouseCode;

    /** 服务提供者id */
    private String            logisticsProviderId;

    /** 丰趣负责人邮箱 */
    private String            principalEmail;

    /** 仓库所在区域 */
    private String            region;

    /** 仓库类型 */
    private String            warehouseType;

    /** 合作状态 */
    private String            cooperationState;

    /** 仓库运行状态 */
    private String            warehouseState;

    /** 合同开始日期 */
    private Date              contractPeriodStart;

    /** 合同截止日期 */
    private Date              contractPeriodEnd;

    /** 是否是存储仓 */
    private Integer           isStorage;

    /** 是否是退货仓 */
    private Integer           isReturn;

    /** 是否是转运仓 */
    private Integer           isTransit;

    /** 是否支持批次管理 */
    private Integer           isSupportBatch;

    /** 仓库所在国家 */
    private String            country;

    /** 仓库所在省 */
    private String            province;

    /** 仓库所在城市 */
    private String            city;

    /** 仓库所在区 */
    private String            district;

    /** 仓库所在详细地址 */
    private String            address;

    /** 仓库所在邮编 */
    private String            zipcode;

    /** 仓库对外显示的发货商名称 */
    private String            senderName;

    /** 仓库对外显示的发货商联系方式 */
    private String            senderTelephone;

    /** 仓库对外显示的发货国家 */
    private String            senderCountry;

    /** 仓库对外显示的发货商省 */
    private String            senderProvince;

    /** 仓库对外显示的发货商城市 */
    private String            senderCity;

    /** 仓库对外显示的发货商区 */
    private String            senderDistrict;

    /** 仓库对外显示的发货商详细地址 */
    private String            senderAddress;

    /** 仓库对外显示的发货商邮编 */
    private String            senderZipcode;

    /** 仓库联系人姓名 */
    private String            contactName;

    /** 仓库联系人邮箱 */
    private String            contactEmail;

    /** 仓库联系人手机 */
    private String            contactCellphone;

    /** 仓库联系人电话 */
    private String            contactTelephone;

    /** 数据创建时间 */
    private Date              gmtCreate;

    /** 数据修改时间 */
    private Date              gmtModified;

    /** 是否删除 */
    private Integer           isDelete;

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

    public String getWarehouseNid() {
        return warehouseNid;
    }

    public void setWarehouseNid(String warehouseNid) {
        this.warehouseNid = warehouseNid;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getLogisticsProviderId() {
        return logisticsProviderId;
    }

    public void setLogisticsProviderId(String logisticsProviderId) {
        this.logisticsProviderId = logisticsProviderId;
    }

    public String getPrincipalEmail() {
        return principalEmail;
    }

    public void setPrincipalEmail(String principalEmail) {
        this.principalEmail = principalEmail;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(String warehouseType) {
        this.warehouseType = warehouseType;
    }

    public String getCooperationState() {
        return cooperationState;
    }

    public void setCooperationState(String cooperationState) {
        this.cooperationState = cooperationState;
    }

    public String getWarehouseState() {
        return warehouseState;
    }

    public void setWarehouseState(String warehouseState) {
        this.warehouseState = warehouseState;
    }

    public Date getContractPeriodStart() {
        return contractPeriodStart;
    }

    public void setContractPeriodStart(Date contractPeriodStart) {
        this.contractPeriodStart = contractPeriodStart;
    }

    public Date getContractPeriodEnd() {
        return contractPeriodEnd;
    }

    public void setContractPeriodEnd(Date contractPeriodEnd) {
        this.contractPeriodEnd = contractPeriodEnd;
    }

    public Integer getIsStorage() {
        return isStorage;
    }

    public void setIsStorage(Integer isStorage) {
        this.isStorage = isStorage;
    }

    public Integer getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Integer isReturn) {
        this.isReturn = isReturn;
    }

    public Integer getIsTransit() {
        return isTransit;
    }

    public void setIsTransit(Integer isTransit) {
        this.isTransit = isTransit;
    }

    public Integer getIsSupportBatch() {
        return isSupportBatch;
    }

    public void setIsSupportBatch(Integer isSupportBatch) {
        this.isSupportBatch = isSupportBatch;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderTelephone() {
        return senderTelephone;
    }

    public void setSenderTelephone(String senderTelephone) {
        this.senderTelephone = senderTelephone;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getSenderProvince() {
        return senderProvince;
    }

    public void setSenderProvince(String senderProvince) {
        this.senderProvince = senderProvince;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderDistrict() {
        return senderDistrict;
    }

    public void setSenderDistrict(String senderDistrict) {
        this.senderDistrict = senderDistrict;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderZipcode() {
        return senderZipcode;
    }

    public void setSenderZipcode(String senderZipcode) {
        this.senderZipcode = senderZipcode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactCellphone() {
        return contactCellphone;
    }

    public void setContactCellphone(String contactCellphone) {
        this.contactCellphone = contactCellphone;
    }

    public String getContactTelephone() {
        return contactTelephone;
    }

    public void setContactTelephone(String contactTelephone) {
        this.contactTelephone = contactTelephone;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
