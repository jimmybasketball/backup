package com.sfebiz.supplychain.autotest.service;

import com.sfebiz.supplychain.autotest.BaseServiceTest;
import com.sfebiz.supplychain.exposed.stock.api.StockService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 批次库存测试类
 * @date 2017-07-28 10:01
 **/
public class StockServiceImplTest extends BaseServiceTest {

    @Autowired
    private StockService stockService;

    /**
     * 新增批次库存
     */
    @Test
    public void incrementStockBatch(){

    }
}
