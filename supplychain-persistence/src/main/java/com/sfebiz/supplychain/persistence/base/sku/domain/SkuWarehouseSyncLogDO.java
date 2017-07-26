package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * Created by sam on 3/25/15.
 * Email: sambean@126.com
 */

public class SkuWarehouseSyncLogDO extends BaseDO {

    private static final long serialVersionUID = 7532511927119046670L;
    /**
     * skuId
     */
    private Long skuId;
    /**
     * 仓库的关联ID
     */
    private Long warehouseId;
    /**
     * 失败的话存储原因
     */
    private String reason;
    /**
     * 响应内容
     */
    private String response;
    /**
     * 成功标识
     */
    private Integer isSuccess;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }
}
