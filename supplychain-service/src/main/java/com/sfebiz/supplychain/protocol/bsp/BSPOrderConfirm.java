package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderId", "mailNo", "dealtype","options" })
@XmlRootElement(name = "OrderConfirm")
@Description("订单确认")
public class BSPOrderConfirm extends BSPBody  implements Serializable {
	private static final long serialVersionUID = 5273444237751424762L;
	@XmlAttribute(name = "orderid")
	@Description("订单号")
	public String orderId;
	@XmlAttribute(name = "maillno")
	@Description("运单号(如果 dealtype=2,可选)")
	public String mailNo;
	@XmlAttribute(name = "dealtype")
	@Description("订单操作标识:1-订单确认 2-消单")
	public Integer dealtype;
	@XmlElements({
		@XmlElement(name = "OrderConfirmOption", type = BSPOrderConfirmOption.class) })
	@Description("订单确认参数")
	public List<BSPOrderConfirmOption> options;
	
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

	public Integer getDealtype() {
		return dealtype;
	}

	public void setDealtype(Integer dealtype) {
		this.dealtype = dealtype;
	}

	public List<BSPOrderConfirmOption> getOptions() {
		if (options == null){
			options = new ArrayList<BSPOrderConfirmOption>();
		}
		return options;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("orderId", orderId)
				.append("mailNo", mailNo)
				.append("dealtype", dealtype)
				.append("options", options)
				.toString();
	}
}
