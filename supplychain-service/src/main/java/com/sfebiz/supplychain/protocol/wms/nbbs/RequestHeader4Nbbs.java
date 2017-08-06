package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"CustomsCode","OrgName","CreateTime","OrderNo","WaybillNo","Flag"})
//@XmlRootElement(name="Header")
@Description("进口订单头信息")
public class RequestHeader4Nbbs implements Serializable{
	@Description("电商企业海关代码")
	@XmlElement(nillable = false, required = false)
	private String CustomsCode;
	
	@Description("电商企业名称")
	@XmlElement(nillable = false, required = false)
	private String OrgName;
	
	@Description("创建时间")
	@XmlElement(nillable = false, required = false)
	private String CreateTime;
	
	@Description("订单号")
	@XmlElement(nillable = false, required = false)
	private String OrderNo;

	@Description("运单号")
	@XmlElement(nillable = false, required = false)
	private String WaybillNo;
	
	@Description("退换货标志")
	@XmlElement(nillable = false, required = false)
	private String Flag;
	
	public String getCustomsCode() {
		return CustomsCode;
	}

	public void setCustomsCode(String customsCode) {
		CustomsCode = customsCode;
	}

	public String getOrgName() {
		return OrgName;
	}

	public void setOrgName(String orgName) {
		OrgName = orgName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public String getWaybillNo() {
		return WaybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		WaybillNo = waybillNo;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}
	
}
