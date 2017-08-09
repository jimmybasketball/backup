package com.sfebiz.supplychain.service.stockout.process.create.validate;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.sfebiz.supplychain.config.line.validate.LineValidateConfig;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;
import com.sfebiz.supplychain.util.JSONUtil;

/**
 * 口岸线路校验器
 * 
 * Created by zhaojingyang on 2015/6/10.
 */
@Component("stockoutOrderLineValidator")
public class StockoutOrderLineValidator extends StockoutOrderValidator {
    private static String NAME = "LINE_VALIDATOR";

    @Override
    public Map<String, Object> queryCfg(StockoutOrderRequest request) throws Exception {
        String cfgStr = LineValidateConfig.getPortNidByLineId(String.valueOf(request.getLineBO()
            .getId()));
        return JSONUtil.parseJSONMessage(cfgStr);
    }

    @Override
    public List<StockoutOrderDO> queryData(StockoutOrderRequest request, Map<String, Object> cfgMap)
                                                                                                    throws Exception {
        /*
        StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
        List<Long> lineIds = new ArrayList<Long>();
        lineIds.add(stockoutOrderBO.getLineId());
        Object obj = cfgMap.get(LineValidateConfig.LINE_GROUP);
        if (null != obj) {
            String[] lines = ((String) obj).split(LineValidateConfig.LINE_GROUP_SEQ);
            for (String s : lines) {
                lineIds.add(Long.valueOf(s));
            }
        }
        //查询口岸时间段内已申报出库单
        List<StockoutOrderDO> declaredDos = stockoutOrderManager.queryValidatePassDosGroup(
            stockoutOrderBO.getBuyerBO().getBuyerCertNo(), stockoutOrderBO.getBuyerBO()
                .getBuyerCertType(), lineIds, stockoutOrderBO.getId(),
            request.getTimeLimitRange()[0], request.getTimeLimitRange()[1]);
        return declaredDos;
        */
        // TODO 新系统待实现
        return null;
    }

    @Override
    public boolean isPass(StockoutOrderRequest request) {
        String cfgStr = LineValidateConfig.getPortNidByLineId(String.valueOf(request.getLineBO()
            .getId()));
        if (StringUtils.isBlank(cfgStr)) {
            return true;
        }
        return false;
    }

    @Override
    public String getValidatorName() {
        return NAME;
    }
}
