package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "weight", "volume", "returnTracking","expressType","childrenNos" })
@Description("订单确认参数")
public class BSPOrderConfirmOption extends BSPBody  implements Serializable {
	private static final long serialVersionUID = -7407700311395254265L;
	@XmlAttribute(name = "weight")
	@Description("重量")
	public Double weight;
	@XmlAttribute(name = "volume")
	@Description("托寄物的长,宽,高,以半角逗号分隔,单位 CM,精确到小数点 后一位")
	public String volume;
	@XmlAttribute(name = "return_tracking")
	@Description("签回单单号")
	public String returnTracking;
	@XmlAttribute(name = "express_type")
	@Description("快件产品类别(可根据需要定制扩展) 1 标准快递 2 顺丰特惠 3 电商特惠 如果此字段为空,则以下单时的为准")
	public String expressType;
	@XmlAttribute(name = "children_nos")
	@Description("子单号(以半角逗号分隔) 如果此字段为空,则以下订单时为准。 如果此字段不为空,则忽略下订单时的子单号,以此字段的单号为准")
	public String childrenNos;

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getReturnTracking() {
		return returnTracking;
	}

	public void setReturnTracking(String returnTracking) {
		this.returnTracking = returnTracking;
	}

	public String getExpressType() {
		return expressType;
	}

	public void setExpressType(String expressType) {
		this.expressType = expressType;
	}

	public String getChildrenNos() {
		return childrenNos;
	}

	public void setChildrenNos(String childrenNos) {
		this.childrenNos = childrenNos;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("weight", weight)
				.append("volume", volume)
				.append("returnTracking", returnTracking)
				.append("expressType", expressType)
				.append("childrenNos", childrenNos)
				.toString();
	}
}
