package com.sfebiz.supplychain.exposed.line.entity;

import net.sf.oval.constraint.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 线路配置实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-10 19:41
 **/
public class LogisticsLineEntity implements Serializable{


    private static final long serialVersionUID = 6326930876199006916L;


    public Long id;

    /**
     * 线路名称
     */
    public String logisticsLineNid;

    /**
     * 线路状态  ENABLE:启用  DISABLE:禁用
     */
    public String state;

    /**
     * 线路运营状态  NORMAL:正常   STOPED:停止
     */
    public String operateState;

    /**
     * 服务类型
     */
    @NotNull(message = "服务类型不能为空")
    public Integer serviceType;

    /**
     * 口岸ID
     */
    @NotNull(message = "口岸ID不能为空")
    public Long portId;

    /**
     * 清关供应商NID
     */
    public String clearanceLogisticsProviderNid;

    /**
     * 仓库ID
     */
    @NotNull(message = "仓库ID不能为空")
    public Long warehouseId;

    /**
     * 集货(转运)仓ID
     */
    public Long transitWarehouseId;

    /**
     * 国内物流供应商NID
     */
    public String domesticLogisticsProviderNid;

    /**
     * 国际物流供应商NID
     */
    public String internationalLogisticsProviderNid;

    /**
     * 国内路由供应商NID
     */
    public String domesticRouteProviderNid;

    /**
     * 国际路由供应商NID
     */
    public String internationalRouteProviderNid;

    /**
     * 所属国家ID
     */
    @NotNull(message = "所属国家ID不能为空")
    public Long nationId;

    /**
     * 发货时效（文字描述）
     */
    @Length(max = 64, message = "发货时效（文字描述）最大长度为64")
    public String timeLimit;

    /**
     * 发货时效 最小天数
     */
    @NotNull(message = "发货时效 最小天数不能为空")
    @Range(min = 1, max = 100, message = "发货时效 最小天数为1-100")
    @Assert(lang = "groovy", expr = "_value <= _this.timeLimitMax", message = "发货时效，最小天数不能大于最大天数")
    public Integer timeLimitMin;

    /**
     * 发货时效 最大天数
     */
    @NotNull(message = "发货时效 最大天数不能为空")
    @Range(min = 1, max = 100, message = "发货时效 最大天数为1-100")
    public Integer timeLimitMax;

    /**
     * 优先级 （数字越大优先级越高）
     */
    @NotNull(message = "优先级不能为空")
    @Range(min = 1, max = 9999, message = "优先级为1-9999")
    public Integer priority;

    /**
     * 用于告知仓库走那条路线的表示
     */
    public String routeCode;

    /**
     * 仓库标识路线的业务编码
     */
    public String routeBizCode;

    /**
     * 是否需要客户提供运单号  0:否  1:是
     */
    @NotNull(message = "是否需要客户提供运单号必填")
    public Integer isNeedWaybillNumber;

    /**
     * 面单格式
     */
    public String pdfTemplate;

    /**
     * 路线清关时是否需要身份证照 0:否 1:是
     */
    @NotNull(message = "路线清关时是否需要身份证照必填")
    public Integer isNeedIdCardPhoto;

    /**
     * 每单固定费用
     */
    @NotNull(message = "每单固定费用必填")
    @Min(value = 0, message = "每单固定费用不能小于零")
    public Integer fixedCost;

    /**
     * 首重重量(克)
     */
    @NotNull(message = "首重重量(克)必填")
    @Min(value = 0, message = "首重重量(克)不能小于零")
    public Integer firstWeight;

    /**
     * 首重成本(分)
     */
    @NotNull(message = "首重成本(分)必填")
    @Min(value = 0, message = "首重成本(分)不能小于零")
    public Integer firstWeightCostRmb;

    /**
     * 续重重量(克)
     */
    @NotNull(message = "续重重量(克)必填")
    @Min(value = 0, message = "续重重量(克)不能小于零")
    public Integer additionalWeight;

    /**
     * 续重成本(分)
     */
    @NotNull(message = "续重成本(分)必填")
    @Min(value = 0, message = "续重成本(分)不能小于零")
    public Integer additionalWeightCostRmb;

    /**
     * 创建时间
     */
    public Date gmtCreate;

    /**
     * 修改时间
     */
    public Date gmtModified;


    /*   生成线路名所需的字段        */
    //服务类型名
    @NotNull(message = "服务类型名不能为空")
    public String serviceTypeName;

    //国家名
    @NotNull(message = "国家名不能为空")
    public String nationName;

    //仓库名
    @NotNull(message = "仓库名不能为空")
    public String warehouseName;

    //清关口岸名
    @NotNull(message = "清关口岸名不能为空")
    public String portName;

    //清关供应商名
    public String clearanceLogisticsProviderName;

    //国际物流供应商名
    public String internationalLogisticsProviderName;

    //国内物流供应商名
    public String domesticLogisticsProviderName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogisticsLineNid() {
        return logisticsLineNid;
    }

    public void setLogisticsLineNid(String logisticsLineNid) {
        this.logisticsLineNid = logisticsLineNid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOperateState() {
        return operateState;
    }

    public void setOperateState(String operateState) {
        this.operateState = operateState;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }


    public String getClearanceLogisticsProviderNid() {
        return clearanceLogisticsProviderNid;
    }

    public void setClearanceLogisticsProviderNid(String clearanceLogisticsProviderNid) {
        this.clearanceLogisticsProviderNid = clearanceLogisticsProviderNid;
    }

    public String getDomesticLogisticsProviderNid() {
        return domesticLogisticsProviderNid;
    }

    public void setDomesticLogisticsProviderNid(String domesticLogisticsProviderNid) {
        this.domesticLogisticsProviderNid = domesticLogisticsProviderNid;
    }

    public String getInternationalLogisticsProviderNid() {
        return internationalLogisticsProviderNid;
    }

    public void setInternationalLogisticsProviderNid(String internationalLogisticsProviderNid) {
        this.internationalLogisticsProviderNid = internationalLogisticsProviderNid;
    }

    public String getDomesticRouteProviderNid() {
        return domesticRouteProviderNid;
    }

    public void setDomesticRouteProviderNid(String domesticRouteProviderNid) {
        this.domesticRouteProviderNid = domesticRouteProviderNid;
    }

    public String getInternationalRouteProviderNid() {
        return internationalRouteProviderNid;
    }

    public void setInternationalRouteProviderNid(String internationalRouteProviderNid) {
        this.internationalRouteProviderNid = internationalRouteProviderNid;
    }

    public String getDomesticLogisticsProviderName() {
        return domesticLogisticsProviderName;
    }

    public void setDomesticLogisticsProviderName(String domesticLogisticsProviderName) {
        this.domesticLogisticsProviderName = domesticLogisticsProviderName;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getTransitWarehouseId() {
        return transitWarehouseId;
    }

    public void setTransitWarehouseId(Long transitWarehouseId) {
        this.transitWarehouseId = transitWarehouseId;
    }


    public Long getNationId() {
        return nationId;
    }

    public void setNationId(Long nationId) {
        this.nationId = nationId;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getTimeLimitMin() {
        return timeLimitMin;
    }

    public void setTimeLimitMin(Integer timeLimitMin) {
        this.timeLimitMin = timeLimitMin;
    }

    public Integer getTimeLimitMax() {
        return timeLimitMax;
    }

    public void setTimeLimitMax(Integer timeLimitMax) {
        this.timeLimitMax = timeLimitMax;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteBizCode() {
        return routeBizCode;
    }

    public void setRouteBizCode(String routeBizCode) {
        this.routeBizCode = routeBizCode;
    }

    public Integer getIsNeedWaybillNumber() {
        return isNeedWaybillNumber;
    }

    public void setIsNeedWaybillNumber(Integer isNeedWaybillNumber) {
        this.isNeedWaybillNumber = isNeedWaybillNumber;
    }

    public String getPdfTemplate() {
        return pdfTemplate;
    }

    public void setPdfTemplate(String pdfTemplate) {
        this.pdfTemplate = pdfTemplate;
    }

    public Integer getIsNeedIdCardPhoto() {
        return isNeedIdCardPhoto;
    }

    public void setIsNeedIdCardPhoto(Integer isNeedIdCardPhoto) {
        this.isNeedIdCardPhoto = isNeedIdCardPhoto;
    }

    public Integer getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(Integer fixedCost) {
        this.fixedCost = fixedCost;
    }

    public Integer getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Integer firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Integer getFirstWeightCostRmb() {
        return firstWeightCostRmb;
    }

    public void setFirstWeightCostRmb(Integer firstWeightCostRmb) {
        this.firstWeightCostRmb = firstWeightCostRmb;
    }

    public Integer getAdditionalWeight() {
        return additionalWeight;
    }

    public void setAdditionalWeight(Integer additionalWeight) {
        this.additionalWeight = additionalWeight;
    }

    public Integer getAdditionalWeightCostRmb() {
        return additionalWeightCostRmb;
    }

    public void setAdditionalWeightCostRmb(Integer additionalWeightCostRmb) {
        this.additionalWeightCostRmb = additionalWeightCostRmb;
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

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getClearanceLogisticsProviderName() {
        return clearanceLogisticsProviderName;
    }

    public void setClearanceLogisticsProviderName(String clearanceLogisticsProviderName) {
        this.clearanceLogisticsProviderName = clearanceLogisticsProviderName;
    }

    public String getInternationalLogisticsProviderName() {
        return internationalLogisticsProviderName;
    }

    public void setInternationalLogisticsProviderName(String internationalLogisticsProviderName) {
        this.internationalLogisticsProviderName = internationalLogisticsProviderName;
    }

    @Override
    public String toString() {
        return "LogisticsLineEntity{" +
                "id=" + id +
                ", logisticsLineNid='" + logisticsLineNid + '\'' +
                ", state='" + state + '\'' +
                ", operateState='" + operateState + '\'' +
                ", serviceType=" + serviceType +
                ", portId=" + portId +
                ", clearanceLogisticsProviderNid='" + clearanceLogisticsProviderNid + '\'' +
                ", warehouseId=" + warehouseId +
                ", transitWarehouseId=" + transitWarehouseId +
                ", domesticLogisticsProviderNid='" + domesticLogisticsProviderNid + '\'' +
                ", internationalLogisticsProviderNid='" + internationalLogisticsProviderNid + '\'' +
                ", domesticRouteProviderNid='" + domesticRouteProviderNid + '\'' +
                ", internationalRouteProviderNid='" + internationalRouteProviderNid + '\'' +
                ", nationId=" + nationId +
                ", timeLimit=" + timeLimit +
                ", timeLimitMin=" + timeLimitMin +
                ", timeLimitMax=" + timeLimitMax +
                ", priority=" + priority +
                ", routeCode='" + routeCode + '\'' +
                ", routeBizCode='" + routeBizCode + '\'' +
                ", isNeedWaybillNumber=" + isNeedWaybillNumber +
                ", pdfTemplate=" + pdfTemplate +
                ", isNeedIdCardPhoto=" + isNeedIdCardPhoto +
                ", fixedCost=" + fixedCost +
                ", firstWeight=" + firstWeight +
                ", firstWeightCostRmb=" + firstWeightCostRmb +
                ", additionalWeight=" + additionalWeight +
                ", additionalWeightCostRmb=" + additionalWeightCostRmb +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", serviceTypeName='" + serviceTypeName + '\'' +
                ", nationName='" + nationName + '\'' +
                ", warehouseName='" + warehouseName + '\'' +
                ", portName='" + portName + '\'' +
                ", clearanceLogisticsProviderName='" + clearanceLogisticsProviderName + '\'' +
                ", internationalLogisticsProviderName='" + internationalLogisticsProviderName + '\'' +
                ", domesticLogisticsProviderName='" + domesticLogisticsProviderName + '\'' +
                '}';
    }
}
