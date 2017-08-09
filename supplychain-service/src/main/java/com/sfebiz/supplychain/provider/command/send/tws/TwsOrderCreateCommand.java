package com.sfebiz.supplychain.provider.command.send.tws;

import java.util.List;
import java.util.Map;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;

public abstract class TwsOrderCreateCommand extends AbstractCommand {

    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                             .getTraceLogger("order");

    /**
     * 出库单对象
     */
    protected StockoutOrderBO                stockoutOrderBO;

    /**
     * 出库单关联的商品信息
     */
    protected List<StockoutOrderDetailBO>    stockoutOrderDetailBOs;

    /**
     * 出库单商品对应的备案信息
     */
    protected Map<Long, SkuDeclareBO>        skuDeclareBOMap;

    /**
     * 出库单关联的路线实体
     */
    protected LogisticsLineBO                lineBO;

    /**
     * 出库单金额相关数据
     */
    protected StockoutOrderDeclarePriceBO    stockoutOrderDeclarePriceBO;

    /**
     * 订单下发失败的原因
     */
    private String                           createFailureMessage;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setStockoutOrderDetailBOs(List<StockoutOrderDetailBO> stockoutOrderDetailBOs) {
        this.stockoutOrderDetailBOs = stockoutOrderDetailBOs;
    }

    public void setSkuDeclareBOMap(Map<Long, SkuDeclareBO> skuDeclareBOMap) {
        this.skuDeclareBOMap = skuDeclareBOMap;
    }

    public void setLineBO(LogisticsLineBO lineBO) {
        this.lineBO = lineBO;
    }

    public void setStockoutOrderDeclarePriceBO(StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO) {
        this.stockoutOrderDeclarePriceBO = stockoutOrderDeclarePriceBO;
    }

    public void setCreateFailureMessage(String createFailureMessage) {
        this.createFailureMessage = createFailureMessage;
    }

    public String getCreateFailureMessage() {
        return createFailureMessage;
    }

    /**
     * 模拟仓库下单成功
     *
     * @return
     */
    protected boolean mockTwsStockoutCreateSuccess() {
        logger.info("添加订单成功,orderId :" + stockoutOrderBO.getBizId());
        // TODO matt
        return true;
    }
}
