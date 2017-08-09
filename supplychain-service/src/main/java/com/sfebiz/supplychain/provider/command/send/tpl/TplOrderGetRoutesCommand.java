package com.sfebiz.supplychain.provider.command.send.tpl;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;

/**
 * 
 * <p>第三方物流承运商，获取包裹物流信息命令</p>
 * 
 * @author matt
 * @Date 2017年7月28日 下午5:42:18
 */
public abstract class TplOrderGetRoutesCommand extends AbstractCommand {

    /** */
    protected StockoutOrderBO stockoutOrderBO;

    //    /** 国内物流信息 */
    //    protected List<LogisticsRouteEntity> routes;
    //
    //    /** 国际物流信息 */
    //    protected List<LogisticsRouteEntity> internationalRoutes;
    //
    //    /**路由类型 */
    //    protected RouteType                  routeType;
    //
    //    /** 出库单关联的路线实体 */
    //    protected LogisticsLineBO            lineEntity;
    //
    //    /** 是否需要再次查询路由信息 */
    //    protected boolean                    isNeedToRefetchRoutes;
    //
    //    public List<LogisticsRouteEntity> getRoutes() {
    //        return routes;
    //    }
    //
    //    public void setRoutes(List<LogisticsRouteEntity> routes) {
    //        this.routes = routes;
    //    }
    //
    //    public List<LogisticsRouteEntity> getInternationalRoutes() {
    //        return internationalRoutes;
    //    }
    //
    //    public void setInternationalRoutes(List<LogisticsRouteEntity> internationalRoutes) {
    //        this.internationalRoutes = internationalRoutes;
    //    }
    //
    //    public StockoutOrderDO getStockoutOrderDO() {
    //        return stockoutOrderBO;
    //    }
    //
    //    public void setStockoutOrderDO(StockoutOrderDO stockoutOrderDO) {
    //        this.stockoutOrderBO = stockoutOrderDO;
    //    }
    //
    //    public LineEntity getLineEntity() {
    //        return lineEntity;
    //    }
    //
    //    public void setLineEntity(LineEntity lineEntity) {
    //        this.lineEntity = lineEntity;
    //    }
    //
    //    public boolean isNeedToRefetchRoutes() {
    //        return isNeedToRefetchRoutes;
    //    }
    //
    //    public void setNeedToRefetchRoutes(boolean needToRefetchRoutes) {
    //        isNeedToRefetchRoutes = needToRefetchRoutes;
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
