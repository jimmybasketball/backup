package com.sfebiz.supplychain.aop.valid;

/**
 * 校验失败异常
 *
 * @author liujc
 * @create 2017-07-01 19:57
 **/
public class ValidateFailException extends RuntimeException{

    private String violation;

    public ValidateFailException(String violation) {
        super(violation);
        this.violation = violation;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }
}
