package com.sfebiz.supplychain.protocol.zto.internation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/2
 * Time: 下午2:31
 */
public class ZTOInternationRoutesResponse implements Serializable {

    private static final long serialVersionUID = 692079261924408430L;

    /**
     * 参数数据
     */
    private ZTORoutesResponseData data;

    /**
     * 异常信息
     */
    private String msg;

    /**
     * 响应结果
     */
    private Boolean status;


    public class ZTORoutesResponseData {
        /**
         * 运单号
         */
        public String logisticsno;

        public List<ZTORouteTraces> traces;

        public String getLogisticsno() {
            return logisticsno;
        }

        public void setLogisticsno(String logisticsno) {
            this.logisticsno = logisticsno;
        }

        public List<ZTORouteTraces> getTraces() {
            return traces;
        }

        public void setTraces(List<ZTORouteTraces> traces) {
            this.traces = traces;
        }
    }

    public class ZTORouteTraces {

        /**
         * 录入时间
         */
        public Date CreateDate;

        /**
         * 操作时间
         */
        public Date OptDate;

        /**
         * 操作人
         */
        public String OptMan;

        /**
         * 操作事由
         */
        public String OptReason;

        /**
         * 操作状态
         */
        public String Status;

    }

    public ZTORoutesResponseData getData() {
        return data;
    }

    public void setData(ZTORoutesResponseData data) {
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
