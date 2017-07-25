package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
@XmlType(propOrder = { "body" })
@Description("BSP请求体")
public class BSPRequestBody  implements Serializable {
	private static final long serialVersionUID = 3244122794436772888L;
	@XmlElements({
			@XmlElement(name = "Order", type = BSPOrder.class),
			@XmlElement(name = "OrderConfirm", type = BSPOrderConfirm.class),
			@XmlElement(name = "OrderConfirmOption", type = BSPOrderConfirmOption.class),
			@XmlElement(name = "OrderSearch", type = BSPOrderSearch.class),
			@XmlElement(name = "OrderFilter", type = BSPOrderFilter.class),
			@XmlElement(name = "RouteRequest", type = BSPRouteRequest.class),
            @XmlElement(name = "WaybillRoute", type = BSPWaybillRouteRequest.class),
			@XmlElement(name = "IdentitySearch", type = BSPIdentitySearch.class)})
	@Description("内容列表")
	public List<BSPBody> body;

	public List<BSPBody> getBody() {
		if (body == null) {
			body = new ArrayList<BSPBody>();
		}
		return body;
	}

	public void setBody(List<BSPBody> body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("body", body)
				.toString();
	}
}
