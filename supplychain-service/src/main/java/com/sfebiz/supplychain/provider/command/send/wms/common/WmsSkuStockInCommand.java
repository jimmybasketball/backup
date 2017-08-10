package com.sfebiz.supplychain.provider.command.send.wms.common;

import com.sfebiz.supplychain.provider.command.send.CommandResponse;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuStockInCommand;
import net.pocrd.entity.ServiceException;

/**
 * 场景描述 通知仓库SKU入库 服务名称 logistics.event.wms.skustockin 消息类型
 */
public class WmsSkuStockInCommand extends WmsOrderSkuStockInCommand {


    @Override
    protected CommandResponse sendStockInCommandToWarehouse() throws ServiceException {
        return new CommandResponse();
    }
}
