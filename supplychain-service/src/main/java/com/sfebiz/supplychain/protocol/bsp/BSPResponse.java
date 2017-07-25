package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Response")
@XmlType(propOrder = { "service", "header", "body", "error" })
@Description("BSP返回结构体")
public class BSPResponse  implements Serializable {
	@XmlAttribute
	@Description("服务号")
	public String service;
	@XmlElement(name = "Head")
	@Description("头")
	public String header;
	@XmlElement(name = "Body")
	@Description("体")
	public BSPResponseBody body;
	@XmlElement(name = "ERROR")
	@Description("错误")
	public BSPError error;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}


	public BSPError getError() {
		return error;
	}

	public void setError(BSPError error) {
		this.error = error;
	}

	public BSPResponseBody getBody() {
		if (body == null){
			body = new BSPResponseBody();
		}
		return body;
	}

	public void setBody(BSPResponseBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("service", service)
				.append("header", header)
				.append("body", body)
				.append("error", error)
				.toString();
	}
}
