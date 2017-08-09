package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OrderSearchResponse")
@Description("订单返回")
public class BSPOrderSearchResponse extends BSPBody  implements Serializable {
	private static final long serialVersionUID = 4038364809475198166L;
	@XmlAttribute(name="orderid")
	@Description("订单ID")
	public String orderId;
	@XmlAttribute(name="mailno")
	@Description("运单号,可多个单号,如子母件,以逗号分隔")
	public String mailNo;
	@XmlAttribute(name="origincode")
	@Description("原寄地代码")
	public String origincode;
	@XmlAttribute(name="destcode")
	@Description("目的地代码")
	public String destcode;
	@XmlAttribute(name="filter_result")
	@Description("筛单结果:1-人工确认,2-可收派 3-不可以收派")
	public String filterResult;
	@XmlAttribute(name="remark")
	@Description("1-收方超范围,2-派方超范围,3-其他原因")
	public String remark;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
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
	public String getFilterResult() {
		return filterResult;
	}
	public void setFilterResult(String filterResult) {
		this.filterResult = filterResult;
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
				.append("mailNo", mailNo)
				.append("origincode", origincode)
				.append("destcode", destcode)
				.append("filterResult", filterResult)
				.append("remark", remark)
				.toString();
	}
}