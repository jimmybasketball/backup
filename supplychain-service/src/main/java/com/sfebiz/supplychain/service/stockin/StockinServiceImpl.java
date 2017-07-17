package com.sfebiz.supplychain.service.stockin;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.generator.UniqueNumberGenerator;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.stockinorder.api.StockInService;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailEntity;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderEntity;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderState;
import com.sfebiz.supplychain.lock.DistributedLock;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderLineDO;
import com.sfebiz.supplychain.persistence.base.merchant.manager.MerchantProviderLineManager;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderStateLogManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.statemachine.EngineService;
import com.sfebiz.supplychain.service.statemachine.Operator;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderActionType;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequest;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequestFactory;
import net.pocrd.entity.ServiceException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangyajing on 2017/7/12.
 */

@Component("stockinService")
public class StockInServiceImpl implements StockInService {

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

    static {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }
    @Override
    @MethodParamValidate
    public CommonRet<Long> createStockinOrder (
            @ParamNotBlank("请求不能为空") StockinOrderEntity stockinOrderEntity, Long userId, String userName) throws ServiceException {
       CommonRet<Long> commonRet = new CommonRet<Long>();
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台入库单-创建]")
                .addParm("入库单信息", stockinOrderEntity)
                .addParm("操作人", userName)
                .log();

        Long stockinOrderId = null;

        if(CollectionUtils.isEmpty(stockinOrderEntity.getSkus())) {
            commonRet.setRetCode(SCReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg("[物流平台入库单-创建]：商品明细不能为空");
            return commonRet;
        }

        WarehouseDO warehouseDO = warehouseManager.getById(stockinOrderEntity.getWarehouseId());
        if (null == warehouseDO) {
            commonRet.setRetCode(SCReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg("[物流平台入库单-创建]:仓库不存在，warehouseId：" + stockinOrderEntity.getWarehouseId());
            return commonRet;
        }

        List<MerchantProviderLineDO> merchantProviderLineDOList = merchantProviderLineManager.getByProviderAndWarehouse(stockinOrderEntity.getMerchantProviderId(), stockinOrderEntity.getWarehouseId());
        if(CollectionUtils.isEmpty(merchantProviderLineDOList)){
            commonRet.setRetCode(SCReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg("[物流平台入库单-创建]:仓库"+stockinOrderEntity.getWarehouseId() + "不属于供应商" + stockinOrderEntity.getMerchantProviderId());
            return commonRet;
        }

        try {
            StockinOrderDO stockinOrderDO = modelMapper.map(stockinOrderEntity, StockinOrderDO.class);
            stockinOrderDO.setStockinId(UniqueNumberGenerator.getUniqueNo("RK"));

            //启动状态机引擎
            StockinOrderRequest stockinOrderRequest = StockinOrderRequestFactory.generateStockinOrderRequest(
                    StockinOrderActionType.STOCKIN_TO_CREATE, stockinOrderDO, null, Operator.valueOf(userId, userName));
            engineService.startStateMachineEngine(stockinOrderRequest);

            stockinOrderStateLogManager.insertOrUpdate(stockinOrderDO.getId(), userId, userName, StockinOrderState.TO_BE_SUBMITED.getValue());
            stockinOrderDO.setState(StockinOrderState.TO_BE_SUBMITED.getValue());


        } catch (ServiceException e) {
            throw  e;
        }
        return null;
    }

    @Override
    public CommonRet<Void> updateStockinOrderDetails(Long stockinOrderId, List<StockinOrderDetailEntity> stockinOrderDetailEntities, Long userId, String userName) throws ServiceException {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-更新入库单明细]")
                .addParm("入库单ID", stockinOrderId)
                .addParm("明细", stockinOrderDetailEntities)
                .addParm("操作者", userName)
               .log();

        if ( null == stockinOrderId) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链-更新入库单明细异常]: " + SCReturnCode.PARAM_ILLEGAL_ERR.getDesc())
                    .addParm("入库单ID", stockinOrderId)
                    .addParm("操作者", userName)
                    .log();
            throw new ServiceException(SCReturnCode.PARAM_ILLEGAL_ERR,
                    "[供应链-更新入库单明细异常]: " + SCReturnCode.PARAM_ILLEGAL_ERR.getDesc());
        }

        //1. 控制并发
        if (distributedLock.fetch(UPDATE_STOCKIN_ORDER_SKUS_KEY + stockinOrderId)) {
            try {
                //1. 验证入库单是否存在
                StockinOrderDO stockinOrderDO = checkStockinOrderById(stockinOrderId);
            } catch (ServiceException e) {

            }
        }
        return null;
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

    private void updateStockinOrderEntity(List<StockinOrderDetailEntity> stockinOrderDetailEntities, StockinOrderDO stockinOrderDO) throws Exception {
    }
}
