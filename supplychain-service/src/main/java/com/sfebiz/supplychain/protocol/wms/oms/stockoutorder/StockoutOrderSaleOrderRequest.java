package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * User:tu.jie@ifunq.com
 * Date: 15/1/21
 * Time: 下午9:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SaleOrderRequest")
@XmlType(propOrder = {"companyCode","stockoutOrderSaleOrders"})
public class StockoutOrderSaleOrderRequest implements Serializable {

    private static final long serialVersionUID = -5876304918512111993L;
    @Description("货主编码")
    @XmlElement(name = "CompanyCode", required = false)
    private String companyCode;

    @XmlElement(name = "SaleOrder", required = false)
    @XmlElementWrapper(name = "SaleOrders")
     public  List<StockoutOrderSaleOrder> stockoutOrderSaleOrders;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public List<StockoutOrderSaleOrder> getStockoutOrderSaleOrders() {
        return stockoutOrderSaleOrders;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public void setStockoutOrderSaleOrders(List<StockoutOrderSaleOrder> stockoutOrderSaleOrders) {
        this.stockoutOrderSaleOrders = stockoutOrderSaleOrders;
    }
}
