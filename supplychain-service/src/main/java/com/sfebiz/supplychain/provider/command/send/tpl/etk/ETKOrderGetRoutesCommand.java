package com.sfebiz.supplychain.provider.command.send.tpl.etk;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.protocol.etk.ETKClient;
import com.sfebiz.supplychain.provider.command.send.tpl.TplOrderGetRoutesCommand;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsRequest;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsResponse;
import com.sfebiz.supplychain.sdk.protocol.Response;
import com.sfebiz.supplychain.sdk.protocol.Trace;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主动查询ETK国内段路由信息
 */
public class ETKOrderGetRoutesCommand extends TplOrderGetRoutesCommand {

    private static volatile AtomicInteger messageId = new AtomicInteger(0);

    @Override
    public boolean execute() {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("[ETK]路由信息查询")
                .addParm("订单ID", orderId)
                .log();
        if (!checkParam()) {
            return false;
        }
        try {
            List<LogisticsUserRouteEntity> routes = queryEtkRoutes();
            if (ListUtil.isNotEmpty(routes)) {
                this.setRoutes(routes);
                return true;
            } else {
                this.setRoutes(new ArrayList<LogisticsUserRouteEntity>());
                return false;
            }
        } catch (Exception e) {
            logger.error("查询路由信息异常", e);
        } catch (Throwable t) {
            logger.error("查询路由信息异常", t);
        }
        return false;
    }

    /**
     * 检查参数信息是否存在；
     *
     * @return
     */
    public boolean checkParam() {
        if (StringUtils.isBlank(orderId)) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("路由信息查询失败-订单为null").log();
            return false;
        }
        if (StringUtils.isEmpty(mailNo)) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("路由信息查询失败-运单号不存在")
                    .addParm("订单:", orderId)
                    .log();
            return false;
        }
        return true;
    }


    /**
     * 从 ETK 查询路由查询
     *
     * @return
     */
    public List<LogisticsUserRouteEntity> queryEtkRoutes() {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("从ETK路由信息查询")
                .setParms(orderId)
                .log();

        LogisticsEventsRequest request = new LogisticsEventsRequest();
        request.getLogisticsEvent().getEventHeader().setEventType("COE_ETK_ORDER_TRACK");
        request.getLogisticsEvent().getEventHeader().setEventMessageId(String.valueOf(messageId.incrementAndGet()));
        request.getLogisticsEvent().getEventHeader().setEventTime(DateUtil.getCurrentDateTime());
        request.getLogisticsEvent().getEventHeader().setEventSource(LogisticsProviderConfig.getEtkSource());
        request.getLogisticsEvent().getEventHeader().setEventTarget(LogisticsProviderConfig.getEtkTarget());

        request.getLogisticsEvent().getEventBody().setTrackingNo(mailNo);
        request.getLogisticsEvent().getEventBody().setNoType(LogisticsProviderConfig.getEtkNoType());
        request.getLogisticsEvent().getEventBody().setCustomerNo(LogisticsProviderConfig.getEtkCustomerNo());

        LogisticsEventsResponse responses = ETKClient.send(request, LogisticsProviderConfig.getEtkInterfaceUrl(),
                LogisticsProviderConfig.getEtkInterfaceKey(), LogisticsProviderConfig.getEtkToken());

        List<LogisticsUserRouteEntity> routeEntities = new ArrayList<LogisticsUserRouteEntity>();
        if (responses != null && CollectionUtils.isNotEmpty(responses.responseItems)) {
            Response response = responses.responseItems.get(0);
            if (response != null && "true".equalsIgnoreCase(response.success)) {
                for (Trace trace : response.getTraces()) {
                    LogisticsUserRouteEntity routeEntity = new LogisticsUserRouteEntity();
                    routeEntity.orderId = orderId;
                    try {
                        routeEntity.eventTime = DateUtil.getDatetime("yyyy-MM-dd HH:mm:ss", trace.time);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                    routeEntity.carrierCode = "SF";
                    routeEntity.mailNo = response.trackingNo;
                    routeEntity.position = trace.location;
                    routeEntity.content = trace.event;
                    routeEntities.add(routeEntity);
                }
            }
        }
        return routeEntities;
    }

}
