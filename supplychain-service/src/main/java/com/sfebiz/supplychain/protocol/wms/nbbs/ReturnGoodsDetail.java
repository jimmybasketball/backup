package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"ProductId","RejectedQty"})
//@XmlRootElement(name="Detail")
@Description("商品列表信息")
public class ReturnGoodsDetail implements Serializable{
	@Description("货号")
	@XmlElement(nillable = false, required = false)
	private String ProductId;
	
	@Description("计量")
	@XmlElement(nillable = false, required = false)
	private int RejectedQty;
	
	

	public String getProductId() {
		return ProductId;
	}

	public void setProductId(String productId) {
		ProductId = productId;
	}

	public int getRejectedQty() {
		return RejectedQty;
	}

	public void setRejectedQty(int rejectedQty) {
		RejectedQty = rejectedQty;
	}

	
}
