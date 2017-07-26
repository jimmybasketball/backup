package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "senderTel", "senderCountry", "senderProvince",
		"senderCity", "senderCounty", "receiverCountry", "receiverProvince",
		"receiverCity", "receiverCounty", "senderAddress", "receiverTel",
		"custId" })
@XmlRootElement(name = "OrderFilterOption")
@Description("订单筛选设置")
public class BSPOrderFilterOption  implements Serializable {
	@XmlAttribute(name = "j_tel")
	@Description("寄方电话")
	public String senderTel;
	@XmlAttribute(name = "country")
	@Description("寄方国家啊")
	public String senderCountry;
	@XmlAttribute(name = "province")
	@Description("寄方省")
	public String senderProvince;
	@XmlAttribute(name = "city")
	@Description("寄方市")
	public String senderCity;
	@XmlAttribute(name = "county")
	@Description("寄方地区")
	public String senderCounty;
	@XmlAttribute(name = "d_country")
	@Description("到方国家")
	public String receiverCountry;
	@XmlAttribute(name = "d_province")
	@Description("到方省")
	public String receiverProvince;
	@XmlAttribute(name = "d_city")
	@Description("到方城市")
	public String receiverCity;
	@XmlAttribute(name = "d_county")
	@Description("到方地区")
	public String receiverCounty;
	@XmlAttribute(name = "j_address")
	@Description("到方地址")
	public String senderAddress;
	@XmlAttribute(name = "d_tel")
	@Description("到方电话")
	public String receiverTel;
	@XmlAttribute(name = "d_custid")
	@Description("寄方月结账号,用于在人工筛单时,筛单人员识别客户使用。")
	public String custId;

	public String getSenderTel() {
		return senderTel;
	}

	public void setSenderTel(String senderTel) {
		this.senderTel = senderTel;
	}

	public String getSenderCountry() {
		return senderCountry;
	}

	public void setSenderCountry(String senderCountry) {
		this.senderCountry = senderCountry;
	}

	public String getSenderProvince() {
		return senderProvince;
	}

	public void setSenderProvince(String senderProvince) {
		this.senderProvince = senderProvince;
	}

	public String getSenderCity() {
		return senderCity;
	}

	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}

	public String getSenderCounty() {
		return senderCounty;
	}

	public void setSenderCounty(String senderCounty) {
		this.senderCounty = senderCounty;
	}

	public String getReceiverCountry() {
		return receiverCountry;
	}

	public void setReceiverCountry(String receiverCountry) {
		this.receiverCountry = receiverCountry;
	}

	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverCounty() {
		return receiverCounty;
	}

	public void setReceiverCounty(String receiverCounty) {
		this.receiverCounty = receiverCounty;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

}
