package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "mailNo", "orderId", "route" })
@Description("路由返回结构体")
public class BSPRouteResponse extends BSPBody  implements Serializable {
	private static final long serialVersionUID = 3514562364829870791L;
	@XmlAttribute(name = "mailno")
	@Description("运单号")
	public String mailNo;
	@XmlAttribute(name = "orderid")
	@Description("订单ID")
	public String orderId;
	@XmlElements(@XmlElement(name = "Route", type = BSPRoute.class))
	@Description("路由列表")
	private List<BSPRoute> route;

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<BSPRoute> getRoute() {
		if (route == null){
			route = new ArrayList<BSPRoute>();
		}
		return route;
	}

	public void setRoute(List<BSPRoute> route) {
		this.route = route;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("mailNo", mailNo)
				.append("orderId", orderId)
				.append("route", route)
				.toString();
	}
}
