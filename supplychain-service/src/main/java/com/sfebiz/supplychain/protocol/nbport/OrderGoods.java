package com.sfebiz.supplychain.protocol.nbport;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ztc on 2016/12/28.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"orderGoodDetailList"})
@XmlRootElement(name = "goods")
public class OrderGoods implements Serializable {

    @XmlElement(name = "detail")
    private List<OrderGoodDetail> orderGoodDetailList;

    public List<OrderGoodDetail> getOrderGoodDetailList() {
        return orderGoodDetailList;
    }

    public void setOrderGoodDetailList(List<OrderGoodDetail> orderGoodDetailList) {
        this.orderGoodDetailList = orderGoodDetailList;
    }
}
