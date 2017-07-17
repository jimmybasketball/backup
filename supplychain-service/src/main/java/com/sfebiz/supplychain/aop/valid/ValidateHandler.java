package com.sfebiz.supplychain.aop.valid;

import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.aop.valid.oval.OvalValidProcessor;
import com.sfebiz.supplychain.aop.valid.processor.ParamNotBlankProcessor;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 处理方法参数验证
 *
 * @author liujc
 * @create 2017-07-01 19:33
 **/
public class ValidateHandler {

    /**
     * 具体的参数值和对应的规则 进行校验
     *
     * @param paramConfig
     * @return
     */
    public static ValidResult<String> validMethodParam(ValidParamConfig paramConfig) {
        Object value = paramConfig.getValidValue();
        List<Annotation> rules = paramConfig.getValidRules();
        if (rules.size() > 0) {
            for (Annotation rule : rules) {
                ValidProcessor validProcessor = getValidProcessorByAnnotation(rule);
                if (validProcessor != null) {
                    ValidResult<String> validResult = validProcessor.validate(value);
                    if (!validResult.isValid()) {
                        //只要一种规则校验失败，直接退出
                        return validResult;
                    }
                }
            }
        }


        return new ValidResult<String>();
    }

    private static ValidProcessor getValidProcessorByAnnotation(Annotation rule) {
        if (ParamNotBlank.class == rule.annotationType()) {
            ParamNotBlank paramNotBlank = (ParamNotBlank)rule;
            return new ParamNotBlankProcessor(new OvalValidProcessor(), paramNotBlank.value());
        } else {
            //暂时还没定义其他类型的注解
            return null;
        }
    }

    /**
     * 将校验违反信息转换为对应返回实体
     *
     * @param violations
     * @param returnType
     * @return
     */
    public static ValidResult<Object> transformResult(List<String> violations, Class returnType) {
        if (violations != null && violations.size() > 0) {

            StringBuilder failMsg = new StringBuilder("");
            for (String violation : violations) {
                failMsg.append(violation).append(",");
            }
            failMsg.deleteCharAt(failMsg.length() - 1);

            if (CommonRet.class.equals(returnType)) {
                //目前支持返回类型为CommonRet的方法，在校验失败后可以正常返回，其他返回类型通过抛出运行时异常返回
                CommonRet ret = new CommonRet();
                ret.setRetCode(SCReturnCode.COMMON_FAIL.getCode());
                ret.setRetMsg(failMsg.toString());

                ValidResult<Object> validResult = new ValidResult<Object>();
                validResult.setValid(false);
                validResult.setResult(ret);
                return validResult;
            } else {
                throw new ValidateFailException(failMsg.toString());
            }
        }
        return new ValidResult<Object>();
    }
}
