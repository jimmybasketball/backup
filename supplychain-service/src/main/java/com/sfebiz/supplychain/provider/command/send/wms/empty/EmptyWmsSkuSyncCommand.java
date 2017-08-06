package com.sfebiz.supplychain.provider.command.send.wms.empty;

import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.exposed.sku.enums.SkuWarehouseSyncStateType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsOperaterType;
import com.sfebiz.supplychain.factory.SpringBeanFactory;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncLogManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncManager;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuSyncCommand;

/**
 * Created by sam on 3/11/15.
 * Email: sambean@126.com
 */

public class EmptyWmsSkuSyncCommand extends WmsOrderSkuSyncCommand {

    /**
     * COE 使用的是通用命令，所以采用通用父类中通用的商品同步方法
     * @return
     */
    @Override
    public boolean execute() {
        SkuWarehouseSyncLogManager skuWarehouseSyncLogManager = SpringBeanFactory.getBean("skuWarehouseSyncLogManager", SkuWarehouseSyncLogManager.class);
        SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager", SkuWarehouseSyncManager.class);
        for (SkuEntity skuEntity : skuEntities) {
            SkuWarehouseSyncDO warehouseSyncDO = skuWarehouseSyncManager.getBySkuIdAndWarehouseId(skuEntity.getId(), this.getWarehouseBO().getId());
            if (warehouseSyncDO != null) {
                if (WmsOperaterType.ADD.equals(getWmsOperaterType())) {
                    warehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.getValue());
                } else {
                    warehouseSyncDO.setSyncUpdateState(SkuWarehouseSyncStateType.SYNC_UPDATE_SUCCESS.value);
                }
                skuWarehouseSyncManager.update(warehouseSyncDO);
            } else {
                SkuWarehouseSyncDO skuWarehouseSyncDO = new SkuWarehouseSyncDO();
                skuWarehouseSyncDO.setSkuId(skuEntity.getId());
                skuWarehouseSyncDO.setWarehouseId(this.getWarehouseBO().getId());
                skuWarehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.value);
                skuWarehouseSyncManager.insert(skuWarehouseSyncDO);
            }
            skuWarehouseSyncLogManager.createLog(skuEntity.id, this.getWarehouseBO().getId(), "成功", "mock", 1);
        }
        return true;
    }
}
