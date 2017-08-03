package com.sfebiz.supplychain.provider.command.send.tpl;

import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.provider.command.AbstractCommand;

import java.util.List;

public abstract class TplOrderGetRoutesCommand extends AbstractCommand {

    protected String mailNo;

    protected String orderId;

    protected String carrierCode;

    /**
     * 物流信息，执行后输出
     */
    private List<LogisticsUserRouteEntity> routes;


    public List<LogisticsUserRouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(List<LogisticsUserRouteEntity> routes) {
        this.routes = routes;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }
}

