package com.sfebiz.supplychain.provider.command.send.ccb;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;

/**
 * 清关公司 订单查询
 */
public abstract class CcbOrderQueryCommand extends AbstractCommand {

    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                             .getTraceLogger("order");

    /**
     * 出库单对象
     */
    protected StockoutOrderDO                stockoutOrderDO;

    /**
     * 出库单关联的路线实体
     */
    protected LogisticsLineBO                lineBO;

    public void setStockoutOrderDO(StockoutOrderDO stockoutOrderDO) {
        this.stockoutOrderDO = stockoutOrderDO;
    }

    public void setLineBO(LogisticsLineBO lineBO) {
        this.lineBO = lineBO;
    }

}
