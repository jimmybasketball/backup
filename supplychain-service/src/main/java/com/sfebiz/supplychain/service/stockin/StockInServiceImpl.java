package com.sfebiz.supplychain.service.stockin;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.generator.UniqueCodeGenerator;
import com.sfebiz.common.utils.generator.UniqueNumberGenerator;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.enums.BatchGeneratePlanType;
import com.sfebiz.supplychain.exposed.sku.api.SkuService;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.exposed.stockinorder.api.StockInService;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailEntity;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderEntity;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderState;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderType;
import com.sfebiz.supplychain.lock.DistributedLock;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderLineDO;
import com.sfebiz.supplychain.persistence.base.merchant.manager.MerchantProviderLineManager;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDetailDO;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderDetailManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderStateLogManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.statemachine.BpmConstants;
import com.sfebiz.supplychain.service.statemachine.EngineService;
import com.sfebiz.supplychain.service.statemachine.Operator;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderActionType;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequest;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequestFactory;
import net.pocrd.entity.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangyajing on 2017/7/17.
 */
@Service("stockInService")
@Transactional(rollbackFor = Exception.class)
public class StockInServiceImpl implements StockInService{
    private static Logger logger = LoggerFactory.getLogger(StockInServiceImpl.class);
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("stockinorder");
    private final static ModelMapper modelMapper = new ModelMapper();

    private static final String UPDATE_STOCKIN_ORDER_SKUS_KEY = "UPDATE_STOCKIN_ORDER_SKUS_KEY:";

    @Resource
    private WarehouseManager warehouseManager;
    @Autowired
    private DistributedLock distributedLock;
    @Resource
    private MerchantProviderLineManager merchantProviderLineManager;
    @Resource
    EngineService engineService;
    @Resource
    StockinOrderStateLogManager stockinOrderStateLogManager;
    @Resource
    StockinOrderManager stockinOrderManager;
    @Resource
    SkuService skuService;
    @Resource
    StockinOrderDetailManager stockinOrderDetailManager;

    static {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @MethodParamValidate
    public CommonRet<List<Long>> createStockinOrder (
            @ParamNotBlank("请求不能为空") StockinOrderEntity stockinOrderEntity, Long userId, String userName) {
        CommonRet<List<Long>> commonRet = new CommonRet<List<Long>>();
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台-创建入库单]")
                .addParm("入库单信息", stockinOrderEntity)
                .addParm("操作人", userName)
                .log();

        Long stockinOrderId = null;

        if(CollectionUtils.isEmpty(stockinOrderEntity.getDetailEntities())) {
            commonRet.setRetCode(SCReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg("[物流平台-创建入库单]：商品明细不能为空");
            return commonRet;
        }

        WarehouseDO warehouseDO = warehouseManager.getById(stockinOrderEntity.getWarehouseId());
        if (null == warehouseDO) {
            commonRet.setRetCode(SCReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg("[物流平台-创建入库单]:仓库不存在，warehouseId：" + stockinOrderEntity.getWarehouseId());
            return commonRet;
        }

        List<MerchantProviderLineDO> merchantProviderLineDOList = merchantProviderLineManager.getByProviderAndWarehouse(stockinOrderEntity.getMerchantProviderId(), stockinOrderEntity.getWarehouseId());
        if(CollectionUtils.isEmpty(merchantProviderLineDOList)){
            commonRet.setRetCode(SCReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg("[物流平台-创建入库单]:仓库"+stockinOrderEntity.getWarehouseId() + "不属于供应商" + stockinOrderEntity.getMerchantProviderId());
            return commonRet;
        }

        try {
            StockinOrderDO stockinOrderDO = modelMapper.map(stockinOrderEntity, StockinOrderDO.class);
            stockinOrderDO.setStockinId(UniqueNumberGenerator.getUniqueNo("RK"));
            stockinOrderDO.setState(BpmConstants.NULL_STATE);
            stockinOrderManager.insert(stockinOrderDO);
            stockinOrderId = stockinOrderDO.getId();

            //更新入库单明细
            CommonRet<List<Long>> detailResult = updateStockinOrderDetails(stockinOrderId, stockinOrderEntity.getDetailEntities(), userId, userName);
            if (detailResult.getRetCode() == SCReturnCode.COMMON_FAIL.getCode()) {
                throw new ServiceException(SCReturnCode.STOCKIN_ORDER_INNER_EXCEPTION,"入库单明细更新失败");
            }
            if (CollectionUtils.isNotEmpty(detailResult.getResult())) {
                commonRet.setResult(detailResult.getResult());
            }
            //启动状态机引擎
            StockinOrderRequest stockinOrderRequest = StockinOrderRequestFactory.generateStockinOrderRequest(
                    StockinOrderActionType.STOCKIN_TO_CREATE, stockinOrderDO, null, Operator.valueOf(userId, userName));
            engineService.startStateMachineEngine(stockinOrderRequest);

            //更新入库单状态日志记录
            stockinOrderStateLogManager.insertOrUpdate(stockinOrderId, userId, userName, StockinOrderState.TO_BE_SUBMITED.getValue());
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, "supplychain"))
                    .setMsg("[物流平台-创建入库单成功]")
                    .addParm("入库单信息", stockinOrderEntity)
                    .addParm("操作者", userName)
                    .log();
        } catch (Exception e) {
            commonRet.setRetCode(SCReturnCode._C_COMMON_FAIL);
            commonRet.setRetMsg(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        commonRet.setRetMsg(SCReturnCode.COMMON_SUCCESS.getDesc());
        return commonRet;
    }

    @Override
    public CommonRet<List<Long>> updateStockinOrderDetails(Long stockinOrderId, List<StockinOrderDetailEntity> stockinOrderDetailEntities, Long userId, String userName){
        CommonRet<List<Long>> commonRet = new CommonRet<List<Long>>();
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台-更新入库单明细]")
                .addParm("入库单ID", stockinOrderId)
                .addParm("明细", stockinOrderDetailEntities)
                .addParm("操作者", userName)
                .log();

        if ( null == stockinOrderId) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台-更新入库单明细异常]: " + SCReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(SCReturnCode._C_COMMON_FAIL);
            commonRet.setRetMsg("更新入库单明细异常");
            return commonRet;
        }

        //1. 控制并发
        if (distributedLock.fetch(UPDATE_STOCKIN_ORDER_SKUS_KEY + stockinOrderId)) {
            try {
                //1. 验证入库单是否存在
                StockinOrderDO stockinOrderDO = checkStockinOrderById(stockinOrderId);

                //2. 更新sku信息
                commonRet.setResult(updateStockinOrderDetail(stockinOrderDetailEntities, stockinOrderDO));

                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId.toString(), SystemConstants.TRACE_APP))
                        .setMsg("[物流平台-更新入库单明细成功]")
                        .addParm("入库单ID", stockinOrderId.toString())
                        .addParm("商品信息", stockinOrderDetailEntities)
                        .addParm("操作者", userName)
                        .log();
            } catch (Exception e) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId.toString(), SystemConstants.TRACE_APP))
                        .setErrorMsg("[物流平台-更新入库单异常]")
                        .addParm("入库单ID", stockinOrderId.toString())
                        .addParm("商品信息", stockinOrderDetailEntities)
                        .addParm("操作者", userName)
                        .setException(e)
                        .log();
                commonRet.setRetCode(SCReturnCode._C_COMMON_FAIL);
                commonRet.setRetMsg(e.getMessage());
                return commonRet;
            } finally {
                distributedLock.realease(UPDATE_STOCKIN_ORDER_SKUS_KEY + stockinOrderId);
            }
        }
        commonRet.setRetCode(SCReturnCode._C_COMMON_SUCCESS);
        return commonRet;
    }

    protected StockinOrderDO checkStockinOrderById(Long stockinOrderId) throws ServiceException {
        if (null == stockinOrderId) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链-入库单操作失败]: " + SCReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .log();
            throw new ServiceException(SCReturnCode.PARAM_ILLEGAL_ERR);
        }
        StockinOrderDO stockinOrderDO = stockinOrderManager.getById(stockinOrderId);
        if (null == stockinOrderDO) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-入库单操作失败]: " + SCReturnCode.STOCKIN_ORDER_NOT_EXIST.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .log();
            throw new ServiceException(SCReturnCode.STOCKIN_ORDER_INNER_EXCEPTION);
        }
        return stockinOrderDO;
    }

    /**
     *
     * @param stockinOrderDetailEntities
     * @param stockinOrderDO
     * @return 入库单明细中重复的商品
     * @throws Exception
     */
    private List<Long> updateStockinOrderDetail(List<StockinOrderDetailEntity> stockinOrderDetailEntities, StockinOrderDO stockinOrderDO) throws Exception {
        Map<String, SkuEntity> skuEntityMap = new HashMap<String, SkuEntity>();
        if (CollectionUtils.isEmpty(stockinOrderDetailEntities)) {
            return null;
        }

        //校验sku
        List<Long> skuList = new ArrayList<Long>();
        List<StockinOrderDetailEntity> detailAfterMerge = new ArrayList<StockinOrderDetailEntity>();
        for (StockinOrderDetailEntity stockinOrderDetailEntity : stockinOrderDetailEntities) {
            checkStockinOrderDetails(skuEntityMap, stockinOrderDetailEntity,detailAfterMerge, skuList);
        }

        /**
         * 创建新增 更新已有
         */
        for (StockinOrderDetailEntity stockinOrderDetailEntity : detailAfterMerge) {
            try {
                if (null == stockinOrderDetailEntity.id || stockinOrderDetailEntity.id.equals(0L)) {
                    List<StockinOrderDetailEntity> tmp = new ArrayList<StockinOrderDetailEntity>();
                    tmp.add(stockinOrderDetailEntity);
                    insertStockinOrderDetails(stockinOrderDO, skuEntityMap, tmp);
                } else {
                    StockinOrderDetailDO stockinOrderDetailDO =  modelMapper.map(stockinOrderDetailEntity, StockinOrderDetailDO.class);
                    stockinOrderDetailManager.update(stockinOrderDetailDO);
                }
            } catch (Exception e) {
                throw new Exception("更新入库单明细：" + stockinOrderDetailEntity.skuId + "信息失败，原因" + e.getMessage());
            }
        }
        return skuList;
    }

    /**
     * 目前只考虑采购入库单，若有新类型入库单，请更新注释
     * @param skuEntityMap 检查后的商品信息列表
     * @param stockinOrderDetailEntity 待检查的入库单明细
     * @param detailAfterMerge 检查后的入库单明细列表
     * @param repeatSku 检查后重复的skuid集合
     * @throws ServiceException
     */
    protected void checkStockinOrderDetails(Map<String, SkuEntity> skuEntityMap, StockinOrderDetailEntity stockinOrderDetailEntity, List<StockinOrderDetailEntity> detailAfterMerge, List<Long> repeatSku) throws ServiceException {
        if (null == stockinOrderDetailEntity) {
            throw new ServiceException(SCReturnCode.PARAM_ILLEGAL_ERR, "入库单明细为空");
        }
        if (null == stockinOrderDetailEntity.skuId || stockinOrderDetailEntity.skuId.equals(0L)) {
            throw new ServiceException(SCReturnCode.PARAM_ILLEGAL_ERR, "入库单的商品" + stockinOrderDetailEntity.skuName + "id为空");
        }
        Long skuId = stockinOrderDetailEntity.skuId;
        CommonRet<SkuEntity> commonRet = new CommonRet<SkuEntity>();
        commonRet = skuService.getSkuOnlySkuInfo(skuId);
        if (commonRet.getRetCode().equals(SCReturnCode._C_COMMON_FAIL)) {
            throw new ServiceException(SCReturnCode.STOCKIN_ORDER_INNER_EXCEPTION, "调用商品查询接口异常" + commonRet.getRetMsg());
        }
        SkuEntity skuEntity = commonRet.getResult();
        if (null == skuEntity) {
            throw new ServiceException(SCReturnCode.STOCKIN_ORDER_INNER_EXCEPTION, "商品(" + skuId + ")不存在");
        }
        if (skuEntityMap.containsKey(skuId+"")) {
            repeatSku.add(skuId);
        } else {
            skuEntityMap.put(skuId+"", skuEntity);
            detailAfterMerge.add(stockinOrderDetailEntity);
        }
    }

    private void insertStockinOrderDetails(StockinOrderDO stockinOrderDO, Map<String, SkuEntity> skuEntityMap, List<StockinOrderDetailEntity> skus) throws Exception {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            for (StockinOrderDetailEntity stockinOrderDetailEntity : skus) {
                Long skuId = stockinOrderDetailEntity.skuId;
                SkuEntity skuEntity = skuEntityMap.get(skuId + "");
                boolean isSaleStockIn = false;//判断是否未采购入库单
                if (StockinOrderType.SALES_STOCK_IN.getValue() == stockinOrderDO.getType()) {
                    isSaleStockIn = true;
                }

                StockinOrderDetailDO stockinOrderDetailDO = modelMapper.map(stockinOrderDetailEntity, StockinOrderDetailDO.class);
                stockinOrderDetailDO.setStockinOrderId(stockinOrderDO.getId());
                if (isSaleStockIn) {
                    String ymd = simpleDateFormat.format(new Date());
                    String skuBatch = ymd + "-" + SystemConstants.CUSTOMER_CODE + "-" + stockinOrderDO.getMerchantProviderId();
                    if (BatchGeneratePlanType.EXPIRE_SAME.getValue().equals(skuEntity.batchGeneratePlan)) {
                        skuBatch += "-EXP" + UniqueCodeGenerator.getUniquCode();
                        stockinOrderDetailDO.setBatchGeneratePlan(BatchGeneratePlanType.EXPIRE_SAME.getValue());
                    } else {
                        skuBatch += "-STOCKIN" + UniqueCodeGenerator.getUniquCode();
                        stockinOrderDetailDO.setBatchGeneratePlan(BatchGeneratePlanType.STOCKIN_SAME.getValue());
                    }
                    stockinOrderDetailDO.setSkuBatch(skuBatch);
                }
                if (StringUtils.isEmpty(skuEntity.batchGeneratePlan)) {
                    skuEntity.setBatchGeneratePlan(BatchGeneratePlanType.STOCKIN_SAME.getValue());
                }
                if (StringUtils.isNotBlank(stockinOrderDetailDO.getSkuBatch())) {
                    stockinOrderDetailDO.setSkuBatch(stockinOrderDetailDO.getSkuBatch().trim().replaceAll("\r\n", ""));
                }
                stockinOrderDetailManager.insert(stockinOrderDetailDO);
            }
        } catch (Exception e) {
            throw new ServiceException(SCReturnCode.STOCKIN_ORDER_INNER_EXCEPTION,"更新入库单" + stockinOrderDO.getId() + "明细失败," + "失败原因" + e.getMessage());
        }
    }
}
