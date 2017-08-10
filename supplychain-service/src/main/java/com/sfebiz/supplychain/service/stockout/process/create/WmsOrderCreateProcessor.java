package com.sfebiz.supplychain.service.stockout.process.create;

import java.util.List;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCreateCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>WMS订单创建</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/20
 * Time: 下午3:31
 */
@Component("wmsOrderCreateProcessor")
public class WmsOrderCreateProcessor extends StockoutProcessAction {

    public static final String   TAG    = "WMS_CREATE";
    private static final Logger  logger = LoggerFactory.getLogger(WmsOrderCreateProcessor.class);
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
            LogisticsLineBO lineBO = stockoutOrderBO.getLineBO();
            List<StockoutOrderDetailBO> detailBOs = stockoutOrderBO.getDetailBOs();

            LogBetter.instance(logger).setLevel(LogLevel.INFO).addParm("路线id", lineBO.getId())
                .addParm("WMS实体", lineBO.getWarehouseBO())
                .addParm("TPL实体", lineBO.getDomesticLogisticsProviderBO()).log();
            if (lineBO.getWarehouseBO().getLogisticsProviderBO() != null) {

                String msgType = WmsMessageType.STOCK_OUT.getValue();
                if (StockoutOrderType.ALLOCATION_STOCK_OUT.getValue() == stockoutOrderBO
                    .getOrderType()) {
                    msgType = WmsMessageType.TRANSFER_STOCK_OUT.getValue();
                }

                logger.info("命令集合："
                            + lineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceType()
                            + ",消息类型：" + msgType);
                ProviderCommand cmd = CommandFactory.createCommand(lineBO.getWarehouseBO()
                    .getLogisticsProviderBO().getInterfaceType(), msgType);
                WmsOrderCreateCommand wmsOrderCreateCommand = (WmsOrderCreateCommand) cmd;
                wmsOrderCreateCommand.setStockoutOrderDeclarePriceBO(stockoutOrderBO
                    .getDeclarePriceBO());
                wmsOrderCreateCommand.setStockoutOrderBO(stockoutOrderBO);
                wmsOrderCreateCommand.setStockoutOrderDetailBOs(detailBOs);
                wmsOrderCreateCommand.setLogisticsLineBO(lineBO);
                wmsOrderCreateCommand.setSkuDeclareBOMap(request.getProductDeclareEntityMap());
                boolean createResult = wmsOrderCreateCommand.execute();
                if (createResult) {
                    result.setSuccess(Boolean.TRUE);
                } else {
                    request.setExceptionMessage(wmsOrderCreateCommand.getCreateFailureMessage());
                    setServiceException(wmsOrderCreateCommand.getCreateFailureMessage(), request);
                    result.setSuccess(Boolean.FALSE);
                }

            } else {
                logger.info("命令集合：" + lineBO);
                result.setSuccess(Boolean.TRUE);
            }
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("执行WMS订单创建命令异常")
                .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
            request.setExceptionMessage("[供应链-下发出库单]执行WMS订单创建命令异常，" + e.getMessage());
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.STOCKOUT_ORDER_WMS_CREATE_FAILURE,
                LogisticsReturnCode.STOCKOUT_ORDER_WMS_CREATE_FAILURE.getDesc()));
            return new BaseResult(Boolean.FALSE);
        }
    }

    @Override
    public String getProcessTag() {
        return TAG;
    }

    private void setServiceException(String errorMsg, StockoutOrderRequest request) {
        if (StringUtils.isNotEmpty(errorMsg)) {
            if (errorMsg.contains("商品外文名称不能为空")) {
                request.setServiceException(new ServiceException(
                    LogisticsReturnCode.SKU_FOREIGN_NAME_NOT_NULL,
                    LogisticsReturnCode.SKU_FOREIGN_NAME_NOT_NULL.getDesc()));
            } else if (errorMsg.contains("商品批次库存数量不够")) {
                request.setServiceException(new ServiceException(
                    LogisticsReturnCode.WH_BATCH_STOCK_NOT_ENOUGH,
                    LogisticsReturnCode.WH_BATCH_STOCK_NOT_ENOUGH.getDesc()));
            }
        } else {
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.STOCKOUT_ORDER_WMS_CREATE_FAILURE,
                LogisticsReturnCode.STOCKOUT_ORDER_WMS_CREATE_FAILURE.getDesc()));
        }
    }

}
