package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddedService", propOrder = { "name", "value", "value1",
		"value2", "value3", "value4" })
@Description("BSP增值服务")
public class BSPAddedService  implements Serializable  {
	@XmlAttribute(name = "name")
	@Description("增值服务名")
	public String name;
	@XmlAttribute(name = "value")
	@Description("增值服务值 1")
	public String value;
	@XmlAttribute(name = "value1")
	@Description("增值服务值 2")
	public String value1;
	@XmlAttribute(name = "value2")
	@Description("增值服务值 3")
	public String value2;
	@XmlAttribute(name = "value3")
	@Description("增值服务值 4")
	public String value3;
	@XmlAttribute(name = "value4")
	@Description("增值服务值 5")
	public String value4;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}
}
