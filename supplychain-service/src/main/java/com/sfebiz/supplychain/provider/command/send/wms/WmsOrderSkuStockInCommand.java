package com.sfebiz.supplychain.provider.command.send.wms;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.exposed.common.code.StockInReturnCode;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderState;
import com.sfebiz.supplychain.provider.command.AbstractCommand;
import com.sfebiz.supplychain.provider.command.send.CommandResponse;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderBO;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderDetailBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import net.pocrd.entity.ServiceException;

import java.util.List;

/**
 * 下发商品入库指令
 * Created by zhangyajing on 2017/7/20.
 */
public abstract class WmsOrderSkuStockInCommand extends AbstractCommand {

    protected static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("stockinorder");
    protected StockinOrderBO stockinOrderBO;
    protected List<StockinOrderDetailBO> stockinOrderDetailBOs;
    protected WarehouseBO warehouseBO;

    public boolean execute() throws ServiceException {
        // 1.检查入库单参数
        checkStateIsCanStockin();

        // 2.发送指令给仓库
        CommandResponse commandResponse = sendStockInCommandToWarehouse();
        if (!commandResponse.isSuccess()) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_SENDSTOCK_FAIL,
                    "[物流平台-提交入库单给仓库失败]: " + commandResponse.getErrorMessage() + " "
                            + "[入库单ID: " + getStockinOrderBO().getId() + "]"
                            + "[调用URL:" + ""+ "]"
                            + "[调用KEY:" + ""+ "]"
            );
        }
        return true;
    }

    protected abstract CommandResponse sendStockInCommandToWarehouse() throws ServiceException;
    /**
     * 检查参数信息，是否可下发入库
     *
     * @return
     */
    protected void checkStateIsCanStockin() throws ServiceException {
        if (null == getStockinOrderBO()) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_NOT_EXIST,
                    "[物流平台-提交入库单异常]: " + StockInReturnCode.STOCKIN_ORDER_NOT_EXIST.getDesc());
        }
        //只有待提交的入库单可提交入库
        if (!StockinOrderState.TO_BE_SUBMITED.getValue().equals(getStockinOrderBO().getState())) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_SUBMIT,
                    "[物流平台-提交入库单异常]: " + StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_SUBMIT.getDesc() + " "
                            + "[入库单ID: " + getStockinOrderBO().getId()
                            + ", 入库单状态: " + getStockinOrderBO().getState()
                            + ", 仓库回传时间: " + getStockinOrderBO().getWarehouseStockinTime()
                            + "]");
        }
    }

    public StockinOrderBO getStockinOrderBO() {
        return stockinOrderBO;
    }

    public void setStockinOrderBO(StockinOrderBO stockinOrderBO) {
        this.stockinOrderBO = stockinOrderBO;
    }

    public List<StockinOrderDetailBO> getStockinOrderDetailBOs() {
        return stockinOrderDetailBOs;
    }

    public void setStockinOrderDetailBOs(List<StockinOrderDetailBO> stockinOrderDetailBOs) {
        this.stockinOrderDetailBOs = stockinOrderDetailBOs;
    }

    public WarehouseBO getWarehouseBO() {
        return warehouseBO;
    }

    public void setWarehouseBO(WarehouseBO warehouseBO) {
        this.warehouseBO = warehouseBO;
    }
}
