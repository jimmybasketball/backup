package com.sfebiz.supplychain.autotest.service;

import com.sfebiz.supplychain.autotest.BaseServiceTest;
import com.sfebiz.supplychain.exposed.user.enums.RNAType;
import org.junit.Assert;
import org.junit.Test;

/**
 * 实名认证服务测试
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-27 09:52
 **/
public class RealNameAuthenticationServiceTest extends BaseServiceTest{

    @Test
    public void rzTest() {
//        boolean isValid = realNameAuthenticationService.rz("刘峻池", "500106199307081312");
//        Assert.assertTrue(isValid);

        boolean isValid = realNameAuthenticationService.rz("刘峻池", "500106199307081312", RNAType.YIJIFU.getValue());
        Assert.assertTrue(isValid);
    }
}
