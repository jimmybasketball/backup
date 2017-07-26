package com.sfebiz.supplychain.aop.valid;

/**
 * 验证结果
 *
 * @author liujc
 * @create 2017-07-01 16:33
 **/
public class ValidResult<T> {

    private boolean isValid = true;

    private T result;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
