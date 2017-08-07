package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/21
 * Time: 下午9:11
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
@XmlType(propOrder = {"saleOrderResponse"})
public class ResponseBody implements Serializable {

    private static final long serialVersionUID = 6701666734189413449L;
    
    @XmlElement(name = "SaleOrderResponse", required = true)
    public SaleOrderResponse saleOrderResponse;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public SaleOrderResponse getSaleOrderResponse() {
        return saleOrderResponse;
    }

    public void setSaleOrderResponse(SaleOrderResponse saleOrderResponse) {
        this.saleOrderResponse = saleOrderResponse;
    }
}
