package com.sfebiz.supplychain.provider.command.send.wms.zebra;

import com.google.gson.Gson;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderType;
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
 * <p>通知仓库SKU入库</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/22
 * Time: 上午10:09
 */
public class ZebraSkuStockInCommand extends WmsOrderSkuStockInCommand {

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
        LogisticsEventsResponse responses = ProviderBizService.getInstance().send(
                logisticsEventsRequest,
                WmsMessageType.STOCK_IN.getValue(),
                url,
                key,
                TraceLogEntity.instance(traceLogger, getStockinOrderBO().getId(), SystemConstants.TRACE_APP), ZebraConstants.PARTNER_CODE);

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
        if (getStockinOrderBO().getType().equals(StockinOrderType.SALES_STOCK_IN.getValue())) {
            logisticsOrder.setStockinType(getStockinOrderBO().getType());
            logisticsOrder.setSupplierId(null != stockinOrderBO.getMerchantProviderId() ? stockinOrderBO.getMerchantProviderId().intValue() : null);
        } // TODO 待处理
//        else if (getStockinOrderBO().getType().equals(StockinOrderType.ALLOCATION_STOCK_IN.getValue())) {
//            logisticsOrder.setStockinType(getStockinOrderBO().getType());
//        } else if (getStockinOrderBO().getType().equals(StockinOrderType.CUSTOMS_RETURN_STOCK_IN.getValue())
//                    || getStockinOrderBO().getType().equals(StockinOrderType.RETURN_STOCK_IN.getValue())){
//            // 海关退货，顾客退货，对于斑马仓库而言都属于退货，相同处理逻辑，沿用消息 入库类型：0-采购 1-转运 2-调拨 3-退货
//            logisticsOrder.setStockinType(3);
//        }
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
