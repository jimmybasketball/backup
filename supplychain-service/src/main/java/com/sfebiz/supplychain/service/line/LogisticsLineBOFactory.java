package com.sfebiz.supplychain.service.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineOperateStateType;
import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineServiceType;
import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineStateType;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderFactory;
import com.sfebiz.supplychain.service.warehouse.WarehouseBOFactory;

/**
 * 
 * <p>线路业务对象工场类</p>
 * 
 * @author matt
 * @Date 2017年7月28日 下午1:54:52
 */
@Component("logisticsLineBOFactory")
public class LogisticsLineBOFactory {

    @Resource
    private LogisticsLineManager     logisticsLineManager;

    @Resource
    private WarehouseManager         warehouseManager;

    @Resource
    private LogisticsProviderFactory logisticsProviderFactory;

    @Resource
    private WarehouseBOFactory       warehouseBOFactory;

    /**
     * 根据lineId，加载完整的线路业务实体
     * 用于：出库单下发流程获取线路
     * 
     * @param lineId
     * @return
     */
    public LogisticsLineBO loadFullLineBO(Long lineId) throws ServiceException {

        if (null == lineId) {
            return null;
        }

        // TODO matt 添加缓存开关，从缓存获取线路实体信息

        LogisticsLineDO lineDO = logisticsLineManager.getById(lineId);
        if (null == lineDO) {
            return null;
        }

        LogisticsLineBO lineBO = new LogisticsLineBO();
        lineBO.setId(lineId);

        // 1. 填充线路基本信息
        fillLineBOBaseInfo(lineBO, lineDO);

        // 2. 填充线路上物流提供者相关实体
        fillLineBOLogisticsProviders(lineBO, lineDO);

        return lineBO;
    }

    /**
     * 填充线路的基本信息
     * 
     * @param lineBO
     */
    private void fillLineBOBaseInfo(LogisticsLineBO lineBO, LogisticsLineDO lineDO) {

        lineBO.setLogisticsLineNid(lineDO.getLogisticsLineNid());
        lineBO.setState(LogisticsLineStateType.valueOf(lineDO.getState()));
        lineBO.setOperateState(LogisticsLineOperateStateType.valueOf(lineDO.getOperateState()));
        lineBO.setServiceType(LogisticsLineServiceType.valueOf(lineDO.getServiceType()));

        lineBO.setNationId(lineDO.getNationId());
        lineBO.setTimeLimit(lineDO.getTimeLimit());
        lineBO.setTimeLimitMax(lineDO.getTimeLimitMax());
        lineBO.setTimeLimitMin(lineDO.getTimeLimitMin());
        lineBO.setPriority(lineDO.getPriority());
        lineBO.setRouteCode(lineDO.getRouteCode());
        lineBO.setRouteBizCode(lineDO.getRouteBizCode());
        lineBO.setIsNeedWaybillNumber(lineDO.getIsNeedWaybillNumber());
        lineBO.setPdfTemplate(lineDO.getPdfTemplate());
        lineBO.setIsNeedIdCardPhoto(BooleanUtils.toBooleanObject(lineDO.getIsNeedIdCardPhoto()));

        lineBO.setFixedCost(lineDO.getFixedCost());
        lineBO.setFirstWeightCostRmb(lineDO.getFirstWeightCostRmb());
        lineBO.setFirstWeight(lineDO.getFirstWeight());
        lineBO.setAdditionalWeightCostRmb(lineDO.getAdditionalWeightCostRmb());
        lineBO.setAdditionalWeight(lineDO.getAdditionalWeight());
    }

    /**
     * 填充线路上物流提供者相关实体
     * 仓库（始发仓、集货仓），ltp（国际，国内），口岸，清关
     * 
     * @param lineBO
     * @param lineDO
     */
    private void fillLineBOLogisticsProviders(LogisticsLineBO lineBO, LogisticsLineDO lineDO) {

        // 1. 获取相关数据信息
        Map<Long, WarehouseDO> warehouseDOMap = warehouseManager
            .getAllWarehouseMap(getWarehouseIdList(lineDO));

        List<String> logisticsProviderNids = new ArrayList<String>();
        if (null != warehouseDOMap) {
            for (WarehouseDO warehouseDO : warehouseDOMap.values()) {
                logisticsProviderNids.add(warehouseDO.getLogisticsProviderNid());
            }
        }
        logisticsProviderNids.addAll(getLogisticsProviderIdList(lineDO));

        Map<String, LogisticsProviderBO> providerNidBOMap = logisticsProviderFactory
            .getLogisticsProviderBOMap(logisticsProviderNids);

        // 2. 组装
        // 2.1. 仓库组装
        lineBO.setWarehouseBO(warehouseBOFactory.getFullWarehouseBOById(lineDO.getId()));
        lineBO.setTransitWarehouseBO(warehouseBOFactory.getFullWarehouseBOById(lineDO
            .getTransitWarehouseId()));

        // 2.2. 口岸实体
        lineBO.setPortBO(logisticsProviderFactory.getLogisticsPortBOByNid(lineDO.getPortNid()));

        // 2.3. 支付申报业务实体
        // TODO matt 待查看修改
        lineBO.setPayDeclareProviderNid(StringUtils.EMPTY);

        // 2.4 清关实体
        lineBO.setClearanceLogisticsProviderBO(providerNidBOMap.get(lineDO
            .getClearanceLogisticsProviderNid()));

        // 2.5 tpl组装
        lineBO.setInternationalLogisticsProviderBO(providerNidBOMap.get(lineDO
            .getInternationalLogisticsProviderNid()));
        lineBO.setInternationalRouteProviderBO(providerNidBOMap.get(lineDO
            .getInternationalRouteProviderNid()));
        lineBO.setDomesticLogisticsProviderBO(providerNidBOMap.get(lineDO
            .getDomesticLogisticsProviderNid()));
        lineBO
            .setDomesticRouteProviderBO(providerNidBOMap.get(lineDO.getDomesticRouteProviderNid()));
    }

    private List<Long> getWarehouseIdList(LogisticsLineDO lineDO) {
        return new ArrayList<Long>(Arrays.asList(lineDO.getWarehouseId(),
            lineDO.getTransitWarehouseId()));
    }

    private List<String> getLogisticsProviderIdList(LogisticsLineDO lineDO) {

        List<String> nidList = new ArrayList<String>();

        if (StringUtils.isNotBlank(lineDO.getInternationalLogisticsProviderNid())) {
            nidList.add(lineDO.getInternationalLogisticsProviderNid());
        }
        if (StringUtils.isNotBlank(lineDO.getInternationalRouteProviderNid())) {
            nidList.add(lineDO.getInternationalRouteProviderNid());
        }

        if (StringUtils.isNotBlank(lineDO.getDomesticLogisticsProviderNid())) {
            nidList.add(lineDO.getInternationalRouteProviderNid());
        }
        if (StringUtils.isNotBlank(lineDO.getDomesticRouteProviderNid())) {
            nidList.add(lineDO.getInternationalRouteProviderNid());
        }

        if (StringUtils.isNotBlank(lineDO.getClearanceLogisticsProviderNid())) {
            nidList.add(lineDO.getClearanceLogisticsProviderNid());
        }

        return nidList;
    }
}
