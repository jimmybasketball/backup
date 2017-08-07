package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/21
 * Time: 下午9:11
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SaleOrderResponse")
@XmlType(propOrder = {"saleOrderList"})
public class SaleOrderResponse implements Serializable {

    private static final long serialVersionUID = 7235260336241428650L;
    
    @Description("销售订单返回集合")
    @XmlElement(name = "SaleOrder", required = true)
    @XmlElementWrapper(name = "SaleOrders")
    private List<SaleOrder> saleOrderList;

    public List<SaleOrder> getSaleOrderList() {
        return saleOrderList;
    }

    public void setSaleOrderList(List<SaleOrder> saleOrderList) {
        this.saleOrderList = saleOrderList;
    }
}
