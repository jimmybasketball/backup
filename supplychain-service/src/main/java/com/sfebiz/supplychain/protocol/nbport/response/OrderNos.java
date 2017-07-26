package com.sfebiz.supplychain.protocol.nbport.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ztc on 2016/12/30.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"orderNoList"})
@XmlRootElement(name = "orderNos")
public class OrderNos implements Serializable{

    @XmlElement(name = "no")
    private List<OrderNo> orderNoList;

    public List<OrderNo> getOrderNoList() {
        return orderNoList;
    }

    public void setOrderNoList(List<OrderNo> orderNoList) {
        this.orderNoList = orderNoList;
    }
}
