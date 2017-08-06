package com.sfebiz.supplychain.provider.command.send.wms.zebra;

import com.google.gson.Gson;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.exposed.sku.enums.SkuWarehouseSyncStateType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsOperaterType;
import com.sfebiz.supplychain.factory.SpringBeanFactory;
import com.sfebiz.supplychain.lock.DistributedLock;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncLogManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncManager;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuSyncCommand;
import com.sfebiz.supplychain.provider.entity.ZebraConstants;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsRequest;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsResponse;
import com.sfebiz.supplychain.sdk.protocol.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Created by yanmingming on 6/25/15.
 */

public class ZebraSkuSyncCommand extends WmsOrderSkuSyncCommand {

    private static final String serviceName = "ProductSync";

    /**
     * COE 使用的是通用命令，所以采用通用父类中通用的商品同步方法
     *
     * @return
     */
    @Override
    public boolean execute() {

        boolean isMockAutoCreated = MockConfig.isMocked("zebra", "skuSyncCommand");
        if (isMockAutoCreated) {
            //直接返回仓库已发货
            logger.info("MOCK：斑马仓库 商品同步 采用MOCK实现");
            return mockSkuSyncSuccess();
        }

        DistributedLock distributedLock = SpringBeanFactory.getBean("distributedLock", DistributedLock.class);
        try {
            if (logisticsProviderBO == null || warehouseBO == null || CollectionUtils.isEmpty(skuEntities)) {
                LogBetter.instance(logger)
                        .setMsg("下发商品同步报文参数异常")
                        .addParm("warehouseBO", warehouseBO)
                        .addParm("logisticsProviderBO", logisticsProviderBO)
                        .addParm("warehouseBO", warehouseBO)
                        .log();

                return false;
            }
            LogBetter.instance(logger)
                    .setMsg("准备下发商品同步报文")
                    .addParm("仓库名称", warehouseBO.getName())
                    .log();

            String url = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
            String key = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
                return false;
            }
            if (!url.endsWith("/")) {
                url = url.concat("/");
            }
            url = url.concat(serviceName);
            LogisticsEventsRequest logisticsEventsRequest = buildLogisticsEventRequest();
            LogBetter.instance(logger)
                    .setMsg(warehouseBO.getName() + "同步sku报文")
                    .addParm("request", logisticsEventsRequest)
                    .setLevel(LogLevel.INFO)
                    .log();
            LogisticsEventsResponse responses = ProviderBizService.getInstance().send(logisticsEventsRequest, WmsMessageType.SKU_SYNC.getValue(), url, key, null, ZebraConstants.PARTNER_CODE);
            LogBetter.instance(logger)
                    .setMsg(warehouseBO.getName() + "同步sku报文响应")
                    .addParm("responses", new Gson().toJson(responses))
                    .setLevel(LogLevel.INFO)
                    .log();
            if (responses.responseItems == null || responses.getResponseItems().size() == 0) {
                logger.error("调用仓库同步sku接口返回信息为空");
                return false;
            }
            SkuWarehouseSyncLogManager skuWarehouseSyncLogManager = SpringBeanFactory.getBean("skuWarehouseSyncLogManager", SkuWarehouseSyncLogManager.class);
            SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager", SkuWarehouseSyncManager.class);
            Response response = responses.getResponseItems().get(0);
            //防止并发插入
            if (distributedLock.fetch("SKU-SYNC-LOCK")) {
                if (isSuccess(response)) {
                    logger.info("同步sku成功");
                    for (SkuEntity skuEntity : skuEntities) {
                        SkuWarehouseSyncDO warehouseSyncDO = skuWarehouseSyncManager.getBySkuIdAndWarehouseId(skuEntity.getId(), this.warehouseBO.getId());
                        if (warehouseSyncDO != null) {
                            logger.info("skuId:{},warehouseId:{} 存在,update", skuEntity.id, this.getWarehouseBO().getId());
                            if (WmsOperaterType.ADD.equals(getWmsOperaterType())) {
                                warehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.value);
                            } else {
                                warehouseSyncDO.setSyncUpdateState(SkuWarehouseSyncStateType.SYNC_UPDATE_SUCCESS.value);
                            }
                        } else {
                            logger.error("skuId:{},warehouseId:{} 不存在", skuEntity.id, this.getWarehouseBO().getId());

                        }
                        skuWarehouseSyncLogManager.createLog(skuEntity.id, this.getWarehouseBO().getId(), "成功", response.toString(), 1);
                    }
                    return true;
                } else {
                    errorMessage = responses.responseItems.get(0).getReason() + " | " + responses.responseItems.get(0).getReasonDesc();
                    skuWarehouseSyncLogManager.createLog(0, this.getWarehouseBO().getId(), responses.responseItems.get(0).getReason() + " | " + responses.responseItems.get(0).getReasonDesc(), "mock", 0);
                    logger.info("同步sku失败");
                }
            }
        } catch (Exception e) {
            errorMessage = "商品同步内部异常";
            logger.error("", e);
        } finally {
            distributedLock.realease("SKU-SYNC-LOCK");
        }
        return false;
    }
}
