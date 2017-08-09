package com.sfebiz.supplychain.provider.command.send.ccb;

import java.util.List;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;

/**
 * 清关公司 订单确认
 */
public abstract class CcbOrderConfirmCommand extends AbstractCommand {

    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                             .getTraceLogger("order");

    /**
     * 出库单对象
     */
    protected StockoutOrderBO                stockoutOrderBO;

    /**
     * 出库单关联的路线实体
     */
    protected LogisticsLineBO                lineBO;

    /**
     * 个人物品申报单申报记录
     */
    protected PortBillDeclareDO              portBillDeclareDO;

    /**
     * 出库单关联的商品信息
     */
    protected List<StockoutOrderDetailBO>    stockoutOrderDetailBOs;

    /**
     * 出库单金额相关数据
     */
    protected StockoutOrderDeclarePriceBO    stockoutOrderDeclarePriceBO;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setLineBO(LogisticsLineBO lineBO) {
        this.lineBO = lineBO;
    }

    public void setPortBillDeclareDO(PortBillDeclareDO portBillDeclareDO) {
        this.portBillDeclareDO = portBillDeclareDO;
    }

    public void setStockoutOrderDetailBOs(List<StockoutOrderDetailBO> stockoutOrderDetailBOs) {
        this.stockoutOrderDetailBOs = stockoutOrderDetailBOs;
    }

    public void setStockoutOrderDeclarePriceBO(StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO) {
        this.stockoutOrderDeclarePriceBO = stockoutOrderDeclarePriceBO;
    }

}
