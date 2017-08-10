package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/1/26 下午12:02
 * <p/>
 * 仓库基础商品信息的查询接口
 */
public abstract class WmsOrderProductQueryCommand extends AbstractCommand {

    /**
     * 待查询的商品SKUID
     */
    private Long skuId;
    /**
     * 保存返回结果
     */
    private SkuEntity skuEntity;

    /**
     * 仓库供应商信息
     */
    private LogisticsProviderBO logisticsProviderBO;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public SkuEntity getSkuEntity() {
        return skuEntity;
    }

    public void setSkuEntity(SkuEntity skuEntity) {
        this.skuEntity = skuEntity;
    }

    public LogisticsProviderBO getLogisticsProviderBO() {
        return logisticsProviderBO;
    }

    public void setLogisticsProviderBO(LogisticsProviderBO logisticsProviderBO) {
        this.logisticsProviderBO = logisticsProviderBO;
    }
}
