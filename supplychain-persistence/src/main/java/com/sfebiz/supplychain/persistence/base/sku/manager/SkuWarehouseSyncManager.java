package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.sku.enums.SkuWarehouseSyncStateType;
import com.sfebiz.supplychain.persistence.base.sku.dao.SkuWarehouseSyncDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncDO;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wang_cl on 2015/1/22.
 */
@Component("skuWarehouseSyncManager")
public class SkuWarehouseSyncManager extends BaseManager<SkuWarehouseSyncDO> {
    @Resource
    private SkuWarehouseSyncDao skuWarehouseSyncDao;

    @Override
    public BaseDao<SkuWarehouseSyncDO> getDao() {
        return skuWarehouseSyncDao;
    }

    public List<SkuWarehouseSyncDO> getUnsyncData(){
        SkuWarehouseSyncDO skuWarehouseSyncDO = new SkuWarehouseSyncDO();
        skuWarehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_FAIL.getValue());
        BaseQuery<SkuWarehouseSyncDO> query = BaseQuery.getInstance(skuWarehouseSyncDO);
        query.addLte("gmtCreate", DateFormatUtils.format(getOneHoursAgoOnCurrentDate(),"yyyy-MM-dd HH:mm:ss"));
        List<SkuWarehouseSyncDO> skuWarehouseSyncDOList = this.query(query);
        return skuWarehouseSyncDOList;
    }

    public SkuWarehouseSyncDO getBySkuIdAndStorehouseId(long skuId,long warehouseId){
        SkuWarehouseSyncDO skuWarehouseSyncDO = new SkuWarehouseSyncDO();
        skuWarehouseSyncDO.setSkuId(skuId);
        skuWarehouseSyncDO.setWarehouseId(warehouseId);
        BaseQuery<SkuWarehouseSyncDO> query = BaseQuery.getInstance(skuWarehouseSyncDO);
        List<SkuWarehouseSyncDO> skuWarehouseSyncDOList = this.query(query);
        if (skuWarehouseSyncDOList != null && skuWarehouseSyncDOList.size() > 0) {
            return skuWarehouseSyncDOList.get(0);
        }
        return null;
    }

    public int getCountBySkuId(long skuId, long warehouseId){
        SkuWarehouseSyncDO skuWarehouseSyncDO = new SkuWarehouseSyncDO();
        skuWarehouseSyncDO.setSkuId(skuId);
        skuWarehouseSyncDO.setWarehouseId(warehouseId);
        BaseQuery<SkuWarehouseSyncDO> query = BaseQuery.getInstance(skuWarehouseSyncDO);
        List<SkuWarehouseSyncDO> skuWarehouseSyncDOList = this.query(query);
        if (skuWarehouseSyncDOList != null && skuWarehouseSyncDOList.size() > 0) {
            return skuWarehouseSyncDOList.size();
        }
        return 0;
    }


    /**
     * 获取当前时间前一个小时的时间
     * @return
     */
    private Date getOneHoursAgoOnCurrentDate(){
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.HOUR_OF_DAY, -1);// before 8 hour
        return cal.getTime();
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("D:/development/IDEA/ifunq-supplychain/haitao-b2b-supplychain/" +
                        "supplychain-persistence/src/main/resources/base/sqlmap/sku/sku-warehouse-sync-sqlmap.xml",
                SkuWarehouseSyncDao.class, SkuWarehouseSyncDO.class, "sc_sku_warehouse_syc");
    }

    public List<SkuWarehouseSyncDO> getAllUnSyncDataByWarehouseId(long warehouseId) {
        SkuWarehouseSyncDO skuWarehouseSyncDOForQuery = new SkuWarehouseSyncDO();
        skuWarehouseSyncDOForQuery.setWarehouseId(warehouseId);
        skuWarehouseSyncDOForQuery.setSyncState(SkuWarehouseSyncStateType.SYNC_FAIL.getValue());
        return query(BaseQuery.getInstance(skuWarehouseSyncDOForQuery));
    }
}
