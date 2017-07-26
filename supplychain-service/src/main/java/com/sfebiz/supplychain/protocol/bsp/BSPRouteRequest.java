package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "trackingType", "trackingNumber", "methodType" })
@XmlRootElement(name = "RouteRequest")
@Description("路由请求结构体")
public class BSPRouteRequest extends BSPBody  implements Serializable {
	private static final long serialVersionUID = 3527129748690013884L;
	@XmlAttribute(name = "tracking_type")
	@Description("查询类别, tracking_type 字段 说明:1-根据运单号查询,order 节点中 track_number 将被当作 运单号处理,2-根据订单号查询, order 节点中 track_number 将 被当作订单号处理")
	public Integer trackingType;
	@XmlAttribute(name = "tracking_number")
	@Description("查询号, 如果 tracking_type=1,则此值为运 单号。如果 tracking_type=2, 则此值为订单号 如果有多个单号,以逗号分隔, 如”123,124,125”")
	public String trackingNumber;
	@XmlAttribute(name = "method_type")
	@Description("查询方法选择 1-标准查询 2-定制查询")
	public Integer methodType;

	public Integer getTrackingType() {
		return trackingType;
	}

	public void setTrackingType(Integer trackingType) {
		this.trackingType = trackingType;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public Integer getMethodType() {
		return methodType;
	}

	public void setMethodType(Integer methodType) {
		this.methodType = methodType;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("trackingType", trackingType)
				.append("trackingNumber", trackingNumber)
				.append("methodType", methodType)
				.toString();
	}
}
