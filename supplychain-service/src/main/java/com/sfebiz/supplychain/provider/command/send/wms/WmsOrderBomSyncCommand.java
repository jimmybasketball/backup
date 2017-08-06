package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.supplychain.exposed.sku.enums.SkuWarehouseSyncStateType;
import com.sfebiz.supplychain.factory.SpringBeanFactory;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncLogManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncManager;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;

import java.util.Date;

/**
 * 抽象与仓库进行商品BOM同步
 */
public abstract class WmsOrderBomSyncCommand extends AbstractCommand {

    /**
     * 同步异常信息
     */
    protected String errorMessage;
	/**
	 * 组合商品ID
	 */
	private String mixedSkuId;
//	/**
//	 * 物料配比清单
//	 */
//	private List<MixedSkuDO> mixedSkuDOs;
	/**
	 * 仓库信息
	 */
	private WarehouseBO warehouseBO;
	/**
	 * 供应商信息
	 */
	private LogisticsProviderBO logisticsProviderBO;

//    public List<MixedSkuDO> getMixedSkuDOs() {
//		return mixedSkuDOs;
//	}
//
//	public void setMixedSkuDOs(List<MixedSkuDO> mixedSkuDOs) {
//		this.mixedSkuDOs = mixedSkuDOs;
//	}

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

	public String getMixedSkuId() {
		return mixedSkuId;
	}

	public void setMixedSkuId(String mixedSkuId) {
		this.mixedSkuId = mixedSkuId;
	}

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean mockSkuSyncSuccess(){
		SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager", SkuWarehouseSyncManager.class);
        SkuWarehouseSyncLogManager skuWarehouseSyncLogManager = SpringBeanFactory.getBean("skuWarehouseSyncLogManager", SkuWarehouseSyncLogManager.class);
		SkuWarehouseSyncDO warehouseSyncDO = skuWarehouseSyncManager.getBySkuIdAndWarehouseId(Long.parseLong(getMixedSkuId()), this.warehouseBO.getId());
		warehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.value);
		warehouseSyncDO.setGmtModified(new Date());
		skuWarehouseSyncManager.update(warehouseSyncDO);
		skuWarehouseSyncLogManager.createLog(Long.parseLong(getMixedSkuId()),this.getWarehouseBO().getId(),"result是1，同步成功","mock",1);        return true;
    }
}
