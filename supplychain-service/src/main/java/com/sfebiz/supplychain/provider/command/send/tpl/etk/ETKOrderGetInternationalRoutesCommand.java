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
import net.pocrd.entity.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 查询ETK国际段物流信息
 * Created by MrChang on 2015/12/24.
 */
public class ETKOrderGetInternationalRoutesCommand extends TplOrderGetRoutesCommand {

    private static volatile AtomicInteger messageId = new AtomicInteger(0);

    @Override
    public boolean execute() throws ServiceException {
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

    private List<LogisticsUserRouteEntity> queryEtkRoutes() {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("从ETK路由信息查询")
                .addParm("订单ID", orderId)
                .addParm("运单号", mailNo)
                .log();

        LogisticsEventsRequest request = new LogisticsEventsRequest();
        request.getLogisticsEvent().getEventHeader().setEventType("COE_ETK_ORDER_TRACK");
        request.getLogisticsEvent().getEventHeader().setEventMessageId(String.valueOf(messageId.incrementAndGet()));
        request.getLogisticsEvent().getEventHeader().setEventTime(DateUtil.getCurrentDateTime());
        request.getLogisticsEvent().getEventHeader().setEventSource(LogisticsProviderConfig.getETKRoutesSource());
        request.getLogisticsEvent().getEventHeader().setEventTarget(LogisticsProviderConfig.getETKRoutesTarget());

        request.getLogisticsEvent().getEventBody().setTrackingNo(mailNo);
        request.getLogisticsEvent().getEventBody().setNoType(LogisticsProviderConfig.getEtkRoutesNoType());
        request.getLogisticsEvent().getEventBody().setCustomerNo(LogisticsProviderConfig.getEtkRoutesCustomerNo());

        LogisticsEventsResponse responses = ETKClient.send(request, LogisticsProviderConfig.getETKRoutesUrl(),
                LogisticsProviderConfig.getEtkRoutesTokenKey(), LogisticsProviderConfig.getEtkRoutesToken());

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
                    routeEntity.carrierCode = "ETK";
                    routeEntity.mailNo = response.trackingNo;
                    routeEntity.position = trace.location;
                    routeEntity.content = trace.event;
                    routeEntities.add(routeEntity);
                }
//                // 根据状态判断是否需要再次查询路由:OK、RT可判断国际段物流已结束
//                for (Trace trace : response.getTraces()) {
//                    if ("OK".equalsIgnoreCase(trace.getEventCode()) || "RT".equalsIgnoreCase(trace.getEventCode())) {
//                        this.setNeedToRefetchRoutes(false);
//                        break;
//                    } else {
//                        this.setNeedToRefetchRoutes(true);
//                    }
//                }

            }
        }
        return routeEntities;
    }

    /**
     * 检查参数信息是否存在
     *
     * @return
     */
    private boolean checkParam() {
        if (StringUtils.isBlank(orderId)) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("路由信息查询失败-订单为null")
                    .log();
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
}
