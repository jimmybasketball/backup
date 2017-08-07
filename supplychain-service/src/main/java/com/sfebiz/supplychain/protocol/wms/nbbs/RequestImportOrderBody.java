package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"Order"})
//@XmlRootElement(name="Body")
@Description("进口订单body信息")
public class RequestImportOrderBody implements Serializable{
	@Description("订单信息")
	@XmlElement(nillable = false, required = false)
	private ImportOrderInfo Order;

	public ImportOrderInfo getOrder() {
		return Order;
	}

	public void setOrder(ImportOrderInfo order) {
		Order = order;
	}
	
}
