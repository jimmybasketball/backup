package com.sfebiz.supplychain.provider.command.send.port;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.supplychain.exposed.common.enums.PortState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.protocol.common.DeclareType;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceDetailBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderRecordBO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订单推送
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017/7/25 10:19
 */
public abstract class PortOrderBillDeclareCommand extends AbstractCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    /**
     * 出库单业务对象
     */
    protected StockoutOrderBO stockoutOrderBO;

    /**
     * 支付申报企业Nid
     */
    protected String payBillDeclareProviderNid;


    /* 根据stockoutOrderBO整理出的数据结构，方便业务调用，不需要外部传入  */
    /**
     * 出库单商品对应申报明细信息
     */
    protected Map<Long, StockoutOrderDeclarePriceDetailBO> declarePriceDetailMap = new HashMap<Long, StockoutOrderDeclarePriceDetailBO>();

    /**
     * 出库单商品对应的备案信息
     */
    protected Map<Long, SkuDeclareBO> skuDeclareMap = new HashMap<Long, SkuDeclareBO>();

    /**
     * 线路业务实体
     */
    protected LogisticsLineBO lineBO;

    /**
     * 出库单下发轨迹记录信息 业务实体
     */
    protected StockoutOrderRecordBO recordBO;

    /**
     * 申报类型
     */
    protected DeclareType declareType = DeclareType.CREATE;

    public StockoutOrderBO getStockoutOrderBO() {
        return stockoutOrderBO;
    }

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;

        //整理出库单商品对应申报明细信息
        List<StockoutOrderDeclarePriceDetailBO> declarePriceDetailBOS = stockoutOrderBO.getDeclarePriceBO().getDeclarePriceDetailBOS();
        if (declarePriceDetailBOS != null && declarePriceDetailBOS.size() > 0) {
            for (StockoutOrderDeclarePriceDetailBO declarePriceDetailBO : declarePriceDetailBOS) {
                if (!declarePriceDetailMap.containsKey(declarePriceDetailBO.getSkuId())) {
                    declarePriceDetailMap.put(declarePriceDetailBO.getSkuId(), declarePriceDetailBO);
                }
            }
        }

        //整理出库单商品对应的备案信息
        List<StockoutOrderDetailBO> stockoutOrderDetailBOList = stockoutOrderBO.getDetailBOs();
        for (StockoutOrderDetailBO stockoutOrderDetailBO : stockoutOrderDetailBOList) {
            if (!skuDeclareMap.containsKey(stockoutOrderDetailBO.getSkuId())) {
                skuDeclareMap.put(stockoutOrderDetailBO.getSkuId(), stockoutOrderDetailBO.getSkuDeclareBO());
            }
        }

        this.lineBO = stockoutOrderBO.getLineBO();
        this.recordBO = stockoutOrderBO.getRecordBO();
    }

    public String getPayBillDeclareProviderNid() {
        return payBillDeclareProviderNid;
    }

    public void setPayBillDeclareProviderNid(String payBillDeclareProviderNid) {
        this.payBillDeclareProviderNid = payBillDeclareProviderNid;
    }

    public DeclareType getDeclareType() {
        return declareType;
    }

    public void setDeclareType(DeclareType declareType) {
        this.declareType = declareType;
    }

    /**
     * 模拟口岸申报成功
     *
     * @return
     */
    protected boolean mockPortStockoutCreateSuccess() {
        stockoutOrderBO.getRecordBO().setPortState(PortState.SUCCESS.getState());
        StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
        stockoutOrderRecordManager.updatePortState(stockoutOrderBO.getId(), PortState.SUCCESS.getState());
        traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO, "[供应链报文状态]下发出库单给口岸：口岸申报成功"));
        return true;
    }


}
