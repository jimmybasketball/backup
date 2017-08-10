package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Detail",propOrder={"ProductId","GoodsName","Qty","Unit","Price","Amount"})
//@XmlRootElement(name="Detail")
@Description("商品列表信息")
public class GoodsDetail implements Serializable{
	@Description("货号")
	@XmlElement(nillable = false, required = false)
	private String ProductId;
	
	@Description("商品名称")
	@XmlElement(nillable = false, required = false)
	private String GoodsName;
	
	@Description("计量")
	@XmlElement(nillable = false, required = false)
	private int Qty;
	
	@Description("计量单位")
	@XmlElement(nillable = false, required = false)
	private String Unit;
	
	@Description("商品单价")
	@XmlElement(nillable = false, required = false)
	private double Price;
	
	@Description("商品总额")
	@XmlElement(nillable = false, required = false)
	private double Amount;

	public String getProductId() {
		return ProductId;
	}

	public void setProductId(String productId) {
		ProductId = productId;
	}

	public String getGoodsName() {
		return GoodsName;
	}

	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}

	public int getQty() {
		return Qty;
	}

	public void setQty(int qty) {
		Qty = qty;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double amount) {
		Amount = amount;
	}

	

}
