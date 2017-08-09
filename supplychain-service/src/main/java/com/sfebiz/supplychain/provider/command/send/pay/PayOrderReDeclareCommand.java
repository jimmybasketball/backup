package com.sfebiz.supplychain.provider.command.send.pay;

import java.util.Date;

import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;

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
    protected StockoutOrderBO             stockoutOrderBO;

    /**
     * 支付单申报企业信息编码
     */
    protected String                      payBillDeclareProviderNid;

    /**
     * 支付单申报记录
     */
    protected PortBillDeclareDO           portBillDeclareDO;

    /**
     * 路线实体
     */
    protected LogisticsLineBO             lineEntity;

    /**
     * 订单申报失败的原因
     */
    protected String                      createFailureMessage;

    /**
     * 出库单金额相关数据
     */
    protected StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceEntity;

    /**
     * 修改/重新申报
     */
    protected String                      actionType;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setLineEntity(LogisticsLineBO lineEntity) {
        this.lineEntity = lineEntity;
    }

    public void setStockoutOrderDeclarePriceEntity(StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceEntity) {
        this.stockoutOrderDeclarePriceEntity = stockoutOrderDeclarePriceEntity;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setPayBillDeclareProviderNid(String payBillDeclareProviderNid) {
        this.payBillDeclareProviderNid = payBillDeclareProviderNid;
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
     * 模拟支付单申报成功
     * @return
     */
    protected boolean mockPayBillDeclareSuccess() {
        StockoutOrderDO stockoutOrderDO = StockoutOrderConvert.convertBOToDO(stockoutOrderBO);
        stockoutOrderDO.setDeclarePayNo(stockoutOrderBO.getMerchantPayNo());
        StockoutOrderManager stockoutOrderManager = (StockoutOrderManager) CommandConfig
            .getSpringBean("stockoutOrderManager");
        PortBillDeclareManager portBillDeclareManager = (PortBillDeclareManager) CommandConfig
            .getSpringBean("portBillDeclareManager");
        stockoutOrderManager.update(stockoutOrderDO);
        portBillDeclareDO.setState(PortBillState.SEND_SUCCESS.getValue());
        portBillDeclareDO.setResult("处理成功");
        portBillDeclareDO.setBillSendTime(new Date());
        portBillDeclareManager.update(portBillDeclareDO);
        return true;
    }
}
