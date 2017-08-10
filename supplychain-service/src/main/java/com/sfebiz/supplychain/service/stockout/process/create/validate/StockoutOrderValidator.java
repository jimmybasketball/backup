package com.sfebiz.supplychain.service.stockout.process.create.validate;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.port.PortConfig;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDeclarePriceDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderDeclarePriceManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.ListUtil;

/**
 * 
 * <p>出库单校验基础类</p>
 * 
 * @author matt
 * @Date 2017年7月25日 下午1:41:52
 */
public abstract class StockoutOrderValidator {

    protected static final Logger              logger      = LoggerFactory
                                                               .getLogger(StockoutOrderValidator.class);
    protected static final HaitaoTraceLogger   traceLogger = HaitaoTraceLoggerFactory
                                                               .getTraceLogger("order");
    @Resource
    protected StockoutOrderManager             stockoutOrderManager;

    @Resource
    protected StockoutOrderDeclarePriceManager stockoutOrderDeclarePriceManager;

    public boolean validate(StockoutOrderRequest request) {
        try {
            if (isPass(request)) {
                return true;
            }
            Map<String, Object> portMeta = queryCfg(request);
            StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
            String timeFm = PortConfig.getPortDecalredTimeFm(portMeta);
            Date[] dates = DateUtil.getPortTimeBw(new Date(), timeFm);
            if (null == dates || dates.length != 2) {
                throw new Exception(getValidatorName() + "口岸时间段查询异常");
            }
            //保存到request 如果异常或验证失败则取出
            request.setTimeLimitRange(dates);

            List<StockoutOrderDO> declaredDos = queryData(request, portMeta);

            Integer limitBillMoneyCNY = PortConfig.getPortBillMoneyLimit(portMeta);
            Integer limitBillMoneyCNF = CommonUtil.castMoneyYuanToFen(limitBillMoneyCNY);
            String limitBillCountCfg = PortConfig.getPortBillCountLimt(portMeta);
            String limitTaxCfg = PortConfig.getPortTaxLimt(portMeta);

            String exceptionMsg = "";

            //如果不存在 即是 第一单
            if (CollectionUtils.isEmpty(declaredDos)) {
                Integer waitDeclareMoney = stockoutOrderBO.getUserActualPaymentAmount();
                //第一单且金额大于限额
                if (waitDeclareMoney >= limitBillMoneyCNF) {
                    List<StockoutOrderDetailBO> basicSkus = request.getDetailBOs();
                    int skuCount = CommonUtil.getBasicSkuCount(basicSkus);
                    Integer portFirstBillSkuLimt = PortConfig.getPortFirstBillSkuLimt(portMeta);

                    //第一单且商品数大于限制数
                    if (skuCount > portFirstBillSkuLimt) {
                        exceptionMsg = "超过"
                                       + request.getStockoutOrderBO().getLineBO().getPortBO()
                                           .getName() + "的海关规则限制: 出库单第一单的商品数大于口岸限制数("
                                       + portFirstBillSkuLimt + ")";
                        LogBetter
                            .instance(logger)
                            .setLevel(LogLevel.WARN)
                            .setMsg(exceptionMsg)
                            .setTraceLogger(
                                TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO()
                                    .getBizId(), SystemConstants.TRACE_APP))
                            .addParm("出库单", stockoutOrderBO.getBizId()).addParm("商品数", skuCount)
                            .log();
                        request.setExceptionMessage(exceptionMsg);
                        return false;
                    }
                }

                LogBetter
                    .instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链-验证口岸避税规则]:" + getValidatorName() + "出库单在口岸第一单申报，验证通过")
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO()
                            .getBizId(), SystemConstants.TRACE_APP))
                    .addParm("出库单", stockoutOrderBO.getBizId()).log();
                return true;
            } else {
                /*
                //验证是否达到口岸限制单数                                         l
                if (!PortConfig.isNoLimit(limitBillCountCfg)) {
                    int declaredBillCount = declaredDos.size();
                    Integer portLimitBillCount = Integer.valueOf(limitBillCountCfg);
                    if (declaredBillCount + 1 > portLimitBillCount) {
                        exceptionMsg = "超过" + request.getLineBO().getPortBO().getName()
                                       + "的海关规则限制: 已达到口岸每天申报限制数(" + portLimitBillCount + ")";
                        LogBetter
                            .instance(logger)
                            .setLevel(LogLevel.WARN)
                            .setMsg(exceptionMsg)
                            .setTraceLogger(
                                TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO()
                                    .getBizId(), SystemConstants.TRACE_APP))
                            .addParm("出库单", stockoutOrderBO.getBizId())
                            .addParm("限制数为", portLimitBillCount).addParm("已申报数", declaredBillCount)
                            .log();
                        request.setExceptionMessage(exceptionMsg);
                        return false;
                    }
                }

                //验证是否达到口岸限制金额数
                Integer declaredBillMoney = calTotalDeclaredMoney(declaredDos);
                Integer currentBillMonery = stockoutOrderBO.getUserActualPaymentAmount();
                if (declaredBillMoney + currentBillMonery >= limitBillMoneyCNF) {
                    exceptionMsg = "超过"
                                   + request.getStockoutOrderBO().getLineBO().getPortBO().getName()
                                   + "的海关规则限制: 已达到口岸申报金额限制数(" + limitBillMoneyCNF / 100 + ")";
                    LogBetter
                        .instance(logger)
                        .setLevel(LogLevel.WARN)
                        .setMsg(exceptionMsg)
                        .setTraceLogger(
                            TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO()
                                .getBizId(), SystemConstants.TRACE_APP))
                        .addParm("出库单", stockoutOrderBO.getBizId())
                        .addParm("已申报金额", declaredBillMoney).addParm("当前订单金额", currentBillMonery)
                        .addParm("口岸限制金额(分)", limitBillMoneyCNF).log();
                    request.setExceptionMessage(exceptionMsg);
                    return false;
                }

                //验证是否达到口岸限制税金数
                if (!PortConfig.isNoLimit(limitTaxCfg)) {
                    Integer totalTaxMoney = calTotalTaxMoney(declaredDos);
                    Integer currentTaxMoney = null == stockoutOrderBO.getDeclarePriceBO()
                        .getTariffFee() ? 0 : stockoutOrderBO.getDeclarePriceBO().getTariffFee()
                        .intValue();
                    Integer portTaxFeeLimit = Integer.valueOf(limitTaxCfg);
                    if (totalTaxMoney + currentTaxMoney >= portTaxFeeLimit) {
                        exceptionMsg = "超过"
                                       + request.getStockoutOrderBO().getLineBO().getPortBO()
                                           .getName() + "的海关规则限制: 已达到口岸申报税金限制数(" + portTaxFeeLimit
                                       / 100 + ")";
                        LogBetter
                            .instance(logger)
                            .setLevel(LogLevel.WARN)
                            .setMsg(exceptionMsg)
                            .setTraceLogger(
                                TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO()
                                    .getBizId(), SystemConstants.TRACE_APP))
                            .addParm("出库单", stockoutOrderBO.getBizId())
                            .addParm("已申报税金", totalTaxMoney).addParm("本次税金", currentTaxMoney)
                            .addParm("口岸税金限制", portTaxFeeLimit).log();
                        request.setExceptionMessage(exceptionMsg);
                        return false;
                    }
                }

                LogBetter
                    .instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链-验证口岸避税规则]:" + getValidatorName() + "出库单避税规则验证通过")
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO()
                            .getBizId(), SystemConstants.TRACE_APP))
                    .addParm("出库单", stockoutOrderBO.getBizId()).log();

                */
                return true;
            }
        } catch (Exception e) {
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.WARN)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO().getBizId(),
                        SystemConstants.TRACE_APP))
                .setErrorMsg("[供应链-验证口岸避税规则]:" + getValidatorName() + "验证失败").setException(e)
                .setParms(request.getStockoutOrderBO().getBizId()).log();
            return false;
        }
    }

    /**
     * 查询避税规则配置
     *
     * @param request
     * @return
     * @throws Exception
     */
    public abstract Map<String, Object> queryCfg(StockoutOrderRequest request) throws Exception;

    /**
     * 查询已申报的订单
     * 排除本次申报的订单
     *
     * @param request
     * @return
     * @throws Exception
     */
    public abstract List<StockoutOrderDO> queryData(StockoutOrderRequest request,
                                                    Map<String, Object> cfgMap) throws Exception;

    /**
     * 是否PASS此校验
     *
     * @param request
     * @return
     */
    public abstract boolean isPass(StockoutOrderRequest request);

    /**
     * 返回校验的名字 主要用于日志区分
     *
     * @return
     */
    public abstract String getValidatorName();

    /**
     * 计算已申报金额 单位元
     * 如果是第一单 那么金额为 此单
     *
     * @param declaredDos
     * @return
     */
    protected Integer calTotalDeclaredMoney(List<StockoutOrderDO> declaredDos) {
        Integer declaredMoney = 0;
        if (ListUtil.isNotEmpty(declaredDos)) {
            for (StockoutOrderDO d : declaredDos) {
                declaredMoney = declaredMoney + d.getUserGoodsPrice();
            }
        }
        return declaredMoney;
    }

    /**
     * 计算申报累计税金
     *
     * @param declaredDos
     * @return
     */
    protected Integer calTotalTaxMoney(List<StockoutOrderDeclarePriceDO> declaredDos) {
        Double declaredTaxMoney = 0.0;
        if (ListUtil.isNotEmpty(declaredDos)) {
            for (StockoutOrderDeclarePriceDO d : declaredDos) {
                declaredTaxMoney = declaredTaxMoney + d.getTariffTax();
            }
        }
        return declaredTaxMoney.intValue();
    }
}
