package com.sfebiz.supplychain.provider.command.send.wms.common;

import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSenderCommand;

/**
 * 7.6. 下发发货指令 logistics.event.wms.send SF -> LP
 *
 * @author xiaoxu LOGISTICS_SEND_GOODS
 */
public class WmsSendCommand extends WmsOrderSenderCommand {
    private StockoutOrderRecordManager stockoutOrderRecordManager;

    @Override
    public boolean execute() {
        logger.info("通用仓库跳过下发命令，修改订单状态后直接返回true");
        stockoutOrderBO.getRecordBO().setLogisticsState(LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue());
        stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
        stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue());
        return true;
    }
}
