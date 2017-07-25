package com.sfebiz.supplychain.sdk.protocol;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "routeDetail", propOrder = {"orderId", "internationalRoute", "internalRoute"})
public class RouteDetail implements Serializable {

    private static final long serialVersionUID = 593392295711614904L;

    @XmlElement(name = "orderId")
    public String orderId;

    @XmlElement(name = "internationalRoute")
    public RouteInfo internationalRoute;

    @XmlElement(name = "internalRoute")
    public RouteInfo internalRoute;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public RouteInfo getInternationalRoute() {
        return internationalRoute;
    }

    public void setInternationalRoute(RouteInfo internationalRoute) {
        this.internationalRoute = internationalRoute;
    }

    public RouteInfo getInternalRoute() {
        return internalRoute;
    }

    public void setInternalRoute(RouteInfo internalRoute) {
        this.internalRoute = internalRoute;
    }
}
