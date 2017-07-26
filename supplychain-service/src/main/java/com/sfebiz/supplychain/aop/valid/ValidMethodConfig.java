package com.sfebiz.supplychain.aop.valid;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法验证配置
 *
 * @author liujc
 * @create 2017-07-01 17:57
 **/
public class ValidMethodConfig {

   /**
     * 方法返回类型
     */
    private Class returnType;

    /**
     * 方法上参数验证配置
     */
    private ThreadLocal<List<ValidParamConfig>> validParamConfigsLocal = new ThreadLocal<List<ValidParamConfig>>();

    /**
     * 这里仅仅存了参数校验的规则集合
     */
    private List<ValidParamConfig> unsafeParamConfig;

    public ValidMethodConfig(List<ValidParamConfig> validParamConfigs, List<ValidParamConfig> unsafeParamConfig,Class returnType) {
        validParamConfigsLocal.set(validParamConfigs);
        this.returnType = returnType;
        this.unsafeParamConfig = unsafeParamConfig;
    }

    /**
     * 执行验证
     * @return
     */
    public ValidResult doValidate() {

        List<ValidParamConfig> validParamConfigs = validParamConfigsLocal.get();
        if (validParamConfigs == null || validParamConfigs.size() < 1) {
            //方法上没有参数需要验证，直接返回
            return new ValidResult();
        }

        List<String> violations = new ArrayList<String>();
        for (ValidParamConfig validParamConfig : validParamConfigs) {
            ValidResult<String> paramValidResult = ValidateHandler.validMethodParam(validParamConfig);
            if (!paramValidResult.isValid()) {
                violations.add(paramValidResult.getResult());
            }
        }
        //信息转换
        return ValidateHandler.transformResult(violations, returnType);
    }

    /**
     * 刷新参数验证配置的验证值，存放到线程局部变量中
     * @param args 新的参数值，依赖了数组中元素的顺序，约定优先于编码
     */
    public void flushParamConfigValue(Object[] args) {
        List<ValidParamConfig> validParamConfigs = new ArrayList<ValidParamConfig>();
        for (int i = 0; i < args.length; i ++) {
            Object value = args[i];
            List<Annotation> validRules = unsafeParamConfig.get(i).getValidRules();

            ValidParamConfig validParamConfig = new ValidParamConfig(value, validRules);
            validParamConfigs.add(validParamConfig);
        }
        validParamConfigsLocal.set(validParamConfigs);
    }
}
