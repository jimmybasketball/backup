package com.sfebiz.supplychain.protocol.zto.internation;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/2
 * Time: 下午2:31
 */
public class ZTOInternationrderResponse implements Serializable {

    private static final long serialVersionUID = 692079261924408430L;

    /**
     * 参数数据
     */
    private ZTOOrderResponseData data;

    /**
     * 异常信息
     */
    private String msg;

    /**
     * 响应结果
     */
    private Boolean status;


    public class ZTOOrderResponseData {
        /**
         * 运单号
         */
        public String logisticsno;

        public String getLogisticsno() {
            return logisticsno;
        }

        public void setLogisticsno(String logisticsno) {
            this.logisticsno = logisticsno;
        }
    }

    public ZTOOrderResponseData getData() {
        return data;
    }

    public void setData(ZTOOrderResponseData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
