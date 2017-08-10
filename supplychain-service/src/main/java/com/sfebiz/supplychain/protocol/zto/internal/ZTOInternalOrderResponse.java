package com.sfebiz.supplychain.protocol.zto.internal;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/2
 * Time: 下午2:31
 */
public class ZTOInternalOrderResponse implements Serializable {

    private static final long serialVersionUID = 692079261924408430L;

    /**
     * 响应结果:true/false
     */
    private String result;

    /**
     * 参数数据
     */
    private ZTOOrderResponseData keys;

    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常描述
     */
    private String remark;



    public class ZTOOrderResponseData {

        /**
         * 订单号
         */
        public String id;

        /**
         * 订单号
         */
        public String orderid;

        /**
         * 运单号
         */
        public String mailno;


        /**
         * 这个是大头笔
         */
        public String mark;

        /**
         * 网点编号
         */
        public String sitecode;

        /**
         * 网点名称
         */
        public String sitename;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMailno() {
            return mailno;
        }

        public void setMailno(String mailno) {
            this.mailno = mailno;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getSitecode() {
            return sitecode;
        }

        public void setSitecode(String sitecode) {
            this.sitecode = sitecode;
        }

        public String getSitename() {
            return sitename;
        }

        public void setSitename(String sitename) {
            this.sitename = sitename;
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ZTOOrderResponseData getKeys() {
        return keys;
    }

    public void setKeys(ZTOOrderResponseData keys) {
        this.keys = keys;
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

    @Override
    public String toString() {
        return "ZTOInternalOrderResponse{" +
                "result='" + result + '\'' +
                ", keys=" + keys +
                ", code='" + code + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
