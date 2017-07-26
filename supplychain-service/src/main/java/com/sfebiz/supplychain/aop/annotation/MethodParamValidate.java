package com.sfebiz.supplychain.aop.annotation;

import java.lang.annotation.*;

/**
 * 开启参数校验
 *
 * @author liujc
 * @create 2017-06-30 18:16
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MethodParamValidate {
    String value() default "";
}
