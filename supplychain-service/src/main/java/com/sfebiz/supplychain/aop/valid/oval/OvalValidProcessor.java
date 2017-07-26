package com.sfebiz.supplychain.aop.valid.oval;

import com.sfebiz.supplychain.aop.valid.ValidProcessor;
import com.sfebiz.supplychain.aop.valid.ValidResult;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import java.util.List;

/**
 * 使用oval校验框架的校验处理
 *
 * @author liujc
 * @create 2017-07-01 21:42
 **/
public class OvalValidProcessor implements ValidProcessor{

    @Override
    public ValidResult<String> validate(Object obj) {
        ValidResult<String> validResult = new ValidResult<String>();
        Validator validator = new Validator();
        List<ConstraintViolation> violations = validator.validate(obj);

        if (violations != null && violations.size() > 0) {
            String className = obj.getClass().getSimpleName();
            StringBuilder failMsg = new StringBuilder("");
            for (ConstraintViolation constraintViolation : violations) {
                failMsg.append(className).append(":").append(constraintViolation.getMessage()).append(",");
            }
            failMsg.deleteCharAt(failMsg.length() - 1);
            validResult.setValid(false);
            validResult.setResult(failMsg.toString());
        }
        return validResult;
    }
}
