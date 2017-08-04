package com.sfebiz.supplychain.service.stockin.statemachine.process;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.WarehouseReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.BatchGeneratePlanType;
import com.sfebiz.supplychain.exposed.stock.api.StockService;
import com.sfebiz.supplychain.exposed.stock.entity.SkuBatchStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.entity.StockBatchEntity;
import com.sfebiz.supplychain.exposed.stockinorder.api.StockInService;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailEntity;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderState;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderType;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDetailDO;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderDetailManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderStateLogManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.statemachine.Operator;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequest;
import net.pocrd.entity.ServiceException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangyajing on 2017/7/24.
 */
@Component("stockinOrderFinishProcessor")
public class StockinOrderFinishProcessor extends StockinAbstractProcessor{
    private static final HaitaoTraceLogger purchaseTraceLogger = HaitaoTraceLoggerFactory.getTraceLogger("stockinOrderFinishProcessor");
    private final static Logger logger = LoggerFactory.getLogger(StockinOrderFinishProcessor.class);
    private final static ModelMapper modelMapper = new ModelMapper();

    @Resource
    WarehouseManager warehouseManager;
    @Resource
    StockinOrderManager stockinOrderManager;
    @Resource
    StockinOrderDetailManager stockinOrderDetailManager;
    @Resource
    StockService stockService;
    @Resource
    StockinOrderStateLogManager stockinOrderStateLogManager;

    static {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public BaseResult process(StockinOrderRequest request) throws ServiceException {
        // 1. 验证请求是否正确
        validateRequest(request);
        StockinOrderDO stockinOrderDO = request.getStockinOrderDO();

        // 2. 检查仓库是否存在
        WarehouseDO warehouseDO = warehouseManager.getById(stockinOrderDO.getWarehouseId());
        if (null == warehouseDO) {
            throw new ServiceException(WarehouseReturnCode.WAREHOUSE_NOT_EXIST_ERR,
                    "[供应链-完成入库单失败]: " + WarehouseReturnCode.WAREHOUSE_NOT_EXIST_ERR.getDesc() + " "
                            + "[入库单ID: " + request.getId()
                            + ", 仓库ID: " + request.getStockinOrderDO().getWarehouseId()
                            + "]"
            );
        }
        // 3. 更新入库单的仓库回调事件
        if (stockinOrderDO.getWarehouseStockinTime() == null) {
            stockinOrderDO.setWarehouseStockinTime(new Date());
        }
        stockinOrderManager.update(stockinOrderDO);

        // 4. 入库完成
        doFinish(request, warehouseDO);

        // 5. 记录日志
        Date date = new Date();
        Operator operator = request.getOperator();
        stockinOrderStateLogManager.insertOrUpdate(stockinOrderDO.getId(), operator.getId(), operator.getName(), StockinOrderState.STOCKIN_FINISH.getValue());

        return BaseResult.SUCCESS_RESULT;
    }

    private void doFinish(StockinOrderRequest request, WarehouseDO warehouseDO) throws ServiceException {
        List<StockBatchEntity> stockBatchEntityList = new ArrayList<StockBatchEntity>();
        StockinOrderDO stockinOrderDO = request.getStockinOrderDO();
        List<StockinOrderDetailEntity> stockinOrderDetailEntityList = request.getStockinOrderDetailEntities();
        for (StockinOrderDetailEntity detailEntity : stockinOrderDetailEntityList) {
            if (detailEntity.id != null) {
                StockinOrderDetailDO stockinOrderDetailDOForUpdate = new StockinOrderDetailDO();
                stockinOrderDetailDOForUpdate = modelMapper.map(detailEntity, StockinOrderDetailDO.class);
                stockinOrderDetailDOForUpdate.setRealDiffCount(stockinOrderDetailDOForUpdate.getCount() - stockinOrderDetailDOForUpdate.getRealCount() - stockinOrderDetailDOForUpdate.getBadRealCount());
                // TODO: 2017/7/24 判断支持批次管理则更新批次号
                if (stockinOrderDetailDOForUpdate.getBatchGeneratePlan().equals(BatchGeneratePlanType.STOCKIN_SAME.getValue())) {
                    stockinOrderDetailDOForUpdate.setProductionDate(null);
                    stockinOrderDetailDOForUpdate.setExpirationDate(null);
                }
                stockinOrderDetailManager.update(stockinOrderDetailDOForUpdate);
            }
            if (detailEntity.realCount > 0 || detailEntity.badRealCount > 0 ) {
                SkuBatchStockOperaterEntity batchStockEntity = buildSkuStockBatch(detailEntity);
                batchStockEntity.setStockinId(stockinOrderDO.getId());
                batchStockEntity.setWarehouseId(warehouseDO.getId());
                batchStockEntity.setMerchantId(stockinOrderDO.getMerchantId());
                batchStockEntity.setProviderId(stockinOrderDO.getMerchantProviderId());
                stockService.incrementSkuBatchStock(warehouseDO.getId(), stockinOrderDO.getId(), StockinOrderType.SALES_STOCK_IN.getValue(), batchStockEntity);
            } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.WARN)
                        .setTraceLogger(TraceLogEntity
                                .instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链-完成入库单]: 商品实收库存为0，忽略之")
                        .addParm("入库单ID", stockinOrderDO.getId())
                        .addParm("入库单明细", detailEntity.getId())
                        .log();
            }
        }
    }

    protected SkuBatchStockOperaterEntity buildSkuStockBatch(StockinOrderDetailEntity stockinOrderDetailEntity) {
        if (null != stockinOrderDetailEntity) {
            SkuBatchStockOperaterEntity batchStockEntity = new SkuBatchStockOperaterEntity();
            batchStockEntity.setSkuId(stockinOrderDetailEntity.getSkuId());
            batchStockEntity.setBatchNo(stockinOrderDetailEntity.getSkuBatch());
            batchStockEntity.setExpirationDate(stockinOrderDetailEntity.getExpirationDate());
            batchStockEntity.setProductionDate(stockinOrderDetailEntity.getProductionDate());
            batchStockEntity.setStockinDate(stockinOrderDetailEntity.getStockinDate());
            batchStockEntity.setCount(stockinOrderDetailEntity.getRealCount());
            batchStockEntity.setWearCount(stockinOrderDetailEntity.getBadRealCount());
            if (stockinOrderDetailEntity.getBatchGeneratePlan().equals(BatchGeneratePlanType.STOCKIN_SAME.getValue())) {
                batchStockEntity.setExpirationDate(null);
                batchStockEntity.setProductionDate(null);
            }
            return batchStockEntity;
        }
        return null;
    }
}
