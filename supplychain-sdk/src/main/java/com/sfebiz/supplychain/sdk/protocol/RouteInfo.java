package com.sfebiz.supplychain.sdk.protocol;


import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/1/26
 * Time: 下午9:48
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "internalRoute", propOrder = {"mailNo", "routes"})
public class RouteInfo implements Serializable {

    private static final long serialVersionUID = 2324229064223873816L;

    @XmlElement(name = "mailNo")
    public String mailNo;

    @XmlElementWrapper(name = "routes")
    @XmlElement(name = "route", nillable = false, required = true)
    private List<Route> routes;

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
