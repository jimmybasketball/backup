package com.sfebiz.supplychain.provider.command.send.tpl;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;

/**
 * 
 * <p>第三方物流承运商，给物流企业下发命名</p>
 * 
 * @author matt
 * @Date 2017年7月28日 下午5:47:35
 */
public abstract class TplOrderSenderCommand extends AbstractCommand {

    /**
     * 出库单对象
     */
    protected StockoutOrderBO stockoutOrderBO;

    /**
     * 出库单关联的路线实体
     */
    protected LogisticsLineBO lineBO;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setLineBO(LogisticsLineBO lineBO) {
        this.lineBO = lineBO;
    }

}
