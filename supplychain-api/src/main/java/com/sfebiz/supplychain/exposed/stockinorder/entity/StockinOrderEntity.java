package com.sfebiz.supplychain.exposed.stockinorder.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 入库单实体
 */
public class StockinOrderEntity implements Serializable {

    private static final Long serialVersionUID = 2701387975579997849L;
    /**
     * id
     */
    public Long id;
    /**
     * 入库单类型：0 - 采购入库；1- 转运入库; 2- 调拨入库；3 - 退货入库；此字段必填
     */
    public Integer type;
    /**
     * 仓库ID，此字段必填"
     */
    public Long warehouseId;
    /**
     * 入库单状态
     */
    public String state;
    /**
     * 业务订单ID
     */
    public String bizId;
    /**
     * 入库单号，此字段必填
     */
    public String stockinId;
    /**
     * 联系人，此字段必填
     */
    public String contactName;
    /**
     * 联系人电话，此字段必填
     */
    public String contactPhone;
    /**
     * 入库运单号
     */
    public String mailNo;
    /**
     * 物流公司
     */
    public String logisticsCompany;
    /**
     * 车牌号
     */
    public String plateNumber;
    /**
     * 供应商id
     */
    public Long merchantProviderId;
    /**
     * 货主代码
     */
    public Long merchantId;
    /**
     * 运输方式
     */
    public Integer transportType;
    /**
     * 预计供应商发货时间
     */
    public Date predictSendTime;
    /**
     * 预计到港时间
     */
    public Date predictArrivePort;
    /**
     * 实际到港时间
     */
    public Date actdictArrivePort;
    /**
     * 仓库理货开始时间
     */
    public Date warehouseConfirmStart;
    /**
     * 仓库理货结束时间
     */
    public Date warehouseConfirmEnd;/**
     * 仓库回传的入库时间
     */
    public Date warehouseStockinTime;
    /**
     * 完成收货时间
     */
    public Date stockinFinishTime;

    /**
     * 入库明细列表
     */
    public List<StockinOrderDetailEntity> skus;

    @Override
    public String toString() {
        return "StockinOrderEntity{" +
                "id=" + id +
                ", type=" + type +
                ", warehouseId=" + warehouseId +
                ", state='" + state + '\'' +
                ", bizId='" + bizId + '\'' +
                ", stockinId='" + stockinId + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", mailNo='" + mailNo + '\'' +
                ", logisticsCompany='" + logisticsCompany + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", merchantProviderId=" + merchantProviderId +
                ", merchantId=" + merchantId +
                ", transportType=" + transportType +
                ", predictSendTime=" + predictSendTime +
                ", predictArrivePort=" + predictArrivePort +
                ", actdictArrivePort=" + actdictArrivePort +
                ", warehouseConfirmStart=" + warehouseConfirmStart +
                ", warehouseConfirmEnd=" + warehouseConfirmEnd +
                ", warehouseStockinTime=" + warehouseStockinTime +
                ", stockinFinishTime=" + stockinFinishTime +
                ", skus=" + skus +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getStockinId() {
        return stockinId;
    }

    public void setStockinId(String stockinId) {
        this.stockinId = stockinId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Long getMerchantProviderId() {
        return merchantProviderId;
    }

    public void setMerchantProviderId(Long merchantProviderId) {
        this.merchantProviderId = merchantProviderId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getTransportType() {
        return transportType;
    }

    public void setTransportType(Integer transportType) {
        this.transportType = transportType;
    }

    public Date getPredictSendTime() {
        return predictSendTime;
    }

    public void setPredictSendTime(Date predictSendTime) {
        this.predictSendTime = predictSendTime;
    }

    public Date getPredictArrivePort() {
        return predictArrivePort;
    }

    public void setPredictArrivePort(Date predictArrivePort) {
        this.predictArrivePort = predictArrivePort;
    }

    public Date getActdictArrivePort() {
        return actdictArrivePort;
    }

    public void setActdictArrivePort(Date actdictArrivePort) {
        this.actdictArrivePort = actdictArrivePort;
    }

    public Date getWarehouseConfirmStart() {
        return warehouseConfirmStart;
    }

    public void setWarehouseConfirmStart(Date warehouseConfirmStart) {
        this.warehouseConfirmStart = warehouseConfirmStart;
    }

    public Date getWarehouseConfirmEnd() {
        return warehouseConfirmEnd;
    }

    public void setWarehouseConfirmEnd(Date warehouseConfirmEnd) {
        this.warehouseConfirmEnd = warehouseConfirmEnd;
    }

    public Date getWarehouseStockinTime() {
        return warehouseStockinTime;
    }

    public void setWarehouseStockinTime(Date warehouseStockinTime) {
        this.warehouseStockinTime = warehouseStockinTime;
    }

    public Date getStockinFinishTime() {
        return stockinFinishTime;
    }

    public void setStockinFinishTime(Date stockinFinishTime) {
        this.stockinFinishTime = stockinFinishTime;
    }

    public List<StockinOrderDetailEntity> getSkus() {
        return skus;
    }

    public void setSkus(List<StockinOrderDetailEntity> skus) {
        this.skus = skus;
    }
}
