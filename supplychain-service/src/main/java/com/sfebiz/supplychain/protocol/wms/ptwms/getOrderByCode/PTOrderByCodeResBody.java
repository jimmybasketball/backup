package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderByCode;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
@Description("PTOrderByCodeResBody")
public class PTOrderByCodeResBody implements Serializable {

	private static final long serialVersionUID = -5911011759664811159L;

	@XmlElement(name = "callServiceResponse", namespace = "http://www.example.org/Ec/")
	public PTOrderByCodeResService service;

	public PTOrderByCodeResService getService() {
		return service;
	}

	public void setService(PTOrderByCodeResService service) {
		this.service = service;
	}

}
