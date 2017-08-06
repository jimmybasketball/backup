package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"Body"})
@XmlRootElement(name="Message")
@Description("用户注册")
public class RequestUserReign implements Serializable{
	private static final long serialVersionUID = -4761580695847244502L;
	@Description("注册body")
	@XmlElement(nillable = false, required = false)
	public UserReignBody Body;

	public UserReignBody getUserReign() {
		if (Body == null){
			Body = new UserReignBody();
		}
		return Body;
	}

	public void setUserReign(UserReignBody Body) {
		this.Body = Body;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("Body", Body)
				.toString();
	}
}
