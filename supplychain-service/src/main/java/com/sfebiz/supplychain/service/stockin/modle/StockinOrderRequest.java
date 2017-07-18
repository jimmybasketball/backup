package com.sfebiz.supplychain.service.stockin.modle;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.exposed.stockinorder.entity.StockinOrderDetailEntity;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import com.sfebiz.supplychain.service.statemachine.EngineRequest;
import com.sfebiz.supplychain.service.statemachine.EngineType;

import java.util.List;

/**
 * Created by zhangyajing on 2017/7/17.
 */
public class StockinOrderRequest extends EngineRequest{

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("stockinorder");

    private StockinOrderDO stockinOrderDO;

    private List<StockinOrderDetailEntity> stockinOrderDetailEntities;

    @Override
    public String getTraceId() {
        if (null != stockinOrderDO && null != stockinOrderDO.getId()) {
            return stockinOrderDO.getId().toString();
        }
        return null != getId() ? getId().toString() : null;
    }

    @Override
    public HaitaoTraceLogger getTraceLogger() {
        return traceLogger;
    }

    @Override
    public EngineType getEngineType() {
        return EngineType.STOCKIN_ORDER;
    }

    public StockinOrderDO getStockinOrderDO() {
        return stockinOrderDO;
    }

    public void setStockinOrderDO(StockinOrderDO stockinOrderDO) {
        this.stockinOrderDO = stockinOrderDO;
    }

    public List<StockinOrderDetailEntity> getStockinOrderDetailEntities() {
        return stockinOrderDetailEntities;
    }

    public void setStockinOrderDetailEntities(List<StockinOrderDetailEntity> stockinOrderDetailEntities) {
        this.stockinOrderDetailEntities = stockinOrderDetailEntities;
    }

    @Override
    public String toString() {
        return "StockinOrderRequest{" +
                "stockinOrderDO=" + stockinOrderDO +
                ", stockinOrderDetailEntities=" + stockinOrderDetailEntities +
                '}';
    }
}
