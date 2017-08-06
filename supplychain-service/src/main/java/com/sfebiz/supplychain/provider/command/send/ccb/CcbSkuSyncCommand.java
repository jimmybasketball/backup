package com.sfebiz.supplychain.provider.command.send.ccb;

import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsOperaterType;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;

import java.util.List;

/**
 * 抽象与清关公司进行商品同步
 */
public abstract class CcbSkuSyncCommand extends AbstractCommand {

    /**
     * 商品基本信息
     */
    protected List<SkuEntity> skuEntities;

    /**
     * 同步失败的异常原因
     */
    protected String errorMessage;

    protected LogisticsProviderBO logisticsProviderBO;

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

    public WmsOperaterType getWmsOperaterType() {
        return wmsOperaterType;
    }

    public void setWmsOperaterType(WmsOperaterType wmsOperaterType) {
        this.wmsOperaterType = wmsOperaterType;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LogisticsProviderBO getLogisticsProviderBO() {
        return logisticsProviderBO;
    }

    public void setLogisticsProviderBO(LogisticsProviderBO logisticsProviderBO) {
        this.logisticsProviderBO = logisticsProviderBO;
    }
}
