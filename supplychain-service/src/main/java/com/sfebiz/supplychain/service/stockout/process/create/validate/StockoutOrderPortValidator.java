package com.sfebiz.supplychain.service.stockout.process.create.validate;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;
import com.sfebiz.supplychain.util.JSONUtil;

/**
 * 出库单口岸相关校验器
 * 
 * Created by zhaojingyang on 2015/6/10.
 */
@Component("stockoutOrderPortValidator")
public class StockoutOrderPortValidator extends StockoutOrderValidator {
    private static String NAME    = "PORT_VALIDATOR";
    private static String DEF_YES = "Y";

    @Override
    public Map<String, Object> queryCfg(StockoutOrderRequest request) {
        return JSONUtil.parseJSONMessage(request.getLineBO().getPortBO().getMeta());
    }

    @Override
    public List<StockoutOrderDO> queryData(StockoutOrderRequest request, Map<String, Object> cfgMap) {
        /*
        StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
        //查询口岸时间段内已申报出库单
        List<StockoutOrderDO> declaredDos = stockoutOrderManager.queryByIdNoSendTime(
            stockoutOrderDO.getIdNo(), stockoutOrderDO.getIdType(),
            request.getLineEntity().portEntity.id, stockoutOrderDO.getId(),
            request.getTimeLimitRange()[0], request.getTimeLimitRange()[1]);
        return declaredDos;
        */
        // TODO 新系统待实现
        return null;
    }

    /**
     * 如果订单不走口岸直接PASS
     * 如果没有线路配置规则 或者线路配置规则上不需要口岸限制 那么直接PASS
     * @param request
     * @return
     */
    @Override
    public boolean isPass(StockoutOrderRequest request) {
        if (null == request.getLineBO().getPortBO()) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("出库单不走口岸，无须验证PASS")
                .addParm("出库单", request.getStockoutOrderBO().getBizId()).log();
            return true;
        }

        Map<String, Object> meta = queryCfg(request);
        if (null != meta && meta.containsKey("isPass") && meta.get("isPass").equals(true)) {
            return true;
        }

        return false;
    }

    @Override
    public String getValidatorName() {
        return NAME;
    }
}
