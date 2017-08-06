package com.sfebiz.supplychain.provider.command.send.wms.empty;

import com.sfebiz.supplychain.provider.command.send.CommandResponse;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuStockInCommand;
import net.pocrd.entity.ServiceException;

/**
 * <p>通知仓库SKU入库</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/22
 * Time: 上午10:09
 */
public class EmptyWmsSkuStockInCommand extends WmsOrderSkuStockInCommand {

    @Override
    protected CommandResponse sendStockInCommandToWarehouse() throws ServiceException {
        return CommandResponse.SUCCESS_RESPONSE;
    }

}
