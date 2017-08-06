package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Header", "Body" })
@XmlRootElement(name = "Message")
@Description("进口订单信息")
public class Request4Nbbs implements Serializable{

	private static final long serialVersionUID = -1317766407944252633L;

	@Description("body信息")
	@XmlElement(nillable = false, required = false)
	// private RequestImportOrderBody body;
	private RequestBody4Nbbs Body;

	@Description("头信息")
	@XmlElement(nillable = false, required = false)
	private RequestHeader4Nbbs Header;

	public RequestBody4Nbbs getBody() {
		return Body;
	}

	public void setBody(RequestBody4Nbbs Body) {
		this.Body = Body;
	}

	// public RequestImportOrderBody getBody() {
	// return body;
	// }
	// public void setBody(RequestImportOrderBody body) {
	// this.body = body;
	// }
	public RequestHeader4Nbbs getHeader() {
		return Header;
	}

	public void setHeader(RequestHeader4Nbbs Header) {
		this.Header = Header;
	}

}
