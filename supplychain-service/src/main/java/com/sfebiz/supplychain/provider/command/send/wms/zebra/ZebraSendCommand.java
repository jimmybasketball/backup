package com.sfebiz.supplychain.provider.command.send.wms.zebra;

import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSenderCommand;

/**
 * 下发发货指令:
 * 香港斑马仓取消了此接口，所以直接返回true
 */
public class ZebraSendCommand extends WmsOrderSenderCommand {

    private StockoutOrderRecordManager stockoutOrderRecordManager;

    @Override
    public boolean execute() {
        stockoutOrderBO.getRecordBO().setLogisticsState(LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue());
        stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
        stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue());
        return true;
    }

}
