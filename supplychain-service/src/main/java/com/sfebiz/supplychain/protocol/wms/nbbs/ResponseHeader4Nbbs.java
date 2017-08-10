package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Result", "ResultMsg"})
@Description("ResponseDMHeader")
public class ResponseHeader4Nbbs implements Serializable{
	private static final long serialVersionUID = -3605379167609369585L;
	@Description("返回结果")
	@XmlElement(nillable=false,required=false)
	public String Result;
	
	@Description("三单返回信息")
	@XmlElement(nillable=false,required=false)
	public String ResultMsg;

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getResultMsg() {
		return ResultMsg;
	}

	public void setResultMsg(String resultMsg) {
		ResultMsg = resultMsg;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("Result", Result)
				.append("ResultMsg", ResultMsg)
				.toString();
	}
}
