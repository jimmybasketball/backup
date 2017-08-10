package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.protocol.wms.gaojie.CcbRouteNote;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;

import java.util.List;

public abstract class WmsOrderGetRoutesCommand extends AbstractCommand {

    protected StockoutOrderBO stockoutOrderBO;

    protected LogisticsLineBO logisticsLineBO;

    //运单路由信息
    protected List<LogisticsUserRouteEntity> userViewInternelRoutes;

    //所有的清关信息
    protected List<CcbRouteNote> allCcbRoutes;

    //清关可见的清关信息
    protected List<CcbRouteNote> userViewCcbRoutes;

    public List<LogisticsUserRouteEntity> getUserViewInternelRoutes() {
        return userViewInternelRoutes;
    }

    public void setUserViewInternelRoutes(List<LogisticsUserRouteEntity> userViewInternelRoutes) {
        this.userViewInternelRoutes = userViewInternelRoutes;
    }

    public StockoutOrderBO getStockoutOrderBO() {
        return stockoutOrderBO;
    }

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public LogisticsLineBO getLogisticsLineBO() {
        return logisticsLineBO;
    }

    public void setLogisticsLineBO(LogisticsLineBO logisticsLineBO) {
        this.logisticsLineBO = logisticsLineBO;
    }

    public List<CcbRouteNote> getAllCcbRoutes() {
        return allCcbRoutes;
    }

    public void setAllCcbRoutes(List<CcbRouteNote> allCcbRoutes) {
        this.allCcbRoutes = allCcbRoutes;
    }

    public List<CcbRouteNote> getUserViewCcbRoutes() {
        return userViewCcbRoutes;
    }

    public void setUserViewCcbRoutes(List<CcbRouteNote> userViewCcbRoutes) {
        this.userViewCcbRoutes = userViewCcbRoutes;
    }
}
