package com.sfebiz.supplychain.service.stockout.process.create.validate;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.service.customs.biz.CustomsLimitBizService;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * 出库单海关相关校验
 * 
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/11/25 下午5:20
 */
@Component("stockoutOrderCustomsValidator")
public class StockoutOrderCustomsValidator extends StockoutOrderValidator {

    private static String  NAME = "CUSTOMS_VALIDATOR";

    @Resource
    CustomsLimitBizService customsLimitBizService;

    //    @Resource
    //    PayerLimitManager      payerLimitManager;

    public boolean validate(StockoutOrderRequest request) {

        /*
        String masterBizId = request.getStockoutOrderBO().getMerchantOrderNo();
        Long portId = request.getLineBO().getPortBO().getId();

        Map<String, Object> meta = JSONUtil.parseJSONMessage(request.getLineBO().getPortBO()
            .getMeta());

        if (null != meta && meta.containsKey("customsLimitPass")
            && meta.get("customsLimitPass").equals(true)) {
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.WARN)
                .setMsg("此口岸不受" + getValidatorName() + "规则限制，忽略")
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO().getBizId(),
                        SystemConstants.TRACE_APP))
                .addParm("出库单", request.getStockoutOrderBO().getBizId())
                .addParm("口岸", PortNid.getDescByCode(portId)).log();
            return true;
        }

        CustomLimitEntity customLimitEntity = customsLimitBizService.getCustomsMaxLimitByOrderId(
            masterBizId, portId.intValue());
        if (null != customLimitEntity && customLimitEntity.getLimitTime().after(new Date())) {

            String whiteListByUserId = LogisticsDynamicConfig.getRiskConfig().getRule("exception",
                "whiteListByUserId");
            if (StringUtils.isNotBlank(whiteListByUserId)) {
                List<String> whiteListByUserIds = Arrays.asList(whiteListByUserId.split(","));
                if (whiteListByUserIds.contains(request.getStockoutOrderBO().getUserId())) {
                    String exceptionMsg = "超过"
                                          + request.getLineBO().getPortBO().getName()
                                          + "的海关规则限制: "
                                          + CustomsReturnReason.valueOf(
                                              customLimitEntity.getReason()).getDescription()
                                          + ", 但是此用户因颜值较高额外开恩，放过!";
                    LogBetter
                        .instance(logger)
                        .setLevel(LogLevel.WARN)
                        .setMsg(exceptionMsg)
                        .setTraceLogger(
                            TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO()
                                .getBizId(), SystemConstants.TRACE_APP))
                        .addParm("出库单", request.getStockoutOrderBO().getBizId())
                        .addParm("口岸", PortNid.getDescByCode(portId))
                        .addParm("支付人", customLimitEntity.getBuyerId())
                        .addParm("支付方式", request.getStockoutOrderBO().getMerchantPayType()).log();

                    //超过30单之后用连连申报方式走单
                    request.getStockoutOrderBO().setDeclarePayType(
                        SystemConstants.DECLARE_YIHUIJINPAY);
                    StockoutOrderDO update = new StockoutOrderDO();
                    update.setId(request.getStockoutOrderBO().getId());
                    update.setDeclarePayType(SystemConstants.DECLARE_YIHUIJINPAY);
                    stockoutOrderManager.update(update);
                    return true;
                }
            }

            String exceptionMsg = "超过"
                                  + request.getLineBO().getPortBO().getName()
                                  + "的海关规则限制: "
                                  + CustomsReturnReason.valueOf(customLimitEntity.getReason())
                                      .getDescription() + ", 延迟到"
                                  + DateUtil.defFormatDateStr(customLimitEntity.getLimitTime())
                                  + "申报";
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.WARN)
                .setMsg(exceptionMsg)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, request.getStockoutOrderBO().getBizId(),
                        SystemConstants.TRACE_APP))
                .addParm("出库单", request.getStockoutOrderBO().getBizId())
                .addParm("口岸", PortNid.getDescByCode(portId))
                .addParm("支付人", customLimitEntity.getBuyerId())
                .addParm("支付方式", request.getStockoutOrderBO().getMerchantPayType()).log();
            request.setNextRetryTime(customLimitEntity.getLimitTime());
            request.setExceptionMessage(exceptionMsg);
            return false;

        }
        */
        // TODO 新系统待实现

        return true;
    }

    @Override
    public Map<String, Object> queryCfg(StockoutOrderRequest request) throws Exception {
        return null;
    }

    @Override
    public List<StockoutOrderDO> queryData(StockoutOrderRequest request, Map<String, Object> cfgMap)
                                                                                                    throws Exception {
        return null;
    }

    @Override
    public boolean isPass(StockoutOrderRequest request) {
        return false;
    }

    @Override
    public String getValidatorName() {
        return NAME;
    }
}
