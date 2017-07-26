package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderId", "mailNo", "remark", "filterResult" })
@XmlRootElement(name = "OrderConfirmResponse")
@Description("退货返回")
public class BSPOrderReverseResponse extends BSPBody  implements Serializable {
	private static final long serialVersionUID = 246530548323803624L;
	@XmlAttribute(name = "orderid")
	@Description("订单ID")
	public String orderId;
	@XmlAttribute(name = "maillno")
	@Description("运单ID")
	public String mailNo;
	@XmlAttribute(name = "remark")
	@Description("备注")
	public String remark;
	@XmlAttribute(name = "filter_result")
	@Description("筛单结果:1-人工确认,2-可收派 3-不可以收派")
	public Integer filterResult;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getFilterResult() {
		return filterResult;
	}

	public void setFilterResult(Integer filterResult) {
		this.filterResult = filterResult;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("orderId", orderId)
				.append("mailNo", mailNo)
				.append("remark", remark)
				.append("filterResult", filterResult)
				.toString();
	}
}
