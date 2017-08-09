package com.sfebiz.supplychain.service.stockout.process.create;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.line.LineConfig;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.line.enums.LineType;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskStatus;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.ProductDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderTaskManager;
import com.sfebiz.supplychain.persistence.base.user.manager.UserAuthenticationManager;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.port.model.LogisticsPortBO;
import com.sfebiz.supplychain.service.sku.model.ProductDeclareState;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;
import com.taobao.tbbpm.common.util.DateUtils;

/**
 * <p>数据准备: 1. 检测线路是否被关闭；2.商品是否已经备案的检查&获取备案信息</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/7/24
 * Time: 下午6:28
 */
@Component("dataPrepareProcessor")
public class DataPrepareProcessor extends StockoutProcessAction {

    public static final String             TAG           = "DATA_PREPARE";
    private static final Logger            logger        = LoggerFactory
                                                             .getLogger(DataPrepareProcessor.class);
    private static final Logger            commandLogger = LoggerFactory.getLogger("CommandLogger");
    private static final HaitaoTraceLogger traceLogger   = HaitaoTraceLoggerFactory
                                                             .getTraceLogger("order");

    @Resource
    private ProductDeclareManager          productDeclareManager;

    @Resource
    private StockoutOrderTaskManager       stockoutOrderTaskManager;

    @Resource
    private StockoutOrderManager           stockoutOrderManager;

    @Resource
    private UserAuthenticationManager      userAuthenticationManager;

    @Override
    public String getProcessTag() {
        return TAG;
    }

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {
        try {
            BaseResult result = new BaseResult();
            if (request == null || request.getStockoutOrderBO() == null
                || request.getStockoutOrderBO().getDetailBOs() == null
                || request.getLineBO() == null) {
                LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("出库单相关参数实体为 null")
                    .addParm("出库单信息", request).log();
                request.setExceptionMessage("[供应链-下发出库单]出库单相关参数实体为空");
                request.setServiceException(new ServiceException(
                    LogisticsReturnCode.STOCKOUT_DATA_IS_NULL,
                    LogisticsReturnCode.STOCKOUT_DATA_IS_NULL.getDesc()));
                return new BaseResult(Boolean.FALSE);
            }
            LogisticsLineBO lineBO = request.getLineBO();

            //如果线路处在关闭状态则进入异常处理器
            boolean isLineInStopStatus = LineConfig.getLineIsTemporaryClose(lineBO.getId());
            boolean isSkip = LineConfig.getLineIsTemporaryCloseSpecialOrderIsSkip(request
                .getStockoutOrderBO().getBizId());
            if (isLineInStopStatus && !isSkip) {
                Long delayMill = LineConfig.getLineCloseDelayMill();
                Date nextExeTime = DateUtils.addTime(new Date(), delayMill);
                request.setNextRetryTime(nextExeTime);
                request.setExceptionMessage("[供应链-出库单下发]路线暂时被关闭");
                request.setServiceException(new ServiceException(
                    LogisticsReturnCode.LINE_IS_CLOSED, LogisticsReturnCode.LINE_IS_CLOSED
                        .getDesc()));
                return new BaseResult(Boolean.FALSE);
            }

            Map<Long, SkuDeclareBO> declareEntityMap = getSkuDeclareEntitisMap(lineBO.getPortBO(),
                request.getDetailBOs(), request.getStockoutOrderBO(), lineBO.getLineType());
            request.setProductDeclareEntityMap(declareEntityMap);

            LogBetter
                .instance(logger)
                .setLevel(LogLevel.INFO)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO().getBizId(),
                        SystemConstants.TRACE_APP)).setMsg("[供应链-出库单下发]数据准备完成")
                .addParm("备案信息", declareEntityMap).log();

            result.setSuccess(Boolean.TRUE);
            return result;
        } catch (ServiceException e) {
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.WARN)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO().getBizId(),
                        SystemConstants.TRACE_APP)).setException(e)
                .setErrorMsg("[供应链-出库单下发]数据准备异常: " + e.getMsg())
                .addParm("订单ID", request.getStockoutOrderBO().getBizId()).log();
            request.setExceptionMessage("[供应链-出库单下发]数据准备异常: " + e.getMsg());
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.DATA_PREPARE_ERROR, LogisticsReturnCode.DATA_PREPARE_ERROR
                    .getDesc()));
            return new BaseResult(Boolean.FALSE, e.getMsg());
        } catch (Exception e) {
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.WARN)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO().getBizId(),
                        SystemConstants.TRACE_APP)).setException(e)
                .setErrorMsg("[供应链-出库单下发]数据准备异常" + e.getMessage())
                .addParm("订单ID", request.getStockoutOrderBO().getBizId()).log();
            request.setExceptionMessage("[供应链-出库单下发]数据准备异常: " + e.getMessage());
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.DATA_PREPARE_ERROR, LogisticsReturnCode.DATA_PREPARE_ERROR
                    .getDesc()));
            return new BaseResult(Boolean.FALSE, e.getMessage());
        }
    }

    /**
     * 重写验证是否需要执行的判断 每次执行process必须执行此验证节点
     *
     * @param request
     * @return
     * @throws ServiceException
     */
    @Override
    public BaseResult process(StockoutOrderRequest request) throws ServiceException {
        BaseResult result = new BaseResult();
        result = doProcess(request);
        request.setCurrentProcssorTag(getProcessTag());
        return result;
    }

    /**
     * 执行出库单下发前，获取商品备案信息
     * 异常后，发进入Task队列：类型为
     *
     * @param portBO
     * @param detailDOs
     * @return
     * @throws ServiceException
     */
    public Map<Long, SkuDeclareBO> getSkuDeclareEntitisMap(LogisticsPortBO portBO,
                                                           List<StockoutOrderDetailBO> detailDOs,
                                                           StockoutOrderBO stockoutOrderBO,
                                                           LineType lineType)
                                                                             throws ServiceException {
        Map<Long, SkuDeclareBO> skuDeclareEntitisMap = new HashMap<Long, SkuDeclareBO>();
        if (CollectionUtils.isEmpty(detailDOs) || stockoutOrderBO == null) {
            return skuDeclareEntitisMap;
        }

        if (portBO != null && portBO.getId() > 0) {
            for (StockoutOrderDetailBO skuBO : detailDOs) {
                if (skuDeclareEntitisMap.containsKey(skuBO.getSkuId())) {
                    continue;
                }

                // 调拨出库、采退出库的商品本就是已备案的商品，无需在验证处理，直接封装备案信息
                if (StockoutOrderType.ALLOCATION_STOCK_OUT.getValue() == stockoutOrderBO
                    .getOrderType()) {
                    SkuDeclareBO productDeclareEntity = new SkuDeclareBO();
                    productDeclareEntity.setPortId(portBO.getId());
                    productDeclareEntity.setSkuId(skuBO.getSkuId());
                    productDeclareEntity.setDeclareName(skuBO.getSkuName());
                    productDeclareEntity.setBarCode(skuBO.getSkuBarcode());
                    skuDeclareEntitisMap.put(skuBO.getSkuId(), productDeclareEntity);
                    continue;
                }

                ProductDeclareDO productDeclareDO = new ProductDeclareDO();
                productDeclareDO.setPortId(portBO.getId());
                productDeclareDO.setSkuId(skuBO.getSkuId());
                productDeclareDO.setState(ProductDeclareState.DECLARE_PASS.getValue());
                productDeclareDO.setDeclareMode(lineType.value);
                List<ProductDeclareDO> productDeclareDOs = productDeclareManager.query(BaseQuery
                    .getInstance(productDeclareDO));
                if (CollectionUtils.isEmpty(productDeclareDOs) || productDeclareDOs.get(0) == null) {
                    throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_NOT_EXIST,
                        LogisticsReturnCode.SKU_DECLARE_NOT_EXIST.getDesc() + "! " + "[商品ID:"
                                + skuBO.getSkuId() + ",申报口岸:"
                                + PortNid.getDescByCode(portBO.getId()) + ",线路类型: "
                                + lineType.getDescription() + "]");
                }
                productDeclareDO = productDeclareDOs.get(0);

                SkuDeclareBO productDeclareEntity = new SkuDeclareBO();
                productDeclareEntity.setDeclareName(productDeclareDO.getDeclareName());
                productDeclareEntity.setPortId(portBO.getId());
                productDeclareEntity.setSkuId(skuBO.getSkuId());
                productDeclareEntity.setProductId(productDeclareDO.getProductId());
                if (StringUtils.isNotBlank(productDeclareDO.getBarCode())) {
                    if (productDeclareDO.getBarCode().contains(",")) {
                        productDeclareEntity
                            .setBarCode(productDeclareDO.getBarCode().split(",")[0]);
                    } else {
                        productDeclareEntity.setBarCode(productDeclareDO.getBarCode());
                    }
                }

                if (StringUtils.isBlank(productDeclareDO.getGrossWeight())
                    && StringUtils.isNotBlank(productDeclareDO.getNetWeight())) {
                    Double netWeight = new Double(productDeclareDO.getNetWeight());
                    Double grossWeight = netWeight + 0.1;
                    productDeclareDO.setGrossWeight(grossWeight.toString());
                }

                productDeclareEntity.setPostTaxNo(productDeclareDO.getPostTaxNo());
                productDeclareEntity.setHsCode(productDeclareDO.getHsCode());
                productDeclareEntity.setMeasuringUnit(productDeclareDO.getMeasuringUnit());
                productDeclareEntity.setTaxRate(productDeclareDO.getTaxRate());
                productDeclareEntity.setTariffRate(productDeclareDO.getTariffRate());
                productDeclareEntity.setAddedValueTaxRate(productDeclareDO.getAddedValueTaxRate());
                productDeclareEntity.setConsumptionDutyRate(productDeclareDO
                    .getConsumptionDutyRate() == null ? "0" : productDeclareDO
                    .getConsumptionDutyRate());
                productDeclareEntity.setGrossWeight(productDeclareDO.getGrossWeight());
                productDeclareEntity
                    .setFirstMeasuringUnit(productDeclareDO.getFirstMeasuringUnit());
                productDeclareEntity.setFirstWeight(productDeclareDO.getFirstWeight());
                productDeclareEntity.setSecondMeasuringUnit(productDeclareDO
                    .getSecondMeasuringUnit());
                productDeclareEntity.setSecondWeight(productDeclareDO.getSecondWeight());
                productDeclareEntity.setNetWeight(StringUtils.isEmpty(productDeclareDO
                    .getNetWeight()) ? productDeclareDO.getGrossWeight() : productDeclareDO
                    .getNetWeight());
                productDeclareEntity.setOrigin(productDeclareDO.getOrigin());
                if (productDeclareDO.getAttributes() != null) {
                    productDeclareEntity.setAttributes(productDeclareDO.getAttributes());
                }
                skuDeclareEntitisMap.put(skuBO.getSkuId(), productDeclareEntity);
            }
        } else {
            //不走口岸，商品备案信息使用商品售卖信息
            for (StockoutOrderDetailBO skuDO : detailDOs) {
                SkuDeclareBO productDeclareEntity = new SkuDeclareBO();
                productDeclareEntity.setDeclareName(skuDO.getSkuName());
                productDeclareEntity.setSkuId(skuDO.getSkuId());
                skuDeclareEntitisMap.put(skuDO.getSkuId(), productDeclareEntity);
            }
        }

        return skuDeclareEntitisMap;
    }

    /**
     * 关闭异常任务并关闭工单
     * @param stockoutOrderTaskDO
     */
    public void closeTaskAndTicket(StockoutOrderTaskDO stockoutOrderTaskDO) {
        if (TaskStatus.WAIT_HANDLE.getValue().equals(stockoutOrderTaskDO.getTaskState())) {
            stockoutOrderTaskDO.setTaskState(TaskStatus.HANDLE_SUCCESS.getValue());
            stockoutOrderTaskManager.update(stockoutOrderTaskDO);
        }
    }
}
