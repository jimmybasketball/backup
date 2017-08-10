package com.sfebiz.supplychain.provider.command.send.wms.zebra;

import com.google.gson.Gson;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.send.CommandResponse;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/7/17 下午5:54
 */
public class ZebraSkuStockInCancelCommand extends ZebraSkuStockInCommand {


    @Override
    protected CommandResponse sendStockInCommandToWarehouse() throws ServiceException {

        String meta = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("meta");
        Map<String, Object> metaData = new Gson().fromJson(meta,
                new com.google.common.reflect.TypeToken<Map<String, Object>>() {
                }.getType()
        );
        LogisticsEventsRequest logisticsEventsRequest = buildRequest(metaData);
        String url = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
        String key = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
            throw new ServiceException(LogisticsReturnCode.STOCKIN_ORDER_SENDSTOCK_URLORKEY_NULL,
                    "[供应链-取消入库单异常]: " + LogisticsReturnCode.STOCKIN_ORDER_SENDSTOCK_URLORKEY_NULL.getDesc() + " "
                            + "[入库单ID: " + getStockinOrderBO().getId()
                            + ", 调用URL: " + url
                            + ", 调用KEY: " + key
                            + "]");
        }

        LogisticsEventsResponse responses = ProviderBizService.getInstance().send(
                logisticsEventsRequest,
                "logistics.event.wms.asn.cancel",
                url,
                key,
                TraceLogEntity.instance(traceLogger, getStockinOrderBO().getId(), SystemConstants.TRACE_APP));

        if (responses.responseItems == null || responses.getResponseItems().size() == 0) {
            return new CommandResponse(false, "调用仓库接口返回信息为空");
        }

        Response response = responses.getResponseItems().get(0);
        if (!isSuccess(response)) {
            return new CommandResponse(false, response.getReasonDesc());
        }

        return CommandResponse.SUCCESS_RESPONSE;
    }


    @Override
    public LogisticsEventsRequest buildRequest(Map<String, Object> metaData) {
        String eventSource = "";
        if (metaData != null && metaData.containsKey("event_source")) {
            eventSource = (String) metaData.get("event_source");
        }
        WarehouseBO warehouse = warehouseBO;
        EventHeader eventHeader = new EventHeader();
        logger.info("logistics.event.wms.asn.cancel");
        eventHeader.setEventType("logistics.event.wms.asn.cancel");
        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
        eventHeader.setEventSource(warehouse.getWarehouseNid());
        eventHeader.setEventTarget(eventSource);

        StockinOrderBO stockinOrderBO = getStockinOrderBO();

        EventBody eventBody = new EventBody();
        eventBody.setStockinId(stockinOrderBO.getStockinId());
        LogisticsEvent logisticsEvent = new LogisticsEvent();
        logisticsEvent.setEventHeader(eventHeader);
        logisticsEvent.setEventBody(eventBody);

        LogisticsEventsRequest logisticsEventsRequest = new LogisticsEventsRequest();
        logisticsEventsRequest.setLogisticsEvent(logisticsEvent);
        return logisticsEventsRequest;
    }

    @Override
    public void checkStateIsCanStockin() throws ServiceException {
        if (null == getStockinOrderBO()) {
            throw new ServiceException(LogisticsReturnCode.STOCKIN_ORDER_NOT_EXIST,
                    "[供应链-取消入库单异常]: " + LogisticsReturnCode.STOCKIN_ORDER_NOT_EXIST.getDesc());
        }
        //只有仓库未回传报文的情况下才允许重复取消
        if (null != getStockinOrderBO().getWarehouseStockinTime()) {
            throw new ServiceException(LogisticsReturnCode.STOCKIN_ORDER_NOT_ALLOW_CANCEL,
                    "[供应链-取消入库单异常]: " + LogisticsReturnCode.STOCKIN_ORDER_NOT_ALLOW_CANCEL.getDesc() + " "
                            + "[入库单ID: " + getStockinOrderBO().getId()
                            + ", 入库单状态: " + getStockinOrderBO().getState()
                            + ", 仓库回传时间: " + getStockinOrderBO().getWarehouseStockinTime()
                            + "]");
        }
    }

}
