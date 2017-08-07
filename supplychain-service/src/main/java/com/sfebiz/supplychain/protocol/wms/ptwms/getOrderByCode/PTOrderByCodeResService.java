package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderByCode;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "callServiceResponse")
@Description("PTOrderByCodeResService")
public class PTOrderByCodeResService implements Serializable {

	private static final long serialVersionUID = -5992011114664818059L;

	@XmlElement(name = "response")
	private String response;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
