package com.sfebiz.supplychain.provider.command.send.pay;

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
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceDetailBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>与支付企业对接支付单申报命令</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/26
 * Time: 下午3:07
 */
public abstract class PayOrderDeclareCommand extends AbstractCommand {

    /**
     * 出库单信息
     */
    protected StockoutOrderBO stockoutOrderBO;

    /**
     * 支付单申报企业信息编码
     */
    protected String payBillDeclareProviderNid;

    /* 根据stockoutOrderBO整理出的数据结构，方便业务调用，不需要外部传入  */
    /**
     * 出库单商品对应申报明细信息
     */
    protected Map<Long, StockoutOrderDeclarePriceDetailBO> declarePriceDetailMap = new HashMap<Long, StockoutOrderDeclarePriceDetailBO>();

    /**
     * 支付单申报记录
     */
    protected PortBillDeclareDO portBillDeclareDO;

    /**
     * 路线实体
     */
    protected LogisticsLineBO logisticsLineBO;

    /**
     * 出库单金额相关数据
     */
    protected StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO;

    /**
     * 出库单商品列表
     */
    protected List<StockoutOrderDetailBO> stockoutOrderDetailBOList;

    /**
     * 订单申报失败的原因
     */
    protected String createFailureMessage;

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

    public List<StockoutOrderDetailBO> getStockoutOrderDetailBOList() {
        return stockoutOrderDetailBOList;
    }

    public void setStockoutOrderDetailBOList(List<StockoutOrderDetailBO> stockoutOrderDetailBOList) {
        this.stockoutOrderDetailBOList = stockoutOrderDetailBOList;
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
        // 更新申报支付号
        stockoutOrderBO.setDeclarePayNo(stockoutOrderBO.getMerchantPayNo());
        StockoutOrderDO stockoutOrderDO = new StockoutOrderDO();
        BeanCopier beanCopier = BeanCopier.create(StockoutOrderBO.class, StockoutOrderDO.class, false);
        beanCopier.copy(stockoutOrderBO, stockoutOrderDO, null);
        stockoutOrderManager.update(stockoutOrderDO);
        portBillDeclareDO.setState(PortBillState.SEND_SUCCESS.getValue());
        portBillDeclareDO.setResult("处理成功");
        portBillDeclareDO.setBillSendTime(new Date());
        portBillDeclareManager.update(portBillDeclareDO);
        return true;
    }
}
