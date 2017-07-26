package com.sfebiz.supplychain.protocol.ceb.order.ptinventory;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "copCode", "copName", "dxpMode", "dxpId", "note" })
@XmlRootElement(name = "BaseTransfer")
public class BaseTransfer implements Serializable {

	private static final long serialVersionUID = -8832111110448868112L;

	@XmlElement(name = "ceb:copCode")
	public String copCode;

	@XmlElement(name = "ceb:copName")
	public String copName;

	@XmlElement(name = "ceb:dxpMode")
	public String dxpMode;

	@XmlElement(name = "ceb:dxpId")
	public String dxpId;

	@XmlElement(name = "ceb:note")
	public String note;

	public String getCopCode() {
		return copCode;
	}

	public void setCopCode(String copCode) {
		this.copCode = copCode;
	}

	public String getCopName() {
		return copName;
	}

	public void setCopName(String copName) {
		this.copName = copName;
	}

	public String getDxpMode() {
		return dxpMode;
	}

	public void setDxpMode(String dxpMode) {
		this.dxpMode = dxpMode;
	}

	public String getDxpId() {
		return dxpId;
	}

	public void setDxpId(String dxpId) {
		this.dxpId = dxpId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
