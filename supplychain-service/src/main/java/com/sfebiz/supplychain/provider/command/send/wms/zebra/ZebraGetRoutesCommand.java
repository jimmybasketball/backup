package com.sfebiz.supplychain.provider.command.send.wms.zebra;

import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderGetRoutesCommand;
import net.pocrd.entity.ServiceException;

/**
 * 获取路由信息
 */
public class ZebraGetRoutesCommand extends WmsOrderGetRoutesCommand {

    @Override
    public boolean execute() throws ServiceException {
        return false;
    }
}
