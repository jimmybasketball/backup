package com.sfebiz.supplychain.service.stock;

import com.aliyun.openservices.ons.api.Message;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.StockOutPlanType;
import com.sfebiz.supplychain.exposed.stock.api.StockService;
import com.sfebiz.supplychain.exposed.stock.entity.SkuBatchStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.entity.SkuStockOperaterEntity;
import com.sfebiz.supplychain.exposed.stock.entity.StockBatchEntity;
import com.sfebiz.supplychain.exposed.stock.entity.StockPhysicalEntity;
import com.sfebiz.supplychain.exposed.stock.enums.StockBatchStateType;
import com.sfebiz.supplychain.exposed.stock.enums.StockFreezeOrderType;
import com.sfebiz.supplychain.exposed.stock.enums.StockFreezeState;
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
import com.sfebiz.supplychain.queue.MessageConstants;
import com.sfebiz.supplychain.queue.MessageProducer;
import com.sfebiz.supplychain.service.stock.convert.StockPhysicalConvertUtil;
import com.sfebiz.supplychain.util.stock.ComparatorExpirationDate;
import com.sfebiz.supplychain.util.stock.ComparatorStockinDate;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
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
    public CommonRet<Long> createStockBatch(StockBatchEntity stockBatchEntity) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        try {
            StockBatchDO stockBatchDO = new StockBatchDO();
            BeanCopier beanCopier = BeanCopier.create(StockBatchEntity.class, StockBatchDO.class, false);
            beanCopier.copy(stockBatchEntity, stockBatchDO, null);
            //TODO 初始化数据,设置批次库存
            stockBatchDO.setState(StockBatchStateType.ENABLE.value);


            stockBatchManager.insert(stockBatchDO);
            commonRet.setResult(stockBatchDO.getId());
            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("[批次库存]创建批次库存成功").log();
        } catch (Exception e) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR).setException(e).setMsg("[批次库存]创建批次库存异常").log();
        }
        return commonRet;
    }

    /**
     * 冻结库存
     *
     * @param skuStockOperaterEntityList 商品库存操作实体集合
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, List<SkuBatchStockOperaterEntity>> freezeStockBatch(List<SkuStockOperaterEntity> skuStockOperaterEntityList) throws ServiceException {
        //1. 判断参数合法性
        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("[供应链-批量冻结库存信息]:开始").addParm("商品信息", skuStockOperaterEntityList).log();
        if (null == skuStockOperaterEntityList || skuStockOperaterEntityList.size() == 0) {
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL, "[供应链-批量冻结库存信息失败]: "
                    + LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL.getDesc() + "[商品信息: " + skuStockOperaterEntityList + "]"
            );
        }
        //2. 声明商品指定批次集合
        List<SkuStockOperaterEntity> productStockOperaterEntityList = new ArrayList<SkuStockOperaterEntity>();

        for (SkuStockOperaterEntity skuStockOperaterEntity : skuStockOperaterEntityList) {
            if (StringUtils.isNotBlank(skuStockOperaterEntity.getBatchNo())) {
                productStockOperaterEntityList.add(skuStockOperaterEntity);
            }
        }
        //3 初始化需要冻结批次库存
        Map<String, List<SkuBatchStockOperaterEntity>> needFreezeBatchStockMap = new HashMap<String, List<SkuBatchStockOperaterEntity>>();
        //4. 按skuId,stockoutOrderId进行merge和排序
        List<SkuStockOperaterEntity> mergeAndSortResult = mergeAndSortSkuStockOperaterEntityBySkuIdAndStockoutOrderId(skuStockOperaterEntityList);
        //5. 按顺序操作库存
        for (SkuStockOperaterEntity skuStockOperaterEntity : mergeAndSortResult) {
            //5.1 计算出需要冻结的批次库存信息
            List<SkuBatchStockOperaterEntity> skuBatchStockOperaterEntities = checkAndGetNeedFreezeBatchStockByWarehouse(skuStockOperaterEntity);
            //5.2 冻结库存
            checkAndFreezeSkuBatchStock(skuStockOperaterEntity.getSkuId(), skuStockOperaterEntity.getWarehouseId(), skuStockOperaterEntity.getCount()
                    , skuStockOperaterEntity.getStockoutOrderId(), skuBatchStockOperaterEntities);
            //5.3 将商品冻结的批次库存信息返回
            String key = skuStockOperaterEntity.getStockoutOrderId() + "_" + skuStockOperaterEntity.getSkuId();
            needFreezeBatchStockMap.put(key, skuBatchStockOperaterEntities);
        }

        //6. 按顺序更新redis库存
//        for (SkuStockOperaterEntity skuStockOperaterEntity : mergeAndSortResult) {
//            sendRedisSyncMessage(skuStockOperaterEntity.getSkuId());
//        }

        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-批量冻结库存信息]:结束")
                .addParm("商品信息", skuStockOperaterEntityList)
                .addParm("商品冻结的批次库存信息", needFreezeBatchStockMap)
                .log();
        return needFreezeBatchStockMap;
    }

    @Override
    public boolean consumeSkuStockInBatch(List<SkuStockOperaterEntity> skuStockOperaterEntityList, long warehouseId, long stockoutOrderId) throws ServiceException {
        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("[供应链-批量消费商品库存]").addParm("商品信息", skuStockOperaterEntityList)
                .addParm("仓库ID", warehouseId).addParm("出库单ID", stockoutOrderId).log();
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

        //4. 按顺序更新redis库存
        for (SkuStockOperaterEntity skuStockOperaterEntity : mergeAndSortResult) {
            sendRedisSyncMessage(skuStockOperaterEntity.getSkuId());
        }

        return true;
    }

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

//        //4. 按顺序更新redis库存
//        for (SkuStockOperaterEntity skuStockOperaterEntity : mergeAndSortResult) {
//            sendRedisSyncMessage(skuStockOperaterEntity.getSkuId());
//        }

        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-批量释放商品库存成功]")
                .addParm("商品信息", skuStockOperaterEntityList)
                .addParm("仓库ID", warehouseId)
                .addParm("出库单ID", stockoutOrderId)
                .log();
        return true;
    }


    @Override
    public StockPhysicalEntity getBatchStockBySkuIdAndWarehouseId(long skuId, long warehouseId) throws ServiceException {
        if (skuId == 0 || warehouseId == 0) {
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL, "[供应链-查询商品实物库存信息失败]: "
                    + LogisticsReturnCode.STOCK_SERVICE_PARAMS_ILLEGAL.getDesc() + "[商品ID: " + skuId + ", 仓库ID: " + warehouseId);
        }
        try {
            StockPhysicalDO stockPhysicalDOList = stockPhysicalManager.getBySkuIdAndWarehouseId(skuId, warehouseId);
            StockPhysicalEntity stockPhysicalEntity = StockPhysicalConvertUtil.convertToStockPhysicalEntity(stockPhysicalDOList);
            return stockPhysicalEntity;
        } catch (Exception e) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR).setMsg("供应链-查询商品实物库存信息异常").setException(e).log();
        }
        return null;
    }


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
        // 不操作可售库存表
        //  getSaleStockBySkuIdForUpdate(skuStockOperaterEntity.getSkuId());
        //1. 获取仓库中所有符合要求的批次信息
        List<StockBatchDO> stockBatchDOList = stockBatchManager.getBySkuIdAndWarehouseId(skuStockOperaterEntity.getSkuId(), warehouseId);

        if (stockBatchDOList == null || stockBatchDOList.size() == 0) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.WARN).setErrorMsg("[供应链-根据商品ID和仓库ID获取批次库存信息失败]: "
                    + LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND.getDesc()).addParm("商品ID", skuStockOperaterEntity.skuId)
                    .addParm("仓库ID", warehouseId).log();
            throw new ServiceException(LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND, "[供应链-根据商品ID和仓库ID获取批次库存信息失败]: " +
                    LogisticsReturnCode.STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND.getDesc() + "[商品ID:" + skuStockOperaterEntity.skuId + ",仓库ID:" + warehouseId + "]");
        }

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
        List<SkuBatchStockOperaterEntity> needFreezeBatchStocks = new ArrayList<SkuBatchStockOperaterEntity>();
        int remainNeedCount = skuStockOperaterEntity.getCount();
        for (StockBatchDO stockBatchDO : sortRusults) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("[供应链-根据商品出库规则计算所需冻结批次库存]").addParm("批次库存信息", stockBatchDO)
                    .addParm("仓库剩余需冻结数量", remainNeedCount).log();
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
                remainNeedCount -= stockBatchDO.getAvailableCount();
                SkuBatchStockOperaterEntity entity = convertFromBatchStockDO(stockBatchDO);
                needFreezeBatchStocks.add(entity);
            }
        }
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.WARN)
                .setErrorMsg("[供应链-根据商品出库规则计算所需冻结批次库存失败]" + LogisticsReturnCode.LOGISTICS_BATCH_STOCK_SKU_NOT_ENOUGH.getDesc())
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

        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
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
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-冻结商品库存]")
                .addParm("商品ID", skuId)
                .addParm("仓库ID", warehouseId)
                .addParm("出库单ID", stockoutOrderId)
                .addParm("需冻结数量", count)
                .log();
        StockPhysicalDO stockPhysicalDO = stockPhysicalManager.getBySkuIdAndWarehouseId(skuId, warehouseId);

        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-多线程获取后的实物库存信息]")
                .addParm("实物库存信息", stockPhysicalDO)
                .log();

        // 3. 判断库存是否满足, 如果满足，则冻结库存
        if (stockPhysicalDO.getAvailableCount() >= count) {
            stockPhysicalDO.setFreezeCount(stockPhysicalDO.getFreezeCount() + count);
            stockPhysicalDO.setAvailableCount(stockPhysicalDO.getAvailableCount() - count);
            stockPhysicalManager.update(stockPhysicalDO);

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链-冻结商品实物库存成功]")
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("出库单ID", stockoutOrderId)
                    .addParm("需冻结数量", count)
                    .addParm("实物库存信息", stockPhysicalDO)
                    .log();
        } else {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链-冻结商品实物库存失败]: (LOGISTICS_STOCK_SKU_NOT_ENOUGH)" + LogisticsReturnCode.LOGISTICS_STOCK_SKU_NOT_ENOUGH)
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
        for (SkuBatchStockOperaterEntity skuBatchStockOperaterEntity : skuBatchStockOperaterEntities) {
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

            StockBatchDO stockBatchDO = new StockBatchDO();
            stockBatchDO.setId(skuBatchStockOperaterEntity.getId());
            stockBatchDO.setFreezeCount(skuBatchStockOperaterEntity.getFreezeCount());
            stockBatchDO.setAvailableCount(skuBatchStockOperaterEntity.getQualityCount());
            stockBatchManager.update(stockBatchDO);

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
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


    public void sendRedisSyncMessage(long skuId) {
        try {
            Message msg = new Message();
            msg.setTopic(MessageConstants.TOPIC_SUPPLY_CHAIN_EVENT);
            msg.setTag(MessageConstants.TAG_REDIS_SKU_STOCK_INFO);
            String body = skuId + "";
            msg.setBody(body.getBytes());
            Properties properties = new Properties();
            msg.setUserProperties(properties);
            msg.setStartDeliverTime(System.currentTimeMillis() + 1000 * 1);
            supplyChainMessageProducer.send(msg);
            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO)
                    .setMsg("发送Redis商品库存信息同步：消息发送成功")
                    .addParm("商品ID", skuId)
                    .log();
        } catch (Exception e) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                    .setMsg("发送Redis商品库存信息同步：消息发送异常")
                    .addParm("商品ID", skuId)
                    .setException(e)
                    .log();
        }
    }

    protected List<SkuStockOperaterEntity> mergeAndSortSkuStockOperaterEntity(List<SkuStockOperaterEntity> souceEntityList) {
        HashMap<Long, SkuStockOperaterEntity> map = new HashMap();
        // 合并skuId重复的数据
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
        List<SkuStockOperaterEntity> resultList = new ArrayList<SkuStockOperaterEntity>();
        for (SkuStockOperaterEntity stockInOutEntity : map.values()) {
            resultList.add(stockInOutEntity);
        }
        // 根据skuId排序
        Collections.sort(resultList);
        return resultList;
    }

    protected boolean consumeSkuStock(long skuId, long warehouseId, int count, long stockoutOrderId) throws ServiceException {
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-消费商品库存]")
                .addParm("商品ID", skuId)
                .addParm("仓库ID", warehouseId)
                .addParm("商品数目", count)
                .addParm("出库单ID", stockoutOrderId)
                .log();
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
            // 一开始就进行锁，防止并发
            //            getSaleStockBySkuIdForUpdate(skuId);

            //1. 查找待消费的冻结库存
            List<StockFreezeDO> freezeStockList = getStockoutFreezeStockList(skuId, warehouseId, stockoutOrderId);

            //2. 检查冻结库存的信息是否正确
            checkFreezeStockInfoIsCorrect(freezeStockList, count, StockFreezeState.CONSUMED);

            //3. 消费冻结库存
            consumeFreezeStock(skuId, warehouseId, freezeStockList, count);
            return true;
        } catch (ServiceException e) {
            //抛出去
            throw e;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
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
        StockPhysicalDO stockPhysicalDO = stockPhysicalManager.getBySkuIdAndWarehouseId(skuId, warehouseId);
        if (null == stockPhysicalDO) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
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
        //更新实物库存的数量
        stockPhysicalDO.setFreezeCount(stockPhysicalDO.getFreezeCount() - count);
        stockPhysicalManager.update(stockPhysicalDO);

        for (StockFreezeDO freezeStockDO : stockFreezeDOS) {
            //更新批次库存信息
            StockBatchDO stockBatchDO = stockBatchManager.getBySkuIdAndWarehouseIdAndBatchNo(skuId, warehouseId, freezeStockDO.getBatchNo().trim());
            if (stockBatchDO == null) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
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
            //更新冻结库存的状态
            freezeStockDO.setFreezeState(StockFreezeState.CONSUMED.getValue());
            freezeStockDO.setRealCount(freezeStockDO.getFreezeCount());
            stockFreezeManager.update(freezeStockDO);

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
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
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
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
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链-检查商品冻结状态 ]")
                    .addParm("freezeStockDO", freezeStockDO)
                    .addParm("count", count)
                    .addParm("toState", toState)
                    .log();
            totalFreezeCount += freezeStockDO.getFreezeCount();
        }

        if (totalFreezeCount != count) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.WARN)
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
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
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
        StockPhysicalDO stockPhysicalDO = stockPhysicalManager.getBySkuIdAndWarehouseId(skuId, warehouseId);
        WarehouseDO warehouseDO = warehouseManager.getById(warehouseId);
        if (null == stockPhysicalDO) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
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


        //更新实物库存的数量，已实际释放库存为准
        stockPhysicalDO.setAvailableCount(stockPhysicalDO.getAvailableCount() + count);
        stockPhysicalDO.setFreezeCount(stockPhysicalDO.getFreezeCount() - count);
        stockPhysicalManager.update(stockPhysicalDO);

        for (StockFreezeDO stockFreezeDO : freezeStockDOs) {
            //更新批次库存信息
            StockBatchDO batchStockDO = stockBatchManager.getBySkuIdAndWarehouseIdAndBatchNo(skuId, warehouseId, stockFreezeDO.getBatchNo());

            if (batchStockDO == null) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
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


            //更新冻结库存的状态
            stockFreezeDO.setFreezeState(StockFreezeState.RELEASED.getValue());
            stockFreezeDO.setRealCount(stockFreezeDO.getFreezeCount());
            stockFreezeManager.update(stockFreezeDO);

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链-释放商品库存成功]")
                    .addParm("商品ID", skuId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("冻结库存信息", stockFreezeDO)
                    .log();
        }

    }

} 
