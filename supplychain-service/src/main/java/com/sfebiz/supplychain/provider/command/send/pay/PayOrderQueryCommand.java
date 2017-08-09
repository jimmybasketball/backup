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
    protected String          payBillDeclareProviderNid;

    /**
     * 路线实体
     */
    protected LogisticsLineBO lineEntity;

    /**
     * 支付单申报状态查询结果
     */
    private String            payBillState;

    /**
     * 支付单申报时的流水号
     */
    private String            payBillTradeNo;

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public void setLineEntity(LogisticsLineBO lineEntity) {
        this.lineEntity = lineEntity;
    }

    public void setPayBillDeclareProviderNid(String payBillDeclareProviderNid) {
        this.payBillDeclareProviderNid = payBillDeclareProviderNid;
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
