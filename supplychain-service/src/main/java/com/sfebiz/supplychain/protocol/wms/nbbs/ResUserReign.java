package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"Body"})
@XmlRootElement(name="Message")
@Description("响应结果信息")
public class ResUserReign implements Serializable{

	private static final long serialVersionUID = -5603201809148114701L;

	@Description("返回信息body")
	@XmlElement(nillable = false, required = false)
	public ResUserReignBody Body;

	public ResUserReignBody getBody() {
		return Body;
	}

	public void setBody(ResUserReignBody body) {
		Body = body;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("Body", Body)
				.toString();
	}
}
