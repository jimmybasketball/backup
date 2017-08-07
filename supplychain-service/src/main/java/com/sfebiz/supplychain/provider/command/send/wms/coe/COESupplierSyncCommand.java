package com.sfebiz.supplychain.provider.command.send.wms.coe;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderDO;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSupplierSyncCommand;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
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

public class COESupplierSyncCommand extends WmsOrderSupplierSyncCommand {

    @Override
    public boolean execute() {
        try {
            if (logisticsProviderBO == null || CollectionUtils.isEmpty(providerDOs)) {
                LogBetter.instance(logger)
                        .setMsg("下发供应商同步报文参数异常")
                        .addParm("logisticsProviderBO", logisticsProviderBO)
                        .addParm("providerDOs", providerDOs)
                        .log();

                return false;
            }
            LogBetter.instance(logger)
                    .setMsg("准备下发供应商同步报文")
                    .log();

            String url = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
            String key = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
                return false;
            }
            LogisticsEventsRequest logisticsEventsRequest = buildLogisticsEventRequest();
            LogBetter.instance(logger)
                    .setMsg("供应商同步报文")
                    .addParm("responses", new Gson().toJson(logisticsEventsRequest))
                    .setLevel(LogLevel.INFO)
                    .log();
            LogisticsEventsResponse responses = ProviderBizService.getInstance().send(logisticsEventsRequest, WmsMessageType.SUPPLIER_SYNC.getValue(), url, key, null);
            LogBetter.instance(logger)
                    .setMsg("供应商同步响应报文")
                    .addParm("responses", new Gson().toJson(responses))
                    .setLevel(LogLevel.INFO)
                    .log();
            if (responses.responseItems == null || responses.getResponseItems().size() == 0) {
                logger.error("调用仓库同步供应商接口返回信息为空");
                return false;
            }
            Response response = responses.getResponseItems().get(0);
            if (isSuccess(response)) {
                logger.info("同步供应商成功");
                return true;
            } else {
                logger.info("同步供应商失败");
            }

        } catch (Exception e) {
            logger.error("", e);
        }
        return false;
    }


    public boolean isSuccess(Response response) {
        if ("true".equals(response.success)) {
            return true;
        } else {
            return false;
        }
    }

    public LogisticsEventsRequest buildLogisticsEventRequest() {
        String eventSource = "";
        String meta = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("meta");
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> metaData = new Gson().fromJson(meta, type);
        if (metaData != null && metaData.containsKey("event_source")) {
            eventSource = (String) metaData.get("event_source");
        }
        WarehouseBO warehouse = getWarehouseBO();
        EventHeader eventHeader = new EventHeader();
        eventHeader.setEventType(EventType.LOGISTICS_SUPPLIER_SYNC.value);
        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
        eventHeader.setEventSource(eventSource);
        eventHeader.setEventTarget(warehouse.getWarehouseNid());
        SupplierDetail supplierDetail = new SupplierDetail();
        List<Supplier> suppliers = new ArrayList<Supplier>();
        for (MerchantProviderDO providerDO : providerDOs) {
            Supplier supplier = new Supplier();
            supplier.setCustomerCode(CommonUtil.getCustomerCode());
            supplier.setSupplierId(providerDO.getId().intValue());
            supplier.setSupplierName("海淘签约供应商");
            suppliers.add(supplier);
        }
        supplierDetail.setSuppliers(suppliers);
        EventBody eventBody = new EventBody();
        eventBody.setSupplierDetail(supplierDetail);
        LogisticsEvent logisticsEvent = new LogisticsEvent();
        logisticsEvent.setEventHeader(eventHeader);
        logisticsEvent.setEventBody(eventBody);
        LogisticsEventsRequest logisticsEventsRequest = new LogisticsEventsRequest();
        logisticsEventsRequest.setLogisticsEvent(logisticsEvent);
        return logisticsEventsRequest;
    }
}
