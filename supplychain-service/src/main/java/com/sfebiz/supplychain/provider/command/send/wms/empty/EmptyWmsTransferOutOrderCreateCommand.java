package com.sfebiz.supplychain.provider.command.send.wms.empty;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.provider.command.send.wms.WmsTransferOutOrderCreateCommand;
import net.pocrd.entity.ServiceException;

/**
 * Created by zhaojingyang on 2015/7/10.
 */
public class EmptyWmsTransferOutOrderCreateCommand extends WmsTransferOutOrderCreateCommand {

    @Override
    public boolean execute() throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).addParm("下发调拨出库单空实现,默认返回true", stockoutOrderBO.getId())
                .log();
        return true;
    }
}
