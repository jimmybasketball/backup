package com.sfebiz.supplychain.service.stockin;

import com.sfebiz.common.dao.domain.BaseQuery;
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
import com.sfebiz.supplychain.exposed.common.code.StockInReturnCode;
import com.sfebiz.supplychain.exposed.common.code.WarehouseReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.common.enums.BatchGeneratePlanType;
import com.sfebiz.supplychain.exposed.sku.enums.SkuWarehouseSyncStateType;
import com.sfebiz.supplychain.exposed.stock.entity.StockBatchEntity;
import com.sfebiz.supplychain.exposed.stockinorder.api.StockInService;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailCargoResultEntity;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailEntity;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderEntity;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderState;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsOperaterType;
import com.sfebiz.supplychain.lock.DistributedLock;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderLineDO;
import com.sfebiz.supplychain.persistence.base.merchant.manager.MerchantProviderLineManager;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuBarcodeDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuBarcodeManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncManager;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;
import com.sfebiz.supplychain.persistence.base.stock.manager.StockBatchManager;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDetailDO;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderFileDO;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderDetailManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderFileManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderStateLogManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.provider.biz.SkuSyncBizService;
import com.sfebiz.supplychain.service.statemachine.BpmConstants;
import com.sfebiz.supplychain.service.statemachine.EngineService;
import com.sfebiz.supplychain.service.statemachine.Operator;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderActionType;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequest;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequestFactory;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseLogisticsProviderBO;
import com.sfebiz.supplychain.util.DateUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangyajing on 2017/7/17.
 */
@Service("stockInService")
public class StockInServiceImpl implements StockInService{
    private static Logger logger = LoggerFactory.getLogger(StockInServiceImpl.class);
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("stockinorder");
    private final static ModelMapper modelMapper = new ModelMapper();

    private static final String UPDATE_STOCKIN_ORDER_SKUS_KEY = "UPDATE_STOCKIN_ORDER_SKUS_KEY:";
    private static final String SUBMIT_STOCKIN_ORDER_KEY = "SUBMIT_STOCKIN_ORDER_KEY:";
    private static final String FINISH_STOCKIN_ORDER_KEY = "FINISH_STOCKIN_ORDER_KEY:";
    private static final String UPDATE_STOCKIN_ORDER_KEY = "UPDATE_STOCKIN_ORDER_KEY:";
    private static final String CANCEL_STOCKIN_ORDER_KEY = "CANCEL_STOCKIN_ORDER_KEY:";

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
    SkuManager skuManager;
    @Resource
    StockinOrderDetailManager stockinOrderDetailManager;
    @Resource
    StockinOrderFileManager stockinOrderFileManager;
    @Resource
    SkuBarcodeManager skuBarcodeManager;
    @Resource
    StockBatchManager stockBatchManager;
    @Resource
    SkuWarehouseSyncManager skuWarehouseSyncManager;
    @Resource
    SkuSyncBizService skuSyncBizService;

    static {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @MethodParamValidate
    @Transactional(rollbackFor = Exception.class)
    public CommonRet<Long> createStockinOrder (
            @ParamNotBlank("请求不能为空") StockinOrderEntity stockinOrderEntity, Long userId, String userName) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台-创建入库单]")
                .addParm("入库单信息", stockinOrderEntity)
                .addParm("操作人", userName)
                .log();

        Long stockinOrderId = null;

        if(CollectionUtils.isEmpty(stockinOrderEntity.getDetailEntities())) {
            commonRet.setRetCode(StockInReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg("[物流平台-创建入库单]：商品明细不能为空");
            return commonRet;
        }

        WarehouseDO warehouseDO = warehouseManager.getById(stockinOrderEntity.getWarehouseId());
        if (null == warehouseDO) {
            commonRet.setRetCode(StockInReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg("[物流平台-创建入库单]:仓库不存在，warehouseId：" + stockinOrderEntity.getWarehouseId());
            return commonRet;
        }

        List<MerchantProviderLineDO> merchantProviderLineDOList = merchantProviderLineManager.getByProviderAndWarehouse(stockinOrderEntity.getMerchantId(), stockinOrderEntity.getMerchantProviderId(), stockinOrderEntity.getWarehouseId());
        if(CollectionUtils.isEmpty(merchantProviderLineDOList)){
            commonRet.setRetCode(StockInReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg("[物流平台-创建入库单]:仓库"+stockinOrderEntity.getWarehouseId() + "不属于供应商" + stockinOrderEntity.getMerchantProviderId() + "货主" + stockinOrderEntity.getMerchantId());
            return commonRet;
        }

        try {
            StockinOrderDO stockinOrderDO = modelMapper.map(stockinOrderEntity, StockinOrderDO.class);
            stockinOrderDO.setStockinId("RK" + UniqueNumberGenerator.getUniqueNo("batchrk"));
            stockinOrderDO.setState(BpmConstants.NULL_STATE);
            stockinOrderManager.insert(stockinOrderDO);
            stockinOrderId = stockinOrderDO.getId();

            //更新入库单明细
            CommonRet<Void> detailResult = updateStockinOrderDetails(stockinOrderId, stockinOrderEntity.getDetailEntities(), userId, userName);
            if (detailResult.getRetCode() == StockInReturnCode.COMMON_FAIL.getCode()) {
                throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION,"入库单明细更新失败");
            }
            //启动状态机引擎
            StockinOrderRequest stockinOrderRequest = StockinOrderRequestFactory.generateStockinOrderRequest(
                    StockinOrderActionType.STOCKIN_TO_CREATE, stockinOrderDO, null, null, Operator.valueOf(userId, userName));
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
            commonRet.setRetCode(StockInReturnCode._C_COMMON_FAIL);
            commonRet.setRetMsg(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return commonRet;
        }
        commonRet.setRetCode(StockInReturnCode.COMMON_SUCCESS.getCode());
        commonRet.setRetMsg(StockInReturnCode.COMMON_SUCCESS.getDesc());
        commonRet.setResult(stockinOrderId);
        return commonRet;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonRet<Void> updateStockinOrderDetails(Long stockinOrderId, List<StockinOrderDetailEntity> stockinOrderDetailEntities, Long userId, String userName){
        CommonRet<Void> commonRet = new CommonRet<Void>();
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
                    .setErrorMsg("[物流平台-更新入库单明细异常]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode._C_COMMON_FAIL);
            commonRet.setRetMsg("更新入库单明细异常");
            return commonRet;
        }

        try {
            //1. 控制并发
            if (distributedLock.fetch(UPDATE_STOCKIN_ORDER_SKUS_KEY + stockinOrderId)) {
                //1. 验证入库单是否存在
                StockinOrderDO stockinOrderDO = checkStockinOrderById(stockinOrderId);

                //2. 更新sku信息
                updateStockinOrderDetail(stockinOrderDetailEntities, stockinOrderDO);

                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId.toString(), SystemConstants.TRACE_APP))
                        .setMsg("[物流平台-更新入库单明细成功]")
                        .addParm("入库单ID", stockinOrderId.toString())
                        .addParm("商品信息", stockinOrderDetailEntities)
                        .addParm("操作者", userName)
                        .log();
            } else {
                commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
                commonRet.setRetMsg("[物流平台-更新入库单并发异常]");
                return commonRet;
            }
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
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
            commonRet.setRetMsg(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return commonRet;
        } finally {
            distributedLock.realease(UPDATE_STOCKIN_ORDER_SKUS_KEY + stockinOrderId);
        }

        commonRet.setRetCode(StockInReturnCode._C_COMMON_SUCCESS);
        return commonRet;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonRet<Void> submitStockinOrder(Long stockinOrderId, Long userId, String userName) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台-提交入库单]")
                .addParm("入库单ID", stockinOrderId)
                .addParm("操作者", userName)
                .log();

        if (null == stockinOrderId) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台-提交入库单异常]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.PARAM_ILLEGAL_ERR.getCode());
            commonRet.setRetMsg(StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc());
            return commonRet;
        }

        //1.控制并发
        try {
            if (distributedLock.fetch(SUBMIT_STOCKIN_ORDER_KEY + stockinOrderId)) {
                //1. 检查入库单是否存在
                StockinOrderDO stockinOrderDO = checkStockinOrderById(stockinOrderId);
                //2. 只有仓库未回传的入库单可以重复提交
                if (null != stockinOrderDO.getWarehouseStockinTime()
                        || stockinOrderDO.getState().equalsIgnoreCase(StockinOrderState.STOCKIN_FINISH.getValue())
                        || stockinOrderDO.getState().equalsIgnoreCase(StockinOrderState.STOCKIN_CANCLE.getValue())) {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.ERROR)
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                            .setErrorMsg("[物流平台-提交入库单异常]: " + StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_FINISH.getDesc())
                            .addParm("入库单ID", stockinOrderId)
                            .addParm("操作者", userName)
                            .log();
                    commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_FINISH.getCode());
                    commonRet.setRetMsg(StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_FINISH.getDesc());
                    return commonRet;
                }
                //3. 如果是待提交状态，则走流程引擎
                if (stockinOrderDO.getState().equalsIgnoreCase(StockinOrderState.TO_BE_SUBMITED.getValue())) {
                    StockinOrderRequest stockinOrderRequest = StockinOrderRequestFactory.generateStockinOrderRequest(
                            StockinOrderActionType.STOCKIN_TO_SUBMIT, stockinOrderDO, null, null, Operator.valueOf(userId, userName));
                    engineService.executeStateMachineEngine(stockinOrderRequest, false);
                }
            }else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                        .setErrorMsg("[物流平台-提交入库单异常]")
                        .addParm("入库单ID", stockinOrderId)
                        .addParm("操作者", userName)
                        .log();
                commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
                commonRet.setRetMsg("[物流平台-提交入库单异常]: 并发异常");
                return commonRet;
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                    .setErrorMsg("[物流平台-提交入库单异常]")
                    .setException(e)
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
            commonRet.setRetMsg(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getDesc());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return commonRet;
        } finally {
            distributedLock.realease(SUBMIT_STOCKIN_ORDER_KEY + stockinOrderId);
        }
        commonRet.setRetCode(StockInReturnCode.COMMON_SUCCESS.getCode());
        commonRet.setRetMsg(StockInReturnCode.COMMON_SUCCESS.getDesc());
        return commonRet;
    }

    @Override
    public CommonRet<Void> importStockinOrderTallyReport(Long stockinOrderId, List<StockinOrderDetailCargoResultEntity> stockinOrderSkuCargoResultEntities, Long userId, String userName) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setErrorMsg("[物流平台-导入入库单理货报告]")
                .addParm("入库单ID", stockinOrderId)
                .addParm("理货报告", stockinOrderSkuCargoResultEntities)
                .addParm("操作者", userName)
                .log();

        CommonRet<Void> commonRet = new CommonRet<Void>();
        if (null == stockinOrderId || null == stockinOrderSkuCargoResultEntities || stockinOrderSkuCargoResultEntities.size() == 0) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台-导入入库单理货报告异常]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("理货报告", stockinOrderSkuCargoResultEntities)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.PARAM_ILLEGAL_ERR.getCode());
            commonRet.setRetMsg("[物流平台-导入入库单理货报告异常]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc());
            return commonRet;
        }
        try {
            //更新sku
            for (StockinOrderDetailCargoResultEntity entity : stockinOrderSkuCargoResultEntities) {
                StockinOrderDetailDO detailDO = stockinOrderDetailManager.getByStockinOrderIdAndSkuId(entity.getStockinOrderId(), entity.getSkuId());
                if (null != detailDO) {
                    entity.setRealDiffCount(detailDO.getCount() - entity.getRealCount() - entity.getBadRealCount());
                    updateStockinOrderSku(entity);
                } else {
                    commonRet.setRetCode(StockInReturnCode.PARAM_ILLEGAL_ERR.getCode());
                    commonRet.setRetMsg("[物流平台-未找到入库单明细]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc());
                    return commonRet;
                }
            }
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                    .setMsg("[物流平台-导入入库单理货报告成功]")
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("理货报告", stockinOrderSkuCargoResultEntities)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.COMMON_SUCCESS.getCode());
            commonRet.setRetMsg(StockInReturnCode.COMMON_SUCCESS.getDesc());
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                    .setErrorMsg("[物流平台-导入入库单理货报告异常]: " + e.getMessage())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("理货报告", stockinOrderSkuCargoResultEntities)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
            commonRet.setRetMsg("[物流平台-导入入库单理货报告异常]: " + StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getDesc());
            return commonRet;
        }
    }

    @Override
    public CommonRet<Long> addFileToStockinOrder(Long stockinOrderId, String fileName, String url, Long userId, String userName) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setErrorMsg("[物流平台-添加入库单文档]")
                .addParm("入库单ID", stockinOrderId)
                .addParm("文件名", fileName)
                .addParm("文件路径", url)
                .addParm("操作者", userName)
                .log();

        CommonRet<Long> commonRet = new CommonRet<Long>();
        if (null == stockinOrderId) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台-添加入库单文档异常]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("文件名", fileName)
                    .addParm("文件路径", url)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.PARAM_ILLEGAL_ERR.getCode());
            commonRet.setRetMsg("[物流平台-添加入库单文档异常]:" + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc());
            return commonRet;
        }

        try {
            StockinOrderFileDO stockinOrderFileDO = new StockinOrderFileDO();
            stockinOrderFileDO.setStockinOrderId(stockinOrderId);
            stockinOrderFileDO.setFileName(fileName);
            stockinOrderFileDO.setUrl(url);
            stockinOrderFileDO.setUserId(userId);
            stockinOrderFileDO.setUserName(userName);
            stockinOrderFileManager.insert(stockinOrderFileDO);

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                    .setMsg("[物流平台-添加入库单文档成功]")
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("文件名", fileName)
                    .addParm("文件路径", url)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.COMMON_SUCCESS.getCode());
            commonRet.setRetMsg(StockInReturnCode.COMMON_SUCCESS.getDesc());
            commonRet.setResult(stockinOrderFileDO.getId());
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                    .setErrorMsg("[物流平台-添加入库单文档异常]: " + e.getMessage())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("文件名", fileName)
                    .addParm("文件路径", url)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
            commonRet.setRetMsg("[物流平台-添加入库单文档异常]:" + StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getDesc());
            return commonRet;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonRet<Void> saveSkusInfo(Long stockinOrderId, List<StockinOrderDetailEntity> stockinOrderDetailEntityList, Long userId, String userName) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-手动填写理货差异]")
                .addParm("理货差异", stockinOrderDetailEntityList)
                .addParm("入库单ID", stockinOrderId)
                .addParm("操作者", userName)
                .log();

        CommonRet<Void> commonRet = new CommonRet<Void>();
        if (null == stockinOrderId || null == stockinOrderDetailEntityList || stockinOrderDetailEntityList.size() == 0) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链-手动填写理货差异异常]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("理货差异", stockinOrderDetailEntityList)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(SCReturnCode.PARAM_ILLEGAL_ERR.getCode());
            commonRet.setRetMsg(SCReturnCode.PARAM_ILLEGAL_ERR.getDesc());
        }

        try {
            for (StockinOrderDetailEntity stockinOrderDetailEntity : stockinOrderDetailEntityList) {
                StockinOrderDetailDO stockinOrderDetailDO = modelMapper.map(stockinOrderDetailEntity, StockinOrderDetailDO.class);
                stockinOrderDetailManager.update(stockinOrderDetailDO);
            }
            commonRet.setRetCode(SCReturnCode.COMMON_SUCCESS.getCode());
            commonRet.setRetMsg(SCReturnCode.COMMON_SUCCESS.getDesc());
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-手动填写理货差异异常]: " + e.getMessage())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("理货差异", stockinOrderDetailEntityList)
                    .addParm("操作者", userName)
                    .log();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
            commonRet.setRetMsg(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getDesc());
            return commonRet;
        }
    }

    @Override
    public CommonRet<Void> finishStockinOrder(Long stockinOrderId, Long warehouseId, List<StockinOrderDetailEntity> stockinOrderDetailEntities, Long userId, String userName) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-手工入库完成]")
                .addParm("入库单ID", stockinOrderId)
                .addParm("仓库ID", warehouseId)
                .addParm("商品详细信息", stockinOrderDetailEntities)
                .addParm("操作者", userName)
                .log();

        CommonRet<Void> commonRet = new CommonRet<Void>();
        if (null == stockinOrderId) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链-手工入库完成异常]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.PARAM_ILLEGAL_ERR.getCode());
            commonRet.setRetMsg(StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc());
            return commonRet;
        }
        try {
            //1.  锁
            if (distributedLock.fetch(FINISH_STOCKIN_ORDER_KEY + stockinOrderId)) {
                // 2. 初始化事务
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                StockinOrderDO stockinOrderDO = null;


                //3.  逻辑处理
                stockinOrderDO = checkStockinOrderById(stockinOrderId);
                StockinOrderRequest stockinOrderRequest = StockinOrderRequestFactory.generateStockinOrderRequest(StockinOrderActionType.STOCKIN_TO_FINISH,
                        stockinOrderDO, stockinOrderDetailEntities, null, Operator.valueOf(userId, userName));
                engineService.executeStateMachineEngine(stockinOrderRequest, false);
            } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                        .setErrorMsg("[供应链-手工完成入库单异常]: 并发异常")
                        .addParm("入库单ID", stockinOrderId)
                        .addParm("仓库ID", warehouseId)
                        .addParm("商品详细信息", stockinOrderDetailEntities)
                        .addParm("操作者", userName)
                        .log();
                commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
                commonRet.setRetMsg(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getDesc());
                return commonRet;
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-手工完成入库单异常]")
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("商品详细信息", stockinOrderDetailEntities)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
            commonRet.setRetMsg(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getDesc());
            return commonRet;
        } finally {
            // 释放锁
            distributedLock.realease(FINISH_STOCKIN_ORDER_KEY + stockinOrderId);
        }
        commonRet.setRetCode(StockInReturnCode.COMMON_SUCCESS.getCode());
        commonRet.setRetMsg(StockInReturnCode.COMMON_SUCCESS.getDesc());
        return commonRet;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonRet<Void> updateStockinOrderBaseInfo(StockinOrderEntity stockinOrderEntity, Long userId, String userName) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-更新入库单基本信息]")
                .addParm("入库单信息", stockinOrderEntity)
                .addParm("操作者", userName)
                .log();

        CommonRet<Void> commonRet = new CommonRet<Void>();
        if (null == stockinOrderEntity || null == stockinOrderEntity.id) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链-更新入库单基本信息异常]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单信息", stockinOrderEntity)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.PARAM_ILLEGAL_ERR.getCode());
            commonRet.setRetMsg("[供应链-更新入库单基本信息异常]:"+StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc());
            return commonRet;
        }


        try {
            if (distributedLock.fetch(UPDATE_STOCKIN_ORDER_KEY + stockinOrderEntity.id)) {
                StockinOrderDO stockinOrderDO = checkStockinOrderById(stockinOrderEntity.id);
                //只要不是已完成状态,预计到港时间,预计发货时间都允许更新
                if (!StockinOrderState.STOCKIN_FINISH.value.equals(stockinOrderDO.getState())
                        && !StockinOrderState.STOCKIN_CANCLE.value.equals(stockinOrderDO.getState())) {
                    stockinOrderDO = modelMapper.map(stockinOrderEntity, StockinOrderDO.class);
                    stockinOrderManager.update(stockinOrderDO);
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.INFO)
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderEntity.getId(), SystemConstants.TRACE_APP))
                            .setErrorMsg("[供应链-更新入库单基本信息成功]")
                            .addParm("入库单信息", stockinOrderEntity)
                            .addParm("操作者", userName)
                            .log();

                } else {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.INFO)
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderEntity.getId(), SystemConstants.TRACE_APP))
                            .setErrorMsg("[供应链-更新入库单基本信息异常]: " + StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_UPDATE.getDesc())
                            .addParm("入库单信息", stockinOrderEntity)
                            .addParm("操作者", userName)
                            .log();

                    throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_UPDATE,
                            "[供应链-更新入库单基本信息异常]: " + StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_UPDATE.getDesc());
                }
            } else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderEntity.id, SystemConstants.TRACE_APP))
                        .setErrorMsg("[供应链-更新入库单基本信息异常]: 并发异常")
                        .addParm("入库单信息", stockinOrderEntity)
                        .addParm("操作者", userName)
                        .log();
                commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
                commonRet.setRetMsg("[供应链-更新入库单基本信息异常]: 并发异常" + StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getDesc());
                return commonRet;
            }
        }catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderEntity.getId(), SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-更新入库单基本信息异常]")
                    .addParm("入库单信息", stockinOrderEntity)
                    .addParm("操作者", userName)
                    .setException(e)
                    .log();
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
            commonRet.setRetMsg("[供应链-更新入库单基本信息异常]: " + StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getDesc());
            return commonRet;
        } finally {
            distributedLock.realease(UPDATE_STOCKIN_ORDER_KEY + stockinOrderEntity.id);
        }
        commonRet.setRetCode(StockInReturnCode.COMMON_SUCCESS.getCode());
        commonRet.setRetMsg(StockInReturnCode.COMMON_SUCCESS.getDesc());
        return commonRet;
    }

    @Override
    public CommonRet<Void> cancelStockinOrder(Long stockinOrderId, Long userId, String userName) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-取消入库单]")
                .addParm("入库单ID", stockinOrderId)
                .addParm("操作者", userName)
                .log();

        CommonRet<Void> commonRet = new CommonRet<Void>();
        commonRet.setRetCode(StockInReturnCode.COMMON_SUCCESS.getCode());
        commonRet.setRetMsg(StockInReturnCode.COMMON_SUCCESS.getDesc());
        if (null == stockinOrderId) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链-取消入库单异常]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.PARAM_ILLEGAL_ERR.getCode());
            commonRet.setRetMsg("[供应链-取消入库单异常]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc());
            return commonRet;
        }
        //1. 控制并发
        try {
            if (distributedLock.fetch(CANCEL_STOCKIN_ORDER_KEY + stockinOrderId)) {
                //1. 检查入库单是否已取消
                StockinOrderDO stockinOrderDO = checkStockinOrderById(stockinOrderId);
                if (StockinOrderState.STOCKIN_CANCLE.getValue().equals(stockinOrderDO.getState())) {
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.INFO)
                            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                            .setMsg("[入库单已是取消状态]")
                            .addParm("入库单ID", stockinOrderId)
                            .addParm("操作者", userName)
                            .log();
                    return commonRet;
                }

                //2. 触发状态引擎
                StockinOrderRequest stockinOrderRequest = StockinOrderRequestFactory.generateStockinOrderRequest(
                        StockinOrderActionType.STOCKIN_TO_CANCEL, stockinOrderDO, null, null, Operator.valueOf(userId, userName));
                engineService.executeStateMachineEngine(stockinOrderRequest, false);
            }else {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                        .setErrorMsg("[供应链-取消入库单异常]")
                        .addParm("入库单ID", stockinOrderId)
                        .addParm("操作者", userName)
                        .log();
                commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
                commonRet.setRetMsg("[供应链-取消入库单异常]:  并发异常" + StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getDesc());
                return commonRet;
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-取消入库单异常]")
                    .setException(e)
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("操作者", userName)
                    .log();
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
            commonRet.setRetMsg("[供应链-取消入库单异常]:  " + StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getDesc());
            return commonRet;
        } finally {
            distributedLock.realease(CANCEL_STOCKIN_ORDER_KEY + stockinOrderId);
        }
        return commonRet;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonRet<Void> editStockinorderWarehouse(Long stockinorderId, Long warehouseId, Long userId, String userName) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        commonRet.setRetCode(StockInReturnCode.COMMON_SUCCESS.getCode());
        commonRet.setRetMsg(StockInReturnCode.COMMON_SUCCESS.getDesc());

        StockinOrderDO stockinOrderDO = stockinOrderManager.getById(stockinorderId);
        if (null == stockinOrderDO) {
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_NOT_EXIST.getCode());
            commonRet.setRetMsg("[供应链-更新入库单仓库异常]: " + StockInReturnCode.STOCKIN_ORDER_NOT_EXIST.getDesc());
            return commonRet;
        }

        if (stockinOrderDO.getState().equals(StockinOrderState.STOCKIN_FINISH.getValue())
                || stockinOrderDO.getState().equals(StockinOrderState.STOCKIN_CANCLE.getValue())) {
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_EDIT_WAREHOUSE.getCode());
            commonRet.setRetMsg("[供应链-更新入库单仓库异常]: " + StockInReturnCode.STOCKIN_ORDER_EDIT_WAREHOUSE.getDesc());
            return commonRet;
        }

        if (stockinOrderDO.getWarehouseId() == warehouseId) {
            return commonRet;
        }

        try {
            List<MerchantProviderLineDO> results = merchantProviderLineManager.getByProviderAndWarehouse(stockinOrderDO.getMerchantId(), stockinOrderDO.getMerchantProviderId(), warehouseId);
            if(CollectionUtils.isEmpty(results)){
                commonRet.setRetCode(StockInReturnCode.COMMON_FAIL.getCode());
                commonRet.setRetMsg("[物流平台-入库单修改仓库失败]:仓库"+warehouseId + "不属于供应商" + stockinOrderDO.getMerchantProviderId() + "货主" + stockinOrderDO.getMerchantId());
                return commonRet;
            }

            //删除入库日志
            stockinOrderStateLogManager.deleteStockinOrderStateLog(stockinorderId);

            //更新仓库与状态为待提交
            stockinOrderDO.setWarehouseId(warehouseId);
            stockinOrderDO.setState(StockinOrderState.TO_BE_SUBMITED.getValue());
            stockinOrderManager.update(stockinOrderDO);

            //插入待提交的入库状态日志
            Date date = new Date();
            stockinOrderStateLogManager.insertOrUpdate(stockinorderId, userId, userName, StockinOrderState.TO_BE_SUBMITED.getValue());

            //判断仓库是否需要同步商品
            WarehouseLogisticsProviderBO logisticsProvider = new WarehouseLogisticsProviderBO();
            if (logisticsProvider.getIntegrationBO().getIsIntegrationSkuSync().compareTo(1) == 0) {
                List<StockinOrderDetailDO> details = stockinOrderDetailManager.queryByStockinOrderId(stockinorderId);
                List<StockinOrderDetailEntity> detailEntitys = new ArrayList<StockinOrderDetailEntity>();
                for (StockinOrderDetailDO detail : details) {
                    StockinOrderDetailEntity detailEntity = modelMapper.map(detail, StockinOrderDetailEntity.class);
                    detailEntitys.add(detailEntity);
                }

                syncSkuToWarehouse(stockinOrderDO, detailEntitys, true);
            }
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinorderId, SystemConstants.TRACE_APP))
                    .setErrorMsg("[物流平台-更新入库单仓库异常]")
                    .addParm("入库单ID", stockinorderId)
                    .addParm("仓库ID", warehouseId)
                    .addParm("操作者", userName)
                    .setException(e)
                    .log();
            commonRet.setRetCode(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION.getCode());
            commonRet.setRetMsg(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return commonRet;
        }
    }


    protected StockinOrderDO checkStockinOrderById(Long stockinOrderId) throws ServiceException {
        if (null == stockinOrderId) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台-入库单操作失败]: " + StockInReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .log();
            throw new ServiceException(StockInReturnCode.PARAM_ILLEGAL_ERR);
        }
        StockinOrderDO stockinOrderDO = stockinOrderManager.getById(stockinOrderId);
        if (null == stockinOrderDO) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockinOrderId, SystemConstants.TRACE_APP))
                    .setErrorMsg("[物流平台-入库单操作失败]: " + StockInReturnCode.STOCKIN_ORDER_NOT_EXIST.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .log();
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION);
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
        Map<String, SkuDO> skuDOMap = new HashMap<String, SkuDO>();
        if (CollectionUtils.isEmpty(stockinOrderDetailEntities)) {
            return null;
        }

        //校验sku
        List<Long> skuList = new ArrayList<Long>();
        List<StockinOrderDetailEntity> detailAfterMerge = new ArrayList<StockinOrderDetailEntity>();
        for (StockinOrderDetailEntity stockinOrderDetailEntity : stockinOrderDetailEntities) {
            checkStockinOrderDetails(skuDOMap, stockinOrderDetailEntity,detailAfterMerge, skuList);
        }

        /**
         * 创建新增 更新已有
         */
        for (StockinOrderDetailEntity entity : detailAfterMerge) {
            try {
                if (null == entity.id || entity.id.equals(0L)) {
                    List<StockinOrderDetailEntity> tmp = new ArrayList<StockinOrderDetailEntity>();
                    tmp.add(entity);
                    insertStockinOrderDetails(stockinOrderDO, skuDOMap, tmp);
                } else {
                    if (entity.getCount() != null && entity.getRealCount() != null && entity.getBadRealCount() != null) {
                        entity.setRealDiffCount(entity.getCount() - entity.getRealCount() - entity.getBadRealCount());
                    }
                    StockinOrderDetailDO stockinOrderDetailDO =  modelMapper.map(entity, StockinOrderDetailDO.class);
                    stockinOrderDetailManager.update(stockinOrderDetailDO);
                }
            } catch (Exception e) {
                throw new Exception("更新入库单明细：" + entity.skuId + "信息失败，原因" + e.getMessage());
            }
        }
        return skuList;
    }

    /**
     * 目前只考虑采购入库单，若有新类型入库单，请更新注释
     * @param skuDOMap 检查后的商品信息列表
     * @param stockinOrderDetailEntity 待检查的入库单明细
     * @param detailAfterMerge 检查后的入库单明细列表
     * @param repeatSku 检查后重复的skuid集合
     * @throws ServiceException
     */
    protected void checkStockinOrderDetails(Map<String, SkuDO> skuDOMap, StockinOrderDetailEntity stockinOrderDetailEntity, List<StockinOrderDetailEntity> detailAfterMerge, List<Long> repeatSku) throws ServiceException {
        if (null == stockinOrderDetailEntity) {
            throw new ServiceException(StockInReturnCode.PARAM_ILLEGAL_ERR, "入库单明细为空");
        }
        if (null == stockinOrderDetailEntity.skuId || stockinOrderDetailEntity.skuId.equals(0L)) {
            throw new ServiceException(StockInReturnCode.PARAM_ILLEGAL_ERR, "入库单的商品" + stockinOrderDetailEntity.skuName + "id为空");
        }
        Long skuId = stockinOrderDetailEntity.skuId;
        SkuDO skuDO = skuManager.getById(skuId);
        if (null == skuDO) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION, "商品(" + skuId + ")不存在");
        }
        if (skuDOMap.containsKey(skuId+"")) {
            repeatSku.add(skuId);
        } else {
            skuDOMap.put(skuId+"", skuDO);
            detailAfterMerge.add(stockinOrderDetailEntity);
        }
    }

    private void insertStockinOrderDetails(StockinOrderDO stockinOrderDO, Map<String, SkuDO> skuDOMap, List<StockinOrderDetailEntity> skus) throws Exception {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            for (StockinOrderDetailEntity stockinOrderDetailEntity : skus) {
                Long skuId = stockinOrderDetailEntity.skuId;
                SkuDO skuDO = skuDOMap.get(skuId + "");
                boolean isSaleStockIn = false;//判断是否未采购入库单
                if (StockinOrderType.SALES_STOCK_IN.getValue() == stockinOrderDO.getType()) {
                    isSaleStockIn = true;
                }

                StockinOrderDetailDO stockinOrderDetailDO = modelMapper.map(stockinOrderDetailEntity, StockinOrderDetailDO.class);
                stockinOrderDetailDO.setStockinOrderId(stockinOrderDO.getId());
                if (isSaleStockIn) {
                    String ymd = simpleDateFormat.format(new Date());
                    String skuBatch = ymd + "-" + SystemConstants.CUSTOMER_CODE + "-" + stockinOrderDO.getMerchantProviderId();
                    if (BatchGeneratePlanType.EXPIRE_SAME.getValue().equals(skuDO.getBatchGeneratePlan())) {
                        skuBatch += "-EXP" + UniqueCodeGenerator.getUniquCode();
                        stockinOrderDetailDO.setBatchGeneratePlan(BatchGeneratePlanType.EXPIRE_SAME.getValue());
                    } else {
                        skuBatch += "-STOCKIN" + UniqueCodeGenerator.getUniquCode();
                        stockinOrderDetailDO.setBatchGeneratePlan(BatchGeneratePlanType.STOCKIN_SAME.getValue());
                    }
                    stockinOrderDetailDO.setSkuBatch(skuBatch);
                }
                if (StringUtils.isNotBlank(stockinOrderDetailDO.getSkuBatch())) {
                    stockinOrderDetailDO.setSkuBatch(stockinOrderDetailDO.getSkuBatch().trim().replaceAll("\r\n", ""));
                }
                stockinOrderDetailManager.insert(stockinOrderDetailDO);
            }

            //异步同步到仓库
            syncSkuToWarehouse(stockinOrderDO, skus, true);
        } catch (Exception e) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_INNER_EXCEPTION,"更新入库单" + stockinOrderDO.getId() + "明细失败," + "失败原因" + e.getMessage());
        }
    }

    protected void updateStockinOrderSku(StockinOrderDetailCargoResultEntity stockinOrderDetailCargoResultEntity) throws Exception {
        if ((stockinOrderDetailCargoResultEntity == null
                ||stockinOrderDetailCargoResultEntity.getSkuId() == null
                || stockinOrderDetailCargoResultEntity.getSkuBarcode() == null)) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_UPDATE, "skuId和条码不能为空");
        }

        StockinOrderDetailDO stockinOrderDetailDO = modelMapper.map(stockinOrderDetailCargoResultEntity, StockinOrderDetailDO.class);
        SkuBarcodeDO skuBarcodeDO = skuBarcodeManager.getSkuBySkuIdAndBarcode(stockinOrderDetailCargoResultEntity.getSkuId(), stockinOrderDetailCargoResultEntity.getSkuBarcode());
        if (null == skuBarcodeDO) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_SKU_NOT_FOUND, "根据条码和skuId信息未找到入库单明细:" + stockinOrderDetailCargoResultEntity.getSkuId() + "，条形编码:" + stockinOrderDetailCargoResultEntity.getSkuBarcode());
        }
        StockinOrderDO stockinOrderDO = checkStockinOrderById(stockinOrderDetailDO.getStockinOrderId());
        if ("STOCKIN_FINISH".equals(stockinOrderDO.getState())) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_UPDATE, "入库单号为:" + stockinOrderDO.getStockinId() + "对应入库单已经入库完成");
        }
        if ("STOCKIN_CANCLE".equals(stockinOrderDO.getState())) {
            throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_UPDATE, "入库单号为:" + stockinOrderDO.getStockinId() + "对应入库单已经取消");
        }
        // 如果生产日期和过期日期只存在其中之一，则需要根据保质期计算出另一个日期的值
        SkuDO skuDO = skuManager.getById(stockinOrderDetailDO.getSkuId());
        Calendar calendar = Calendar.getInstance();
        int diffDays = skuDO.getGuarantyPeriod();
        if (stockinOrderDetailDO.getProductionDate() == null && stockinOrderDetailDO.getExpirationDate() != null) {
            calendar.setTime(stockinOrderDetailDO.getExpirationDate());
            calendar.add(Calendar.DAY_OF_YEAR, diffDays);
            stockinOrderDetailDO.setProductionDate(calendar.getTime());
        } else if (stockinOrderDetailDO.getProductionDate() != null && stockinOrderDetailDO.getExpirationDate() == null) {
            calendar.setTime(stockinOrderDetailDO.getProductionDate());
            calendar.add(Calendar.DAY_OF_YEAR, diffDays);
            stockinOrderDetailDO.setExpirationDate(calendar.getTime());
        }
        if (stockinOrderDetailDO.getProductionDate() != null) {
            if (stockinOrderDetailDO.getProductionDate().after(new Date()) || stockinOrderDetailDO.getExpirationDate().before(new Date())) {
                throw new ServiceException(StockInReturnCode.STOCKIN_ORDER_NOT_ALLOW_UPDATE, "入库单的商品" + stockinOrderDetailDO.getSkuId() + " 的生产日期不能晚于当天且过期日期不能早于当天！"
                        + "生产日期：" + DateUtil.defFormatDateStr(stockinOrderDetailDO.getProductionDate()) + "，过期日期：" + DateUtil.defFormatDateStr(stockinOrderDetailDO.getExpirationDate()));
            }
        }
        if (StringUtils.isNotBlank(stockinOrderDetailDO.getSkuBatch())) {
            stockinOrderDetailDO.setSkuBatch(stockinOrderDetailDO.getSkuBatch().trim());
        }
        stockinOrderDetailManager.updateByBarcodeAndSkuId(stockinOrderDetailDO);
    }

    protected void syncSkuToWarehouse(StockinOrderDO stockinOrder, List<StockinOrderDetailEntity> skusAfterMerge, boolean isSync) throws ServiceException {
        //同步商品
        Long warehouseId = stockinOrder.getWarehouseId();
        WarehouseDO warehouseDO = warehouseManager.getById(warehouseId);
        if (warehouseDO == null) {
            throw new ServiceException(WarehouseReturnCode.WAREHOUSE_NOT_EXIST_ERR, WarehouseReturnCode.WAREHOUSE_NOT_EXIST_ERR.getDesc());
        }
        //// TODO: 2017/8/7
//        WarehouseLogisticsProviderBO logisticsProvider = new WarehouseLogisticsProviderBO();
//        if (logisticsProvider.getIntegrationBO().getIsIntegrationSkuSync() == 1) {
            for (StockinOrderDetailEntity skuEntity : skusAfterMerge) {
                SkuWarehouseSyncDO syncDO = new SkuWarehouseSyncDO();
                syncDO.setSkuId(skuEntity.skuId);
                syncDO.setWarehouseId(warehouseId);
                List<SkuWarehouseSyncDO> exists = skuWarehouseSyncManager.query(BaseQuery.getInstance(syncDO));
                if (exists.size() == 0) {
                    SkuWarehouseSyncDO skuWarehouseSyncDO = new SkuWarehouseSyncDO();
                    skuWarehouseSyncDO.setSkuId(skuEntity.skuId);
                    skuWarehouseSyncDO.setWarehouseId(warehouseId);
                    skuWarehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_FAIL.getValue());
                    skuWarehouseSyncManager.insert(skuWarehouseSyncDO);
                    List<Long> skuIds = new ArrayList<Long>();
                    skuIds.add(skuEntity.skuId);
                    if (isSync) {
                        skuSyncBizService.sendProductBasicInfo2Wms(skuIds, warehouseId, WmsOperaterType.ADD);
                    } else {
                        skuSyncBizService.sendProductBasicInfo2WmsNotSync(skuIds, warehouseId, WmsOperaterType.ADD);
                    }
                }
//            }
        }
    }
}
