package com.sfebiz.supplychain.provider.command.send.port;

import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.service.port.model.LogisticsPortBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;

/**
 * <p>口岸商品备案命令</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/12
 * Time: 下午2:00
 */
public abstract class PortProductDeclareCommand extends AbstractCommand {

    //待备案商品
    protected SkuDeclareBO skuDeclareBO;
    //需要备案的口岸
    protected LogisticsPortBO portBO;

    public SkuDeclareBO getSkuDeclareBO() {
        return skuDeclareBO;
    }

    public void setSkuDeclareBO(SkuDeclareBO skuDeclareBO) {
        this.skuDeclareBO = skuDeclareBO;
    }

    public LogisticsPortBO getPortBO() {
        return portBO;
    }

    public void setPortBO(LogisticsPortBO portBO) {
        this.portBO = portBO;
    }
}
