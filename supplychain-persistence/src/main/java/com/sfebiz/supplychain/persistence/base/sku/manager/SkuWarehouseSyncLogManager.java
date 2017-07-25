package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.SkuWarehouseSyncLogDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncLogDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by sam on 3/25/15.
 * Email: sambean@126.com
 */

@Component("skuWarehouseSyncLogManager")
public class SkuWarehouseSyncLogManager extends BaseManager<SkuWarehouseSyncLogDO> {

    @Resource
    private SkuWarehouseSyncLogDao skuWarehouseSyncLogDao;

    @Override
    public BaseDao<SkuWarehouseSyncLogDO> getDao() {
        return skuWarehouseSyncLogDao;
    }

    public void createLog(long skuId, long warehouseId,String reason,String response,int isSuccess) {
        SkuWarehouseSyncLogDO skuWarehouseSyncLogDO = new SkuWarehouseSyncLogDO();
        skuWarehouseSyncLogDO.setSkuId(skuId);
        skuWarehouseSyncLogDO.setWarehouseId(warehouseId);
        skuWarehouseSyncLogDO.setReason(reason);
        skuWarehouseSyncLogDO.setResponse(response);
        skuWarehouseSyncLogDO.setIsSuccess(isSuccess);
        insert(skuWarehouseSyncLogDO);
    }

    public static void main(String[] args) {
        DaoHelper.genXML("D:/development/IDEA/ifunq-supplychain/haitao-b2b-supplychain/" +
                        "supplychain-persistence/src/main/resources/base/sqlmap/sku/sku-warehouse-sync-log-sqlmap.xml",
                SkuWarehouseSyncLogDao.class, SkuWarehouseSyncLogDO.class, "sc_sku_warehouse_sync_log",false);
    }
}
