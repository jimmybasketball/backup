package com.sfebiz.supplychain.provider.command.send.tpl.bsp;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.protocol.bsp.*;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.send.tpl.TplOrderGetRoutesCommand;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取BSP路由信息
 */
public class BSPOrderGetRoutesCommand extends TplOrderGetRoutesCommand {

    private static final String serviceName = "RouteService";

    @Override
    public boolean execute() {
        try {
            if (!doCheck()) {
                return false;
            }

            List<LogisticsUserRouteEntity> routes = doQueryFmBsp();
            if (ListUtil.isNotEmpty(routes)) {
                this.setRoutes(routes);
                return true;
            } else {
                this.setRoutes(new ArrayList<LogisticsUserRouteEntity>());
                return false;
            }
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("[物流平台报文-路由查询异常]: " + e.getMessage())
                    .addParm("订单ID", orderId)
                    .log();
        }
        return false;
    }

    /**
     * 从BSP接口查询
     *
     * @return
     */
    public List<LogisticsUserRouteEntity> doQueryFmBsp() {

        BSPRequest req = new BSPRequest();
        req.service = serviceName;
        Map<String, Object> meta = JSONUtil.parseJSONMessage(LogisticsProviderConfig.getBspMeta());
        req.lang = "" + meta.get("lang");
        req.header = LogisticsProviderConfig.getBspCode();
        BSPRouteRequest route = new BSPRouteRequest();
        req.getBody().getBody().add(route);
        route.methodType = 1;
        if (!StringUtils.isEmpty(mailNo)) {
            route.trackingType = 1;
            route.trackingNumber = mailNo;
        } else {
            route.trackingType = 2;
            route.trackingNumber = orderId;
        }
        List<LogisticsUserRouteEntity> list = new ArrayList<LogisticsUserRouteEntity>();
        BSPResponse resp = ProviderBizService.getInstance().sendBSPRequest(
                LogisticsProviderConfig.getBspInterfaceUrl(),
                LogisticsProviderConfig.getBspInterfaceKey(),
                req,
                orderId);
        if (resp != null && "OK".equals(resp.getHeader())) {
            if (resp.getBody().getBody().size() > 0) {
                BSPBody body = resp.getBody().getBody().get(0);
                if (body instanceof BSPRouteResponse) {
                    BSPRouteResponse r = (BSPRouteResponse) body;
                    for (BSPRoute rr : r.getRoute()) {
                        LogisticsUserRouteEntity lr = new LogisticsUserRouteEntity();
                        lr.orderId = orderId;
                        try {
                            lr.eventTime = DateUtil.getDatetime(
                                    "yyyy-MM-dd HH:mm:ss", rr.acceptTime);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                        lr.carrierCode = "SF";
                        lr.mailNo = r.mailNo;
                        lr.position = rr.acceptAddress;
                        lr.content = rr.remark;
                        list.add(lr);
                    }
                }
            }
        }
        return list;
    }

    public boolean doCheck() {
        //如果未获取到运单号，则不查询
        if (StringUtils.isBlank(orderId)) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setMsg("[物流平台报文-路由查询失败]: 订单为null")
                    .log();
            return false;
        }
        if (StringUtils.isEmpty(mailNo)) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN)
                    .setMsg("[物流平台报文-路由查询失败]: 运单号不存在")
                    .addParm("订单ID", orderId)
                    .log();
            return false;
        }
        return true;
    }

}
