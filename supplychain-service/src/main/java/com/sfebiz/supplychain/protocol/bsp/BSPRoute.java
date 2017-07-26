package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "acceptTime", "acceptAddress", "remark", "opcode" })
@Description("BSP路由结构体")
public class BSPRoute  implements Serializable {
	private static final long serialVersionUID = 2501382811415318086L;
	@XmlAttribute(name = "accept_time")
	@Description("时间")
	public String acceptTime;
	@XmlAttribute(name = "accept_address")
	@Description("接收地址")
	public String acceptAddress;
	@XmlAttribute(name = "remark")
	@Description("备注")
	public String remark;
	@XmlAttribute(name = "opcode")
	@Description("操作码")
	public String opcode;

	public String getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getAcceptAddress() {
		return acceptAddress;
	}

	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("acceptTime", acceptTime)
				.append("acceptAddress", acceptAddress)
				.append("remark", remark)
				.append("opcode", opcode)
				.toString();
	}
}
