package com.sfebiz.supplychain.config.paytype;

/**
 * 支付方式
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-19 13:46
 **/
public class PayType {

    /**
     * 编码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

    public PayType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public PayType() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "PayType{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
