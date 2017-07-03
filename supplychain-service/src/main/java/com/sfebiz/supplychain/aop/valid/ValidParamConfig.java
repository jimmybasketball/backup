package com.sfebiz.supplychain.aop.valid;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 验证参数配置
 *
 * @author liujc
 * @create 2017-07-01 17:06
 **/
public class ValidParamConfig {

    /**
     * 待验证的值
     */
    private Object validValue;

    /**
     * 验证规则集合
     */
    private List<Annotation> validRules;

    public ValidParamConfig(Object validValue, List<Annotation> validRules) {
        this.validValue = validValue;
        this.validRules = validRules;
    }

    public Object getValidValue() {
        return validValue;
    }

    public List<Annotation> getValidRules() {
        return validRules;
    }
}
