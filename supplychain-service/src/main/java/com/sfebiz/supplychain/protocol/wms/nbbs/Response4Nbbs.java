package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"Header","Body"})
@XmlRootElement(name="Message")
@Description("响应结果信息")
public class Response4Nbbs implements Serializable{

	private static final long serialVersionUID = -6191127286375722395L;
	@Description("返回信息header")
	@XmlElement(nillable = false, required = false)
	private ResponseHeader4Nbbs Header;
	
	@Description("返回信息body")
	@XmlElement(nillable = false, required = false)
	public ResponseBody4Nbbs Body;

	public ResponseBody4Nbbs getUserReign() {
		return Body;
	}

	public void setUserReign(ResponseBody4Nbbs Body) {
		this.Body = Body;
	}

	public ResponseHeader4Nbbs getHeader() {
		return Header;
	}

	public void setHeader(ResponseHeader4Nbbs header) {
		Header = header;
	}

	public ResponseBody4Nbbs getBody() {
		return Body;
	}

	public void setBody(ResponseBody4Nbbs body) {
		Body = body;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("Header", Header)
				.append("Body", Body)
				.toString();
	}
}
