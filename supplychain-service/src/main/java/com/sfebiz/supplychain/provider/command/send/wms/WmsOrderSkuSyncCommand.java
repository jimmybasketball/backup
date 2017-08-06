package com.sfebiz.supplychain.provider.command.send.wms;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.exposed.sku.enums.SkuWarehouseSyncStateType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsOperaterType;
import com.sfebiz.supplychain.factory.SpringBeanFactory;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncLogManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncManager;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 抽象与仓库进行商品同步
 */
public abstract class WmsOrderSkuSyncCommand extends AbstractCommand {

    /**
     * 商品基本信息
     */
    protected List<SkuEntity> skuEntities;

    /**
     * 仓库信息
     */
    protected WarehouseBO warehouseBO;

    /**
     * 供应商信息
     */
    protected LogisticsProviderBO logisticsProviderBO;
    /**
     * 同步失败的异常原因
     */
    protected String errorMessage;
    /**
     * 同步类型，新增或者更新
     */
    private WmsOperaterType wmsOperaterType;

    public List<SkuEntity> getSkuEntities() {
        return skuEntities;
    }

    public void setSkuEntities(List<SkuEntity> skuEntities) {
        this.skuEntities = skuEntities;
    }

    public WarehouseBO getWarehouseBO() {
        return warehouseBO;
    }

    public void setWarehouseBO(WarehouseBO warehouseBO) {
        this.warehouseBO = warehouseBO;
    }

    public LogisticsProviderBO getLogisticsProviderBO() {
        return logisticsProviderBO;
    }

    public void setLogisticsProviderBO(LogisticsProviderBO logisticsProviderBO) {
        this.logisticsProviderBO = logisticsProviderBO;
    }

    public WmsOperaterType getWmsOperaterType() {
        return wmsOperaterType;
    }

    public void setWmsOperaterType(WmsOperaterType wmsOperaterType) {
        this.wmsOperaterType = wmsOperaterType;
    }

    /**
     * 通用商品同步方法
     *
     * @return
     */
    @Override
    public boolean execute() {

        boolean isMockAutoCreated = MockConfig.isMocked("command", "skuSyncCommand");
        if (isMockAutoCreated) {
            //直接返回仓库已发货
            logger.info("MOCK：所有仓库 商品同步 采用MOCK实现");
            return mockSkuSyncSuccess();
        }
//
//        DistributedLock distributedLock = SpringBeanFactory.getBean("distributedLock", DistributedLock.class);
//        try {
//            if (logisticsProviderBO == null || warehouseBO == null || CollectionUtils.isEmpty(skuEntities)) {
//                LogBetter.instance(logger)
//                        .setMsg("下发商品同步报文参数异常")
//                        .addParm("warehouseBO", warehouseBO)
//                        .addParm("logisticsProviderBO", logisticsProviderBO)
//                        .addParm("warehouseBO", warehouseBO)
//                        .log();
//
//                return false;
//            }
//            LogBetter.instance(logger)
//                    .setMsg("准备下发商品同步报文")
//                    .addParm("仓库名称", warehouseBO.getName())
//                    .log();
//
//            String url = getLogisticsProviderBO().getInterfaceUrl();
//            String key = getLogisticsProviderBO().getInterfaceKey();
//            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
//                return false;
//            }
//            LogisticsEventsRequest logisticsEventsRequest = buildLogisticsEventRequest();
//            LogBetter.instance(logger)
//                    .setMsg(warehouseBO.getName() + "同步sku报文")
//                    .addParm("responses", new Gson().toJson(logisticsEventsRequest))
//                    .setLevel(LogLevel.INFO)
//                    .log();
//            LogisticsEventsResponse responses = ProviderBO.getInstance().send(logisticsEventsRequest, WmsMessageType.SKU_SYNC.getValue(), url, key, null);
//            LogBetter.instance(logger)
//                    .setMsg(warehouseBO.getName() + "同步sku报文响应")
//                    .addParm("responses", new Gson().toJson(responses))
//                    .setLevel(LogLevel.INFO)
//                    .log();
//            if (responses.responseItems == null || responses.getResponseItems().size() == 0) {
//                logger.error("调用仓库同步sku接口返回信息为空");
//                return false;
//            }
//            SkuWarehouseSyncLogManager skuWarehouseSyncLogManager = SpringBeanFactory.getBean("skuWarehouseSyncLogManager", SkuWarehouseSyncLogManager.class);
//            Response response = responses.getResponseItems().get(0);
//            //防止并发插入
//            if (distributedLock.fetch("SKU-SYNC-LOCK")) {
//                if (isSuccess(response)) {
//                    logger.info("同步sku成功");
//                    for (SkuEntity skuEntity : skuEntities) {
//                        SkuWarehouseSyncDO warehouseSyscDO = SkuBO.getInstance().getSkuWarehouseSyscManager().getBySkuIdAndStorehouseId(skuEntity.getId(), this.getWarehouseBO().getId(), SkuType.BASIC_SKU.getValue());
//                        if (warehouseSyscDO != null) {
//                            logger.info("skuId:{},warehouseId:{} 存在,update", skuEntity.id, this.getWarehouseBO().getId());
//                            warehouseSyscDO.setIsSync(SyncState.SYNC_SUCCESS.value);
//                            SkuBO.getInstance().getSkuWarehouseSyscManager().update(warehouseSyscDO);
//                        } else {
//                            logger.error("skuId:{},warehouseId:{} 不存在", skuEntity.id, this.getWarehouseBO().getId());
//
//                        }
//                        skuWarehouseSyncLogManager.createLog(skuEntity.id, this.getWarehouseBO().getId(), "成功", 0, 1);
//                    }
//                    return true;
//                } else {
//                    errorMessage = responses.responseItems.get(0).getReason() + " | " + responses.responseItems.get(0).getReasonDesc();
//                    skuWarehouseSyncLogManager.createLog(0, this.getWarehouseBO().getId(), responses.responseItems.get(0).getReason() + " | " + responses.responseItems.get(0).getReasonDesc(), 0, 0);
//                    logger.info("同步sku失败");
//                }
//            }
//        } catch (Exception e) {
//            errorMessage = "商品同步内部异常";
//            logger.error("", e);
//        } finally {
//            distributedLock.realease("SKU-SYNC-LOCK");
//        }
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
        eventHeader.setEventType(EventType.LOGISTICS_SKU_SYNC.value);
        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
        eventHeader.setEventSource(eventSource);
        eventHeader.setEventTarget(warehouse.getWarehouseNid());
        List<LogisticsOrder> logisticsOrders = new ArrayList<LogisticsOrder>();
        List<SkuEntity> skuEntities = getSkuEntities();
        SkuDetail skuDetail = new SkuDetail();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SkuEntity skuEntity : skuEntities) {
            Sku sku = new Sku();
//            sku.skuId = "" + skuEntity.id;
//            sku.skuCode = skuEntity.getBarcode() == null ? "" : skuEntity.getBarcode();
//            // - 是非法字符
//            sku.skuCode = sku.skuCode.replaceAll("-", "");
//            sku.skuName = skuEntity.name;
//            sku.specification = SkuAttributeConvertUtil.convertAttributesToString(skuEntity.attributes);
//            sku.batchMix = skuEntity.isMixBatch;
//            try {
//                if (skuEntity.qualityTestingPlan != null) {
//                    sku.qualityTestRate = Integer.parseInt(skuEntity.qualityTestingPlan);
//                }
//            } catch (Exception e) {
//                LogBetter.instance(logger)
//                        .addParm("skuEntity", new Gson().toJson(skuEntity))
//                        .log();
//            }
//            sku.createTime = simpleDateFormat.format(new Date(skuEntity.gmtCreate));
//            sku.customerCode = CommonUtil.getCustomerCode();
//            sku.stockinPeriodPlan = skuEntity.stockinPeriodPlan;
//            sku.stockinPeriodPlanValue = skuEntity.stockinPeriodPlanValue;
//            sku.stockoutPeriodPlan = skuEntity.stockinPeriodPlan;
//            sku.stockoutPeriodPlanValue = skuEntity.stockoutPeriodPlanValue;
//            sku.grossWeight = skuEntity.grossWeight;
//            sku.skuNetWeight = skuEntity.netWeight;
//            sku.batchMakePlan = skuEntity.batchMakePlan;
//            sku.stockoutPlan = skuEntity.stockoutPlan;
//            sku.length = skuEntity.length;
//            sku.height = skuEntity.height;
//            sku.width = skuEntity.width;
//            sku.stockoutPlan = skuEntity.stockoutPlan;
//            sku.stockAlarmLimit = skuEntity.stockAlarmLimit;
//            sku.maxStockAlarmLimit = skuEntity.maxStockAlarmLimit;
//            sku.status = 1;
//            sku.guarantyPeriod = skuEntity.guarantyPeriod;
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

    public boolean mockSkuSyncSuccess() {
        SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager", SkuWarehouseSyncManager.class);
        SkuWarehouseSyncLogManager skuWarehouseSyncLogManager = SpringBeanFactory.getBean("skuWarehouseSyncLogManager", SkuWarehouseSyncLogManager.class);
        for (SkuEntity skuEntity : skuEntities) {
            SkuWarehouseSyncDO warehouseSyncDO = skuWarehouseSyncManager.getBySkuIdAndWarehouseId(skuEntity.getId(), this.warehouseBO.getId());
            if (warehouseSyncDO != null) {
                if (WmsOperaterType.ADD.equals(getWmsOperaterType())) {
                    warehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.value);
                } else {
                    warehouseSyncDO.setSyncUpdateState(SkuWarehouseSyncStateType.SYNC_UPDATE_SUCCESS.value);
                }
                warehouseSyncDO.setGmtModified(new Date());
                skuWarehouseSyncManager.update(warehouseSyncDO);
            } else {
                logger.error("skuId:{},warehouseId:{} 不存在", skuEntity.id, this.getWarehouseBO().getId());
            }
            skuWarehouseSyncLogManager.createLog(skuEntity.id, this.getWarehouseBO().getId(), "成功", "mock", 1);
        }
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
