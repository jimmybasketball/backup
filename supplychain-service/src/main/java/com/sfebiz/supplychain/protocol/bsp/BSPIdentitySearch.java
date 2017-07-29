package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "name", "phone" })
@XmlRootElement(name = "IdentitySearch")
@Description("证件查询")
public class BSPIdentitySearch extends BSPBody  implements Serializable {
	private static final long serialVersionUID = -376081735973055025L;
	@XmlAttribute(name = "name")
	@Description("姓名")
	public String name;
	@XmlAttribute(name = "phone")
	@Description("电话号码")
	public String phone;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", name)
				.append("phone", phone)
				.toString();
	}
}
