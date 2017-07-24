package com.sfebiz.supplychain.service.warehouse.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>仓库业务处理实体</p>
 * 
 * @author matt
 * @Date 2017年7月20日 上午10:50:48
 */
public class WarehouseBO implements Serializable {

    /** 序号 */
    private static final long            serialVersionUID = -3116989086753548935L;

    /** 仓库id */
    private Long                         id;

    /** 仓库名称 */
    private String                       name;

    /** 仓库内部系统使用业务id */
    private String                       warehouseNid;

    /** 仓库对接由真实仓库方提供的仓库编码 */
    private String                       warehouseCode;

    /** 服务提供者id */
    private String                       logisticsProviderId;

    /** 丰趣负责人邮箱 */
    private String                       principalEmail;

    /** 仓库所在区域 */
    private String                       region;

    /** 仓库类型 */
    private String                       warehouseType;

    /** 合作状态 */
    private String                       cooperationState;

    /** 仓库运行状态 */
    private String                       warehouseState;

    /** 合同开始日期 */
    private Date                         contractPeriodStart;

    /** 合同截止日期 */
    private Date                         contractPeriodEnd;

    /** 是否是存储仓 */
    private Integer                      isStorage;

    /** 是否是退货仓 */
    private Integer                      isReturn;

    /** 是否是转运仓 */
    private Integer                      isTransit;

    /** 是否支持批次管理 */
    private Integer                      isSupportBatch;

    /** 仓库地址信息 */
    private WarehouseAddressBO           addressBO;

    /** 仓库发货相关信息 */
    private WarehouseSenderBO            senderBO;

    /** 仓库联系信息 */
    private WarehouseContactBO           contactBO;

    /** 仓库物流提供者信息 */
    private WarehouseLogisticsProviderBO logisticsProviderBO;

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

    public WarehouseAddressBO getAddressBO() {
        return addressBO;
    }

    public void setAddressBO(WarehouseAddressBO addressBO) {
        this.addressBO = addressBO;
    }

    public WarehouseSenderBO getSenderBO() {
        return senderBO;
    }

    public void setSenderBO(WarehouseSenderBO senderBO) {
        this.senderBO = senderBO;
    }

    public WarehouseContactBO getContactBO() {
        return contactBO;
    }

    public void setContactBO(WarehouseContactBO contactBO) {
        this.contactBO = contactBO;
    }

    public WarehouseLogisticsProviderBO getLogisticsProviderBO() {
        return logisticsProviderBO;
    }

    public void setLogisticsProviderBO(WarehouseLogisticsProviderBO logisticsProviderBO) {
        this.logisticsProviderBO = logisticsProviderBO;
    }

}
