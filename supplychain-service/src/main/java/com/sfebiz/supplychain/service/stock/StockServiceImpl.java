package com.sfebiz.supplychain.service.stock;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.StockOutPlanType;
import com.sfebiz.supplychain.exposed.stock.api.StockService;
import com.sfebiz.supplychain.exposed.stock.entity.SkuBatchStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.entity.SkuStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.enums.StockBatchStateType;
import com.sfebiz.supplychain.exposed.stock.enums.StockFreezeOrderType;
import com.sfebiz.supplychain.exposed.stock.enums.StockFreezeState;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderType;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuManager;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockFreezeDO;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockPhysicalDO;
import com.sfebiz.supplychain.persistence.base.stock.manager.StockBatchManager;
import com.sfebiz.supplychain.persistence.base.stock.manager.StockFreezeManager;
import com.sfebiz.supplychain.persistence.base.stock.manager.StockPhysicalManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.queue.MessageProducer;
import com.sfebiz.supplychain.service.stock.util.ComparatorExpirationDate;
import com.sfebiz.supplychain.service.stock.util.ComparatorStockinDate;
import net.pocrd.entity.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 库存相关操作
 * @date 2017-07-25 10:01
 **/
public class StockServiceImpl implements StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
    @Resource
    private StockBatchManager stockBatchManager;
    @Resource
    private StockFreezeManager stockFreezeManager;
    @Resource
    private StockPhysicalManager stockPhysicalManager;
    @Resource
    private SkuManager skuManager;
    @Resource
    private MessageProducer supplyChainMessageProducer;
    @Resource
    private WarehouseManager warehouseManager;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incrementSkuBatchStock(long warehouseId, long stockinOrderId, int stockinType, SkuBatchStockOperaterEntity skuBatchStockOperaterEntity) throws ServiceException {
        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                .setMsg("[供应链-增加商品批次库存]")
                .addParm("商品信息", skuBatchStockOperaterEntity)
                .addParm("仓库ID", warehouseId)
                .addParm("入库单ID", stockinOrderId)
                .addParm("入库单类型", stockinType)
                .log();

        if (0 == warehouseId || 0 == stockinOrderId || null == skuBatchStockOperaterEntity || 0 == skuBatchStockOperaterEntity.getSkuId() || skuBatchStockOperaterEntity.getCount() < 0
                || skuBatchStockOperaterEntity.getWearCount() < 0 || skuBatchStockOperaterEntity.getPrice() < 0 || skuBatchStockOperaterEntity.getPriceRmb() < 0) {
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL,
                    "[供应链-增加商品库存失败]: " + LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL.getDesc() + " "
                            + "[商品信息: " + skuBatchStockOperaterEntity
                            + ", 仓库ID: " + warehouseId
                            + ", 入库单ID: " + stockinOrderId
                            + "]"
            );
        }

        try {
            //1. 获取实物库存记录，如果不存在则创建
            StockPhysicalDO stockPhysicalDO = getStockPhysicalBySkuIdAndwarehouseIdIfNotExistThenCreate(skuBatchStockOperaterEntity.getSkuId(), warehouseId);
            //2. 更新实物库存的信息和成本价
            return updateSkuBatchStockCount(stockPhysicalDO, stockinOrderId, stockinType, skuBatchStockOperaterEntity);
        } catch (ServiceException e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.WARN)
                    .setErrorMsg(e.getMessage())
                    .setException(e)
                    .log();
            throw e;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链-增加商品库存失败]" + e.getMessage())
                    .addParm("商品信息", skuBatchStockOperaterEntity)
                    .addParm("仓库ID", warehouseId)
                    .addParm("入库单ID", stockinOrderId)
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_INNER_EXCEPTION,
                    "[供应链-增加商品库存异常]: " + e.getMessage() + " "
                            + "[商品信息: " + skuBatchStockOperaterEntity
                            + ", 仓库ID: " + warehouseId
                            + ", 入库单ID: " + stockinOrderId
                            + "]"
            );
        }
    }

    /**
     * 批量冻结库存(销售出库单)
     * PS：提前一定要指定好批次库存
     *
     * @param skuStockOperaterEntityList 商品库存操作实体集合
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, List<SkuBatchStockOperaterEntity>> freezeSkuStockBatch(List<SkuStockOperaterEntity> skuStockOperaterEntityList) throws ServiceException {
        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                .setMsg("[供应链-批量冻结库存信息]:开始")
                .addParm("商品信息", skuStockOperaterEntityList).log();
        //1. 判断参数合法性
        if (null == skuStockOperaterEntityList || skuStockOperaterEntityList.size() == 0) {
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL,
                    "[供应链-批量冻结库存信息失败]: " + LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL.getDesc() + "[商品信息: " + skuStockOperaterEntityList + "]"
            );
        }

        //2 初始化已冻结批次库存
        Map<String, List<SkuBatchStockOperaterEntity>> freezeBatchStockMap = new HashMap<String, List<SkuBatchStockOperaterEntity>>();
        //3. 按skuId,stockoutOrderId进行merge和排序
        List<SkuStockOperaterEntity> mergeAndSortResult = mergeAndSortSkuStockOperaterEntityBySkuIdAndStockoutOrderId(skuStockOperaterEntityList);
        //4. 按顺序操作库存
        for (SkuStockOperaterEntity skuStockOperaterEntity : mergeAndSortResult) {
            //4.1 计算出需要冻结的批次库存信息
            List<SkuBatchStockOperaterEntity> skuBatchStockOperaterEntities = checkAndGetNeedFreezeBatchStockByWarehouse(skuStockOperaterEntity);
            //4.2 冻结库存
            checkAndFreezeSkuBatchStock(skuStockOperaterEntity.getSkuId(), skuStockOperaterEntity.getWarehouseId(), skuStockOperaterEntity.getCount()
                    , skuStockOperaterEntity.getStockoutOrderId(), skuBatchStockOperaterEntities);
            //4.3 将商品冻结的批次库存信息返回
            String key = skuStockOperaterEntity.getStockoutOrderId() + "_" + skuStockOperaterEntity.getSkuId();
            freezeBatchStockMap.put(key, skuBatchStockOperaterEntities);
        }

        //TODO 5. 按顺序更新redis库存
        // for (SkuStockOperaterEntity skuStockOperaterEntity : mergeAndSortResult) {
        // sendRedisSyncMessage(skuStockOperaterEntity.getSkuId());
        // }

        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                .setMsg("[供应链-批量冻结库存信息]:结束")
                .addParm("商品信息", skuStockOperaterEntityList)
                .addParm("商品冻结的批次库存信息", freezeBatchStockMap).log();
        return freezeBatchStockMap;
    }

    /**
     * 批量消费库存
     *
     * @param skuStockOperaterEntityList 库存操作集合
     * @param warehouseId                仓库
     * @param stockoutOrderId            出库单ID
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean consumeSkuStockInBatch(List<SkuStockOperaterEntity> skuStockOperaterEntityList, long warehouseId, long stockoutOrderId) throws ServiceException {
        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                .setMsg("[供应链-批量消费商品库存]")
                .addParm("商品信息", skuStockOperaterEntityList)
                .addParm("仓库ID", warehouseId)
                .addParm("出库单ID", stockoutOrderId)
                .log();
        //1. 判断参数合法性
        if (0 == warehouseId || 0 == stockoutOrderId || null == skuStockOperaterEntityList || skuStockOperaterEntityList.size() == 0) {
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL,
                    "[供应链-批量消费商品库存]: " + LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL.getDesc()
                            + "[商品信息: " + skuStockOperaterEntityList
                            + ", 仓库ID: " + warehouseId
                            + ", 出库单ID: " + stockoutOrderId
                            + "]"
            );
        }

        //2. 按skuId去重和排序
        List<SkuStockOperaterEntity> mergeAndSortResult = mergeAndSortSkuStockOperaterEntity(skuStockOperaterEntityList);

        //3. 按顺序去消费实物库存，只要有一个失败就全部回滚
        for (SkuStockOperaterEntity skuStockOperaterEntity : mergeAndSortResult) {
            consumeSkuStock(skuStockOperaterEntity.getSkuId(), warehouseId, skuStockOperaterEntity.getCount(), stockoutOrderId);
        }

        //TODO 4. 按顺序更新redis库存
        //for (SkuStockOperaterEntity skuStockOperaterEntity : mergeAndSortResult) {
        //    sendRedisSyncMessage(skuStockOperaterEntity.getSkuId());
        //}

        return true;
    }

    /**
     * 批量释放库存
     *
     * @param skuStockOperaterEntityList 库存操作实体集合
     * @param warehouseId                仓库id
     * @param stockoutOrderId            出库单ID
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean releaseSkuStockInBatch(List<SkuStockOperaterEntity> skuStockOperaterEntityList, long warehouseId, long stockoutOrderId) throws ServiceException {
        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("[供应链-批量释放商品库存]").addParm("商品信息", skuStockOperaterEntityList).addParm("仓库ID", warehouseId)
                .addParm("出库单ID", stockoutOrderId).log();
        if (0 == warehouseId || 0 == stockoutOrderId || null == skuStockOperaterEntityList || skuStockOperaterEntityList.size() == 0) {
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL,
                    "[供应链-批量释放商品库存]: " + LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL.getDesc()
                            + "[商品信息: " + skuStockOperaterEntityList
                            + ", 仓库ID: " + warehouseId
                            + ", 出库单ID: " + stockoutOrderId
                            + "]"
            );
        }

        //2. 按skuId去重和排序
        List<SkuStockOperaterEntity> mergeAndSortResult = mergeAndSortSkuStockOperaterEntity(skuStockOperaterEntityList);

        //3. 按顺序去释放实物库存，只要有一个失败就全部回滚
        for (SkuStockOperaterEntity skuStockOperaterEntity : mergeAndSortResult) {
            releaseSkuStock(skuStockOperaterEntity.getSkuId(), warehouseId, skuStockOperaterEntity.getCount(), stockoutOrderId);
        }

        //TODO 4. 按顺序更新redis库存
        //for (SkuStockOperaterEntity skuStockOperaterEntity : mergeAndSortResult) {
        //       sendRedisSyncMessage(skuStockOperaterEntity.getSkuId());
        //     }

        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-批量释放商品库存成功]")
                .addParm("商品信息", skuStockOperaterEntityList)
                .addParm("仓库ID", warehouseId)
                .addParm("出库单ID", stockoutOrderId)
                .log();
        return true;
    }

    /**
     * 查询实物库存信息是否存在
     *
     * @param skuId
     * @param warehouseId
     * @return
     * @throws ServiceException
     */
    protected StockPhysicalDO getStockPhysicalBySkuIdAndwarehouseIdIfNotExistThenCreate(long skuId, long warehouseId) throws ServiceException {
        StockPhysicalDO stockPhysicalDO = stockPhysicalManager.getBySkuIdAndWarehouseId(skuId, warehouseId);
        if (null == stockPhysicalDO) {
            stockPhysicalDO = new StockPhysicalDO();
            stockPhysicalDO.setSkuId(skuId);
            stockPhysicalDO.setWarehouseId(warehouseId);
            stockPhysicalDO.setAvailableCount(0);
            stockPhysicalDO.setFreezeCount(0);
            stockPhysicalDO.setDamagedCount(0);
        }
        return stockPhysicalDO;
    }

    /**
     * 更新批次库存信息
     *
     * @param stockPhysicalDO
     * @param stockinOrderId
     * @param stockinType
     * @param skuBatchStockOperaterEntity
     * @return
     * @throws ServiceException
     */
    protected boolean updateSkuBatchStockCount(StockPhysicalDO stockPhysicalDO, long stockinOrderId, int stockinType,
                                               SkuBatchStockOperaterEntity skuBatchStockOperaterEntity) throws ServiceException {
        LogBetter.instance(LOGGER)
                .setMsg("[供应链-增加商品库存]: 更新商品批次库存的数量和价格")
                .addParm("商品信息", skuBatchStockOperaterEntity)
                .addParm("入库单ID", stockinOrderId)
                .addParm("入库单类型", stockinType)
                .log();

        //更新实物库存数量
        updateStockPhysicalCount(stockPhysicalDO, stockinOrderId, skuBatchStockOperaterEntity);

        //更新批次库存信息
        addBatchStock(stockPhysicalDO, stockinOrderId, stockinType, skuBatchStockOperaterEntity);

        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-增加商品库存成功]")
                .addParm("商品信息", skuBatchStockOperaterEntity)
                .addParm("入库单ID", stockinOrderId)
                .log();
        return true;
    }

    /**
     * 增加sku批次库存
     *
     * @param stockPhysicalDO
     * @param stockinOrderId
     * @param stockinType
     * @param skuBatchStockOperaterEntity
     * @throws ServiceException
     */
    private void addBatchStock(StockPhysicalDO stockPhysicalDO, long stockinOrderId, int stockinType,
                               SkuBatchStockOperaterEntity skuBatchStockOperaterEntity) throws ServiceException {
        //增加sku批次库存
        StockBatchDO existBatchStockDO = stockBatchManager.getBySkuIdAndWarehouseIdAndBatchNo(stockPhysicalDO.getSkuId(), stockPhysicalDO.getWarehouseId(), skuBatchStockOperaterEntity.getBatchNo());
        if (existBatchStockDO == null) {
            StockBatchDO stockBatchDO = new StockBatchDO();
            stockBatchDO.setBatchNo(skuBatchStockOperaterEntity.getBatchNo());
            stockBatchDO.setMerchantBatchNo(skuBatchStockOperaterEntity.getBatchNo());
            stockBatchDO.setAvailableCount(skuBatchStockOperaterEntity.getCount());
            stockBatchDO.setSkuId(stockPhysicalDO.getSkuId());
            stockBatchDO.setStockPhysicalId(stockPhysicalDO.getId());
            stockBatchDO.setExpirationDate(skuBatchStockOperaterEntity.getExpirationDate());
            stockBatchDO.setFreezeCount(0);
            stockBatchDO.setState(StockBatchStateType.ENABLE.getValue());
            stockBatchDO.setProductionDate(skuBatchStockOperaterEntity.getProductionDate());
            stockBatchDO.setMerchantProviderId(skuBatchStockOperaterEntity.getProviderId());
            stockBatchDO.setWarehouseId(stockPhysicalDO.getWarehouseId());
            stockBatchDO.setDamagedCount(skuBatchStockOperaterEntity.getWearCount());
            stockBatchDO.setStockinOrderId(skuBatchStockOperaterEntity.getStockinId());
            //对于那些通过线下入库的sku,是没有设置入库日期的,设置手工入库完成日期作为入库日期
            if (null == skuBatchStockOperaterEntity.getStockinDate()) {
                stockBatchDO.setStockinDate(new Date());
            } else {
                stockBatchDO.setStockinDate(skuBatchStockOperaterEntity.getStockinDate());
            }
            stockBatchManager.insert(stockBatchDO);
        } else {
            // 如果已经存在该批次信息，则说明，有可能是调拨入库或者销售退货入库或者海关退货入库，需要判断
            if (stockinType == StockinOrderType.SALES_STOCK_IN.getValue()) {
                throw new ServiceException(
                        LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECODE_DUPLICATE,
                        "[供应链-供应链-增加商品库存异常: ]" + LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECODE_DUPLICATE.getDesc() + " "
                                + "[商品信息: " + skuBatchStockOperaterEntity
                                + ", 入库单ID: " + stockinOrderId
                                + "]"
                );
            } else {
                existBatchStockDO.setAvailableCount(existBatchStockDO.getAvailableCount().intValue() + skuBatchStockOperaterEntity.getCount().intValue());
                existBatchStockDO.setDamagedCount(existBatchStockDO.getDamagedCount() + skuBatchStockOperaterEntity.getWearCount());
                existBatchStockDO.setExpirationDate(skuBatchStockOperaterEntity.getExpirationDate());
                existBatchStockDO.setProductionDate(skuBatchStockOperaterEntity.getProductionDate());
                stockBatchManager.update(existBatchStockDO);
            }
        }
    }


    /**
     * 合并出库单中Sku信息
     *
     * @param souceEntityList
     * @return
     */
    protected List<SkuStockOperaterEntity> mergeAndSortSkuStockOperaterEntityBySkuIdAndStockoutOrderId(List<SkuStockOperaterEntity> souceEntityList) {
        HashMap<String, SkuStockOperaterEntity> map = new HashMap();
        // 合并skuId重复的数据
        for (SkuStockOperaterEntity skuStockOperaterEntity : souceEntityList) {
            String key = skuStockOperaterEntity.getSkuId() + "_" + skuStockOperaterEntity.getStockoutOrderId();
            if (map.containsKey(key)) {
                SkuStockOperaterEntity oldSkuStockOperaterEntity = map.get(key);
                oldSkuStockOperaterEntity.setCount(oldSkuStockOperaterEntity.getCount() + skuStockOperaterEntity.getCount());
                oldSkuStockOperaterEntity.setWearCount(oldSkuStockOperaterEntity.getWearCount() + skuStockOperaterEntity.getWearCount());
                oldSkuStockOperaterEntity.setOverFlowCount(oldSkuStockOperaterEntity.getOverFlowCount() + skuStockOperaterEntity.getOverFlowCount());
                oldSkuStockOperaterEntity.setOverFlowWearCount(oldSkuStockOperaterEntity.getOverFlowWearCount() + skuStockOperaterEntity.getOverFlowWearCount());
            } else {
                map.put(key, skuStockOperaterEntity);
            }
        }
        List<SkuStockOperaterEntity> resultList = new ArrayList<SkuStockOperaterEntity>();
        for (SkuStockOperaterEntity stockInOutEntity : map.values()) {
            resultList.add(stockInOutEntity);
        }
        // 根据skuId排序
        Collections.sort(resultList);
        return resultList;
    }

    protected List<SkuBatchStockOperaterEntity> checkAndGetNeedFreezeBatchStockByWarehouse(SkuStockOperaterEntity skuStockOperaterEntity) throws ServiceException {
        long warehouseId = skuStockOperaterEntity.getWarehouseId();
        //1. 获取仓库中所有符合要求的批次信息
        List<StockBatchDO> stockBatchDOList = stockBatchManager.getBySkuIdAndWarehouseId(skuStockOperaterEntity.getSkuId(), warehouseId);

        //2.判断是否存在批次库存信息
        if (stockBatchDOList == null || stockBatchDOList.size() == 0) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.WARN).setErrorMsg("[供应链-根据商品ID和仓库ID获取批次库存信息失败]: "
                    + LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND.getDesc()).addParm("商品ID", skuStockOperaterEntity.skuId)
                    .addParm("仓库ID", warehouseId).log();
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND, "[供应链-根据商品ID和仓库ID获取批次库存信息失败]: " +
                    LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND.getDesc() + "[商品ID:" + skuStockOperaterEntity.skuId + ",仓库ID:" + warehouseId + "]");
        }

        //3.根据Sku出库方案进行批次库存排序
        SkuDO skuDO = skuManager.getById(skuStockOperaterEntity.skuId);
        List<StockBatchDO> sortRusults = sortBatchStock(stockBatchDOList, skuDO);

        //4. 根据订单数量确定出库批次信息
        List<SkuBatchStockOperaterEntity> freezeBatchStocks = getNeedFreezeBatchStock(sortRusults, skuStockOperaterEntity);

        return freezeBatchStocks;
    }

    /**
     * 根据出库规则，对搜油批次库存进行排序
     *
     * @param StockBatchDOs
     * @param skuDO
     * @return
     */
    protected List<StockBatchDO> sortBatchStock(List<StockBatchDO> StockBatchDOs, SkuDO skuDO) {
        // 如果没设置，默认按照先入库先出规则
        if (StockOutPlanType.STOCKIN_FIRST.getValue().equals(skuDO.getStockoutPlan())) {
            Collections.sort(StockBatchDOs, new ComparatorStockinDate());
        } else if (StockOutPlanType.EXPIRE_FIRST.getValue().equals(skuDO.getStockoutPlan())) {
            Collections.sort(StockBatchDOs, new ComparatorExpirationDate());
        }
        return StockBatchDOs;
    }

    protected List<SkuBatchStockOperaterEntity> getNeedFreezeBatchStock(List<StockBatchDO> sortRusults, SkuStockOperaterEntity skuStockOperaterEntity) throws ServiceException {
        //1.初始化需要冻结库存信息集合
        List<SkuBatchStockOperaterEntity> needFreezeBatchStocks = new ArrayList<SkuBatchStockOperaterEntity>();
        //2.初始化本次商品批次库存所需数量
        int remainNeedCount = skuStockOperaterEntity.getCount();
        //3. 计算整理后批次库存信息
        for (StockBatchDO stockBatchDO : sortRusults) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("[供应链-根据商品出库规则计算所需冻结批次库存]").addParm("批次库存信息", stockBatchDO)
                    .addParm("仓库剩余需冻结数量", remainNeedCount).log();
            //3.1 批次可用数量> 所需数量  加入冻结集合里面
            if (stockBatchDO.getAvailableCount() >= remainNeedCount) {
                SkuBatchStockOperaterEntity entity = convertFromBatchStockDO(stockBatchDO);
                entity.setCount(remainNeedCount);
                entity.setQualityCount(stockBatchDO.getAvailableCount() - remainNeedCount);
                entity.setFreezeCount(stockBatchDO.getFreezeCount() + remainNeedCount);
                needFreezeBatchStocks.add(entity);
                LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("[供应链-根据商品出库规则计算所需冻结批次库存结束]").addParm("商品ID", skuStockOperaterEntity.getSkuId())
                        .addParm("商品冻结数量", skuStockOperaterEntity.getCount()).addParm("仓库ID", stockBatchDO.getWarehouseId()).log();
                return needFreezeBatchStocks;
            } else {
                //3.2 批次可用数量< 所需数量  减掉可用数量，冻结部分后继续处理，若处理完都没有return就gg了
                remainNeedCount -= stockBatchDO.getAvailableCount();
                SkuBatchStockOperaterEntity entity = convertFromBatchStockDO(stockBatchDO);
                needFreezeBatchStocks.add(entity);
            }
        }
        LogBetter.instance(LOGGER).setLevel(LogLevel.WARN)
                .setErrorMsg("[供应链-根据商品出库规则计算所需冻结批次库存失败]"
                        + LogisticsReturnCode.LOGISTICS_BATCH_STOCK_SKU_NOT_ENOUGH.getDesc())
                .addParm("商品ID", skuStockOperaterEntity.getSkuId())
                .addParm("仓库ID", sortRusults.get(0).getWarehouseId())
                .addParm("商品冻结数量", skuStockOperaterEntity.getCount())
                .log();
        throw new ServiceException(LogisticsReturnCode.LOGISTICS_BATCH_STOCK_SKU_NOT_ENOUGH,
                "[供应链-根据商品出库规则计算所需冻结批次库存失败]: " + LogisticsReturnCode.LOGISTICS_BATCH_STOCK_SKU_NOT_ENOUGH.getDesc()
                        + "[商品ID:" + skuStockOperaterEntity.skuId
                        + ",仓库ID:" + sortRusults.get(0).getWarehouseId()
                        + ",商品冻结数量:" + skuStockOperaterEntity.getCount()
                        + "]"
        );
    }

    /**
     * 根据批次库存信息转换成sku操作实体
     *
     * @param stockBatchDO
     * @return
     */
    protected SkuBatchStockOperaterEntity convertFromBatchStockDO(StockBatchDO stockBatchDO) {
        SkuBatchStockOperaterEntity skuBatchStockOperaterEntity = new SkuBatchStockOperaterEntity();
        skuBatchStockOperaterEntity.setSkuId(stockBatchDO.getSkuId());
        skuBatchStockOperaterEntity.setBatchNo(stockBatchDO.getBatchNo());
        skuBatchStockOperaterEntity.setWarehouseId(stockBatchDO.getWarehouseId());
        skuBatchStockOperaterEntity.setId(stockBatchDO.getId());
        skuBatchStockOperaterEntity.setCount(stockBatchDO.getAvailableCount());
        skuBatchStockOperaterEntity.setFreezeCount(stockBatchDO.getAvailableCount() + stockBatchDO.getFreezeCount());
        skuBatchStockOperaterEntity.setProviderId(stockBatchDO.getMerchantProviderId());
        skuBatchStockOperaterEntity.setQualityCount(0);
        skuBatchStockOperaterEntity.setStockinDate(stockBatchDO.getStockinDate());
        skuBatchStockOperaterEntity.setProductionDate(stockBatchDO.getProductionDate());
        skuBatchStockOperaterEntity.setExpirationDate(stockBatchDO.getExpirationDate());
        return skuBatchStockOperaterEntity;
    }

    protected boolean checkAndFreezeSkuBatchStock(long skuId, long warehouseId, int count, long stockoutOrderId, List<SkuBatchStockOperaterEntity> skuBatchStockOperaterEntities) throws ServiceException {
        if (0 == skuId || 0 == warehouseId || 0 == stockoutOrderId || 0 == count || skuBatchStockOperaterEntities == null || skuBatchStockOperaterEntities.size() == 0) {
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL,
                    "[供应链-根据需要冻结的批次库存信息检查并冻结商品库存失败]: " + LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL.getDesc()
                            + "[商品ID: " + skuId
                            + ", 仓库ID: " + warehouseId
                            + ", 商品数目: " + count
                            + ", 出库单ID: " + stockoutOrderId
                            + ", 批次库存信息: " + skuBatchStockOperaterEntities
                            + "]"
            );
        }

        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                .setMsg("[供应链-根据需要冻结的批次库存信息检查并冻结商品库存]")
                .addParm("商品ID", skuId)
                .addParm("仓库ID", warehouseId)
                .addParm("商品数目", count)
                .addParm("出库单ID", stockoutOrderId)
                .addParm("批次库存数量", skuBatchStockOperaterEntities.size())
                .log();
        try {
            //1. 检查此销售单是否已经有冻结库存
            List<StockFreezeDO> freezeStockList = getStockoutFreezeStockList(skuId, warehouseId, stockoutOrderId);
            if (null != freezeStockList && freezeStockList.size() > 0) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.WARN)
                        .setMsg("[供应链-检查并冻结商品库存]: 该订单的冻结库存记录已存在")
                        .addParm("商品ID", skuId)
                        .addParm("仓库ID", warehouseId)
                        .addParm("商品数目", count)
                        .addParm("出库单ID", stockoutOrderId)
                        .addParm("冻结记录数量", freezeStockList.size())
                        .log();
                return true;
            }
            //2. 判断现有库存是否满足需求，如果满足，则冻结库存
            return checkStockIsEnoughThenFreezeByNeedFreezeBatchStock(skuId, warehouseId, stockoutOrderId, count, skuBatchStockOperaterEntities);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setErrorMsg(LogisticsReturnCode.STOCK_SERVICE_INNER_EXCEPTION.getDesc())
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("商品数目", count)
                    .addParm("出库单ID", stockoutOrderId)
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_INNER_EXCEPTION,
                    "[供应链-检查并冻结商品库存异常]: " + e.getMessage()
                            + "[商品ID: " + skuId
                            + ", 仓库ID: " + warehouseId
                            + ", 商品数目: " + count
                            + ", 出库单ID: " + stockoutOrderId
                            + "]"
            );
        }
    }

    protected List<StockFreezeDO> getStockoutFreezeStockList(long skuId, long warehouseId, long stockoutOrderId) throws ServiceException {
        return stockFreezeManager.getBySkuIdAndwarehouseIdAndStockoutOrderId(skuId, warehouseId, stockoutOrderId);
    }


    protected boolean checkStockIsEnoughThenFreezeByNeedFreezeBatchStock(long skuId, long warehouseId, long stockoutOrderId, int count, List<SkuBatchStockOperaterEntity> skuBatchStockOperaterEntities) throws ServiceException {
        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                .setMsg("[供应链-冻结商品库存]")
                .addParm("商品ID", skuId)
                .addParm("仓库ID", warehouseId)
                .addParm("出库单ID", stockoutOrderId)
                .addParm("需冻结数量", count)
                .log();
        //1.查询商品实物库存信息
        StockPhysicalDO stockPhysicalDO = stockPhysicalManager.getBySkuIdAndWarehouseId(skuId, warehouseId);

        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                .setMsg("[供应链-多线程获取后的实物库存信息]")
                .addParm("实物库存信息", stockPhysicalDO)
                .log();

        // 2.判断库存是否满足, 如果满足，则冻结库存
        if (stockPhysicalDO.getAvailableCount() >= count) {
            stockPhysicalDO.setFreezeCount(stockPhysicalDO.getFreezeCount() + count);
            stockPhysicalDO.setAvailableCount(stockPhysicalDO.getAvailableCount() - count);
            stockPhysicalManager.update(stockPhysicalDO);

            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                    .setMsg("[供应链-冻结商品实物库存成功]")
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("出库单ID", stockoutOrderId)
                    .addParm("需冻结数量", count)
                    .addParm("实物库存信息", stockPhysicalDO)
                    .log();
        } else {
            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                    .setMsg("[供应链-冻结商品实物库存失败]:" + LogisticsReturnCode.LOGISTICS_STOCK_SKU_NOT_ENOUGH)
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("出库单ID", stockoutOrderId)
                    .addParm("需冻结数量", count)
                    .addParm("实物库存信息", stockPhysicalDO)
                    .log();
            throw new ServiceException(LogisticsReturnCode.LOGISTICS_STOCK_SKU_NOT_ENOUGH,
                    "[供应链-冻结商品实物库存失败]: " + LogisticsReturnCode.LOGISTICS_STOCK_SKU_NOT_ENOUGH.getDesc()
                            + ", [商品ID: " + skuId
                            + ", 仓库ID: " + warehouseId
                            + ", 出库单ID: " + stockoutOrderId
                            + ", 需冻结数量: " + count
                            + ", 实物库存信息: " + stockPhysicalDO
                            + "]"
            );
        }
        //3. 冻结冻结批次库存记录
        for (SkuBatchStockOperaterEntity skuBatchStockOperaterEntity : skuBatchStockOperaterEntities) {
            //3.1 记录冻结库存表
            StockFreezeDO stockFreezeDO = new StockFreezeDO();
            stockFreezeDO.setSkuId(skuId);
            stockFreezeDO.setWarehouseId(stockPhysicalDO.getWarehouseId());
            stockFreezeDO.setStockOrderId(stockoutOrderId);
            stockFreezeDO.setFreezeState(StockFreezeState.FREEZED.getValue());
            stockFreezeDO.setFreezeCount(skuBatchStockOperaterEntity.getCount());
            stockFreezeDO.setRealCount(0);
            stockFreezeDO.setBatchNo(skuBatchStockOperaterEntity.getBatchNo());
            stockFreezeDO.setOrderType(Integer.parseInt(StockFreezeOrderType.SALE_OUT_ORDER_TYPE.getValue()));
            stockFreezeManager.insert(stockFreezeDO);
            //3.2 更新批次库存表
            StockBatchDO stockBatchDO = new StockBatchDO();
            stockBatchDO.setId(skuBatchStockOperaterEntity.getId());
            stockBatchDO.setFreezeCount(skuBatchStockOperaterEntity.getFreezeCount());
            stockBatchDO.setAvailableCount(skuBatchStockOperaterEntity.getQualityCount());
            stockBatchManager.update(stockBatchDO);

            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                    .setMsg("[供应链-冻结商品批次库存成功]")
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("出库单ID", stockoutOrderId)
                    .addParm("需冻结数量", count)
                    .addParm("冻结批次库存信息", skuBatchStockOperaterEntity)
                    .log();
        }
        return true;
    }


//    public void sendRedisSyncMessage(long skuId) {
//        try {
//            Message msg = new Message();
//            msg.setTopic(MessageConstants.TOPIC_SUPPLY_CHAIN_EVENT);
//            msg.setTag(MessageConstants.TAG_REDIS_SKU_STOCK_INFO);
//            String body = skuId + "";
//            msg.setBody(body.getBytes());
//            Properties properties = new Properties();
//            msg.setUserProperties(properties);
//            msg.setStartDeliverTime(System.currentTimeMillis() + 1000 * 1);
//            supplyChainMessageProducer.send(msg);
//            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
//                    .setMsg("发送Redis商品库存信息同步：消息发送成功")
//                    .addParm("商品ID", skuId)
//                    .log();
//        } catch (Exception e) {
//            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
//                    .setMsg("发送Redis商品库存信息同步：消息发送异常")
//                    .addParm("商品ID", skuId)
//                    .setException(e)
//                    .log();
//        }
//    }

    protected List<SkuStockOperaterEntity> mergeAndSortSkuStockOperaterEntity(List<SkuStockOperaterEntity> souceEntityList) {
        HashMap<Long, SkuStockOperaterEntity> map = new HashMap();
        // 1.合并skuId重复的数据
        for (SkuStockOperaterEntity skuStockOperaterEntity : souceEntityList) {
            if (map.containsKey(skuStockOperaterEntity.getSkuId())) {
                SkuStockOperaterEntity oldSkuStockOperaterEntity = map.get(skuStockOperaterEntity.getSkuId());
                oldSkuStockOperaterEntity.setCount(oldSkuStockOperaterEntity.getCount() + skuStockOperaterEntity.getCount());
                oldSkuStockOperaterEntity.setWearCount(oldSkuStockOperaterEntity.getWearCount() + skuStockOperaterEntity.getWearCount());
                oldSkuStockOperaterEntity.setOverFlowCount(oldSkuStockOperaterEntity.getOverFlowCount() + skuStockOperaterEntity.getOverFlowCount());
                oldSkuStockOperaterEntity.setOverFlowWearCount(oldSkuStockOperaterEntity.getOverFlowWearCount() + skuStockOperaterEntity.getOverFlowWearCount());
            } else {
                SkuStockOperaterEntity newEntity = new SkuStockOperaterEntity();
                BeanUtils.copyProperties(skuStockOperaterEntity, newEntity);
                map.put(skuStockOperaterEntity.getSkuId(), newEntity);
            }
        }
        // 2.把整合好的SkuStockOperaterEntity Map放到list集合排序
        List<SkuStockOperaterEntity> resultList = new ArrayList<SkuStockOperaterEntity>();
        for (SkuStockOperaterEntity stockInOutEntity : map.values()) {
            resultList.add(stockInOutEntity);
        }

        Collections.sort(resultList);
        return resultList;
    }

    protected boolean consumeSkuStock(long skuId, long warehouseId, int count, long stockoutOrderId) throws ServiceException {
        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                .setMsg("[供应链-消费商品库存]")
                .addParm("商品ID", skuId)
                .addParm("仓库ID", warehouseId)
                .addParm("商品数目", count)
                .addParm("出库单ID", stockoutOrderId)
                .log();
        //1.校验参数合法性
        if (0 == skuId || 0 == warehouseId || 0 == stockoutOrderId || 0 == count) {
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL,
                    "[供应链-消费商品库存失败]: " + LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL.getDesc()
                            + "[商品ID: " + skuId
                            + ", 仓库ID: " + warehouseId
                            + ", 商品数目: " + count
                            + ", 出库单ID: " + stockoutOrderId
                            + "]"
            );
        }
        try {
            //2. 查找待消费的冻结库存
            List<StockFreezeDO> freezeStockList = getStockoutFreezeStockList(skuId, warehouseId, stockoutOrderId);

            //3. 检查冻结库存的信息是否正确
            checkFreezeStockInfoIsCorrect(freezeStockList, count, StockFreezeState.CONSUMED);

            //4. 消费冻结库存
            consumeFreezeStock(skuId, warehouseId, freezeStockList, count);
            return true;
        } catch (ServiceException e) {
            //抛出去
            throw e;
        } catch (Exception e) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                    .setErrorMsg(LogisticsReturnCode.STOCK_SERVICE_INNER_EXCEPTION.getDesc())
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("商品数目", count)
                    .addParm("出库单ID", stockoutOrderId)
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_INNER_EXCEPTION,
                    "[供应链-消费商品库存异常]: " + e.getMessage()
                            + "[商品ID: " + skuId
                            + ", 仓库ID: " + warehouseId
                            + ", 商品数目: " + count
                            + ", 出库单ID: " + stockoutOrderId
                            + "]"
            );
        }
    }

    protected void consumeFreezeStock(long skuId, long warehouseId, List<StockFreezeDO> stockFreezeDOS, int count) throws ServiceException {
        //1.校验实物库存
        StockPhysicalDO stockPhysicalDO = stockPhysicalManager.getBySkuIdAndWarehouseId(skuId, warehouseId);
        if (stockPhysicalDO == null) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                    .setErrorMsg(LogisticsReturnCode.STOCK_SERVICE_ACTUAL_RECORD_NOT_FOUND.getDesc())
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .log();
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_ACTUAL_RECORD_NOT_FOUND,
                    "[供应链-查找商品库存失败]: " + LogisticsReturnCode.STOCK_SERVICE_ACTUAL_RECORD_NOT_FOUND.getDesc()
                            + "[商品ID: " + skuId
                            + ", 仓库ID: " + warehouseId
                            + "]"
            );
        }
        //2.更新实物库存的数量
        stockPhysicalDO.setFreezeCount(stockPhysicalDO.getFreezeCount() - count);
        stockPhysicalManager.update(stockPhysicalDO);

        //3.更新批次库存信息
        for (StockFreezeDO freezeStockDO : stockFreezeDOS) {
            //3.1更新批次库存信息
            StockBatchDO stockBatchDO = stockBatchManager.getBySkuIdAndWarehouseIdAndBatchNo(skuId, warehouseId, freezeStockDO.getBatchNo().trim());
            if (stockBatchDO == null) {
                LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                        .setErrorMsg(LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND.getDesc())
                        .addParm("商品ID", skuId)
                        .addParm("仓库ID", warehouseId)
                        .addParm("冻结库存信息", freezeStockDO)
                        .log();
                throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND,
                        "[供应链-查找商品库存失败]: " + LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND.getDesc()
                                + "[商品ID: " + skuId
                                + ", 仓库ID: " + warehouseId
                                + ", 冻结库存信息: " + freezeStockDO
                                + "]"
                );
            }
            stockBatchDO.setFreezeCount(stockBatchDO.getFreezeCount() - freezeStockDO.getFreezeCount());
            stockBatchManager.update(stockBatchDO);
            //3.2更新冻结库存的状态
            freezeStockDO.setFreezeState(StockFreezeState.CONSUMED.getValue());
            freezeStockDO.setRealCount(freezeStockDO.getFreezeCount());
            stockFreezeManager.update(freezeStockDO);

            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                    .setMsg("[供应链-消费商品库存成功]")
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("冻结库存信息", freezeStockDO)
                    .log();
        }
    }

    protected boolean checkFreezeStockInfoIsCorrect(List<StockFreezeDO> freezeStockList, int count, StockFreezeState toState) throws ServiceException {
        if (null == freezeStockList || freezeStockList.size() == 0) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.WARN)
                    .setErrorMsg(LogisticsReturnCode.STOCK_SERVICE_FREEZE_RECORD_NOT_FOUND.getDesc())
                    .addParm("freezeStockList", freezeStockList)
                    .addParm("count", count)
                    .addParm("toState", toState)
                    .log();
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_FREEZE_RECORD_NOT_FOUND,
                    LogisticsReturnCode.STOCK_SERVICE_FREEZE_RECORD_NOT_FOUND.getDesc());
        }

        int totalFreezeCount = 0;
        for (StockFreezeDO freezeStockDO : freezeStockList) {
            if (!freezeStockDO.getFreezeState().equals(StockFreezeState.FREEZED.getValue())) {
                LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                        .setErrorMsg(LogisticsReturnCode.STOCK_SERVICE_FREEZE_RECORD_STATE_ERROR.getDesc())
                        .addParm("freezeStockDO", freezeStockDO)
                        .addParm("count", count)
                        .addParm("toState", toState)
                        .log();
                throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_FREEZE_RECORD_STATE_ERROR,
                        "[供应链-商品冻结库存状态异常: ]" + LogisticsReturnCode.STOCK_SERVICE_FREEZE_RECORD_STATE_ERROR.getDesc()
                                + "[冻结库存记录: " + freezeStockDO
                                + ", 商品数目: " + count
                                + ", 转换状态: " + toState
                                + "]"
                );
            }
            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                    .setMsg("[供应链-检查商品冻结状态 ]")
                    .addParm("freezeStockDO", freezeStockDO)
                    .addParm("count", count)
                    .addParm("toState", toState)
                    .log();
            totalFreezeCount += freezeStockDO.getFreezeCount();
        }

        if (totalFreezeCount != count) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.WARN)
                    .setErrorMsg("实际消费库存和冻结库存不符")
                    .addParm("totalFreezeCount", totalFreezeCount)
                    .addParm("count", count)
                    .addParm("toState", toState)
                    .log();
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_FREEZE_COUNT_NOT_MATCH,
                    "[供应链-商品冻结库存状态异常: ]" + LogisticsReturnCode.STOCK_SERVICE_FREEZE_COUNT_NOT_MATCH.getDesc()
                            + "[冻结库存总数: " + totalFreezeCount
                            + ", 商品数目: " + count
                            + ", 转换状态: " + toState
                            + "]"
            );
        }
        return true;
    }

    private boolean releaseSkuStock(Long skuId, long warehouseId, Integer count, long stockoutOrderId) throws ServiceException {
        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                .setMsg("[供应链-释放商品库存]")
                .addParm("商品ID", skuId)
                .addParm("仓库ID", warehouseId)
                .addParm("商品数目", count)
                .addParm("出库单ID", stockoutOrderId)
                .log();
        if (0 == skuId || 0 == warehouseId || 0 == stockoutOrderId || 0 == count) {
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL,
                    "[供应链-释放商品库存失败]: " + LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL.getDesc()
                            + "[商品ID: " + skuId
                            + ", 仓库ID: " + warehouseId
                            + ", 商品数目: " + count
                            + ", 出库单ID: " + stockoutOrderId
                            + "]"
            );
        }
        try {
            //1. 查找待释放的冻结库存
            List<StockFreezeDO> stockFreezeDOList = getStockoutFreezeStockList(skuId, warehouseId, stockoutOrderId);

            //2. 检查冻结库存的信息是否正确
            checkFreezeStockInfoIsCorrect(stockFreezeDOList, count, StockFreezeState.RELEASED);

            //3. 释放冻结库存
            releaseFreezeStock(skuId, warehouseId, stockFreezeDOList, count);
            return true;
        } catch (ServiceException e) {
            //抛出去
            throw e;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setMsg("[供应链-释放商品库存失败]" + e.getMessage())
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("商品数目", count)
                    .addParm("出库单ID", stockoutOrderId)
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_INNER_EXCEPTION,
                    "[供应链-释放商品库存失败]: " + e.getMessage()
                            + "[商品ID: " + skuId
                            + ", 仓库ID: " + warehouseId
                            + ", 商品数目: " + count
                            + ", 出库单ID: " + stockoutOrderId
                            + "]"
            );
        }
    }

    protected void releaseFreezeStock(long skuId, long warehouseId, List<StockFreezeDO> freezeStockDOs, int count) throws ServiceException {
        //1.校验实物库存和仓库信息
        StockPhysicalDO stockPhysicalDO = stockPhysicalManager.getBySkuIdAndWarehouseId(skuId, warehouseId);
        WarehouseDO warehouseDO = warehouseManager.getById(warehouseId);
        if (null == stockPhysicalDO) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                    .setErrorMsg(LogisticsReturnCode.STOCK_SERVICE_ACTUAL_RECORD_NOT_FOUND.getDesc())
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .log();
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_ACTUAL_RECORD_NOT_FOUND,
                    "[供应链-查找商品库存失败]: " + LogisticsReturnCode.STOCK_SERVICE_ACTUAL_RECORD_NOT_FOUND.getDesc()
                            + "[商品ID: " + skuId
                            + ", 仓库ID: " + warehouseId
                            + "]"
            );
        }


        //2.更新实物库存的数量，已实际释放库存为准
        stockPhysicalDO.setAvailableCount(stockPhysicalDO.getAvailableCount() + count);
        stockPhysicalDO.setFreezeCount(stockPhysicalDO.getFreezeCount() - count);
        stockPhysicalManager.update(stockPhysicalDO);

        //3.更新批次库存信息
        for (StockFreezeDO stockFreezeDO : freezeStockDOs) {
            //3.1更新批次库存信息
            StockBatchDO batchStockDO = stockBatchManager.getBySkuIdAndWarehouseIdAndBatchNo(skuId, warehouseId, stockFreezeDO.getBatchNo());

            if (batchStockDO == null) {
                LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                        .setErrorMsg(LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND.getDesc())
                        .addParm("商品ID", skuId)
                        .addParm("仓库ID", warehouseId)
                        .addParm("冻结库存信息", batchStockDO)
                        .log();
                throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND,
                        "[供应链-查找商品库存失败]: " + LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND.getDesc()
                                + "[商品ID: " + skuId
                                + ", 仓库ID: " + warehouseId
                                + ", 冻结库存信息: " + batchStockDO
                                + "]"
                );
            }
            batchStockDO.setAvailableCount(batchStockDO.getAvailableCount() + stockFreezeDO.getFreezeCount());
            batchStockDO.setFreezeCount(batchStockDO.getFreezeCount() - stockFreezeDO.getFreezeCount());
            stockBatchManager.update(batchStockDO);


            //3.2更新冻结库存的状态
            stockFreezeDO.setFreezeState(StockFreezeState.RELEASED.getValue());
            stockFreezeDO.setRealCount(stockFreezeDO.getFreezeCount());
            stockFreezeManager.update(stockFreezeDO);

            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                    .setMsg("[供应链-释放商品库存成功]")
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("冻结库存信息", stockFreezeDO)
                    .log();
        }
    }

    /**
     * 更新实物库存数量
     *
     * @param stockPhysicalDO
     * @param stockinOrderId
     * @param skuBatchStockOperaterEntity
     * @throws ServiceException
     */
    private void updateStockPhysicalCount(StockPhysicalDO stockPhysicalDO,
                                          long stockinOrderId,
                                          SkuBatchStockOperaterEntity skuBatchStockOperaterEntity) throws ServiceException {
        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                .setMsg("[供应链-增加商品库存]: 更新实物库存数量")
                .addParm("商品信息", skuBatchStockOperaterEntity)
                .addParm("入库单ID", stockinOrderId)
                .log();
        // 更新实物库存的破损库存
        stockPhysicalDO.setDamagedCount(stockPhysicalDO.getDamagedCount() + skuBatchStockOperaterEntity.getWearCount());
        stockPhysicalDO.setAvailableCount(stockPhysicalDO.getAvailableCount() + skuBatchStockOperaterEntity.getCount());
        if (null != stockPhysicalDO.getId()) {
            stockPhysicalManager.update(stockPhysicalDO);
        } else {
            stockPhysicalManager.insert(stockPhysicalDO);
        }
    }


}
