package com.sfebiz.supplychain.provider.command.send.wms.nbbs.xhtd;

import com.sfebiz.supplychain.provider.command.send.CommandResponse;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuStockInCommand;
import net.pocrd.entity.ServiceException;

/**
 * Description: 用于入库指令创建
 * Created by yanghua on 2017/3/21.
 */
public class NBBSXHTDOrderStockInCommand extends WmsOrderSkuStockInCommand {

    @Override
    protected CommandResponse sendStockInCommandToWarehouse() throws ServiceException {
        return CommandResponse.SUCCESS_RESPONSE;
    }
}
