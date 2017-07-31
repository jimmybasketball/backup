package com.sfebiz.supplychain.autotest;

import com.sfebiz.supplychain.exposed.demo.api.DemoApi;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockinorder.api.StockInService;
import com.sfebiz.supplychain.exposed.user.api.RealNameAuthenticationService;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.annotation.Resource;

/**
 * 测试基类
 *
 * @author liujc
 * @create 2017-06-30 10:35
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {
        "classpath*:/META-INF/spring/application-context.xml",
        "classpath*:/META-INF/spring/application-statemachine.xml",
        "classpath*:/META-INF/spring/application-aop.xml",
        "classpath*:/META-INF/spring/application-task.xml"
})
@Ignore
public class BaseServiceTest {

    @Resource
    protected DemoApi demoApi;

    @Resource
    protected StockInService stockInService;

    @Resource
    protected RealNameAuthenticationService realNameAuthenticationService;

    @Resource
    protected RouteService routeService;
}
