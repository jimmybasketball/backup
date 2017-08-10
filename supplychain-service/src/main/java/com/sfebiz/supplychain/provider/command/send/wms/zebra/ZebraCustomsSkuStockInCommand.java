package com.sfebiz.supplychain.provider.command.send.wms.zebra;

import com.google.gson.Gson;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.CommandResponse;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuStockInCommand;
import com.sfebiz.supplychain.provider.entity.ZebraConstants;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderBO;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderDetailBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.NumberUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created for com.sfebiz.logistics.provider.command.send.wms.zebra
 * description:海关退货入库单，通知仓库商品入库（提交商品给香港斑马仓库）
 * User: 成刚
 * Date: 2015/11/11
 * Time: 11:12
 * Created with IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 */
public class ZebraCustomsSkuStockInCommand extends WmsOrderSkuStockInCommand {

    private static final String serviceName = "SkuInboundOrder";

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
                    "[供应链-提交入库单异常]: " + LogisticsReturnCode.STOCKIN_ORDER_SENDSTOCK_URLORKEY_NULL.getDesc() + " "
                            + "[入库单ID: " + getStockinOrderBO().getId()
                            + ", 调用URL: " + url
                            + ", 调用KEY: " + key
                            + "]");
        }
        if (!url.endsWith("/")) {
            url = url.concat("/");
        }
        url = url.concat(serviceName);

        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("海关退货调用香港斑马仓库发送报文信息开始")
                .addParm("请求报文信息", logisticsEventsRequest)
                .addParm("调用URL", url)
                .addParm("调用KEY", key)
                .log();

        LogisticsEventsResponse responses = ProviderBizService.getInstance().send(
                logisticsEventsRequest,
                WmsMessageType.STOCK_IN.getValue(),
                url,
                key,
                TraceLogEntity.instance(traceLogger, getStockinOrderBO().getId(), SystemConstants.TRACE_APP), ZebraConstants.PARTNER_CODE);

        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("海关退货调用香港斑马仓库发送报文信息结束")
                .addParm("请求报文信息", logisticsEventsRequest)
                .addParm("调用URL", url)
                .addParm("调用KEY", key)
                .addParm("返回信息", responses)
                .log();
        if (responses.responseItems == null || responses.getResponseItems().size() == 0) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("海关退货调用香港斑马仓库接口返回信息为空")
                    .addParm("请求报文信息", logisticsEventsRequest)
                    .addParm("调用URL", url)
                    .addParm("调用KEY", key)
                    .log();
            return new CommandResponse(false, "海关退货调用香港斑马仓库接口返回信息为空");
        }

        Response response = responses.getResponseItems().get(0);
        if (!isSuccess(response)) {
            return new CommandResponse(false, response.getReasonDesc());
        }

        return CommandResponse.SUCCESS_RESPONSE;
    }

    protected boolean isSuccess(Response response) {
        if ("true".equals(response.success)) {
            return true;
        } else {
            if (response.getReason().equals("B0200")) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 构建请求体
     *
     * @param metaData
     * @return
     */
    public LogisticsEventsRequest buildRequest(Map<String, Object> metaData) {
        String eventSource = "";
        if (metaData != null && metaData.containsKey("event_source")) {
            eventSource = (String) metaData.get("event_source");
        }
        WarehouseBO warehouse = warehouseBO;
        EventHeader eventHeader = new EventHeader();
        eventHeader.setEventType(EventType.LOGISTICS_SKU_STOCKIN_INFO.value);
        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
        eventHeader.setEventSource(eventSource);
        eventHeader.setEventTarget(warehouse.getWarehouseNid());

        List<LogisticsOrder> logisticsOrders = new ArrayList<LogisticsOrder>();
        StockinOrderBO stockinOrderBO = getStockinOrderBO();
        LogisticsOrder logisticsOrder = new LogisticsOrder();
        logisticsOrder.setOccurTime(DateUtil.getCurrentDateTime());
        logisticsOrder.setLogisticsType(NumberUtil.parseInt(stockinOrderBO.getType(), 0));
//        logisticsOrder.setCarrierCode(stockinOrderBO.getCarrierCode());
        logisticsOrder.setMailNo(stockinOrderBO.getMailNo());
        logisticsOrder.setStockinType(3);
        logisticsOrder.setSkuStockInId(stockinOrderBO.getStockinId());
        logisticsOrder.setCustomerCode(CommonUtil.getCustomerCode());
        logisticsOrders.add(logisticsOrder);

        SkuDetail skuDetail = new SkuDetail();
        logisticsOrder.setSkuDetail(skuDetail);

        List<StockinOrderDetailBO> stockinOrderDetailBOs = this.getStockinOrderDetailBOs();
        for (StockinOrderDetailBO stockinOrderDetailBO : stockinOrderDetailBOs) {
            Sku sku = new Sku();
            sku.skuId = "" + stockinOrderDetailBO.getSkuId();
            sku.skuQty = stockinOrderDetailBO.getCount();
            sku.skuPriceCurrency = "CNY";//此字段表示价格单位为分
            sku.batchMakePlan = stockinOrderDetailBO.getBatchGeneratePlan();
            sku.batchNo = stockinOrderDetailBO.getSkuBatch();
            skuDetail.getSkus().add(sku);
        }

        LogisticsDetail logisticsDetail = new LogisticsDetail();
        logisticsDetail.setLogisticsOrders(logisticsOrders);
        EventBody eventBody = new EventBody();
        eventBody.setLogisticsDetail(logisticsDetail);
        LogisticsEvent logisticsEvent = new LogisticsEvent();
        logisticsEvent.setEventHeader(eventHeader);
        logisticsEvent.setEventBody(eventBody);

        LogisticsEventsRequest logisticsEventsRequest = new LogisticsEventsRequest();
        logisticsEventsRequest.setLogisticsEvent(logisticsEvent);
        return logisticsEventsRequest;
    }
}
