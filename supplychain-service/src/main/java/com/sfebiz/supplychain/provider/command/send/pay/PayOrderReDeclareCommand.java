package com.sfebiz.supplychain.provider.command.send.pay;

import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Date;

/**
 * <p>与支付企业对接支付单申报命令</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/26
 * Time: 下午3:07
 */
public abstract class PayOrderReDeclareCommand extends AbstractCommand {

    /**
     * 出库单信息
     */
    protected StockoutOrderBO stockoutOrderBO;

    /**
     * 支付单申报记录
     */
    protected PortBillDeclareDO portBillDeclareDO;

    /* 根据stockoutOrderBO整理出的数据结构，方便业务调用，不需要外部传入  */
    /**
     * 支付单申报企业信息编码
     */
    protected String payBillDeclareProviderNid;

    /**
     * 路线实体
     */
    protected LogisticsLineBO logisticsLineBO;

    /**
     * 出库单金额相关数据
     */
    protected StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO;

    /**
     * 订单申报失败的原因
     */
    protected String createFailureMessage;

    /**
     * 修改/重新申报
     */
    protected String actionType;

    public StockoutOrderBO getStockoutOrderBO() {
        return stockoutOrderBO;
    }

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public PortBillDeclareDO getPortBillDeclareDO() {
        return portBillDeclareDO;
    }

    public void setPortBillDeclareDO(PortBillDeclareDO portBillDeclareDO) {
        this.portBillDeclareDO = portBillDeclareDO;
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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getCreateFailureMessage() {
        return createFailureMessage;
    }

    public void setCreateFailureMessage(String createFailureMessage) {
        this.createFailureMessage = createFailureMessage;
    }

    /**
     * 模拟支付单申报成功
     * @return
     */
    protected boolean mockPayBillDeclareSuccess(){
        StockoutOrderManager stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
        PortBillDeclareManager portBillDeclareManager = (PortBillDeclareManager) CommandConfig.getSpringBean("portBillDeclareManager");
        stockoutOrderBO.setDeclarePayNo(stockoutOrderBO.getMerchantPayNo());
        StockoutOrderDO stockoutOrderDO = new StockoutOrderDO();
        BeanCopier beanCopier = BeanCopier.create(StockoutOrderBO.class, StockoutOrderDO.class, false);
        beanCopier.copy(stockoutOrderBO, stockoutOrderDO, null);
        stockoutOrderManager.update(stockoutOrderDO);
        portBillDeclareDO.setResult("处理成功");
        portBillDeclareDO.setBillSendTime(new Date());
        portBillDeclareManager.update(portBillDeclareDO);
        return true;
    }
}
