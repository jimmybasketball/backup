package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "Body")
@XmlType(propOrder = { "Result", "Remark"})
@Description("ResponseUserReignbody")
public class DeclarationFormBody4Nbbs extends ResponseBody4Nbbs implements Serializable{
	@Description("返回结果")
	@XmlElement(nillable=false,required=false)
	public String Result;
	
	@Description("返回信息")
	@XmlElement(nillable=false,required=false)
	public String Remark;

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}
	
	
	
}
