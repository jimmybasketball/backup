package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"Status","Result","CreateTime"})
@Description("申报单列表信息")
public class MftInfo implements Serializable{
	
	@Description("订单状态")
	@XmlElement(nillable = false, required = false)
	private String Status;		
	@Description("描述")
	@XmlElement(nillable = false, required = false)
	private String Result;			
	@Description("操作时间")
	@XmlElement(nillable = false, required = false)
	private Date CreateTime;
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public Date getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}
	
	
	
}
