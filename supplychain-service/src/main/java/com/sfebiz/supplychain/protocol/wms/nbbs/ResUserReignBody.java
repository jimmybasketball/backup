package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Result", "Remark"})
@Description("ResponseUserReignbody")
public class ResUserReignBody extends NbbsBody implements Serializable{
	private static final long serialVersionUID = -7720306781929157325L;
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


	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("Result", Result)
				.append("Remark", Remark)
				.toString();
	}
}
