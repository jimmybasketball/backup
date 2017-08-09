package com.sfebiz.supplychain.provider.command.send.tpl;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;

/**
 * 
 * <p>第三方物流承运商，获取剩余运单号数量</p>
 * 
 * @author matt
 * @Date 2017年7月28日 下午5:37:50
 */
public abstract class TplOrderGetCountCommand extends AbstractCommand {

    /** 国内物流供应商信息 */
    protected LogisticsProviderBO logisticsProviderEntity;

    /** 最后一次申请到的运单号 */
    protected String              lastApplyMailNo;

    /** 剩余数量 */
    protected String              count;

    public LogisticsProviderBO getLogisticsProviderEntity() {
        return logisticsProviderEntity;
    }

    public void setLogisticsProviderEntity(LogisticsProviderBO logisticsProviderEntity) {
        this.logisticsProviderEntity = logisticsProviderEntity;
    }

    public String getLastApplyMailNo() {
        return lastApplyMailNo;
    }

    public void setLastApplyMailNo(String lastApplyMailNo) {
        this.lastApplyMailNo = lastApplyMailNo;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
