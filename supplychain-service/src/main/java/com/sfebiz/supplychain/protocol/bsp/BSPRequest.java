package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Request")
@XmlType(propOrder = { "service", "lang", "header", "body" })
@Description("BSP请求")
public class BSPRequest  implements Serializable   {
	private static final long serialVersionUID = 2372629706068924096L;
	@XmlAttribute
	@Description("服务名")
	public String service;
	@XmlAttribute
	@Description("语言")
	public String lang;
	@XmlElement(name = "Head")
	@Description("头")
	public String header;
	@XmlElement(name = "Body")
	@Description("体")
	public BSPRequestBody body;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public BSPRequestBody getBody() {
		if (body == null){
			body = new BSPRequestBody();
		}
		return body;
	}

	public void setBody(BSPRequestBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("service", service)
				.append("lang", lang)
				.append("header", header)
				.append("body", body)
				.toString();
	}
}
