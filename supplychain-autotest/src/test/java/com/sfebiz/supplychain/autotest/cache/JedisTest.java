package com.sfebiz.supplychain.autotest.cache;

import com.sfebiz.supplychain.autotest.BaseServiceTest;
import org.junit.Test;

/**
 * redis测试
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-08-04 15:30
 **/
public class JedisTest extends BaseServiceTest{

    @Test
    public void testJedisCluster() {
        String result = jedisCluster.set("test", "test");
        System.out.println(result);
    }
}
