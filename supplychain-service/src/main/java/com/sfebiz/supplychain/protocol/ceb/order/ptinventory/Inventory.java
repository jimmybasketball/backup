package com.sfebiz.supplychain.protocol.ceb.order.ptinventory;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "inventoryHead", "inventoryList" })
@XmlRootElement(name = "ceb:Inventory")
public class Inventory implements Serializable {

	private static final long serialVersionUID = -8832098720448868112L;

	@XmlElement(name = "ceb:InventoryHead")
	public InventoryHead inventoryHead;
	
	@XmlElements({ @XmlElement(name = "ceb:InventoryList", type = InventoryList.class) })
	public List<InventoryList> inventoryList;

	public List<InventoryList> getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List<InventoryList> inventoryList) {
		this.inventoryList = inventoryList;
	}

	public InventoryHead getInventoryHead() {
		return inventoryHead;
	}

	public void setInventoryHead(InventoryHead inventoryHead) {
		this.inventoryHead = inventoryHead;
	}

}
