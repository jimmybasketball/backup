package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.supplychain.exposed.warehouse.enums.WmsOperaterType;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderDO;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;

import java.util.List;

/**
 * 抽象与仓库进行商品同步
 */
public abstract class WmsOrderSupplierSyncCommand extends AbstractCommand {

    /**
     * 仓库
     */
    protected WarehouseBO warehouseBO;

    /**
     * 供应商信息
     */
    protected LogisticsProviderBO logisticsProviderBO;


    /**
     * 需要同步的供应商信息
     */
    protected List<MerchantProviderDO> providerDOs;

    /**
     * 同步类型，新增或者更新
     */
    protected WmsOperaterType wmsOperaterType;

    public WarehouseBO getWarehouseBO() {
        return warehouseBO;
    }

    public void setWarehouseBO(WarehouseBO warehouseBO) {
        this.warehouseBO = warehouseBO;
    }

    public LogisticsProviderBO getLogisticsProviderBO() {
        return logisticsProviderBO;
    }

    public void setLogisticsProviderBO(LogisticsProviderBO logisticsProviderBO) {
        this.logisticsProviderBO = logisticsProviderBO;
    }

    public List<MerchantProviderDO> getProviderDOs() {
        return providerDOs;
    }

    public void setProviderDOs(List<MerchantProviderDO> providerDOs) {
        this.providerDOs = providerDOs;
    }

    public WmsOperaterType getWmsOperaterType() {
        return wmsOperaterType;
    }

    public void setWmsOperaterType(WmsOperaterType wmsOperaterType) {
        this.wmsOperaterType = wmsOperaterType;
    }
}
