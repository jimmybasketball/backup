package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;

/**
 * Created by wang_cl on 2015/4/13.
 */
public abstract class WmsGalOrderConfirmCommand extends AbstractCommand {
//    /**
//     * 损溢单信息
//     */
//    private GalOrderDO galOrderDO;

    /**
     * 供应商信息
     */
    private LogisticsProviderBO logisticsProviderBO;
    /**
     * 仓库信息
     */
    private WarehouseBO warehouseBO;

//    public GalOrderDO getGalOrderDO() {
//        return galOrderDO;
//    }
//
//    public void setGalOrderDO(GalOrderDO galOrderDO) {
//        this.galOrderDO = galOrderDO;
//    }

    public LogisticsProviderBO getLogisticsProviderBO() {
        return logisticsProviderBO;
    }

    public void setLogisticsProviderBO(LogisticsProviderBO logisticsProviderBO) {
        this.logisticsProviderBO = logisticsProviderBO;
    }

    public WarehouseBO getWarehouseBO() {
        return warehouseBO;
    }

    public void setWarehouseBO(WarehouseBO warehouseBO) {
        this.warehouseBO = warehouseBO;
    }
}
