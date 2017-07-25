package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.code.StockInReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderState;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDetailDO;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import net.pocrd.entity.ServiceException;

import java.util.List;

/**
 * 下发商品入库指令
 * Created by zhangyajing on 2017/7/20.
 */
public abstract class WmsOrderSkuStockInCommand extends AbstractCommand {

    public static final HaitaoTraceLogger teaceLogger = HaitaoTraceLoggerFactory.getTraceLogger("stockinorder");
    private StockinOrderDO stockinOrderDO;
    private List<StockinOrderDetailDO> stockinOrderDetailDOs;
    private WarehouseDO warehouseDO;

    public boolean execute() throws ServiceException {
        // 1.检查入库单参数
        checkStateIsCanStockin();

        // 2.发送指令给仓库
        CommonRet<Void> commonRet = sendStockInCommandToWarehouse();
        if (commonRet.getRetCode().equals(SCReturnCode.COMMON_FAIL)) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_SENDSTOCK_FAIL,
                    "[物流平台-提交入库单给仓库失败]: " + commonRet.getRetMsg() + " "
                            + "[入库单ID: " + getStockinOrderDO().getId() + "]"
                            + "[调用URL:" + ""+ "]"
                            + "[调用KEY:" + ""+ "]"
            );
        }
        return true;
    }

    protected abstract CommonRet<Void> sendStockInCommandToWarehouse() throws ServiceException;
    /**
     * 检查参数信息，是否可下发入库
     *
     * @return
     */
    protected void checkStateIsCanStockin() throws ServiceException {
        if (null == getStockinOrderDO()) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_NOT_EXIST,
                    "[物流平台-提交入库单异常]: " + StockInReturnCode.STOCKIN_ORDER_NOT_EXIST.getDesc());
        }
        //只有待提交的入库单可提交入库
        if (!StockinOrderState.TO_BE_SUBMITED.getValue().equals(getStockinOrderDO().getState())) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_SUBMIT,
                    "[物流平台-提交入库单异常]: " + StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_SUBMIT.getDesc() + " "
                            + "[入库单ID: " + getStockinOrderDO().getId()
                            + ", 入库单状态: " + getStockinOrderDO().getState()
                            + ", 仓库回传时间: " + getStockinOrderDO().getWarehouseStockinTime()
                            + "]");
        }
    }

    public StockinOrderDO getStockinOrderDO() {
        return stockinOrderDO;
    }

    public void setStockinOrderDO(StockinOrderDO stockinOrderDO) {
        this.stockinOrderDO = stockinOrderDO;
    }

    public List<StockinOrderDetailDO> getStockinOrderDetailDOs() {
        return stockinOrderDetailDOs;
    }

    public void setStockinOrderDetailDOs(List<StockinOrderDetailDO> stockinOrderDetailDOs) {
        this.stockinOrderDetailDOs = stockinOrderDetailDOs;
    }

    public WarehouseDO getWarehouseDO() {
        return warehouseDO;
    }

    public void setWarehouseDO(WarehouseDO warehouseDO) {
        this.warehouseDO = warehouseDO;
    }
}
