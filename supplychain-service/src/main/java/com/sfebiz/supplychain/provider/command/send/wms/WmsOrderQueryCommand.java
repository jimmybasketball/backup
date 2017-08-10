package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.statemachine.EngineService;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;

import java.util.List;
import java.util.Map;

/**
 * Description: 用于查询宁波保税(鑫海通达)   EDI查询订单接口
 * Created by yanghua on 2017/3/21.
 */
public abstract class WmsOrderQueryCommand extends AbstractCommand {
    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    /**
     * 出库单对象
     */
    protected StockoutOrderBO stockoutOrderBO;

    /**
     * 出库单关联的商品信息
     */
    protected List<StockoutOrderDetailBO> stockoutOrderDetailBOs;

    /**
     * 出库单商品对应的备案信息
     */
    protected Map<Long, SkuDeclareBO> skuDeclareBOMap;

    /**
     * 出库单关联的路线实体
     */
    protected LogisticsLineBO logisticsLineBO;

    /**
     * 出库单金额相关数据
     */
    protected StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO;

    protected EngineService engineService;

    /**
     *  物流提供商实体
     */
    protected LogisticsProviderBO logisticsProviderBO;

//    public static HaitaoTraceLogger getTraceLogger() {
//        return traceLogger;
//    }

    public StockoutOrderBO getStockoutOrderBO() {
        return stockoutOrderBO;
    }

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public List<StockoutOrderDetailBO> getStockoutOrderDetailBOs() {
        return stockoutOrderDetailBOs;
    }

    public void setStockoutOrderDetailBOs(List<StockoutOrderDetailBO> stockoutOrderDetailBOs) {
        this.stockoutOrderDetailBOs = stockoutOrderDetailBOs;
    }

    public Map<Long, SkuDeclareBO> getSkuDeclareBOMap() {
        return skuDeclareBOMap;
    }

    public void setSkuDeclareBOMap(Map<Long, SkuDeclareBO> skuDeclareBOMap) {
        this.skuDeclareBOMap = skuDeclareBOMap;
    }

    public LogisticsLineBO getLogisticsLineBO() {
        return logisticsLineBO;
    }

    public void setLogisticsLineBO(LogisticsLineBO logisticsLineBO) {
        this.logisticsLineBO = logisticsLineBO;
    }

    public StockoutOrderDeclarePriceBO getStockoutOrderDeclarePriceBO() {
        return stockoutOrderDeclarePriceBO;
    }

    public void setStockoutOrderDeclarePriceBO(StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO) {
        this.stockoutOrderDeclarePriceBO = stockoutOrderDeclarePriceBO;
    }


    public LogisticsProviderBO getLogisticsProviderBO() {
        return logisticsProviderBO;
    }

    public void setLogisticsProviderBO(LogisticsProviderBO logisticsProviderBO) {
        this.logisticsProviderBO = logisticsProviderBO;
    }

    public EngineService getEngineService() {
        return engineService;
    }

    public void setEngineService(EngineService engineService) {
        this.engineService = engineService;
    }
}
