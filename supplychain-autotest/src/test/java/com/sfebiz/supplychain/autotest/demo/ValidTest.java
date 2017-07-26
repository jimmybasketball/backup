package com.sfebiz.supplychain.autotest.demo;

import com.sfebiz.supplychain.exposed.demo.entity.DemoDetailEntity;
import com.sfebiz.supplychain.exposed.demo.entity.DemoEntity;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 校验测试
 *
 * @author liujc
 * @create 2017-07-01 15:55
 **/
public class ValidTest {

    public static final Validator VALIDATOR = new Validator();

    @Test
    public void testValid() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.name = "11";
        demoEntity.gmtCreate = new Date();
        List<DemoDetailEntity> demoDetailEntities = new ArrayList<DemoDetailEntity>();
        DemoDetailEntity demoDetailEntity = new DemoDetailEntity();
        demoDetailEntities.add(demoDetailEntity);
        demoEntity.demoDetailEntities = demoDetailEntities;
        List<ConstraintViolation> violations = VALIDATOR.validate(demoEntity);
        for (ConstraintViolation violation : violations) {
            System.out.println(violation.getCauses() + "," + violation.getMessage());
        }

        List<ConstraintViolation> violations1 = VALIDATOR.validate(demoDetailEntity);
        System.out.println(violations1.toString());
    }

}
