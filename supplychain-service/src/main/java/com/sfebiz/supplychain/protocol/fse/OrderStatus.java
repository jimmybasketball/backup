package com.sfebiz.supplychain.protocol.fse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class OrderStatus implements Serializable{
    private static final long serialVersionUID = 4720554014112510978L;

    public String orderCode;

    public String carrier;

    public String logisticsNO;

    public List<Status> status;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getLogisticsNO() {
        return logisticsNO;
    }

    public void setLogisticsNO(String logisticsNO) {
        this.logisticsNO = logisticsNO;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "orderCode='" + orderCode + '\'' +
                ", carrier='" + carrier + '\'' +
                ", logisticsNO='" + logisticsNO + '\'' +
                ", status=" + status +
                '}';
    }
}
