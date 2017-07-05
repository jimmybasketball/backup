package com.sfebiz.supplychain.autotest.demo;

import com.sfebiz.supplychain.autotest.BaseServiceTest;
import com.sfebiz.supplychain.export.common.entity.CommonRet;
import com.sfebiz.supplychain.export.demo.entity.DemoEntity;
import com.sfebiz.supplychain.export.common.entity.Void;
import org.junit.Test;

import java.util.Date;

/**
 * 测试用例
 *
 * @author liujc
 * @create 2017-06-30 10:57
 **/
public class DemoTest extends BaseServiceTest{



    @Test
    public void testAdd() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.name = "哈哈哈哈";

        CommonRet<Long> addRet = demoApi.addEntity(demoEntity);
        System.out.println(addRet.toString());

    }

    @Test
    public void testValidDemoApi() {
        CommonRet<Void> commonRet = demoApi.validTest(null,"haha", null);
        System.out.println(commonRet.toString());

        DemoEntity demoEntity2 = new DemoEntity();
        demoEntity2.name = "测试";
        demoEntity2.gmtCreate = new Date();
        CommonRet<Void> commonRet2 = demoApi.validTest(123L,"yeye", demoEntity2);
        System.out.println(commonRet2.toString());
    }

    @Test
    public void testUpdate() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.name = "修改了";

        Long id = 4L;
        CommonRet<Void> ret = demoApi.updateEntity(id, demoEntity);
        System.out.println(ret.toString());
    }

}
