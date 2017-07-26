package com.sfebiz.supplychain.aop.valid;

/**
 * 验证处理接口
 *
 * @author liujc
 * @create 2017-07-01 16:34
 **/
public interface ValidProcessor {

    /**
     * 验证基本数据类型 + 字符串类型
     * @param obj
     * @return
     */
    public ValidResult<String> validate(Object obj);
}
