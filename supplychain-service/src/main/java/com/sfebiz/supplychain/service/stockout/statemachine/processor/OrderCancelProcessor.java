package com.sfebiz.supplychain.service.stockout.statemachine.processor;

import java.util.Date;
import java.util.List;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.BillType;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.stock.entity.SkuStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.ccb.CcbOrderCancelCommand;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCancelCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderRecordBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>出库单取消处理器</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/16
 * Time: 下午4:23
 */
@Component("orderCancelProcessor")
public class OrderCancelProcessor extends TemplateProcessor implements
                                                           EngineProcessor<StockoutOrderRequest> {

    private static final Logger            logger      = LoggerFactory
                                                           .getLogger(OrderCancelProcessor.class);
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");

    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {
        //1. 初始化事务
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transactionStatus = transactionManager.getTransaction(definition);
        boolean transactionSuccess = false;
        BaseResult result = new BaseResult();
        try {
            if (request == null || request.getStockoutOrderBO() == null) {
                LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("参数异常").log();
                throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL);
            }

            StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
            // 检查并取消出库单, 向仓库/报关企业等下发取消指令
            if (checkAndCancelStockoutOrder(stockoutOrderBO)) {

                //2.1. 释放冻结库存
                if (stockoutOrderBO.getOrderType() == StockoutOrderType.SALES_STOCK_OUT.value) {
                    releaseSkuFreezeStocks(stockoutOrderBO);
                }

                //2.1. 如果有异常Task，则重置为已完成
                finishExceptionTask(stockoutOrderBO.getBizId());

                //2.2 更新状态表
                stockoutOrderStateLogManager.updateStockOutOrderStateLog(
                    StockoutOrderConvert.convertBOToDO(stockoutOrderBO),
                    StockoutOrderState.STOCKOUT_CANCEL.getValue(),
                    null == request.getProcessDateTime() ? new Date() : request
                        .getProcessDateTime(), SystemUserName.SYSTEM.getValue(), request
                        .getStateChangeRemark());

                //2.3. 标记事务执行成功
                transactionSuccess = true;
            } else {
                //3. 如果取消失败，则直接抛异常出去
                LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("订单取消失败")
                    .addParm("订单号", stockoutOrderBO.getBizId()).log();

                throw new ServiceException(LogisticsReturnCode.LOGISTICS_ORDER_CANT_CANCEL_ERR);
            }
            //出库单已出库时，作为仓库向商户发送状态信息
            stockoutOrderNoticeBizService.sendMsgStockoutFinish2Merchant(
                stockoutOrderBO.getBizId(), 0, "");

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(),
                TraceLog.TraceLevel.INFO, "发货单状态变更为[已取消]"));
            result.setSuccess(true);
            return result;
        } catch (ServiceException e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg(e.getMsg())
                .setException(e).addParm("订单号", request.getStockoutOrderBO().getBizId()).log();
            traceLogger.log(new TraceLog(request.getBizId(), "supplychain", new Date(),
                TraceLog.TraceLevel.ERROR, e.getMsg()));
            throw e;
        } finally {
            if (transactionSuccess) {
                transactionManager.commit(transactionStatus);
            } else {
                transactionManager.rollback(transactionStatus);
            }
        }
    }

    /**
     * 检查并下发取消出库命令给仓库
     *
     * @param stockoutOrderDO
     * @return
     * @throws ServiceException
     */
    public Boolean checkAndCancelStockoutOrder(StockoutOrderBO stockoutOrderBO)
                                                                               throws ServiceException {
        Boolean cancelResult = false;
        if (stockoutOrderBO.getOrderState().equals(StockoutOrderState.STOCKOUT_FINISH.toString())) {
            return false;
        }

        StockoutOrderRecordBO recordBO = stockoutOrderBO.getRecordBO();

        //海外仓状态为0、12 或者 出库单为待出库 均可以取消
        if (recordBO.getLogisticsState() == LogisticsState.LOGISTICS_STATE_INIT.getValue()
            || recordBO.getLogisticsState() == LogisticsState.LOGISTICS_STATE_WMS_CANCEL.getValue()
            || StockoutOrderState.WAIT_STOCKOUT.getValue().equalsIgnoreCase(
                stockoutOrderBO.getOrderState())) {
            recordBO.setLogisticsState(LogisticsState.LOGISTICS_STATE_CANCEL_SUCCESS.getValue());
            stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(),
                LogisticsState.LOGISTICS_STATE_CANCEL_SUCCESS.getValue());
            //如果存在支付单带申报记录，则置状态为已取消
            cancelPayBillRecordState(stockoutOrderBO.getId());
            cancelResult = true;
        } else {
            LogisticsLineBO lineEntity = stockoutOrderBO.getLineBO();

            //如果是虚拟仓，处于出库中的订单不允许取消
            //            if (WarehouseSplitType.VIRTUAL_WAREHOUSE.getType().equalsIgnoreCase(
            //                lineEntity.warehouseEntity.isVirtualWarehouse)) {
            //                throw new ServiceException(LogisticsReturnCode.LOGISTICS_ORDER_CANT_CANCEL_ERR,
            //                    "虚拟仓订单已经下发，不支持系统取消");
            //            }

            if (lineEntity != null && lineEntity.getWarehouseBO().getLogisticsProviderBO() != null) {
                try {
                    ProviderCommand cmd = CommandFactory.createCommand(lineEntity.getWarehouseBO()
                        .getLogisticsProviderBO().getInterfaceType(),
                        WmsMessageType.CANCEL_DELIVER.getValue());
                    WmsOrderCancelCommand cancelCommand = (WmsOrderCancelCommand) cmd;
                    cancelCommand.setStockoutOrderBO(stockoutOrderBO);
                    cancelCommand.setLogisticsLineBO(stockoutOrderBO.getLineBO());
                    cancelCommand.setStockoutOrderDetailBOList(stockoutOrderBO.getDetailBOs());
                    cancelResult = cancelCommand.execute();
                } catch (ServiceException e) {
                    LogBetter.instance(logger).setLevel(LogLevel.WARN)
                        .addParm("[供应链-wms]供应链调用仓库取消订单异常,订单ID", stockoutOrderBO.getBizId())
                        .setException(e).log();
                    return false;
                }
            }

            if (cancelResult && lineEntity.getPortBO() != null
                && lineEntity.getClearanceLogisticsProviderBO() != null) {
                if (lineEntity.getClearanceLogisticsProviderBO().getLogisticsProviderNid()
                    .startsWith("bsp")) {
                    //如果清关供应商为BSP，则直接跳过，海淘未与BSP的关务做系统对接
                    return cancelResult;
                } else if (StringUtils.isBlank(lineEntity.getClearanceLogisticsProviderBO()
                    .getInterfaceType())
                           || "-1".equals(lineEntity.getClearanceLogisticsProviderBO()
                               .getInterfaceType())) {
                    return cancelResult;
                } else {
                    try {
                        ProviderCommand cmd = CommandFactory.createCommand(lineEntity
                            .getClearanceLogisticsProviderBO().getInterfaceType(),
                            WmsMessageType.CCB_ORDER_CANCEL.getValue());
                        CcbOrderCancelCommand cancelCommand = (CcbOrderCancelCommand) cmd;
                        cancelCommand.setStockoutOrderBO(stockoutOrderBO);
                        cancelCommand.setLineBO(stockoutOrderBO.getLineBO());
                        cancelResult = cancelCommand.execute();
                    } catch (ServiceException e) {
                        LogBetter.instance(logger).setLevel(LogLevel.WARN)
                            .addParm("[供应链-wms]供应链调用清关企业的取消订单异常,订单ID", stockoutOrderBO.getBizId())
                            .setException(e).log();
                        return false;
                    }
                }
            }
        }
        return cancelResult;
    }

    /**
     * 释放冻结库存
     * 如果为组合商品，释放物料库存
     *
     * @param stockoutOrderDO
     * @return
     * @throws ServiceException
     */
    protected Boolean releaseSkuFreezeStocks(StockoutOrderBO stockoutOrderBO)
                                                                             throws ServiceException {
        return stockService.releaseSkuStockInBatch(getReleaseOperaterList(stockoutOrderBO),
            stockoutOrderBO.getWarehouseId(), stockoutOrderBO.getId());
    }

    private List<SkuStockOperaterEntity> getReleaseOperaterList(StockoutOrderBO stockoutOrderBO) {
        // TODO matt 构造方法
        return null;
    }

    /**
     * 查询支付申报单，如果存在，则置为取消状态
     *
     * @param stockoutId
     */
    private void cancelPayBillRecordState(Long stockoutId) {
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutId);
        portBillDeclareDO.setBillType(BillType.PAY_BILL.getType());
        List<PortBillDeclareDO> portBillDeclareDOs = portBillDeclareManager.query(BaseQuery
            .getInstance(portBillDeclareDO));
        if (portBillDeclareDOs.size() > 0) {
            portBillDeclareDO = portBillDeclareDOs.get(0);
            portBillDeclareDO.setState(PortBillState.CANCEL.getValue());
            portBillDeclareManager.update(portBillDeclareDO);
        }
    }
}
