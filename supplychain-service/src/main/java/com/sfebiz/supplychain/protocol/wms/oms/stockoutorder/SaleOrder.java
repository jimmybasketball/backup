package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/21
 * Time: 下午9:11
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SaleOrder")
@XmlType(propOrder = {"result","erpOrder","shipmentId","note"})
public class SaleOrder implements Serializable {


    private static final long serialVersionUID = 4633317821691911094L;
    @Description("返回结果码")
    @XmlElement(name = "Result", required = true)
    private String result;

    @Description("子单号")
    @XmlElement(name = "ErpOrder", required = true)
    private String erpOrder;

    @Description("仓库订单号")
    @XmlElement(name = "ShipmentId", required = true)
    private String shipmentId;

    @Description("返回信息")
    @XmlElement(name = "Note", required = true)
    private String note;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getResult() {
        return result;
    }

    public String getErpOrder() {
        return erpOrder;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setErpOrder(String erpOrder) {
        this.erpOrder = erpOrder;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
