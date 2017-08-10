package com.sfebiz.supplychain.provider.command.send.pay;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;

/**
 * <p>与支付企业对接支付单申报状态查询命令</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/26
 * Time: 下午3:07
 */
public abstract class PayOrderQueryCommand extends AbstractCommand {

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
     * 路线实体
     */
    protected LogisticsLineBO logisticsLineBO;

    /**
     * 支付单申报状态查询结果
     */
    private String payBillState;

    /**
     * 支付单申报时的流水号
     */
    private String payBillTradeNo;

    public StockoutOrderBO getStockoutOrderBO() {
        return stockoutOrderBO;
    }

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public LogisticsLineBO getLogisticsLineBO() {
        return logisticsLineBO;
    }

    public void setLogisticsLineBO(LogisticsLineBO logisticsLineBO) {
        this.logisticsLineBO = logisticsLineBO;
    }

    public String getPayBillState() {
        return payBillState;
    }

    public void setPayBillState(String payBillState) {
        this.payBillState = payBillState;
    }

    public String getPayBillTradeNo() {
        return payBillTradeNo;
    }

    public void setPayBillTradeNo(String payBillTradeNo) {
        this.payBillTradeNo = payBillTradeNo;
    }
}
