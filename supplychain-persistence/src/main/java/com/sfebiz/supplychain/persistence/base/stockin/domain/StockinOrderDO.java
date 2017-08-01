package com.sfebiz.supplychain.persistence.base.stockin.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * <p></p>
 * User: 张雅静
 * Date: 17/07/10
 * Time: 下午3:37
 */
public class StockinOrderDO extends BaseDO {

    private static final long serialVersionUID = 6112749332678842964L;
    //入库单类型：0 - 采购入库；1- 转运入库; 2- 调拨入库；3 - 退货入库；
    private Integer type;
    //仓库ID
    private Long warehouseId;
    //入库单状态：TO_BE_SUBMITED:待提交,TO_BE_SUBMITED:待审核,AUDIT_NOT_PASS:审核不通过,WAREHOUSING:运输中,COUNT_CONFIRM:收货完成
    private String state;
    //业务订单ID：关联单号，如退货入库则是订单号
    private String bizId;
    //入库单号
    private String stockinId;
    //联系人
    private String contactName;
    //联系人电话
    private String contactPhone;
    //运单号
    private String mailNo;
    //物流公司
    private String logisticsCompany;
    //车牌号
    private String plateNumber;
    //供应商ID
    private Long merchantProviderId;
    //货主代码
    private Long merchantId;
    //运输方式
    private Integer transportType;
    //成功推送仓库时间
    private Date asnSuccessTime;
    //预计供应商发货时间
    private Date predictSendTime;
    //预计到港时间
    private Date predictArrivePort;
    //仓库回传的入库时间
    private Date warehouseStockinTime;
    //完成收货时间
    private Date stockinFinishTime;
    //仓库理货开始时间
    private Date warehouseConfirmStart;
    //仓库理货结束时间
    private Date warehouseConfirmEnd;

    @Override
    public String toString() {
        return "StockinOrderDO{" +
                "type=" + type +
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
                ", asnSuccessTime=" + asnSuccessTime +
                ", predictSendTime=" + predictSendTime +
                ", predictArrivePort=" + predictArrivePort +
                ", warehouseStockinTime=" + warehouseStockinTime +
                ", stockinFinishTime=" + stockinFinishTime +
                ", warehouseConfirmStart=" + warehouseConfirmStart +
                ", warehouseConfirmEnd=" + warehouseConfirmEnd +
                '}';
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

    public Date getAsnSuccessTime() {
        return asnSuccessTime;
    }

    public void setAsnSuccessTime(Date asnSuccessTime) {
        this.asnSuccessTime = asnSuccessTime;
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
}
