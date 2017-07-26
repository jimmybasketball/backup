package com.sfebiz.supplychain.aop.valid.processor;

import com.sfebiz.supplychain.aop.valid.ValidProcessor;
import com.sfebiz.supplychain.aop.valid.ValidResult;

import java.util.Collection;
import java.util.Map;

/**
 * 参数不为空校验
 * <p>
 * 如果被校验的类型不是基本数据类型，则继续委托Oval进行实体属性校验
 *
 * @author liujc
 * @create 2017-07-01 21:47
 **/
public class ParamNotBlankProcessor implements ValidProcessor {


    private ValidProcessor entityValidProcessor;

    private String failMsg = "param is required";

    public ParamNotBlankProcessor(ValidProcessor entityValidProcessor, String failMsg) {
        this.entityValidProcessor = entityValidProcessor;
        this.failMsg = failMsg;
    }

    @Override
    public ValidResult<String> validate(Object obj) {
        ValidResult<String> validResult = new ValidResult<String>();

        if (obj == null) {
            validResult.setValid(false);
            validResult.setResult(failMsg);
            return validResult;
        }

        Class type = obj.getClass();
        //判断对象的类型
        if (obj instanceof String) {
            String value = (String) obj;
            if ("".equals(value.trim())) {
                validResult.setValid(false);
                validResult.setResult(failMsg);
                return validResult;
            }
        } else if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            if (collection.size() < 1) {
                validResult.setValid(false);
                validResult.setResult(failMsg);
                return validResult;
            }
            //TODO 集合中的元素是否需要验证
        } else if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.isEmpty()) {
                validResult.setValid(false);
                validResult.setResult(failMsg);
                return validResult;
            }
        } else if (type.getName().endsWith("Entity")) {
            //约定以Entity结尾的类型 可以触发对象验证
            return entityValidProcessor.validate(obj);
        }

        return validResult;
    }
}
