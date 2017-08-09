package com.sfebiz.supplychain.service.stockout.process.create;

import java.util.List;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.FeeSplitBizService;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>订单费用拆分处理器</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/20
 * Time: 下午3:31
 */
@Component("orderFeeSplitProcessor")
public class OrderFeeSplitProcessor extends StockoutProcessAction {

    public static final String  TAG    = "FEE_SPLIT";
    private static final Logger logger = LoggerFactory.getLogger(OrderFeeSplitProcessor.class);

    @Resource
    private FeeSplitBizService  feeSplitBizService;

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {

        try {
            BaseResult result = new BaseResult();
            if (request == null || request.getStockoutOrderBO() == null
                || CollectionUtils.isEmpty(request.getDetailBOs()) || request.getLineBO() == null
                || request.getProductDeclareEntityMap() == null) {
                LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("出库单相关参数实体为 null")
                    .addParm("出库单信息", request).log();
                request.setExceptionMessage("[供应链-下发出库单]出库单相关参数实体为空");
                request.setServiceException(new ServiceException(
                    LogisticsReturnCode.DATA_PREPARE_ERROR, LogisticsReturnCode.DATA_PREPARE_ERROR
                        .getDesc()));
                return new BaseResult(Boolean.FALSE);
            }
            StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
            List<StockoutOrderDetailBO> detailBOs = stockoutOrderBO.getDetailBOs();
            LogisticsLineBO lineEntity = request.getLineBO();
            StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceEntity;

            //如果是测试路线，直接跳过税费拆分
            // TODO matt
            if (stockoutOrderBO.getLineId().intValue() >= 100
                && stockoutOrderBO.getLineId().intValue() < 200) {
                stockoutOrderDeclarePriceEntity = feeSplitBizService.notCalculatPriceSplit(
                    stockoutOrderBO, detailBOs, lineEntity, "测试路线不需要拆分");
                stockoutOrderBO.setDeclarePriceBO(stockoutOrderDeclarePriceEntity);
                return new BaseResult(Boolean.TRUE);
            }

            if (lineEntity.getPortBO() == null) {
                stockoutOrderDeclarePriceEntity = feeSplitBizService.notCalculatPriceSplit(
                    stockoutOrderBO, detailBOs, lineEntity, "不走口岸不需要拆分");
                stockoutOrderBO.setDeclarePriceBO(stockoutOrderDeclarePriceEntity);
                return new BaseResult(Boolean.TRUE);
            }

            if (PortNid.GUANGZHOU.getValue() == lineEntity.getPortBO().getId()) {
                stockoutOrderDeclarePriceEntity = feeSplitBizService
                    .calculatPriceDeclarePriceOnGuangzhouVersion2(stockoutOrderBO, detailBOs,
                        lineEntity, request.getProductDeclareEntityMap());
                stockoutOrderBO.setDeclarePriceBO(stockoutOrderDeclarePriceEntity);
                result.setSuccess(Boolean.TRUE);
            } else if (PortNid.HANGZHOU.getValue() == lineEntity.getPortBO().getId()) {
                stockoutOrderDeclarePriceEntity = feeSplitBizService
                    .calculatPriceDeclarePriceOnHzPortNoFreight(stockoutOrderBO, detailBOs,
                        lineEntity, request.getProductDeclareEntityMap());
                stockoutOrderBO.setDeclarePriceBO(stockoutOrderDeclarePriceEntity);
                result.setSuccess(Boolean.TRUE);
            } else if (PortNid.JINAN.getValue() == lineEntity.getPortBO().getId()) {
                stockoutOrderDeclarePriceEntity = feeSplitBizService.notCalculatPriceSplit(
                    stockoutOrderBO, detailBOs, lineEntity, "不走口岸不需要拆分");
                stockoutOrderBO.setDeclarePriceBO(stockoutOrderDeclarePriceEntity);
                result.setSuccess(Boolean.TRUE);
            } else if (PortNid.XIAMEN.getValue() == lineEntity.getPortBO().getId()) {
                stockoutOrderDeclarePriceEntity = feeSplitBizService.notCalculatPriceSplit(
                    stockoutOrderBO, detailBOs, lineEntity, "不走口岸不需要拆分");
                stockoutOrderBO.setDeclarePriceBO(stockoutOrderDeclarePriceEntity);
                result.setSuccess(Boolean.TRUE);
            } else if (PortNid.SHATIAN.getValue() == lineEntity.getPortBO().getId()) {
                stockoutOrderDeclarePriceEntity = feeSplitBizService.notCalculatPriceSplit(
                    stockoutOrderBO, detailBOs, lineEntity, "不走口岸不需要拆分");
                stockoutOrderBO.setDeclarePriceBO(stockoutOrderDeclarePriceEntity);
                result.setSuccess(Boolean.TRUE);
            } else {
                stockoutOrderDeclarePriceEntity = feeSplitBizService
                    .calculatPriceDeclarePriceOnCustomsOffice(stockoutOrderBO, detailBOs,
                        lineEntity, request.getProductDeclareEntityMap());
                stockoutOrderBO.setDeclarePriceBO(stockoutOrderDeclarePriceEntity);
                result.setSuccess(Boolean.TRUE);
            }
            return result;

        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN)
                .setErrorMsg("[供应链-运费税费拆分]税费、运费拆分逻辑异常，" + e.getMessage()).setException(e)
                .setParms(request.getStockoutOrderBO().getBizId()).log();
            request.setExceptionMessage("[供应链-运费税费拆分]税费、运费拆分逻辑异常，" + e.getMessage());
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.ORDER_FEE_SPLIT_ERROR,
                LogisticsReturnCode.ORDER_FEE_SPLIT_ERROR.getDesc()));
            return new BaseResult(Boolean.FALSE);
        }
    }

    @Override
    public String getProcessTag() {
        return TAG;
    }

}
