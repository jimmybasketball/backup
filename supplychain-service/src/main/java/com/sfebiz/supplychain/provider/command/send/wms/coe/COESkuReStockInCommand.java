package com.sfebiz.supplychain.provider.command.send.wms.coe;

import com.google.gson.Gson;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.send.CommandResponse;
import com.sfebiz.supplychain.sdk.protocol.EventType;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsRequest;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsResponse;
import com.sfebiz.supplychain.sdk.protocol.Response;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/5/15 下午2:32
 */
public class COESkuReStockInCommand extends COESkuStockInCommand {

    @Override
    protected CommandResponse sendStockInCommandToWarehouse() throws ServiceException {

        String meta = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("meta");
        Map<String, Object> metaData = new Gson().fromJson(meta,
                new com.google.common.reflect.TypeToken<Map<String, Object>>() {
                }.getType()
        );
        LogisticsEventsRequest logisticsEventsRequest = buildRequest(metaData);
        //重新配置类型
        logisticsEventsRequest.getLogisticsEvent().getEventHeader().setEventType(EventType.LOGISTICS_SKU_RE_STOCKIN_INFO.value);
        String url = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
        String key = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
            throw new ServiceException(LogisticsReturnCode.STOCKIN_ORDER_SENDSTOCK_URLORKEY_NULL,
                    "[供应链-重新提交入库单异常]: " + LogisticsReturnCode.STOCKIN_ORDER_SENDSTOCK_URLORKEY_NULL.getDesc() + " "
                            + "[入库单ID: " + getStockinOrderBO().getId()
                            + ", 调用URL: " + url
                            + ", 调用KEY: " + key
                            + "]");
        }

        LogisticsEventsResponse responses = ProviderBizService.getInstance().send(
                logisticsEventsRequest,
                WmsMessageType.RE_STOCK_IN.getValue(),
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
    public void checkStateIsCanStockin() throws ServiceException {
        if (null == getStockinOrderBO()) {
            throw new ServiceException(LogisticsReturnCode.STOCKIN_ORDER_NOT_EXIST,
                    "[供应链-提交入库单异常]: " + LogisticsReturnCode.STOCKIN_ORDER_NOT_EXIST.getDesc());
        }
        //只有仓库未回传报文的情况下才允许重复提交
        if (null != getStockinOrderBO().getWarehouseStockinTime()) {
            throw new ServiceException(LogisticsReturnCode.STOCKIN_ORDER_NOT_ALLOW_SUBMIT,
                    "[供应链-提交入库单异常]: " + LogisticsReturnCode.STOCKIN_ORDER_NOT_ALLOW_SUBMIT.getDesc() + " "
                            + "[入库单ID: " + getStockinOrderBO().getId()
                            + ", 入库单状态: " + getStockinOrderBO().getState()
                            + ", 仓库回传时间: " + getStockinOrderBO().getWarehouseStockinTime()
                            + "]");
        }
    }


}
