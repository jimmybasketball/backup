package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;

public abstract class WmsOrderUpdateCommand extends AbstractCommand {

    /**
     * 出库单信息
     */
    protected StockoutOrderBO stockoutOrderBO;

    /**
    /**
     * 出库单关联的路线、供应商信息
     */
    protected LogisticsLineBO logisticsLineBO;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setLogisticsLineBO(LogisticsLineBO logisticsLineBO) {
        this.logisticsLineBO = logisticsLineBO;
    }

}