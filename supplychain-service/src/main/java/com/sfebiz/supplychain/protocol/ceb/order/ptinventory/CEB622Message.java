package com.sfebiz.supplychain.protocol.ceb.order.ptinventory;


import com.sfebiz.supplychain.util.XMLUtil;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "guid", "version", "xmlns", "xmlnsXsi", "inventoryReturn" })
@XmlRootElement(name = "CEB622Message")
public class CEB622Message implements Serializable {


	private static final long serialVersionUID = -8809109811148868112L;

	@XmlAttribute(name = "guid")
	private String guid;

	@XmlAttribute(name = "version")
	private String version = "1.0";

	@XmlAttribute(name = "xmlns:ceb")
	private String xmlns = "http://www.chinaport.gov.cn/ceb";

	@XmlAttribute(name = "xmlns:xsi")
	private String xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance";

	@XmlElement(name = "InventoryReturn")
	public InventoryReturn inventoryReturn;

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

	public InventoryReturn getOrderReturn() {
		return inventoryReturn;
	}

	public void setOrderReturn(InventoryReturn inventoryReturn) {
		this.inventoryReturn = inventoryReturn;
	}
	
	public static void main(String[] args) {
		InventoryReturn data = new InventoryReturn("1", "1","1","1","1","1","1","1","1","1","1");
		CEB622Message re = new CEB622Message();
		re.setGuid("11");
		re.setOrderReturn(data);
		try {
			System.out.println(XMLUtil.convertToXml(re));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
