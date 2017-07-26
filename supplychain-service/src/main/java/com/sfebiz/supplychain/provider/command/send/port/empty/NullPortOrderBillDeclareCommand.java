package com.sfebiz.supplychain.provider.command.send.port.empty;

import com.sfebiz.supplychain.exposed.common.enums.PortState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.port.PortOrderBillDeclareCommand;

public class NullPortOrderBillDeclareCommand extends PortOrderBillDeclareCommand {

    @Override
    public boolean execute() {
        stockoutOrderBO.getRecordBO().setPortState(PortState.SUCCESS.getState());
        StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
        int count = stockoutOrderRecordManager.updatePortState(stockoutOrderBO.getId(), PortState.SUCCESS.getState());
        return count > 0;
    }
}
