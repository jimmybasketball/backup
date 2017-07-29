package com.sfebiz.supplychain.service.line.model;

import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineOperateStateType;
import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineServiceType;
import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineStateType;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.port.model.LogisticsPortBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 
 * <p>线路业务实体</p>
 * 
 * @author matt
 * @Date 2017年7月20日 上午10:48:13
 */
public class LogisticsLineBO implements Serializable {

    /** 序号 */
    private static final long             serialVersionUID = 7095562055249630562L;

    /**
     * 线路ID
     */
    private Long                          id;

    /**
     * 线路名称
     */
    private String                        logisticsLineNid;

    /**
     * 线路状态
     */
    private LogisticsLineStateType        state;

    /**
     * 线路运营状态
     */
    private LogisticsLineOperateStateType operateState;

    /**
     * 服务类型
     */
    private LogisticsLineServiceType      serviceType;

    /**
     * 始发仓业务实体
     */
    private WarehouseBO                   warehouseBO;

    /**
     * 集货(转运)仓业务实体
     */
    private WarehouseBO                   transitWarehouseBO;

    /**
     * 口岸相关业务信息
     */
    private LogisticsPortBO               portBO;

    /**
     * 清关服务提供商相关信息
     */
    public LogisticsProviderBO            clearanceLogisticsProviderBO;

    /**
     * 国际运输服务提供商相关信息
     */
    public LogisticsProviderBO            internationalLogisticsProviderBO;

    /**
     * 国际物流查询服务提供商相关信息
     */
    public LogisticsProviderBO            internationalRouteProviderBO;

    /**
     * 国际运输服务提供商相关信息
     */
    public LogisticsProviderBO            domesticLogisticsProviderBO;

    /**
     * 国际物流查询服务提供商相关信息
     */
    public LogisticsProviderBO            domesticRouteProviderBO;

    /**
     * 所属国家ID
     */
    private Long                          nationId;

    /**
     * 发货时效（文字描述）
     */
    private String                        timeLimit;

    /**
     * 发货时效 最小天数
     */
    private Integer                       timeLimitMin;

    /**
     * 发货时效 最大天数
     */
    private Integer                       timeLimitMax;

    /**
     * 优先级 （数字越大优先级越高）
     */
    private Integer                       priority;

    /**
     * 用于告知仓库走哪条路线的表示
     */
    private String                        routeCode;

    /**
     * 仓库标识路线的业务编码
     */
    private String                        routeBizCode;

    /**
     * 是否需要客户提供运单号  0:否  1:是
     */
    private Integer                       isNeedWaybillNumber;

    /**
     * 面单格式 // TODO 枚举
     */
    private String                        pdfTemplate;

    /**
     * 路线清关时是否需要身份证照 0:否 1:是
     */
    private Integer                       isNeedIdCardPhoto;

    /**
     * 每单固定费用
     */
    private Integer                       fixedCost;

    /**
     * 首重重量(克)
     */
    private Integer                       firstWeight;

    /**
     * 首重成本(分)
     */
    private Integer                       firstWeightCostRmb;

    /**
     * 续重重量(克)
     */
    private Integer                       additionalWeight;

    /**
     * 续重成本(分)
     */
    private Integer                       additionalWeightCostRmb;

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

    public LogisticsLineStateType getState() {
        return state;
    }

    public void setState(LogisticsLineStateType state) {
        this.state = state;
    }

    public LogisticsLineOperateStateType getOperateState() {
        return operateState;
    }

    public void setOperateState(LogisticsLineOperateStateType operateState) {
        this.operateState = operateState;
    }

    public LogisticsLineServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(LogisticsLineServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public WarehouseBO getWarehouseBO() {
        return warehouseBO;
    }

    public void setWarehouseBO(WarehouseBO warehouseBO) {
        this.warehouseBO = warehouseBO;
    }

    public WarehouseBO getTransitWarehouseBO() {
        return transitWarehouseBO;
    }

    public void setTransitWarehouseBO(WarehouseBO transitWarehouseBO) {
        this.transitWarehouseBO = transitWarehouseBO;
    }

    public LogisticsPortBO getPortBO() {
        return portBO;
    }

    public void setPortBO(LogisticsPortBO portBO) {
        this.portBO = portBO;
    }

    public LogisticsProviderBO getClearanceLogisticsProviderBO() {
        return clearanceLogisticsProviderBO;
    }

    public void setClearanceLogisticsProviderBO(LogisticsProviderBO clearanceLogisticsProviderBO) {
        this.clearanceLogisticsProviderBO = clearanceLogisticsProviderBO;
    }

    public LogisticsProviderBO getInternationalLogisticsProviderBO() {
        return internationalLogisticsProviderBO;
    }

    public void setInternationalLogisticsProviderBO(LogisticsProviderBO internationalLogisticsProviderBO) {
        this.internationalLogisticsProviderBO = internationalLogisticsProviderBO;
    }

    public LogisticsProviderBO getInternationalRouteProviderBO() {
        return internationalRouteProviderBO;
    }

    public void setInternationalRouteProviderBO(LogisticsProviderBO internationalRouteProviderBO) {
        this.internationalRouteProviderBO = internationalRouteProviderBO;
    }

    public LogisticsProviderBO getDomesticLogisticsProviderBO() {
        return domesticLogisticsProviderBO;
    }

    public void setDomesticLogisticsProviderBO(LogisticsProviderBO domesticLogisticsProviderBO) {
        this.domesticLogisticsProviderBO = domesticLogisticsProviderBO;
    }

    public LogisticsProviderBO getDomesticRouteProviderBO() {
        return domesticRouteProviderBO;
    }

    public void setDomesticRouteProviderBO(LogisticsProviderBO domesticRouteProviderBO) {
        this.domesticRouteProviderBO = domesticRouteProviderBO;
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

    //获取线路类型， 根据服务类型获取
    public String getLineType() {
        return null;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
