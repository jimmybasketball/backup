package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderId", "filterResult", "origincode", "destcode",
		"remark" })
@XmlRootElement(name = "OrderConfirmResponse")
@Description("订单筛选")
public class BSPOrderFilterResponse extends BSPBody implements Serializable {
	private static final long serialVersionUID = -1096297863427437663L;
	@XmlAttribute(name = "orderid")
	@Description("订单UD")
	public String orderId;
	@XmlAttribute(name = "filter_result")
	@Description("筛单结果:1-人工确认,2-可收派 3-不可以收派")
	public Integer filterResult;
	@XmlAttribute(name = "origincode")
	@Description("原寄地代码")
	public String origincode;
	@XmlAttribute(name = "destcode")
	@Description("目的地代码,如果可收派,此项不能为 空")
	public String destcode;
	@XmlAttribute(name = "remark")
	@Description("1-收方超范围,2-派方超范围,3-其 他原因")
	public String remark;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getFilterResult() {
		return filterResult;
	}

	public void setFilterResult(Integer filterResult) {
		this.filterResult = filterResult;
	}

	public String getOrigincode() {
		return origincode;
	}

	public void setOrigincode(String origincode) {
		this.origincode = origincode;
	}

	public String getDestcode() {
		return destcode;
	}

	public void setDestcode(String destcode) {
		this.destcode = destcode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("orderId", orderId)
				.append("filterResult", filterResult)
				.append("origincode", origincode)
				.append("destcode", destcode)
				.append("remark", remark)
				.toString();
	}
}
