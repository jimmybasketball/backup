package com.sfebiz.supplychain.provider.command.send.tpl;

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

/**
 * 物流企业 下发订单
 * TPL (Third Part Logistics) 三方物流
 */
public abstract class TplOrderCreateCommand extends AbstractCommand {

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
     * 支付单申报企业的承运商
     */
    protected String                         payDeclareProviderNid;

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

    public void setPayDeclareProviderNid(String payDeclareProviderNid) {
        this.payDeclareProviderNid = payDeclareProviderNid;
    }

    public void setStockoutOrderDeclarePriceBO(StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO) {
        this.stockoutOrderDeclarePriceBO = stockoutOrderDeclarePriceBO;
    }

    public String getCreateFailureMessage() {
        return createFailureMessage;
    }

    public void setCreateFailureMessage(String createFailureMessage) {
        this.createFailureMessage = createFailureMessage;
    }

    /**
     * Mock Tpl 订单创建
     * @return
     */
    protected void mockTplStockoutCreateSuccess() {

        logger.info("[MOCK]BSP 物流下单 采用MOCK实现，订单id：" + stockoutOrderBO.getBizId());

        // TODO matt
    }
}
