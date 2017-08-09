package com.sfebiz.supplychain.service.warehouse.model;

import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;

/**
 * 
 * <p>仓库物流提供者业务实体</p>
 * 
 * @author matt
 * @Date 2017年7月20日 下午2:30:40
 */
public class WarehouseLogisticsProviderBO extends LogisticsProviderBO {

    /** 序号 */
    private static final long      serialVersionUID = 702821555323111930L;

    /** 与实体仓库系统功能的对接明细 */
    private WarehouseIntegrationBO integrationBO;

    public WarehouseIntegrationBO getIntegrationBO() {
        return integrationBO;
    }

    public void setIntegrationBO(WarehouseIntegrationBO integrationBO) {
        this.integrationBO = integrationBO;
    }

}
