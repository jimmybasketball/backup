package com.sfebiz.supplychain.service.stockout.statemachine.processor;

import java.util.Date;
import java.util.List;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.alarm.EffectiveAlarmConfig;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.exposed.stock.entity.SkuStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>仓库出库处理器</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/17
 * Time: 上午11:20
 */
@Component("orderSendProcessor")
public class OrderSendProcessor extends TemplateProcessor implements
                                                         EngineProcessor<StockoutOrderRequest> {

    private static final Logger            logger      = LoggerFactory
                                                           .getLogger(OrderSendProcessor.class);
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");

    /**
     * 处理仓库已出库逻辑
     *
     * @param request 出库单请求信息,stockoutOrderDO对象为必填参数
     * @return result  处理结果
     * @throws ServiceException
     */
    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {
        try {
            BaseResult result = new BaseResult();
            StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
            if (stockoutOrderBO == null) {
                throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL,
                    "出库单对象为null");
            }

            Date eventTime = null == request.getProcessDateTime() ? new Date() : request
                .getProcessDateTime();

            List<StockoutOrderDetailBO> stockoutOrderDetailBOs = stockoutOrderBO.getDetailBOs();

            // 只有出库类型为销售出库单的时候才消费库存 给订单发消息更新状态为已出库
            if (stockoutOrderBO.getOrderType() == StockoutOrderType.SALES_STOCK_OUT.value) {
                // 消费出库单的所有冻结库存
                consumeSkuFreezeStocks(stockoutOrderBO);
                LogBetter
                    .instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                            SystemConstants.TRACE_APP))
                    .addParm("[供应链-订单出库]消费冻结库存成功", stockoutOrderBO.getBizId()).log();

                //获取下一节点的预警时间
                LogisticsLineBO lineEntity = stockoutOrderBO.getLineBO();
                Date alarmTime = EffectiveAlarmConfig.getAlarmDate(lineEntity.getId(), eventTime,
                    StockoutOrderState.STOCKOUT_FINISH.getValue());

                //                if (lineEntity != null && lineEntity.engineTag != null) {
                //                    Integer engineTag = lineEntity.engineTag;
                //                    if (2 == engineTag || 4 == engineTag) {
                //                        alarmTime = effectiveAlarmBizService.getAlarmDate(lineEntity.lineId,
                //                            new Date(), StockoutOrderState.OVERSEA_STOCKIN_FINISH.getValue());
                //                    } else if (3 == engineTag) {
                //                        alarmTime = effectiveAlarmBizService.getAlarmDate(lineEntity.lineId,
                //                            new Date(), StockoutOrderState.CLEARANCE_PICKING.getValue());
                //                    }
                //                }
                // 更新状态信息
                stockoutOrderStateLogManager.updateStockOutOrderStateLog(
                    StockoutOrderConvert.convertBOToDO(stockoutOrderBO),
                    StockoutOrderState.STOCKOUT_FINISH.getValue(), eventTime, alarmTime,
                    SystemUserName.SYSTEM.getValue(), request.getStateChangeRemark());

                stockoutOrderBO.setOrderState(StockoutOrderState.STOCKOUT_FINISH.getValue());
                //                if (stockoutOrderDO.getWarehouseStockoutTime() == null) {
                //                    stockoutOrderDO.setWarehouseStockoutTime(new Date());
                //                }

                stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(),
                    LogisticsState.LOGISTICS_STATE_STOCKOUT.getValue());

                // LSCM系统的路由注册
                //                if (request.getLineEntity() != null
                //                    && request.getLineEntity().wmsProviderEntity != null
                //                    && request.getLineEntity().wmsProviderEntity.logisticsProviderNid != null
                //                    && request.getLineEntity().wmsProviderEntity.logisticsProviderNid
                //                        .startsWith("lscm") && request.getLineEntity().carrierCode.equals("SF")) {
                //                    stockoutBizService.sendBspRegistMessage(request.getStockoutOrderDO(), 60);
                //                }

                // 先判断该路线是否需要注册,再根据国内、国际运单号分别去注册
                registKd100ForOrderRoutes(stockoutOrderBO);

                stockoutOrderDeclarePriceManager.setOrderDeclarePriceIsPayTax(stockoutOrderBO
                    .getId());

                //出库单已出库时，作为仓库向商户发送状态信息
                stockoutOrderNoticeBizService.sendMsgStockoutFinish2Merchant(
                    stockoutOrderBO.getBizId(), 0, "");

            } else {
                // 更新状态信息,不进行状态预警
                stockoutOrderStateLogManager.updateStockOutOrderStateLog(
                    StockoutOrderConvert.convertBOToDO(stockoutOrderBO),
                    StockoutOrderState.STOCKOUT_FINISH.getValue(), eventTime,
                    SystemUserName.SYSTEM.getValue());
                stockoutOrderBO.setOrderState(StockoutOrderState.STOCKOUT_FINISH.getValue());
                stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(),
                    LogisticsState.LOGISTICS_STATE_STOCKOUT.getValue());
            }

            //记录route
            writeSendCommandSuccessLog(stockoutOrderBO);

            LogBetter
                .instance(logger)
                .setLevel(LogLevel.INFO)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                        SystemConstants.TRACE_APP))
                .addParm(
                    "[供应链-订单出库]发货单状态变更为[已出库]，运单号[" + stockoutOrderBO.getIntlMailNo() + "]订单号：",
                    stockoutOrderBO.getBizId()).log();

            result.setSuccess(true);
            return result;
        } catch (Throwable e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                .addParm("出库处理异常,订单ID", request.getStockoutOrderBO().getBizId())
                .addParm("订单BO", request.getStockoutOrderBO()).setException(e).log();
            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_SEND_EXCEPTION, "发货处理异常");
        }
    }

    /**
     * 消费冻结库存
     * 如果为组合商品，消费物料库存
     *
     * @param stockoutOrderDO
     * @param stockoutOrderSkuDOList
     * @return
     * @throws ServiceException
     * @paran stockoutOrderSkuDOList
     */
    public Boolean consumeSkuFreezeStocks(StockoutOrderBO stockoutOrderBO) throws ServiceException {
        return stockService.consumeSkuStockInBatch(
            buildSkuStockOperaterEntityList(stockoutOrderBO), stockoutOrderBO.getWarehouseId(),
            stockoutOrderBO.getId());
    }

    private List<SkuStockOperaterEntity> buildSkuStockOperaterEntityList(StockoutOrderBO stockoutOrderBO) {
        //        stockBO.computeConsumeSkuList(stockoutOrderSkuDOList),
        return null;
    }

    /**
     * 记录下发出库指令成功日志
     */
    private void writeSendCommandSuccessLog(StockoutOrderBO stockoutOrderBO) {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "包裹已出库",
            SystemConstants.INFO_LEVEL, new Date(), SystemUserName.SYSTEM.getValue());
    }

    /**
     * 注册快递100
     *
     * @param lineEntity
     * @param stockoutOrderDO
     */
    private void registKd100ForOrderRoutes(StockoutOrderBO stockoutOrderBO) throws ServiceException {

        // TODO matt 判断是否需要注册快递100
        LogisticsLineBO lineEntity = stockoutOrderBO.getLineBO();

        String internalMailNo = stockoutOrderBO.getIntrMailNo();
        String internationMailNo = stockoutOrderBO.getIntlMailNo();
        if (StringUtils.isBlank(internalMailNo) && StringUtils.isBlank(internationMailNo)) {
            return;
        }
        if (StringUtils.isNotBlank(internalMailNo) && StringUtils.isNotBlank(internationMailNo)) {
            if (internalMailNo.trim().equalsIgnoreCase(internationMailNo.trim())) {
                routeService.registKD100Routes(stockoutOrderBO.getBizId(),
                    RouteType.INTERNAL.getType());
                return;
            } else {
                routeService.registKD100Routes(stockoutOrderBO.getBizId(),
                    RouteType.INTERNAL.getType());
                routeService.registKD100Routes(stockoutOrderBO.getBizId(),
                    RouteType.INTERNATIONAL.getType());
                return;
            }
        }

        if (StringUtils.isNotBlank(internalMailNo) && StringUtils.isBlank(internationMailNo)) {
            routeService
                .registKD100Routes(stockoutOrderBO.getBizId(), RouteType.INTERNAL.getType());
            return;
        }

        if (StringUtils.isBlank(internalMailNo) && StringUtils.isNotBlank(internationMailNo)) {
            routeService.registKD100Routes(stockoutOrderBO.getBizId(),
                RouteType.INTERNATIONAL.getType());
            return;
        }

    }

}
