package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.exposed.stockout.enums.TplIntrState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class WmsOrderCreateCommand extends AbstractCommand {

    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    /**
     * 出库单对象
     */
    protected StockoutOrderBO stockoutOrderBO;

    /* 根据stockoutOrderBO整理出的数据结构，方便业务调用，不需要外部传入  */
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

    /**
     * 订单下发失败的原因
     */
    private String createFailureMessage;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setStockoutOrderDetailBOs(List<StockoutOrderDetailBO> stockoutOrderDetailBOs) {
        this.stockoutOrderDetailBOs = stockoutOrderDetailBOs;
    }

    public void setLogisticsLineBO(LogisticsLineBO logisticsLineBO) {
        this.logisticsLineBO = logisticsLineBO;
    }

    public void setSkuDeclareBOMap(Map<Long, SkuDeclareBO> skuDeclareBOMap) {
        this.skuDeclareBOMap = skuDeclareBOMap;
    }

//    public boolean isWarehouseSupportCombination() {
//        WarehouseBO warehouseBO = logisticsLineBO.getWarehouseBO();
//        return (warehouseBO != null && warehouseBO.isSupportCombination == 1);
//    }

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
    protected boolean mockWarehouseStockoutCreateSuccess() {
        logger.info("添加订单成功,orderId :" + stockoutOrderBO.getBizId());
        StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
        if (StringUtils.isBlank(stockoutOrderBO.getIntrMailNo())) {
            stockoutOrderRecordManager.updateTplIntrState(
                    stockoutOrderBO.getId(),
                    "mock mailNo",
                    "mock returnTrackingNo",
                    "mock origincode",
                    "mock destcode",
                    TplIntrState.UNUSING.getValue());
        }
        stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
        traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO, "[供应链报文状态]下发出库单给仓库：物流下单成功"));
        return true;
    }
}
