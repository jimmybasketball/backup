package com.sfebiz.supplychain.service.stockout.process.create.validate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * 出库单风控校验器
 * 
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/11/25 下午6:52
 */

@Component("stockoutOrderRiskValidator")
public class StockoutOrderRiskValidator extends StockoutOrderValidator {

    private static String NAME   = "RISK_VALIDATOR";

    private static Random random = new Random(new Date().getTime());

    //    @Resource
    //    RiskControlManager    riskControlManager;

    public boolean validate(StockoutOrderRequest request) {

        /*
        StockoutOrderDO stockoutOrderDO = request.getStockoutOrderDO();
        Long portId = request.getLineEntity().portEntity.id;

        Map<String, Object> meta = JSONUtil
            .parseJSONMessage(request.getLineEntity().portEntity.meta);

        if (null != meta && meta.containsKey("riskControlPass")
            && meta.get("riskControlPass").equals(true)) {
            LogBetter
                .instance(logger)
                .setLevel(LogLevel.WARN)
                .setMsg("此口岸不受" + getValidatorName() + "规则限制，忽略")
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, request.getStockoutOrderDO().getBizId(),
                        SystemConstants.TRACE_APP))
                .addParm("出库单", request.getStockoutOrderDO().getBizId())
                .addParm("口岸", PortNid.getDescByCode(portId)).log();
            return true;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        BaseQuery<RiskControlDO> query = new BaseQuery<RiskControlDO>(new RiskControlDO());
        query.getData().setPortId(portId.intValue());
        query.getData().setIdNo(stockoutOrderDO.getIdNo());
        query.getData().setBuyerCellphone(stockoutOrderDO.getBuyerCellphone());
        query.getData().setBuyerAddress(
            stockoutOrderDO.getBuyerProvince() + stockoutOrderDO.getBuyerCity()
                    + stockoutOrderDO.getBuyerDistrict() + stockoutOrderDO.getBuyerAddress());
        query.getData().setPortValidatePassMonth(
            Integer.parseInt(simpleDateFormat.format(new Date())));

        if (riskControlManager.countByBuyerInfoAndPortId(query) > 0) {

            Calendar time = Calendar.getInstance();
            time.setTime(new Date());

            time.add(Calendar.MONTH, 1);
            time.set(Calendar.DAY_OF_MONTH, 1);
            time.set(Calendar.HOUR_OF_DAY, random.nextInt(24));

            String exceptionMsg = "超过" + request.getLineEntity().portEntity.name
                                  + "的海关规则限制: 单月一个用户（同一个省份证/同一个收货人/同一个收获地址）在一个口岸申报超过30单, 延迟到"
                                  + DateUtil.defFormatDateStr(time.getTime()) + "申报";

            LogBetter
                .instance(logger)
                .setLevel(LogLevel.WARN)
                .setMsg(exceptionMsg)
                .setTraceLogger(
                    TraceLogEntity.instance(traceLogger, request.getStockoutOrderDO().getBizId(),
                        SystemConstants.TRACE_APP))
                .addParm("出库单", request.getStockoutOrderDO().getBizId())
                .addParm("口岸", PortNid.getDescByCode(portId))
                .addParm("支付方式", request.getStockoutOrderDO().getPayType()).log();
            request.setNextRetryTime(time.getTime());
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

    public static void main(String[] args) {

        Calendar time = Calendar.getInstance();
        time.setTime(new Date());

        time.add(Calendar.MONTH, 1);
        time.set(Calendar.DAY_OF_MONTH, 1);
        time.set(Calendar.HOUR_OF_DAY, random.nextInt(24));

        System.out.println("xxxx: " + time.getTime());
    }
}
