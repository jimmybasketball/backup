package com.sfebiz.supplychain.provider.command.send.wms.coe;

import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuSyncCommand;

/**
 * Created by sam on 3/11/15.
 * Email: sambean@126.com
 */

public class COESkuSyncCommand extends WmsOrderSkuSyncCommand {

    /**
     * COE 使用的是通用命令，所以采用通用父类中通用的商品同步方法
     * @return
     */
    @Override
    public boolean execute() {
        return super.execute();
    }
}
