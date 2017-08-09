package com.sfebiz.supplychain.queue.consumer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderStateLogDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderStateLogManager;

/**
 * Created by sam on 3/25/15.
 * Email: sambean@126.com
 */
public abstract class AbstractMessageProcesser implements MessageProcesser {

    @Autowired
    protected StockoutOrderManager           stockoutOrderManager;

    @Resource
    protected StockoutOrderStateLogManager   stockoutOrderStateLogManager;

    protected static final HaitaoTraceLogger traceLogger     = HaitaoTraceLoggerFactory
                                                                 .getTraceLogger("order");
    protected static final HaitaoTraceLogger openTraceLogger = HaitaoTraceLoggerFactory
                                                                 .getTraceLogger("openOrder");
    protected static final Logger            logger          = LoggerFactory
                                                                 .getLogger(AbstractMessageProcesser.class);

    public void writeLogisticsLog(StockoutOrderStateLogDO bean) {
        if (bean == null) {
            return;
        }
        stockoutOrderStateLogManager.insert(bean);
    }
}
