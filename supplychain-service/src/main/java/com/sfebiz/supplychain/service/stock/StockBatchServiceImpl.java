package com.sfebiz.supplychain.service.stock;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.line.entity.LogisticsLineEntity;
import com.sfebiz.supplychain.exposed.stock.api.StockBatchService;
import com.sfebiz.supplychain.exposed.stock.entity.StockBatchEntity;
import com.sfebiz.supplychain.exposed.stock.enums.StockBatchStateType;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;
import com.sfebiz.supplychain.persistence.base.stock.manager.StockBatchManager;
import com.sfebiz.supplychain.service.line.LogisticsLineServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 批次库存服务
 * @date 2017-07-14 14:46
 **/
public class StockBatchServiceImpl implements StockBatchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockBatchServiceImpl.class);
    @Resource
    private StockBatchManager stockBatchManager;

    @Override
    public CommonRet<Long> createStockBatch(StockBatchEntity stockBatchEntity) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        try {
            StockBatchDO stockBatchDO = new StockBatchDO();
            BeanCopier beanCopier = BeanCopier.create(StockBatchEntity.class, StockBatchDO.class, false);
            beanCopier.copy(stockBatchEntity, stockBatchDO, null);
            //TODO 初始化数据,设置批次库存
            stockBatchDO.setState(StockBatchStateType.ENABLE.value);


            stockBatchManager.insert(stockBatchDO);
            commonRet.setResult(stockBatchDO.getId());
            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("[批次库存]创建批次库存成功").log();
        } catch (Exception e) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR).setException(e).setMsg("[批次库存]创建批次库存异常").log();
        }
        return commonRet;
    }
}
