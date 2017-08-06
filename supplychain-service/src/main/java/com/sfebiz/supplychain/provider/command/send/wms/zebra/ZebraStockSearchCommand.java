package com.sfebiz.supplychain.provider.command.send.wms.zebra;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.send.wms.WmsStockSearchCommand;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sam on 3/16/15.
 * Email: sambean@126.com
 */

public class ZebraStockSearchCommand extends WmsStockSearchCommand {

    private static final String serviceName = "InventorySearch";

    @Override
    public boolean execute() throws ServiceException {
        Response response = new Response();
        try {
            if ( logisticsProviderBO == null || warehouseBO == null || CollectionUtils.isEmpty(skuIds)){
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("下发商品库存查询报文参数异常")
                        .addParm("仓库信息",warehouseBO)
                        .addParm("国内供应商信息",logisticsProviderBO)
                        .addParm("商品列表",skuIds)
                        .log();
                return false;
            }
            String url = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
            String key = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
                return false;
            }
            LogisticsEventsRequest logisticsEventsRequest = buildLogisticsEventRequest();
            LogisticsEventsResponse responses = ProviderBizService.getInstance().send(logisticsEventsRequest, EventType.LOGISTICS_STOCK_SEARCH.value, url, key, null);

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("商品库存查询请求成功")
                    .addParm("请求报文", new Gson().toJson(logisticsEventsRequest))
                    .addParm("回复报文",new Gson().toJson(responses))
                    .addParm("仓库名称", warehouseBO.getName())
                    .log();

            if (responses.responseItems == null || responses.getResponseItems().size() == 0) {
                logger.error("调用仓库库存查询接口返回信息为空");
                return false;
            }

            response = responses.getResponseItems().get(0);
            logger.info(response.getSuccess());
            stockResponse = response;
        } catch (Exception e) {
            logger.error("", e);
        }
        return true;
    }

    public boolean isSuccess(Response response){
        if ("true".equals(response.success)) {
            return true;
        } else {
            return false;
        }
    }

    public LogisticsEventsRequest buildLogisticsEventRequest() {
        String eventSource = "SF";
        String meta = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("meta");
        Type type = new TypeToken<Map<String,Object>>(){}.getType();
        Map<String, Object> metaData =  new Gson().fromJson(meta,type);
        if (metaData != null && metaData.containsKey("event_source")) {
            eventSource = (String) metaData.get("event_source");
        }
        WarehouseBO warehouse = warehouseBO;
        EventHeader eventHeader = new EventHeader();
        eventHeader.setEventType(EventType.LOGISTICS_STOCK_SEARCH.value);
        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
        eventHeader.setEventSource(eventSource);
        eventHeader.setEventTarget(warehouse.getWarehouseNid());
        List<LogisticsOrder> logisticsOrders = new ArrayList<LogisticsOrder>();
        SkuDetail skuDetail = new SkuDetail();
        for (long skuId : skuIds) {
            Sku sku = new Sku();
            sku.skuId = skuId + "";
            sku.customerCode = "SFHT";
            skuDetail.getSkus().add(sku);
        }

        LogisticsDetail logisticsDetail = new LogisticsDetail();
        logisticsDetail.setLogisticsOrders(logisticsOrders);
        EventBody eventBody = new EventBody();
        eventBody.setSkuDetail(skuDetail);
        LogisticsEvent logisticsEvent = new LogisticsEvent();
        logisticsEvent.setEventHeader(eventHeader);
        logisticsEvent.setEventBody(eventBody);
        LogisticsEventsRequest logisticsEventsRequest = new LogisticsEventsRequest();
        logisticsEventsRequest.setLogisticsEvent(logisticsEvent);
        return logisticsEventsRequest;
    }

    public void setSkuIds(List<Long> skuIds) {
        this.skuIds = skuIds;
    }
}
