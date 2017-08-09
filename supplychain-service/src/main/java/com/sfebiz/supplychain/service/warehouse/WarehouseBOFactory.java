package com.sfebiz.supplychain.service.warehouse;

import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.sfebiz.supplychain.exposed.warehouse.enums.WarehouseCooperationState;
import com.sfebiz.supplychain.exposed.warehouse.enums.WarehouseState;
import com.sfebiz.supplychain.exposed.warehouse.enums.WarehouseType;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderFactory;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseAddressBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseContactBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseLogisticsProviderBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseSenderBO;

@Component("warehouseBOFactory")
public class WarehouseBOFactory {

    @Resource
    private WarehouseManager         warehouseManager;

    @Resource
    private LogisticsProviderFactory logisticsProviderFactory;

    /**
     * 获取完整的仓库业务实体
     * 
     * @param warehouseId
     * @return
     */
    public WarehouseBO getFullWarehouseBOById(Long warehouseId) {
        WarehouseDO warehouseDO = warehouseManager.getById(warehouseId);
        if (null == warehouseDO) {
            return null;
        }
        return bulidWarehouseBO(warehouseDO);
    }

    /**
     * 获取完整的仓库业务实体
     * 
     * @param warehouseId
     * @return
     */
    public WarehouseBO getFullWarehouseBOByNid(String warehouseNid) {
        WarehouseDO warehouseDO = warehouseManager.getByNid(warehouseNid);
        if (null == warehouseDO) {
            return null;
        }
        return bulidWarehouseBO(warehouseDO);
    }

    private WarehouseBO bulidWarehouseBO(WarehouseDO warehouseDO) {
        WarehouseBO warehouseBO = new WarehouseBO();

        // 1. 构造基本信息
        fillWarehouseBOBaseInfo(warehouseBO, warehouseDO);

        // 2. 添加logisticsProvider
        WarehouseLogisticsProviderBO logisticsProviderBO = logisticsProviderFactory
            .getWarehouseLogisticsProviderBOByNid(warehouseDO.getLogisticsProviderNid());
        warehouseBO.setLogisticsProviderBO(logisticsProviderBO);

        return warehouseBO;
    }

    /**
     * 填充业务实体基本信息
     * 
     * @param warehouseBO
     * @param warehouseDO
     */
    private void fillWarehouseBOBaseInfo(WarehouseBO warehouseBO, WarehouseDO warehouseDO) {

        warehouseBO.setId(warehouseDO.getId());
        warehouseBO.setName(warehouseDO.getName());
        warehouseBO.setWarehouseNid(warehouseDO.getWarehouseNid());
        warehouseBO.setWarehouseCode(warehouseDO.getWarehouseCode());
        warehouseBO.setLogisticsProviderNid(warehouseDO.getLogisticsProviderNid());
        warehouseBO.setPrincipalEmail(warehouseDO.getPrincipalEmail());
        warehouseBO.setRegion(warehouseDO.getRegion());
        warehouseBO.setWarehouseType(WarehouseType.valueOf(warehouseDO.getWarehouseType()));
        warehouseBO.setWarehouseState(WarehouseState.valueOf(warehouseDO.getWarehouseState()));
        warehouseBO.setCooperationState(WarehouseCooperationState.valueOf(warehouseDO
            .getCooperationState()));
        warehouseBO.setContractPeriodStart(warehouseDO.getContractPeriodStart());
        warehouseBO.setContractPeriodEnd(warehouseDO.getContractPeriodEnd());
        warehouseBO.setIsStorage(BooleanUtils.toBooleanObject(warehouseDO.getIsStorage()));
        warehouseBO.setIsReturn(BooleanUtils.toBooleanObject(warehouseDO.getIsReturn()));
        warehouseBO.setIsTransit(BooleanUtils.toBooleanObject(warehouseDO.getIsTransit()));
        warehouseBO
            .setIsSupportBatch(BooleanUtils.toBooleanObject(warehouseDO.getIsSupportBatch()));

        WarehouseAddressBO addressBO = new WarehouseAddressBO();
        BeanUtils.copyProperties(warehouseDO, addressBO);
        warehouseBO.setAddressBO(addressBO);

        WarehouseSenderBO senderBO = new WarehouseSenderBO();
        BeanUtils.copyProperties(warehouseDO, senderBO);
        warehouseBO.setSenderBO(senderBO);

        WarehouseContactBO contactBO = new WarehouseContactBO();
        BeanUtils.copyProperties(warehouseDO, contactBO);
        warehouseBO.setContactBO(contactBO);
    }
}
