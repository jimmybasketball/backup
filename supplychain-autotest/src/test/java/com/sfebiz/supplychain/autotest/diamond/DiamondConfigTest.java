package com.sfebiz.supplychain.autotest.diamond;

import com.sfebiz.supplychain.config.SupplyChainAutoTestConfig;
import org.junit.Assert;
import org.junit.Test;

/**
 * diamond配置测试
 *
 * @author liujc
 * @create 2017-06-30 14:41
 **/
public class DiamondConfigTest {

    @Test
    public void testDiamondConfig() {
        SupplyChainAutoTestConfig supplyChainAutoTestConfig = SupplyChainAutoTestConfig.getTest();
        String value = supplyChainAutoTestConfig.getRule("rule1", "1");
        Assert.assertNotNull(value);
    }
}
