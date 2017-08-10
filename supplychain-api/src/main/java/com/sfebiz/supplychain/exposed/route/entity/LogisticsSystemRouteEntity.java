package com.sfebiz.supplychain.exposed.route.entity;

import com.sfebiz.supplychain.exposed.route.enums.SystemRouteLevel;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;

/**
 * 订单物流系统路由
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-31 11:36
 **/
public class LogisticsSystemRouteEntity implements Serializable, Comparable<LogisticsSystemRouteEntity> {
    private static final long serialVersionUID = 1856822817169691033L;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    public String orderId;

    /**
     * 路由发生时间
     */
    public Long eventTime;


    /**
     * 记录级别 INFO/WARN/ERROR
     */
    public String level = SystemRouteLevel.INFO.getValue();


    /**
     * 路由信息内容
     */
    @NotNull(message = "路由信息内容不能为空")
    public String content;


    /**
     * 路由操作人
     */
    @NotNull(message = "操作人不能为空")
    public String opreator;


    @Override
    public int compareTo(LogisticsSystemRouteEntity o) {
        //三个字段作为唯一key
        String otherKey = o.orderId + o.eventTime + o.content;
        String thisKey = this.orderId + this.eventTime + o.content;
        if (thisKey.equals(otherKey)) {
            return 0;
        }
        //按日期倒序排序
        return o.eventTime >= this.eventTime ? 1 : -1;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOpreator() {
        return opreator;
    }

    public void setOpreator(String opreator) {
        this.opreator = opreator;
    }
}