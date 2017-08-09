package com.sfebiz.supplychain.provider.command.send.tpl;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;

/**
 * 
 * <p>第三方物流承运商，订阅或注册路由查询命名</p>
 * 
 * @author matt
 * @Date 2017年7月28日 下午5:45:09
 */
public abstract class TplOrderRegistRoutesCommand extends AbstractCommand {

    protected StockoutOrderBO stockoutOrderBO;

    //    protected RouteType       routeType;
    //
    //    public StockoutOrderDO getStockoutOrderDO() {
    //        return stockoutOrderDO;
    //    }
    //
    //    public void setStockoutOrderDO(StockoutOrderDO stockoutOrderDO) {
    //        this.stockoutOrderDO = stockoutOrderDO;
    //    }
    //
    //    public RouteType getRouteType() {
    //        return routeType;
    //    }
    //
    //    public void setRouteType(RouteType routeType) {
    //        this.routeType = routeType;
    //    }
}
