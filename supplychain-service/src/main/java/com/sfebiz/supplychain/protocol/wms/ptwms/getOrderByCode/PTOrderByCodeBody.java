package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderByCode;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
@Description("PTOrderByCodeBody")
public class PTOrderByCodeBody implements Serializable {

	private static final long serialVersionUID = -5992002759610008059L;

	@XmlElement(name = "callService", namespace = "http://www.example.org/Ec/")
	public PTOrderByCodeService service;

	public PTOrderByCodeService getService() {
		return service;
	}

	public void setService(PTOrderByCodeService service) {
		this.service = service;
	}

}
