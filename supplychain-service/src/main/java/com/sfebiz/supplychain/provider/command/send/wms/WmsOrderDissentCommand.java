package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;

public abstract class WmsOrderDissentCommand extends AbstractCommand {
	private LogisticsProviderBO lpDetail;
	private StockoutOrderDetailBO orderDetail;

	public LogisticsProviderBO getLpDetail() {
		return lpDetail;
	}

	public void setLpDetail(LogisticsProviderBO lpDetail) {
		this.lpDetail = lpDetail;
	}

	public StockoutOrderDetailBO getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(StockoutOrderDetailBO orderDetail) {
		this.orderDetail = orderDetail;
	}

	
}
