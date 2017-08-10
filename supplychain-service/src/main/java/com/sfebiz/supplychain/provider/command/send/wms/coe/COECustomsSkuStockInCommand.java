package com.sfebiz.supplychain.provider.command.send.wms.coe;

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
import com.sfebiz.supplychain.provider.entity.PriceUnit;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderBO;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderDetailBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.NumberUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created for com.sfebiz.logistics.provider.command.send.wms.coe
 * description:海关退货入库单，通知仓库商品入库（提交商品给COE仓库）
 * User: 成刚
 * Date: 2015/11/11
 * Time: 10:31
 * Created with IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 */
public class COECustomsSkuStockInCommand extends WmsOrderSkuStockInCommand {

    private static Logger logger = LoggerFactory.getLogger(COECustomsSkuStockInCommand.class);

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
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("[供应链-提交入库单]: COE仓库海关退货入库提交给仓库")
                .addParm("url", url).addParm("key",key).addParm("请求报文",logisticsEventsRequest)
                .log();

        LogisticsEventsResponse responses = ProviderBizService.getInstance().send(
                logisticsEventsRequest,
                WmsMessageType.STOCK_IN.getValue(),
                url,
                key,
                TraceLogEntity.instance(traceLogger, getStockinOrderBO().getId(), SystemConstants.TRACE_APP));

        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("[供应链-提交入库单]: COE仓库海关退货入库提交给仓库")
                .addParm("返回报文值：", responses)
                .log();
        if (responses.responseItems == null || responses.getResponseItems().size() == 0) {
            return new CommandResponse(false, "调用仓库接口返回信息为空");
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
            sku.skuPriceCurrency = PriceUnit.CNY;//此字段表示价格单位为分
            sku.batchMakePlan = stockinOrderDetailBO.getBatchGeneratePlan();
            sku.batchNo = stockinOrderDetailBO.getSkuBatch();
//            sku.lastSendBatchNo = stockinOrderDetailBO.getLastSendSkuBatch();
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
