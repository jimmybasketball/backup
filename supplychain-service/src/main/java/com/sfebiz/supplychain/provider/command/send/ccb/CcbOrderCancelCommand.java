package com.sfebiz.supplychain.provider.command.send.ccb;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;

/**
 * 清关公司 订单取消
 */
public abstract class CcbOrderCancelCommand extends AbstractCommand {

    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                             .getTraceLogger("order");

    /**
     * 出库单对象
     */
    protected StockoutOrderBO                stockoutOrderBO;

    /**
     * 出库单关联的路线实体
     * 
     */
    protected LogisticsLineBO                lineBO;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setLineBO(LogisticsLineBO lineBO) {
        this.lineBO = lineBO;
    }

}
