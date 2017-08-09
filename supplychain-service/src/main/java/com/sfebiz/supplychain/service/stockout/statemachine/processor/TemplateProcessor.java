package com.sfebiz.supplychain.service.stockout.statemachine.processor;

import javax.annotation.Resource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.sfebiz.supplychain.exposed.sku.api.SkuService;
import com.sfebiz.supplychain.exposed.stock.api.StockService;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderBuyerManager;
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
    protected StockoutOrderStateBizService  stockoutOrderStateBizService;

    @Resource
    protected StockoutOrderStateLogManager  stockoutOrderStateLogManager;

    @Resource
    protected StockoutOrderNoticeBizService stockoutOrderNoticeBizService;

    /*----------------- 出库单数据服务 -----------------*/
    @Resource
    protected StockoutOrderManager          stockoutManager;

    @Resource
    protected StockoutOrderBuyerManager     stockoutOrderBuyerManager;

    @Resource
    protected StockoutOrderRecordManager    stockoutOrderRecordManager;

    @Resource
    protected StockoutOrderManager          stockoutOrderManager;

    @Resource
    protected StockoutOrderDetailManager    stockoutOrderDetailManager;

    @Resource
    protected StockoutOrderTaskManager      stockoutOrderTaskManager;

    /*-------------------- 基础服务 ---------------------*/
    @Resource
    protected Lock                          distributedLock;

    @Resource
    protected DataSourceTransactionManager  transactionManager;

    //    @Resource
    //    protected PortParamManager               portParamManager;
    //
    //    @Resource
    //    protected FreezeStockManager             freezeStockManager;

    @Resource
    protected SkuService                    skuService;

    @Resource
    protected LogisticsLineManager          logisticsLineManager;

    @Resource
    protected WarehouseManager              warehouseManager;

    //    @Resource
    //    protected LogisticsProviderDetailManager logisticsProviderDetailManager;

    //    @Resource
    //    protected StockService                   stockService;
    //
    //    @Resource
    //    protected StockBO                        stockBO;

    //    @Resource
    //    protected StockoutBizService             stockoutBizService;

    @Resource
    protected EngineService                 engineService;

    //    @Resource
    //    protected DataBaseDataSouce             dataBaseDataSouce;
    //
    //    @Resource
    //    protected MessageProducer                supplyChainMessageProducer;

    @Resource
    protected StockService                  stockService;

    //    @Resource
    //    OrderCancelProcessor                   orderCancelProcessor;
    //
    //    @Resource
    //    SkuSaleStockManager                    skuSaleStockManager;
    //
    //    @Resource
    //    ActualStockManager                     actualStockManager;
    //
    //    @Resource
    //    BatchStockManager                      batchStockManager;
    //
    //    @Resource
    //    TicketHandleService                    ticketHandleService;
    //
    //    @Resource
    //    protected ProviderSkuManager             providerSkuManager;
    //
    //    @Resource
    //    CostCalculateService                   costCalculateService;
    //
    //    @Resource
    //    protected PortService                    portService;
    //
    //    @Resource
    //    protected ChannelStockService            channelStockService;
}
