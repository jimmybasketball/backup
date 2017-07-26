package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by wang_cl on 2015/1/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "idError"})
@XmlRootElement(name = "WaybillRouteResponse")
@Description("路由返回结构体")
public class BSPWaybillRouteResponse extends BSPBody  implements Serializable {
    private static final long serialVersionUID = 4753875354470045103L;
    @XmlAttribute(name = "id")
    @Description("成功接收的路由编号，如果有多个路由编号，以逗号分隔，如“123,124,125“")
    public String id;
    @XmlAttribute(name = "id_error")
    @Description("未成功接收的路由编号，如果有多个路由编号，以逗号分隔，如“ 123,124,125 “ ，这部分路由编号，顺丰会定时重发")
    public String idError;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdError() {
        return idError;
    }

    public void setIdError(String idError) {
        this.idError = idError;
    }
}
