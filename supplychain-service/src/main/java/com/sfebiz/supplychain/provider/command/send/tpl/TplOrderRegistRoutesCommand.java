package com.sfebiz.supplychain.provider.command.send.tpl;

import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.provider.command.AbstractCommand;

public abstract class TplOrderRegistRoutesCommand extends AbstractCommand {

	protected StockoutOrderDO stockoutOrderDO;

	protected RouteType routeType;

	public StockoutOrderDO getStockoutOrderDO() {
		return stockoutOrderDO;
	}

	public void setStockoutOrderDO(StockoutOrderDO stockoutOrderDO) {
		this.stockoutOrderDO = stockoutOrderDO;
	}

	public RouteType getRouteType() {
		return routeType;
	}

	public void setRouteType(RouteType routeType) {
		this.routeType = routeType;
	}
}
