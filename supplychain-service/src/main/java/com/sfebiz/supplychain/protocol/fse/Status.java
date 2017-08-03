package com.sfebiz.supplychain.protocol.fse;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Status implements Serializable{
    private static final long serialVersionUID = 4990662481402104219L;

    /**
     * 订单生产状态代码
     */
    public String orderStatus;

    /**
     * 订单生产处理状态
     */
    public String orderComment;

    /**
     * 订单节点处理时间
     */
    public String orderMakeDate;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public String getOrderMakeDate() {
        return orderMakeDate;
    }

    public void setOrderMakeDate(String orderMakeDate) {
        this.orderMakeDate = orderMakeDate;
    }

    @Override
    public String toString() {
        return "Status{" +
                "orderStatus='" + orderStatus + '\'' +
                ", orderComment='" + orderComment + '\'' +
                ", orderMakeDate='" + orderMakeDate + '\'' +
                '}';
    }
}
