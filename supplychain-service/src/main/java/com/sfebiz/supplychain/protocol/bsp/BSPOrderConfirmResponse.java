package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderId", "mailNo", "reStatus" })
@XmlRootElement(name = "OrderConfirmResponse")
@Description("订单确认返回")
public class BSPOrderConfirmResponse extends BSPBody implements Serializable {
	private static final long serialVersionUID = 6795766807189763116L;
	@XmlAttribute(name = "orderid")
	@Description("订单ID")
	public String orderId;
	@XmlAttribute(name = "maillno")
	@Description("运单号")
	public String mailNo;
	@XmlAttribute(name = "res_status")
	@Description("备注 1 订单号与运单不匹配 2 成功")
	public Integer reStatus;

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

	public Integer getReStatus() {
		return reStatus;
	}

	public void setReStatus(Integer reStatus) {
		this.reStatus = reStatus;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("orderId", orderId)
				.append("mailNo", mailNo)
				.append("reStatus", reStatus)
				.toString();
	}
}
