package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "filterType", "orderId", "receiverAddress" })
@XmlRootElement(name = "OrderFilter")
@Description("订单筛选")
public class BSPOrderFilter extends BSPBody  implements Serializable {
	private static final long serialVersionUID = -3745872380062639414L;
	@XmlAttribute(name = "orderid")
	@Description("订单UD")
	public String orderId;
	@XmlAttribute(name = "filter_type")
	@Description("筛单类别:1-自动筛单(系统根据地 址库进行判断,并返回结果),2-可人 工筛单(系统首先根据地址库判断, 如果无法自动判断是否收派,系统将 生成需要人工判断的任务,后续由人 工处理,处理结束后,顺丰可主动推 送给客户系统)")
	public Integer filterType;
	@XmlAttribute(name = "d_address")
	@Description("到件方详细地址")
	public String receiverAddress;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getFilterType() {
		return filterType;
	}

	public void setFilterType(Integer filterType) {
		this.filterType = filterType;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("orderId", orderId)
				.append("filterType", filterType)
				.append("receiverAddress", receiverAddress)
				.toString();
	}
}
