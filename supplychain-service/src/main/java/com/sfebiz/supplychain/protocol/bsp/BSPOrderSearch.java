package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderId" })
@XmlRootElement(name = "OrderSearch")
@Description("订单查询")
public class BSPOrderSearch extends BSPBody  implements Serializable {
	private static final long serialVersionUID = 8249964825689722899L;
	@XmlAttribute(name = "orderid")
	@Description("订单ID")
	public String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("orderId", orderId)
				.toString();
	}
}
