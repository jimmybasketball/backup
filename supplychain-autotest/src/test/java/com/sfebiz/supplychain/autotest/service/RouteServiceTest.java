package com.sfebiz.supplychain.autotest.service;

import com.alibaba.fastjson.JSON;
import com.sfebiz.supplychain.autotest.BaseServiceTest;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsSystemRouteEntity;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderRecordBO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 路由服务测试
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-31 13:54
 **/
public class RouteServiceTest extends BaseServiceTest{

    private static final String ORDER_ID = "3201707141000093190S0002";

    @Test
    public void initUserRoute() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -20);

        //国际物流
        LogisticsUserRouteEntity userRouteEntity = new LogisticsUserRouteEntity();
        userRouteEntity.eventTime = calendar.getTimeInMillis();
        userRouteEntity.content = "国际物流路由信息";
        userRouteEntity.mailNo = "3333";

        routeService.appendUserRoute(ORDER_ID, RouteType.INTERNATIONAL.getType(), userRouteEntity);

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        //集货
        LogisticsUserRouteEntity jihuoRoute = new LogisticsUserRouteEntity();
        jihuoRoute.eventTime = calendar.getTimeInMillis();
        jihuoRoute.content = "集货路由信息01";

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        LogisticsUserRouteEntity jihuoRoute2 = new LogisticsUserRouteEntity();
        jihuoRoute2.eventTime = calendar.getTimeInMillis();;
        jihuoRoute2.content = "集货路由信息02";
        routeService.appendUserRoute(ORDER_ID, RouteType.TRANSIT.getType(), jihuoRoute);
        routeService.appendUserRoute(ORDER_ID, RouteType.TRANSIT.getType(), jihuoRoute2);

        //清关
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        LogisticsUserRouteEntity qgRoute = new LogisticsUserRouteEntity();
        qgRoute.eventTime = calendar.getTimeInMillis();;
        qgRoute.content = "清关路由信息01";

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        LogisticsUserRouteEntity qgRoute2 = new LogisticsUserRouteEntity();
        qgRoute2.eventTime = calendar.getTimeInMillis();;
        qgRoute2.content = "清关路由信息02";

        routeService.appendUserRoute(ORDER_ID, RouteType.CLEARANCE.getType(), qgRoute);
        routeService.appendUserRoute(ORDER_ID, RouteType.CLEARANCE.getType(), qgRoute2);


        //国内物流
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        LogisticsUserRouteEntity gnRoute = new LogisticsUserRouteEntity();
        gnRoute.eventTime = calendar.getTimeInMillis();;
        gnRoute.content = "国内路由信息01";
        gnRoute.carrierCode = "SF";
        gnRoute.mailNo = "555555";

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        LogisticsUserRouteEntity gnRoute2 = new LogisticsUserRouteEntity();
        gnRoute2.eventTime = calendar.getTimeInMillis();;
        gnRoute2.content = "国内路由信息02";
        gnRoute2.carrierCode = "SF";
        gnRoute2.mailNo = "555555";


        routeService.appendUserRoute(ORDER_ID, RouteType.INTERNAL.getType(), gnRoute);
        routeService.appendUserRoute(ORDER_ID, RouteType.INTERNAL.getType(), gnRoute2);


        //自定义路由信息
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        LogisticsUserRouteEntity zdyRoute = new LogisticsUserRouteEntity();
        zdyRoute.eventTime = calendar.getTimeInMillis();;
        zdyRoute.content = "自定义路由01";

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        LogisticsUserRouteEntity zdyRoute02 = new LogisticsUserRouteEntity();
        zdyRoute02.eventTime = calendar.getTimeInMillis();;
        zdyRoute02.content = "自定义路由02";

        routeService.appendUserRoute(ORDER_ID, RouteType.USERDEFINED.getType(), zdyRoute);
        routeService.appendUserRoute(ORDER_ID, RouteType.USERDEFINED.getType(), zdyRoute02);

    }

    @Test
    public void testOverrideUserRoute() {
        LogisticsUserRouteEntity userRouteEntity = new LogisticsUserRouteEntity();
        userRouteEntity.eventTime = 155L;
        userRouteEntity.content = "国内路由信息覆盖了";
        userRouteEntity.mailNo = "23232";

        List<LogisticsUserRouteEntity> routeEntities = new ArrayList<LogisticsUserRouteEntity>();
        routeEntities.add(userRouteEntity);
        routeService.overrideUserRoute(ORDER_ID, RouteType.INTERNAL.getType(), routeEntities);
    }

    @Test
    public void testGetUserRoute() {
        CommonRet<List<LogisticsUserRouteEntity>> commonRet = routeService.getUserRoutes(ORDER_ID);
        System.out.println(JSON.toJSONString(commonRet));
    }


    @Test
    public void testAddSystemRoute() {
        LogisticsSystemRouteEntity systemRouteEntity = new LogisticsSystemRouteEntity();
        systemRouteEntity.orderId = ORDER_ID;
        systemRouteEntity.content = "仓库已发货";
        systemRouteEntity.opreator = SystemUserName.OPSC.getValue();

        LogisticsSystemRouteEntity systemRouteEntity2 = new LogisticsSystemRouteEntity();
        systemRouteEntity2.orderId = ORDER_ID;
        systemRouteEntity2.content = "海外仓已入库";
        systemRouteEntity2.opreator = SystemUserName.OPSC.getValue();

        routeService.appandSystemRoute(systemRouteEntity);
        routeService.appandSystemRoute(systemRouteEntity2);
    }


    @Test
    public void testGetSystemRoute() {
        CommonRet<List<LogisticsSystemRouteEntity>> systemRouteList = routeService.getSystemRouteList(ORDER_ID);
        System.out.println(JSON.toJSONString(systemRouteList));
    }

    @Test
    public void testFetchRoute() throws Exception {
        StockoutOrderBO stockoutOrderBO = new StockoutOrderBO();
        stockoutOrderBO.setId(1L);
        stockoutOrderBO.setBizId("3201707141000093190S0002");
        stockoutOrderBO.setOrderState(StockoutOrderState.SIGNED.getValue());
        stockoutOrderBO.setIntlCarrierCode("EMS");
        stockoutOrderBO.setIntlMailNo("20202020202");
        stockoutOrderBO.setIntrCarrierCode("SF");
        stockoutOrderBO.setIntrMailNo("10101010101");

        StockoutOrderRecordBO recordBO = new StockoutOrderRecordBO();
        recordBO.setTplIntlState(1);

        stockoutOrderBO.setRecordBO(recordBO);
        boolean isPolling = internationalRouteFetchHandler.fetchRouteByStockoutOrder(stockoutOrderBO);

    }
}
