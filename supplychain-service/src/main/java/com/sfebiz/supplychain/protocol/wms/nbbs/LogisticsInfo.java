package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"LogisticsNo","OrderNo","LogisticsName","Consignee","Province","City","District",
		"ConsigneeAddr","ConsigneeTel","GoodsName"})
@XmlRootElement(name="Logistics")
@Description("进口运单信息")
public class LogisticsInfo extends NbbsBody implements Serializable{
	private static final long serialVersionUID = -3317046022390540602L;
	@Description("运单号")
	@XmlElement(nillable = false, required = false)
	private String LogisticsNo;
	
	@Description("订单号")
	@XmlElement(nillable = false, required = false)
	private String OrderNo;
	
	@Description("物流企业名称")
	@XmlElement(nillable = false, required = false)
	private String LogisticsName;
	
	@Description("收货人名称")
	@XmlElement(nillable = false, required = false)
	private String Consignee;
	
	@Description("省")
	@XmlElement(nillable = false, required = false)
	private String Province;
	
	@Description("市")
	@XmlElement(nillable = false, required = false)
	private String City;
	
	@Description("区")
	@XmlElement(nillable = false, required = false)
	private String District;
	
	@Description("收货地址")
	@XmlElement(nillable = false, required = false)
	private String ConsigneeAddr;
	
	@Description("收货电话")
	@XmlElement(nillable = false, required = false)
	private String ConsigneeTel;
	
	@Description("货物名称")
	@XmlElement(nillable = false, required = false)
	private String GoodsName;

	public String getLogisticsNo() {
		return LogisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		LogisticsNo = logisticsNo;
	}

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public String getLogisticsName() {
		return LogisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		LogisticsName = logisticsName;
	}

	public String getConsignee() {
		return Consignee;
	}

	public void setConsignee(String consignee) {
		Consignee = consignee;
	}

	public String getProvince() {
		return Province;
	}

	public void setProvince(String province) {
		Province = province;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getDistrict() {
		return District;
	}

	public void setDistrict(String district) {
		District = district;
	}

	public String getConsigneeAddr() {
		return ConsigneeAddr;
	}

	public void setConsigneeAddr(String consigneeAddr) {
		ConsigneeAddr = consigneeAddr;
	}

	public String getConsigneeTel() {
		return ConsigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		ConsigneeTel = consigneeTel;
	}

	public String getGoodsName() {
		return GoodsName;
	}

	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("LogisticsNo", LogisticsNo)
				.append("OrderNo", OrderNo)
				.append("LogisticsName", LogisticsName)
				.append("Consignee", Consignee)
				.append("Province", Province)
				.append("City", City)
				.append("District", District)
				.append("ConsigneeAddr", ConsigneeAddr)
				.append("ConsigneeTel", ConsigneeTel)
				.append("GoodsName", GoodsName)
				.toString();
	}
}
