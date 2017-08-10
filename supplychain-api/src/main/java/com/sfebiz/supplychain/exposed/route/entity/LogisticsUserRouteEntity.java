package com.sfebiz.supplychain.exposed.route.entity;

import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 订单物流用户路由
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017/7/28 12:05
 */
public class LogisticsUserRouteEntity implements Serializable, Comparable<LogisticsUserRouteEntity> {
    private static final long serialVersionUID = 3689828946676157183L;

    /**
     * 订单ID
     */
    public String orderId;

    /**
     * 路由发生时间
     */
    @NotNull(message = "路由时间不能为空")
    public Long eventTime;

    /**
     * 承运商编码
     */
    public String carrierCode;

    /**
     * 运单号
     */
    public String mailNo;

    /**
     * 发生位置
     */
    public String position;

    /**
     * 路由信息内容
     */
    @NotNull(message = "路由信息内容不能为空")
    public String content;

    /**
     * 操作码
     */
    public String opcode;


    /**
     * 用户路由类型
     */
    public String routeType;

    @Override
    public String toString() {
        return "LogisticsUserRouteEntity{" +
                "orderId='" + orderId + '\'' +
                ", eventTime=" + eventTime +
                ", carrierCode='" + carrierCode + '\'' +
                ", mailNo='" + mailNo + '\'' +
                ", position='" + position + '\'' +
                ", content='" + content + '\'' +
                ", opcode='" + opcode + '\'' +
                '}';
    }

    @Override
    public int compareTo(LogisticsUserRouteEntity o) {
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

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public static void main(String[] args) {
        LogisticsUserRouteEntity logisticsRouteEntity1 = new LogisticsUserRouteEntity();
        logisticsRouteEntity1.orderId = "1";
        logisticsRouteEntity1.eventTime = 3L;

        LogisticsUserRouteEntity logisticsRouteEntity2 = new LogisticsUserRouteEntity();
        logisticsRouteEntity2.orderId = "1";
        logisticsRouteEntity2.eventTime = 2L;

        LogisticsUserRouteEntity logisticsRouteEntity3 = new LogisticsUserRouteEntity();
        logisticsRouteEntity3.orderId = "1";
        logisticsRouteEntity3.eventTime = 2L;

        List<LogisticsUserRouteEntity> logisticsRouteEntities = new ArrayList<LogisticsUserRouteEntity>();
        logisticsRouteEntities.add(logisticsRouteEntity1);
        logisticsRouteEntities.add(logisticsRouteEntity2);
        logisticsRouteEntities.add(logisticsRouteEntity3);

        Set<LogisticsUserRouteEntity> set = new TreeSet<LogisticsUserRouteEntity>();
        set.addAll(logisticsRouteEntities);

        for (LogisticsUserRouteEntity logisticsRouteEntity : set) {
            System.out.println(logisticsRouteEntity);
        }
    }
}
