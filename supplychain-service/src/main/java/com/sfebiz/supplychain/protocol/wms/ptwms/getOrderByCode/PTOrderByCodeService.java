package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderByCode;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "callService", namespace = "http://www.example.org/Ec/")
@Description("PTOrderByCodeService")
public class PTOrderByCodeService implements Serializable {

	private static final long serialVersionUID = -5199002754664118059L;

	@XmlElement(name = "paramsJson")
	private String paramsJson;

	@XmlElement(name = "appToken")
	private String appToken;

	@XmlElement(name = "appKey")
	private String appKey;

	@XmlElement(name = "service")
	private String service;

	public String getParamsJson() {
		return paramsJson;
	}

	public void setParamsJson(String paramsJson) {
		this.paramsJson = paramsJson;
	}

	public String getAppToken() {
		return appToken;
	}

	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
}
