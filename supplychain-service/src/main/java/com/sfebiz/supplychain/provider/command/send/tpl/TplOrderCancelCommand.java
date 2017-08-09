package com.sfebiz.supplychain.provider.command.send.tpl;

import java.util.List;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;

/**
 * 
 * <p>国内物流承运商，物流订单取消命令</p>
 * @author matt
 * @Date 2017年7月28日 下午5:23:55
 */
public abstract class TplOrderCancelCommand extends AbstractCommand {

    protected StockoutOrderBO             stockoutOrderBO;

    protected List<StockoutOrderDetailBO> stockoutOrderDetailBOList;

    protected LogisticsLineBO             lineBO;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setStockoutOrderDetailBOList(List<StockoutOrderDetailBO> stockoutOrderDetailBOList) {
        this.stockoutOrderDetailBOList = stockoutOrderDetailBOList;
    }

    public void setLineBO(LogisticsLineBO lineBO) {
        this.lineBO = lineBO;
    }

}
