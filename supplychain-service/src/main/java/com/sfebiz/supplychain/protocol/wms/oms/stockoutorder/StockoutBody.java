package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User:tu.jie@ifunq.com
 * Date: 15/1/21
 * Time: 下午9:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
@XmlType(propOrder = {"stockoutOrderSaleOrderRequest"})
public class StockoutBody implements Serializable {

    private static final long serialVersionUID = -5731039197744392297L;

    @XmlElement(name = "SaleOrderRequest", required = false)
    private StockoutOrderSaleOrderRequest stockoutOrderSaleOrderRequest;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public StockoutOrderSaleOrderRequest getSaleOrderRequest() {
        return stockoutOrderSaleOrderRequest;
    }

    public void setSaleOrderRequest(StockoutOrderSaleOrderRequest saleOrderRequest) {
        this.stockoutOrderSaleOrderRequest = saleOrderRequest;
    }
}
