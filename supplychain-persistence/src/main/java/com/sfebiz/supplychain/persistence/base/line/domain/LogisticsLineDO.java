package com.sfebiz.supplychain.persistence.base.line.domain;

import java.util.Date;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 线路配置持久化对象
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-10 19:41
 **/
public class LogisticsLineDO extends BaseDO {

    private static final long serialVersionUID = -8712509899508374345L;
    private Long              id;

    /**
     * 线路名称
     */
    private String            logisticsLineNid;

    /**
     * 线路状态  ENABLE:启用  DISABLE:禁用
     */
    private String            state;

    /**
     * 线路运营状态  NORMAL:正常   STOPED:停止
     */
    private String            operateState;

    /**
     * 服务类型
     */
    private Integer           serviceType;

    /**
     * 口岸ID
     */
    private Long              portId;

    /**
     * 口岸的业务id
     */
    // TODO 待修改
    private String            portNid;

    /**
     * 清关供应商NID
     */
    public String             clearanceLogisticsProviderNid;

    /**
     * 仓库ID
     */
    private Long              warehouseId;

    /**
     * 集货(转运)仓ID
     */
    private Long              transitWarehouseId;

    /**
      * 国内物流供应商NID
      */
    public String             domesticLogisticsProviderNid;

    /**
     * 国际物流供应商NID
     */
    public String             internationalLogisticsProviderNid;

    /**
     * 国内路由供应商NID
     */
    public String             domesticRouteProviderNid;

    /**
     * 国际路由供应商NID
     */
    public String             internationalRouteProviderNid;

    /**
     * 所属国家ID
     */
    private Long              nationId;

    /**
     * 发货时效（文字描述）
     */
    private String            timeLimit;

    /**
     * 发货时效 最小天数
     */
    private Integer           timeLimitMin;

    /**
     * 发货时效 最大天数
     */
    private Integer           timeLimitMax;

    /**
     * 优先级 （数字越大优先级越高）
     */
    private Integer           priority;

    /**
     * 用于告知仓库走那条路线的表示
     */
    private String            routeCode;

    /**
     * 仓库标识路线的业务编码
     */
    private String            routeBizCode;

    /**
     * 是否需要客户提供运单号  0:否  1:是
     */
    private Integer           isNeedWaybillNumber;

    /**
     * 面单格式
     */
    private String            pdfTemplate;

    /**
     * 路线清关时是否需要身份证照 0:否 1:是
     */
    private Integer           isNeedIdCardPhoto;

    /**
     * 每单固定费用
     */
    private Integer           fixedCost;

    /**
     * 首重重量(克)
     */
    private Integer           firstWeight;

    /**
     * 首重成本(分)
     */
    private Integer           firstWeightCostRmb;

    /**
     * 续重重量(克)
     */
    private Integer           additionalWeight;

    /**
     * 续重成本(分)
     */
    private Integer           additionalWeightCostRmb;

    /**
     * 创建时间
     */
    private Date              gmtCreate;

    /**
     * 修改时间
     */
    private Date              gmtModified;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public String getPortNid() {
        return portNid;
    }

    public void setPortNid(String portNid) {
        this.portNid = portNid;
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

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    @Override
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public Date getGmtModified() {
        return gmtModified;
    }

    @Override
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "LogisticsLineDO{" + "id=" + id + ", logisticsLineNid='" + logisticsLineNid + '\''
               + ", state='" + state + '\'' + ", operateState='" + operateState + '\''
               + ", serviceType=" + serviceType + ", portId=" + portId
               + ", clearanceLogisticsProviderNid='" + clearanceLogisticsProviderNid + '\''
               + ", warehouseId=" + warehouseId + ", transitWarehouseId=" + transitWarehouseId
               + ", domesticLogisticsProviderNid='" + domesticLogisticsProviderNid + '\''
               + ", internationalLogisticsProviderNid='" + internationalLogisticsProviderNid + '\''
               + ", domesticRouteProviderNid='" + domesticRouteProviderNid + '\''
               + ", internationalRouteProviderNid='" + internationalRouteProviderNid + '\''
               + ", nationId=" + nationId + ", timeLimit=" + timeLimit + ", timeLimitMin="
               + timeLimitMin + ", timeLimitMax=" + timeLimitMax + ", priority=" + priority
               + ", routeCode='" + routeCode + '\'' + ", routeBizCode='" + routeBizCode + '\''
               + ", isNeedWaybillNumber=" + isNeedWaybillNumber + ", pdfTemplate=" + pdfTemplate
               + ", isNeedIdCardPhoto=" + isNeedIdCardPhoto + ", fixedCost=" + fixedCost
               + ", firstWeight=" + firstWeight + ", firstWeightCostRmb=" + firstWeightCostRmb
               + ", additionalWeight=" + additionalWeight + ", additionalWeightCostRmb="
               + additionalWeightCostRmb + ", gmtCreate=" + gmtCreate + ", gmtModified="
               + gmtModified + '}';
    }
}
