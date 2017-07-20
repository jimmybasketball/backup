package com.sfebiz.supplychain.service.stockout.service.exception;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/8/24 下午5:05
 */
@Component("exceptionHandlerFactory")
public class ExceptionHandlerFactory {


    @Resource
    private Map<String, ExceptionHandler> exceptionHandlerMap;

    /**
     * 根据异常类型查找异常处理类
     *
     * @param exceptionType
     * @return
     */
    public ExceptionHandler getExceptionHandlerByExceptionType(String exceptionType) {
        if (null != exceptionHandlerMap) {
            return exceptionHandlerMap.get(exceptionType);
        }
        return null;
    }
}
