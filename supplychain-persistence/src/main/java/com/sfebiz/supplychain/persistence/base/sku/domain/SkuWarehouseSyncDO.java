package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * Created by wang_cl on 2015/1/22.
 */
public class SkuWarehouseSyncDO extends BaseDO {
    private static final long serialVersionUID = -7812543477136566611L;
    /**
     * skuId
     */
    private Long skuId;
    /**
     * 商品条码
     */
    private String barCode;
    /**
     * 仓库的关联ID
     */
    private Long warehouseId;
    /**
     * 同步标识(SYNC_SUCCESS:同步成功,SYNC_FAIL:同步失败)
     */
    private String syncState;
    /**
     * 同步更新标识(SYNC_UPDATE_SUCCESS:同步更新成功,SYNC_UPDATE_FAIL:同步更新失败)
     */
    private String syncUpdateState;
    /**
     * 删除标识
     */
    private int isDelete;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getSyncState() {
        return syncState;
    }

    public void setSyncState(String syncState) {
        this.syncState = syncState;
    }

    public String getSyncUpdateState() {
        return syncUpdateState;
    }

    public void setSyncUpdateState(String syncUpdateState) {
        this.syncUpdateState = syncUpdateState;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "SkuWarehouseSyncDO{" +
                "skuId=" + skuId +
                ", barCode=" + barCode +
                ", warehouseId=" + warehouseId +
                ", syncState=" + syncState +
                ", syncUpdateState=" + syncUpdateState +
                '}';
    }
}
