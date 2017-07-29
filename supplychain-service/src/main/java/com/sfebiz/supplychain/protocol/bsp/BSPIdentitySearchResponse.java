package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "result" })
@XmlRootElement(name = "OrderSearchResponse")
@Description("证件查询返回")
public class BSPIdentitySearchResponse extends BSPBody implements Serializable {
	private static final long serialVersionUID = 5675507448878418896L;
	@XmlAttribute(name = "result")
	@Description("证件查询结果，Yes,No")
	public String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("result", result)
				.toString();
	}
}
