package com.sfebiz.supplychain.service.lp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import com.sfebiz.supplychain.config.port.PortConfig;
import com.sfebiz.supplychain.persistence.base.lp.domain.LogisticsProviderDO;
import com.sfebiz.supplychain.persistence.base.lp.manager.LogisticsProviderManager;
import com.sfebiz.supplychain.service.port.model.LogisticsPortBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseIntegrationBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseLogisticsProviderBO;

/**
 * 
 * <p>物流提供者工厂类</p>
 * 
 * @author matt
 * @Date 2017年8月2日 下午5:43:29
 */
@Component("logisticsProviderFactory")
public class LogisticsProviderFactory {

    @Resource
    private LogisticsProviderManager logisticsProviderManager;

    /**
     * 根据口岸nid获取口岸业务实体
     * 
     * @param portNid
     * @return
     */
    // TODO matt 存储格式待修改
    public LogisticsPortBO getLogisticsPortBOByNid(String portNid) {
        Map<String, String> properties = PortConfig.getPortPropertiesByPortNid(portNid);
        if (properties == null) {
            return null;
        }
        LogisticsPortBO portBO = new LogisticsPortBO();
        portBO.setId(properties.containsKey("id") ? Long.valueOf(properties.get("id")) : 0);
        portBO.setCode(properties.containsKey("code") ? properties.get("code") : null);
        portBO.setName(properties.containsKey("name") ? properties.get("name") : null);
        portBO.setPortNid(portNid);
        portBO.setCompanyCode(properties.containsKey("company_code") ? properties
            .get("company_code") : null);
        portBO.setCompanyName(properties.containsKey("company_name") ? properties
            .get("company_name") : null);
        portBO.seteCommerceCode(properties.containsKey("e_commerce_code") ? properties
            .get("e_commerce_code") : null);
        portBO.seteCommerceName(properties.containsKey("e_commerce_name") ? properties
            .get("e_commerce_name") : null);
        portBO.setLogisCompanyCode(properties.containsKey("logis_company_code") ? properties
            .get("logis_company_code") : null);
        portBO.setLogisCompanyName(properties.containsKey("logis_company_name") ? properties
            .get("logis_company_name") : null);
        portBO.setUrl(properties.containsKey("url") ? properties.get("url") : null);
        portBO.setMeta(properties.containsKey("meta") ? properties.get("meta") : null);
        portBO.setType(properties.containsKey("type") ? properties.get("type") : null);
        portBO.setLimitRange(properties.containsKey("limit_range") ? properties.get("limit_range")
            : null);

        return portBO;
    }

    /**
     * 根据仓库Nid获取仓库对应的物流供应商实体
     * 
     * @param logisticsProviderNid
     * @return
     */
    public WarehouseLogisticsProviderBO getWarehouseLogisticsProviderBOByNid(String logisticsProviderNid) {
        LogisticsProviderBO providerBO = getBOByNid(logisticsProviderNid);
        WarehouseLogisticsProviderBO warehouseLogisticsProviderBO = new WarehouseLogisticsProviderBO();
        BeanUtils.copyProperties(providerBO, warehouseLogisticsProviderBO);
        // 与实体仓的集成信息
        String integrationMeta = LogisticsDynamicConfig.getLP().getRule(
            warehouseLogisticsProviderBO.getLogisticsProviderNid(), "integrationMeta");
        if (StringUtils.isNotBlank(integrationMeta)) {
            warehouseLogisticsProviderBO.setIntegrationBO(JSON.parseObject(integrationMeta,
                WarehouseIntegrationBO.class));
        }
        return warehouseLogisticsProviderBO;
    }

    public LogisticsProviderBO getBOByNid(String logisticsProviderNid) {
        List<String> logisticsProviderNids = new ArrayList<String>();
        logisticsProviderNids.add(logisticsProviderNid);
        return getLogisticsProviderBOMap(logisticsProviderNids).get(logisticsProviderNid);
    }

    @SuppressWarnings("unchecked")
    public Map<String, LogisticsProviderBO> getLogisticsProviderBOMap(List<String> logisticsProviderNids) {
        Map<String, LogisticsProviderBO> providerBOMap = new HashMap<String, LogisticsProviderBO>();

        List<LogisticsProviderDO> providerDOList = logisticsProviderManager
            .queryLogisticsProviderByNidList(logisticsProviderNids);
        for (LogisticsProviderDO providerDO : providerDOList) {

            if (providerBOMap.containsKey(providerDO.getLogisticsProviderNid())) {
                continue;
            }

            LogisticsProviderBO providerBO = new LogisticsProviderBO();

            // 1. copy基本信息
            BeanUtils.copyProperties(providerDO, providerBO);

            // 2. 从动态配置上获取相应扩展信息
            // 2.1. 业务扩展信息
            String bizMeta = LogisticsDynamicConfig.getLP().getRule(
                providerDO.getLogisticsProviderNid(), "bizMeta");
            if (StringUtils.isNotBlank(bizMeta)) {
                providerBO.setBizMeta(JSON.parseObject(bizMeta, Map.class));
            }
            // 2.2. 接口对接相关扩展信息
            String interfaceMeta = LogisticsDynamicConfig.getLP().getRule(
                providerDO.getLogisticsProviderNid(), "interfaceMeta");
            if (StringUtils.isNotBlank(interfaceMeta)) {
                providerBO.setInterfaceMeta(JSON.parseObject(interfaceMeta, Map.class));
            }

            providerBOMap.put(providerDO.getLogisticsProviderNid(), providerBO);
        }

        return providerBOMap;
    }
}
