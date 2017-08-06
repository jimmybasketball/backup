package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
@XmlType(propOrder = { "body" })
@Description("dm请求body")
public class RequestBody4Nbbs implements Serializable{
	private static final long serialVersionUID = 3205369633144722848L;
	@XmlElements({
		@XmlElement(name = "Order", type = ImportOrderInfo.class),
		@XmlElement(name = "Pay", type = PayInfo.class),
		@XmlElement(name = "Logistics", type = LogisticsInfo.class),
		@XmlElement(name = "RejectedInfo", type = Rejected.class)})	
	@Description("内容信息")
	public NbbsBody body;

	public NbbsBody getBody() {
		return body;
	}

	public void setBody(NbbsBody body) {
		this.body = body;
	}
	
	
}
