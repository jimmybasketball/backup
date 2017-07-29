package com.sfebiz.supplychain.protocol.ceb.order.ptinventory;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "guid", "version", "xmlns", "xmlnsXsi", "inventory", "baseTransfer" })
@XmlRootElement(name = "ceb:CEB621Message")
public class CEB621Message implements Serializable {

	private static final long serialVersionUID = -8809198711148868112L;

	@XmlAttribute(name = "guid")
	private String guid;

	@XmlAttribute(name = "version")
	private String version = "1.0";

	@XmlAttribute(name = "xmlns:ceb")
	private String xmlns = "http://www.chinaport.gov.cn/ceb";

	@XmlAttribute(name = "xmlns:xsi")
	private String xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance";

	@XmlElements({ @XmlElement(name = "ceb:Inventory", type = Inventory.class) })
	public List<Inventory> inventory;

	@XmlElement(name = "ceb:BaseTransfer")
	public BaseTransfer baseTransfer;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getXmlnsXsi() {
		return xmlnsXsi;
	}

	public void setXmlnsXsi(String xmlnsXsi) {
		this.xmlnsXsi = xmlnsXsi;
	}

	public List<Inventory> getInventory() {
		return inventory;
	}

	public void setInventory(List<Inventory> inventory) {
		this.inventory = inventory;
	}

	public BaseTransfer getBaseTransfer() {
		return baseTransfer;
	}

	public void setBaseTransfer(BaseTransfer baseTransfer) {
		this.baseTransfer = baseTransfer;
	}
}
