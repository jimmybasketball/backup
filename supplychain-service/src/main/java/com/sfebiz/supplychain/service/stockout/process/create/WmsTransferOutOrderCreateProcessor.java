package com.sfebiz.supplychain.service.stockout.process.create;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.wms.WmsTransferOutOrderCreateCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * 仓库下发调拨出库单处理器
 * <p/>
 * 创建日期: 2015-10-08
 *
 * @author jackiehff
 */
@Component("wmsTransferOutOrderCreateProcessor")
public class WmsTransferOutOrderCreateProcessor extends StockoutProcessAction {

    public static final String   TAG    = "WMS_CREATE";

    private static final Logger  logger = LoggerFactory
                                            .getLogger(WmsTransferOutOrderCreateProcessor.class);

    @Resource
    private StockoutOrderManager stockoutOrderManager;

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {
        try {
            BaseResult result = new BaseResult();
            if (!checkoutRequestBaseInfo(request)) {
                return new BaseResult(Boolean.FALSE);
            }
            StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
            LogisticsLineBO lineEntity = stockoutOrderBO.getLineBO();
            if (lineEntity.getWarehouseBO().getLogisticsProviderBO() != null) {
                String msgType = WmsMessageType.TRANSFER_STOCK_OUT.getValue();
                ProviderCommand cmd = CommandFactory.createCommand(lineEntity.getWarehouseBO()
                    .getLogisticsProviderBO().getInterfaceType(), msgType);
                WmsTransferOutOrderCreateCommand wmsTransferOutOrderCreateCommand = (WmsTransferOutOrderCreateCommand) cmd;
                wmsTransferOutOrderCreateCommand.setStockoutOrderBO(stockoutOrderBO);
                wmsTransferOutOrderCreateCommand.setStockoutOrderDetailBOList(stockoutOrderBO
                    .getDetailBOs());
                wmsTransferOutOrderCreateCommand.setLineBO(lineEntity);
                boolean createResult = wmsTransferOutOrderCreateCommand.execute();
                if (createResult) {
                    result.setSuccess(Boolean.TRUE);
                } else {
                    request.setExceptionMessage(wmsTransferOutOrderCreateCommand
                        .getCreateFailureMessage());
                    result.setSuccess(Boolean.FALSE);
                }
            } else {
                result.setSuccess(Boolean.TRUE);
            }
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("执行WMS调拨出库单创建命令异常")
                .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
            request.setExceptionMessage("[供应链-下发调拨出库单]执行WMS调拨出库单创建命令异常，" + e.getMessage());
            return new BaseResult(Boolean.FALSE);
        }
    }

    @Override
    public String getProcessTag() {
        return TAG;
    }
}
