package com.sfebiz.supplychain.provider.command.send.ccb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;

/**
 * 
 * <p>清关公司下单命令</p>
 * 
 * @author matt
 * @Date 2017年7月30日 下午9:40:01
 */
public abstract class CcbOrderCreateCommand extends AbstractCommand {

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
     * 个人物品申报单申报记录
     */
    protected PortBillDeclareDO              portBillDeclareDO;

    /**
     * 订单下发失败的原因
     */
    protected String                         createFailureMessage;

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

    public void setPortBillDeclareDO(PortBillDeclareDO portBillDeclareDO) {
        this.portBillDeclareDO = portBillDeclareDO;
    }

    public String getCreateFailureMessage() {
        return createFailureMessage;
    }

    public void setCreateFailureMessage(String createFailureMessage) {
        this.createFailureMessage = createFailureMessage;
    }

    /**
     * Mock 清关供应商 订单创建
     * @return
     */
    protected boolean mockCcbStockoutCreateSuccess() {
        PortBillDeclareManager portBillDeclareManager = (PortBillDeclareManager) CommandConfig
            .getSpringBean("portBillDeclareManager");
        portBillDeclareDO.setResult("Mock Sucess");
        portBillDeclareDO.setBillSendTime(new Date());
        portBillDeclareDO.setState(PortBillState.SEND_SUCCESS.getValue());
        portBillDeclareManager.update(portBillDeclareDO);
        return true;
    }
}
