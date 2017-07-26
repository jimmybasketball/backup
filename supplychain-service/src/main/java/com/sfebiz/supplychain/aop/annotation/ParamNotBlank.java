package com.sfebiz.supplychain.aop.annotation;

import java.lang.annotation.*;

/**
 * 参数非空标记
 *
 * @author liujc
 * @create 2017-07-01 16:46
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ParamNotBlank {
    String value() default "";
}
