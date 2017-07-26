package com.sfebiz.supplychain.protocol.ceb.order.ptinventory;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "gnum", "itemRecordNo", "itemNo", "itemName", "gcode", "gname", "gmodel", "barCode", "country",
		"currency", "qty", "unit", "qty1", "unit1", "qty2", "unit2", "price", "totalPrice", "note" })
@XmlRootElement(name = "ceb:InventoryList")
public class InventoryList implements Serializable {

	private static final long serialVersionUID = -8830081110448868112L;

	@XmlElement(name = "ceb:gnum")
	public String gnum;

	@XmlElement(name = "ceb:itemRecordNo")
	public String itemRecordNo;

	@XmlElement(name = "ceb:itemNo")
	public String itemNo;

	@XmlElement(name = "ceb:itemName")
	public String itemName;

	@XmlElement(name = "ceb:gcode")
	public String gcode;

	@XmlElement(name = "ceb:gname")
	public String gname;

	@XmlElement(name = "ceb:gmodel")
	public String gmodel;

	@XmlElement(name = "ceb:barCode")
	public String barCode;

	@XmlElement(name = "ceb:country")
	public String country;

	@XmlElement(name = "ceb:currency")
	public String currency;

	@XmlElement(name = "ceb:qty")
	public String qty;

	@XmlElement(name = "ceb:unit")
	public String unit;

	@XmlElement(name = "ceb:qty1")
	public String qty1;

	@XmlElement(name = "ceb:unit1")
	public String unit1;

	@XmlElement(name = "ceb:qty2")
	public String qty2;

	@XmlElement(name = "ceb:unit2")
	public String unit2;

	@XmlElement(name = "ceb:price")
	public String price;

	@XmlElement(name = "ceb:totalPrice")
	public String totalPrice;

	@XmlElement(name = "ceb:note")
	public String note;

	public String getGnum() {
		return gnum;
	}

	public void setGnum(String gnum) {
		this.gnum = gnum;
	}

	public String getItemRecordNo() {
		return itemRecordNo;
	}

	public void setItemRecordNo(String itemRecordNo) {
		this.itemRecordNo = itemRecordNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getGcode() {
		return gcode;
	}

	public void setGcode(String gcode) {
		this.gcode = gcode;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getGmodel() {
		return gmodel;
	}

	public void setGmodel(String gmodel) {
		this.gmodel = gmodel;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getQty1() {
		return qty1;
	}

	public void setQty1(String qty1) {
		this.qty1 = qty1;
	}

	public String getUnit1() {
		return unit1;
	}

	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}

	public String getQty2() {
		return qty2;
	}

	public void setQty2(String qty2) {
		this.qty2 = qty2;
	}

	public String getUnit2() {
		return unit2;
	}

	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
