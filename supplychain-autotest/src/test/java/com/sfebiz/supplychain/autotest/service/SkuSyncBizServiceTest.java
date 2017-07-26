package com.sfebiz.supplychain.autotest.service;

import com.sfebiz.supplychain.autotest.BaseServiceTest;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsOperaterType;
import com.sfebiz.supplychain.provider.biz.SkuSyncBizService;
import net.pocrd.entity.ServiceException;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品同步测试
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-26 11:30
 **/
public class SkuSyncBizServiceTest extends BaseServiceTest {

    @Resource
    protected SkuSyncBizService skuSyncBizService;

    @Test
    public void sendProductBasicInfo2WmsAdd(){
        List<Long> skuIds = new ArrayList<Long>();
        skuIds.add(1132012l);
        try {
            skuSyncBizService.sendProductBasicInfo2Wms(skuIds, 60l, WmsOperaterType.ADD);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendProductBasicInfo2WmsUpdate(){
        List<Long> skuIds = new ArrayList<Long>();
        skuIds.add(1132012l);
        try {
            skuSyncBizService.sendProductBasicInfo2Wms(skuIds, 60l, WmsOperaterType.UPDATE);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}