package com.sfebiz.supplychain.provider.command.send.wms.common;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.protocol.bsp.*;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderGetRoutesCommand;
import com.sfebiz.supplychain.provider.entity.BspServiceCode;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.ListUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 仓库获得订单路由
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017/8/3 16:40
 */
public class WmsOrderGetRoutesDefCommand extends WmsOrderGetRoutesCommand {

//    protected RouteBizService routeBizService;

    @Override
    public boolean execute() {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg(getWhName() + "路由信息查询")
                .setParms(this.getStockoutOrderBO().getBizId())
                .log();
        if (null == this.getStockoutOrderBO()) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg(getWhName() + "路由信息查询失败-订单为null").log();
            return false;
        }

        if (!doCheck()) {
            return false;
        }

        // TODO 等待处理
//        routeBizService = (RouteBizService) CommandConfig.getSpringBean("routeBizService");
        try {
//            List<LogisticsUserRouteEntity> routes = routeBizService.getUserRoutes(0,getStockoutOrderDO().getBizId());
            List<LogisticsUserRouteEntity> routes = new ArrayList<LogisticsUserRouteEntity>();
            if (ListUtil.isNotEmpty(routes)) {
                this.setUserViewInternelRoutes(routes);
                return true;
            } else {
                routes = doQueryFmBsp();
                if (ListUtil.isNotEmpty(routes)) {
                    this.setUserViewInternelRoutes(routes);
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error(getWhName() + "查询路由信息异常", e);
        } catch (Throwable t) {
            logger.error(getWhName() + "查询路由信息异常", t);
        }
        return false;
    }

    public List<LogisticsUserRouteEntity> doQueryFmBsp() {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg(getWhName() + "从BSP路由信息查询")
                .setParms(this.getStockoutOrderBO().getBizId())
                .log();

        Map<String, Object> meta = JSONUtil.parseJSONMessage(LogisticsProviderConfig.getBspMeta());
        BSPRequest req = new BSPRequest();
        req.service = BspServiceCode.ORDER_ROUTE;
        if (meta.containsKey("lang")) {
            req.lang = String.valueOf(meta.get("lang"));
        } else {
            req.lang = "";
        }
        req.header = LogisticsProviderConfig.getBspCode();
        BSPRouteRequest route = new BSPRouteRequest();
        req.getBody().getBody().add(route);
        route.methodType = 1;
        if (!StringUtils.isEmpty(this.getStockoutOrderBO().getIntrMailNo())) {
            route.trackingType = 1;
            route.trackingNumber = this.getStockoutOrderBO().getIntrMailNo();
        } else {
            route.trackingType = 2;
            route.trackingNumber = this.getStockoutOrderBO().getBizId();
        }
        BSPResponse resp = ProviderBizService.getInstance().sendBSPRequest(
                LogisticsProviderConfig.getBspInterfaceUrl(),
                LogisticsProviderConfig.getBspInterfaceKey(),
                req, this.getStockoutOrderBO().getBizId());
        List<LogisticsUserRouteEntity> list = new ArrayList<LogisticsUserRouteEntity>();
        if (resp != null && BSPReturnCode.SUCCESS.getCode().equalsIgnoreCase(resp.getHeader())
                && resp.getBody().getBody().size() > 0) {
            BSPBody body = resp.getBody().getBody().get(0);
            if (body instanceof BSPRouteResponse) {
                BSPRouteResponse routeResponse = (BSPRouteResponse) body;
                for (BSPRoute bspRoute : routeResponse.getRoute()) {
                    LogisticsUserRouteEntity routeEntity = new LogisticsUserRouteEntity();
                    routeEntity.orderId = this.getStockoutOrderBO().getBizId();
                    try {
                        routeEntity.eventTime = DateUtil.getDatetime("yyyy-MM-dd HH:mm:ss", bspRoute.acceptTime);
                    } catch (Exception e) {
                        logger.error(getWhName() + "获取BSP物流信息异常", e);
                    }
                    routeEntity.carrierCode = "SF";
                    routeEntity.mailNo = routeResponse.mailNo;
                    routeEntity.position = bspRoute.acceptAddress;
                    routeEntity.content = bspRoute.remark;
                    list.add(routeEntity);
                }
            }
        }
        return list;
    }

    public boolean doCheck() {
        return true;
    }

    public String getWhName() {
        return "DEF";
    }
}