package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
@XmlType(propOrder = { "body" })
@Description("返回结构体")
public class BSPResponseBody  implements Serializable {
	@XmlElements({
			@XmlElement(name="RouteResponse",type=BSPRouteResponse.class),
			@XmlElement(name="OrderResponse",type=BSPOrderResponse.class),
			@XmlElement(name="OrderConfirmResponse",type=BSPOrderConfirmResponse.class),
			@XmlElement(name="OrderFilterResponse",type=BSPOrderFilterResponse.class),
			@XmlElement(name="OrderReverseResponse",type=BSPOrderReverseResponse.class),
            @XmlElement(name="WaybillRouteResponse",type=BSPWaybillRouteResponse.class),
			@XmlElement(name="OrderSearchResponse",type=BSPOrderSearchResponse.class)})
	@Description("返回结构提列表")
	public List<BSPBody> body;

	public List<BSPBody> getBody() {
		if (body == null){
			body = new ArrayList<BSPBody>();
		}
		return body;
	}

	public void setBody(List<BSPBody> body) {
		this.body = body;
	}
	
	
}
