package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;


public abstract class WmsOrderClearIdentityCommand extends AbstractCommand {
	protected StockoutOrderBO stockoutOrderBO;
	private StockoutOrderDetailBO orderDetail;
	private LogisticsProviderBO lpDetail;
	private boolean forceExecute = false;

	public boolean isForceExecute() {
		return forceExecute;
	}

	public void setForceExecute(boolean forceExecute) {
		this.forceExecute = forceExecute;
	}


	public StockoutOrderDetailBO getOrderDetail() {
		return orderDetail;
	}


	public void setOrderDetail(StockoutOrderDetailBO orderDetail) {
		this.orderDetail = orderDetail;
	}


	public LogisticsProviderBO getLpDetail() {
		return lpDetail;
	}


	public void setLpDetail(LogisticsProviderBO lpDetail) {
		this.lpDetail = lpDetail;
	}

	public StockoutOrderBO getStockoutOrderBO() {
		return stockoutOrderBO;
	}

	public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
		this.stockoutOrderBO = stockoutOrderBO;
	}
}
