package com.sfebiz.supplychain.service.stockout.statemachine.processor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.sku.api.SkuService;
import com.sfebiz.supplychain.exposed.stock.api.StockService;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskStatus;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderBuyerManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderDeclarePriceManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderDetailManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderStateLogManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderTaskManager;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.statemachine.EngineService;
import com.sfebiz.supplychain.service.stockout.biz.StockoutOrderNoticeBizService;
import com.sfebiz.supplychain.service.stockout.biz.StockoutOrderStateBizService;

public class TemplateProcessor {

    /*----------------- 出库单业务相关服务 -----------------*/
    @Resource
    protected StockoutOrderStateBizService     stockoutOrderStateBizService;

    @Resource
    protected StockoutOrderStateLogManager     stockoutOrderStateLogManager;

    @Resource
    protected StockoutOrderNoticeBizService    stockoutOrderNoticeBizService;

    /*----------------- 出库单数据服务 -----------------*/
    @Resource
    protected StockoutOrderManager             stockoutManager;

    @Resource
    protected StockoutOrderBuyerManager        stockoutOrderBuyerManager;

    @Resource
    protected StockoutOrderRecordManager       stockoutOrderRecordManager;

    @Resource
    protected StockoutOrderManager             stockoutOrderManager;

    @Resource
    protected StockoutOrderDetailManager       stockoutOrderDetailManager;

    @Resource
    protected StockoutOrderTaskManager         stockoutOrderTaskManager;

    @Resource
    protected StockoutOrderDeclarePriceManager stockoutOrderDeclarePriceManager;

    /*---------------------其他服务---------------------*/
    @Resource
    protected StockService                     stockService;

    @Resource
    protected PortBillDeclareManager           portBillDeclareManager;

    @Resource
    protected LogisticsLineManager             logisticsLineManager;

    @Resource
    protected SkuService                       skuService;

    @Resource
    protected WarehouseManager                 warehouseManager;

    @Resource
    protected RouteService                     routeService;

    /*-------------------- 基础服务 ---------------------*/
    @Resource
    protected Lock                             distributedLock;

    @Resource
    protected DataSourceTransactionManager     transactionManager;

    @Resource
    protected EngineService                    engineService;

    /**
     * 设置异常任务表中的任务为已完成
     *
     * @param bizId
     */
    public void finishExceptionTask(String bizId) {
        StockoutOrderTaskDO query = new StockoutOrderTaskDO();
        query.setBizId(bizId);
        query.setTaskState(TaskStatus.WAIT_HANDLE.getValue());
        List<StockoutOrderTaskDO> stockoutOrderTaskDOs = stockoutOrderTaskManager.query(BaseQuery
            .getInstance(query));
        if (stockoutOrderTaskDOs != null && stockoutOrderTaskDOs.size() > 0) {
            //如果此时出库单状态不为待出库，则将任务表置为已处理
            for (StockoutOrderTaskDO stockoutOrderTaskDO : stockoutOrderTaskDOs) {
                stockoutOrderTaskDO.setTaskState(TaskStatus.HANDLE_SUCCESS.getValue());
                stockoutOrderTaskManager.update(stockoutOrderTaskDO);
            }
        }
    }

    public LogisticsUserRouteEntity buildLogisticsUserRoute(String bizId, String content,
                                                            Long eventTime) {
        LogisticsUserRouteEntity logisticsRouteEntity = new LogisticsUserRouteEntity();
        logisticsRouteEntity.setOrderId(bizId);
        logisticsRouteEntity.setContent(content);
        logisticsRouteEntity.setEventTime(eventTime);
        return logisticsRouteEntity;
    }
}
