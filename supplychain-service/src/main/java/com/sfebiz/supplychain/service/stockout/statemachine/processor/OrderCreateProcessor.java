package com.sfebiz.supplychain.service.stockout.statemachine.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.LogisticsLineReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.stock.entity.SkuStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderLineDO;
import com.sfebiz.supplychain.persistence.base.merchant.manager.MerchantProviderLineManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuManager;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;
import com.sfebiz.supplychain.persistence.base.stock.manager.StockBatchManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDetailDO;
import com.sfebiz.supplychain.service.statemachine.EngineProcessor;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.model.StockoutOrderRequestFactory;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderActionType;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * 
 * <p>出库单创建处理器，此处理器并不作为初始状态的前置Action</p>
 * 
 * @author matt
 * @Date 2017年7月21日 上午11:59:45
 */
@Component("orderCreateProcessor")
public class OrderCreateProcessor extends TemplateProcessor implements
                                                           EngineProcessor<StockoutOrderRequest> {

    /** 日志 */
    private static final Logger            logger                   = LoggerFactory
                                                                        .getLogger(OrderCreateProcessor.class);

    /** 跟踪日志 */
    private static final HaitaoTraceLogger traceLogger              = HaitaoTraceLoggerFactory
                                                                        .getTraceLogger("order");

    @Resource
    private SkuManager                     skuManager;

    @Resource
    private LogisticsLineManager           logisticsLineManager;

    @Resource
    private MerchantProviderLineManager    merchantProviderLineManager;

    @Resource
    private StockBatchManager              stockBatchManager;

    /** 创建出库单的key */
    private static final String            CREATE_STOCKOUTORDER_KEY = "CREATE_STOCKOUTORDER_KEY";

    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {
        return new BaseResult(Boolean.TRUE);
    }

    /**
     * 创建出库单
     * 
     * @param stockoutOrderBO
     * @return
     * @throws ServiceException
     */
    public String createStockoutOrder(StockoutOrderBO stockoutOrderBO) throws ServiceException {

        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链-创建出库单]:")
            .addParm("出库单详情", stockoutOrderBO).log();

        // 获取分布式锁
        if (distributedLock.fetch(CREATE_STOCKOUTORDER_KEY + stockoutOrderBO.getMerchantOrderNo())) {
            // 2.1. 开启事务
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus transactionStatus = transactionManager.getTransaction(def);
            boolean transactionSuccess = false;

            try {
                // 1. 根据仓库id和服务类型，获取正在服务中的线路列表
                List<LogisticsLineDO> inServiceLineList = logisticsLineManager
                    .getInServiceLineByWarehouseIdAndServiceType(stockoutOrderBO.getWarehouseId(),
                        stockoutOrderBO.getServiceType());
                if (CollectionUtils.isEmpty(inServiceLineList)) {

                    throw new ServiceException(LogisticsLineReturnCode.LINE_NOT_EXIST,
                        "出库单创建，没有查找到可用的线路");
                }

                // 2. 获取skuId对应的所有可用的批次库存信息
                Map<Long, List<StockBatchDO>> skuId4StockBatchDOsMap = new HashMap<Long, List<StockBatchDO>>();
                for (StockoutOrderDetailBO detailBO : stockoutOrderBO.getDetailBOs()) {
                    List<StockBatchDO> stockBatchDOList = stockBatchManager
                        .getForCreateStockoutOrder(detailBO.getSkuId(),
                            stockoutOrderBO.getWarehouseId(), stockoutOrderBO.getMerchantId(),
                            stockoutOrderBO.getMerchantProviderId(), detailBO.getSkuBatch(),
                            detailBO.getStockOutPlan(), detailBO.getQuantity());
                    if (CollectionUtils.isEmpty(stockBatchDOList)) {
                        throw new ServiceException(LogisticsLineReturnCode.LINE_NOT_EXIST,
                            "未找到可用的批次库存信息，barCode=" + detailBO.getSkuBarcode());
                    }
                    skuId4StockBatchDOsMap.put(detailBO.getSkuId(), stockBatchDOList);
                }

                // 3. 根据服务中的线路列表，去筛选商品所对应的批次信息，线路筛选从高优先级开始
                Map<Long, StockBatchDO> skuId4SelectedStockBatchDOMap = new HashMap<Long, StockBatchDO>();
                LogisticsLineDO selectedLineDO = null;
                Map<String, List<MerchantProviderLineDO>> tempMerchantProviderLineDOListMap = new HashMap<String, List<MerchantProviderLineDO>>();
                for (LogisticsLineDO lineDO : inServiceLineList) {
                    skuId4SelectedStockBatchDOMap.clear();
                    // 循环出库单中的商品
                    for (StockoutOrderDetailBO detailBO : stockoutOrderBO.getDetailBOs()) {
                        List<StockBatchDO> stockBatchDOList = skuId4StockBatchDOsMap.get(detailBO
                            .getSkuId());
                        StockBatchDO selectedStockBatchDO = null;
                        for (StockBatchDO stockBatchDO : stockBatchDOList) {
                            String tempListMapKey = stockBatchDO.getMerchantProviderId() + "_"
                                                    + lineDO.getId();
                            List<MerchantProviderLineDO> merchantProviderLineDOList = null;
                            if (tempMerchantProviderLineDOListMap.containsKey(tempListMapKey)) {
                                merchantProviderLineDOList = tempMerchantProviderLineDOListMap
                                    .get(tempListMapKey);
                            } else {
                                merchantProviderLineDOList = merchantProviderLineManager
                                    .getByMerchantProviderIdAndLineId(
                                        stockBatchDO.getMerchantProviderId(), lineDO.getId());
                                tempMerchantProviderLineDOListMap.put(tempListMapKey,
                                    merchantProviderLineDOList);
                            }
                            if (CollectionUtils.isEmpty(merchantProviderLineDOList)) {
                                continue;
                            } else {
                                selectedStockBatchDO = stockBatchDO;
                                break;
                            }
                        }
                        if (null == selectedStockBatchDO) {
                            break;
                        } else {
                            skuId4SelectedStockBatchDOMap.put(detailBO.getSkuId(),
                                selectedStockBatchDO);
                        }
                    }

                    // 判断是否所有的商品都已选中了批次
                    if (skuId4SelectedStockBatchDOMap.size() == stockoutOrderBO.getDetailBOs()
                        .size()) {
                        selectedLineDO = lineDO;
                        break;
                    }
                }
                if (null == selectedLineDO) {
                    throw new ServiceException(LogisticsLineReturnCode.LINE_NOT_EXIST,
                        "出库单创建，根据出库单中的商品，没有获取到一条所有商品都可走的线路");
                }

                // 4. 设置线路及批次库存相关信息
                stockoutOrderBO.setLineId(selectedLineDO.getId());
                for (StockoutOrderDetailBO detailBO : stockoutOrderBO.getDetailBOs()) {
                    StockBatchDO stockBatchDO = skuId4SelectedStockBatchDOMap.get(detailBO
                        .getSkuId());

                    detailBO.setMerchantProviderId(stockBatchDO.getMerchantProviderId());
                    detailBO.setSkuBatch(stockBatchDO.getBatchNo());
                    detailBO.setStockinOrderId(stockBatchDO.getStockinOrderId());
                }

                // 5. 保存出库单信息
                saveStockoutOrderForCreate(stockoutOrderBO);

                // 6. 冻结批次库存，转运出库不用冻结库存
                if (stockoutOrderBO.getOrderType() != StockoutOrderType.TRANSPORT_STOCK_OUT
                    .getValue()) {
                    stockService
                        .freezeSkuStockBatch(buildSkuStockOperaterEntityList(stockoutOrderBO));
                }

                // 7. 触发状态变更为待发货
                stockoutOrderStateBizService.triggerOrderStateChange(stockoutOrderBO,
                    StockoutOrderActionType.AUDIT);
                stockoutOrderBO.setOrderState(StockoutOrderState.WAIT_STOCKOUT.getValue());

                // 8. 【执行出库单下发流程】消息通知
                stockoutOrderNoticeBizService
                    .noticeExecStockoutSendProcess(stockoutOrderBO.getId());

                transactionSuccess = true;

            } catch (ServiceException e) {
                LogBetter
                    .instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getMerchantOrderNo(),
                            SystemConstants.TRACE_APP)).setErrorMsg("[供应链-创建出库单异常]: " + e.getMsg())
                    .addParm("主订单ID", stockoutOrderBO.getMerchantOrderNo()).log();
                throw e;
            } catch (Exception e) {
                LogBetter
                    .instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getMerchantOrderNo(),
                            SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-创建出库单异常]: " + e.getMessage())
                    .addParm("主订单ID", stockoutOrderBO.getMerchantOrderNo()).log();
                throw new ServiceException(LogisticsReturnCode.LOGISTICS_ORDER_CREATE_ERR,
                    "[供应链-创建出库单异常]: " + e.getMessage());
            } finally {

                //6. 提交事务
                if (transactionSuccess) {
                    transactionManager.commit(transactionStatus);
                } else {
                    transactionManager.rollback(transactionStatus);
                }

                //7. 释放乐观锁
                try {
                    distributedLock.realease(CREATE_STOCKOUTORDER_KEY
                                             + stockoutOrderBO.getMerchantOrderNo());
                } catch (Exception e) {
                    LogBetter
                        .instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(
                            TraceLogEntity.instance(traceLogger,
                                stockoutOrderBO.getMerchantOrderNo(), SystemConstants.TRACE_APP))
                        .setErrorMsg("[供应链-创建出库单失败]: 未能释放乐观锁").addParm("出库单详情", stockoutOrderBO)
                        .setException(e).log();
                }
            }
        } else {
            LogBetter
                .instance(logger)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getMerchantOrderNo(),
                        SystemConstants.TRACE_APP)).setErrorMsg("[供应链-创建出库单失败]: 并发异常")
                .setParms(stockoutOrderBO).log();
            throw new ServiceException(LogisticsReturnCode.LOGISTICS_CONCURRENT_ERR,
                "[供应链-创建出库单失败]: 并发异常");
        }
        return null;
    }

    /**
     * 执行分单策略
     *
     * @param stockoutOrderSkuEntities
     * @return
     * @throws ServiceException
     */
    /*
    public List<SplitOrderBO> splitOrder(StockoutOrderBO stockoutBO) throws ServiceException {
        // 初始化事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        boolean transactionSuccess = false;

        // 根据商品类别以及税金总额进行分单
        List<SplitOrderBO> splitResult = new ArrayList<SplitOrderBO>();

        try {
            splitResult = OrderSplit.splitOrderByDataSouce(stockoutBO.getDetailBOs(),
                dataBaseDataSouce, stockoutBO.getMerchantAccountId(), stockoutBO);
            transactionSuccess = true;
        } catch (ServiceException e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN)
                .setErrorMsg("[供应链-根据商品信息重新分单失败]:" + e.getMsg()).setException(e).log();
            throw e;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                .setErrorMsg("[供应链-根据商品信息重新分单失败]:" + e.getMessage()).setException(e).log();
            throw new ServiceException(LogisticsReturnCode.LOGISTICS_ORDER_CREATE_ERR,
                "[供应链-根据商品信息重新分单失败]: 内部系统异常");
        } finally {
            //6. 提交事务
            if (transactionSuccess) {
                transactionManager.commit(transactionStatus);
            } else {
                transactionManager.rollback(transactionStatus);
            }
        }
        return splitResult;
    }
    */

    /**
     * 订单创建时，保存出库单信息
     * 
     * 
     * @param stockoutOrderBO
     * @throws ServiceException
     */
    private void saveStockoutOrderForCreate(StockoutOrderBO stockoutOrderBO)
                                                                            throws ServiceException {

        // 1. 保存出库单主表信息
        StockoutOrderDO orderDO = StockoutOrderConvert
            .convertStockoutOrderBOToStockoutOrderDO(stockoutOrderBO);
        stockoutOrderManager.insert(orderDO);
        stockoutOrderBO.updateStockoutOrderId(orderDO.getId(), orderDO.getBizId());

        // 2. 初始化状态机
        StockoutOrderRequest stockoutOrderRequest = StockoutOrderRequestFactory
            .generateStockoutOrderRequest(StockoutOrderActionType.CREATE, stockoutOrderBO, null,
                null);
        engineService.startStateMachineEngine(stockoutOrderRequest);

        // 3. 保存出库单买家信息
        stockoutOrderBuyerManager.insert(StockoutOrderConvert.convertBuyerBOToDO(stockoutOrderBO
            .getBuyerBO()));

        // 4. 保存出库单记录表信息
        stockoutOrderRecordManager.insert(StockoutOrderConvert.buildInitRecordDO(stockoutOrderBO));

        // 5. 保存出库单明细信息
        List<StockoutOrderDetailDO> detailDOList = StockoutOrderConvert
            .convertBOToDetailDOList(stockoutOrderBO);
        for (StockoutOrderDetailDO detailDO : detailDOList) {
            stockoutOrderDetailManager.insert(detailDO);
        }

    }

    private List<SkuStockOperaterEntity> buildSkuStockOperaterEntityList(StockoutOrderBO stockoutOrderBO) {
        List<SkuStockOperaterEntity> stockOperaterEntityList = new ArrayList<SkuStockOperaterEntity>();
        for (StockoutOrderDetailBO detailBO : stockoutOrderBO.getDetailBOs()) {
            SkuStockOperaterEntity skuStockOperaterEntity = new SkuStockOperaterEntity();
            skuStockOperaterEntity.setCount(detailBO.getQuantity());
            skuStockOperaterEntity.setSkuId(detailBO.getSkuId());
            skuStockOperaterEntity.setWarehouseId(stockoutOrderBO.getWarehouseId());
            skuStockOperaterEntity.setStockoutOrderId(stockoutOrderBO.getId());
            skuStockOperaterEntity.setBatchNo(detailBO.getSkuBatch());
            stockOperaterEntityList.add(skuStockOperaterEntity);
        }
        return stockOperaterEntityList;
    }

}
