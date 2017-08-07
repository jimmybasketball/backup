package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;

import java.util.List;


public abstract class WmsOrderCancelCommand extends AbstractCommand {

    protected StockoutOrderBO stockoutOrderBO;

    protected List<StockoutOrderDetailBO> stockoutOrderDetailBOList;

    protected LogisticsLineBO logisticsLineBO;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setStockoutOrderDetailBOList(List<StockoutOrderDetailBO> stockoutOrderDetailBOList) {
        this.stockoutOrderDetailBOList = stockoutOrderDetailBOList;
    }

    public void setLogisticsLineBO(LogisticsLineBO logisticsLineBO) {
        this.logisticsLineBO = logisticsLineBO;
    }
}
