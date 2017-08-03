package com.sfebiz.supplychain.protocol.zto.internal;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/2
 * Time: 下午2:31
 */
public class ZTOInternalCountResponse implements Serializable {

    private static final long serialVersionUID = 692079261924408430L;

    /**
     * 响应结果:true/false
     */
    private String result;

    /**
     * 参数数据
     */
    private ZTOCountResponseData counter;

    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常描述
     */
    private String remark;



    public class ZTOCountResponseData {

        /**
         * 剩余数量
         */
        public String available;

        public String getAvailable() {
            return available;
        }

        public void setAvailable(String available) {
            this.available = available;
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ZTOCountResponseData getCounter() {
        return counter;
    }

    public void setCounter(ZTOCountResponseData counter) {
        this.counter = counter;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
