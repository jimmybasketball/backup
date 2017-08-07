package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.sdk.protocol.Response;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;

import java.util.List;

/**
 */
public abstract class WmsStockSearchCommand extends AbstractCommand {


	/**
	 * 商品基本信息
	 */
	protected List<Long> skuIds;

	/**
	 * 仓库信息
	 */
    protected WarehouseBO warehouseBO;

	/**
	 * 供应商信息
	 */
    protected LogisticsProviderBO logisticsProviderBO;

    /**
     * 仓库响应的库存信息
     */
    protected Response stockResponse;


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

    public Response getStockResponse() {
        return stockResponse;
    }


	public List<Long> getSkuIds() {
		return skuIds;
	}

	public void setSkuIds(List<Long> skuIds) {
		this.skuIds = skuIds;
	}
}
