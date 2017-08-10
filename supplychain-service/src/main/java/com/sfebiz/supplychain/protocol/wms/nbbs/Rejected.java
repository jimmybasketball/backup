package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"OrderNo","WaybillNo","Flag","RejectedGoods"})
@XmlRootElement(name="RejectedInfo")
@Description("退换货信息")
public class Rejected extends NbbsBody implements Serializable{
	private static final long serialVersionUID = -1548065273528727993L;
	@Description("购物网站代码")
	@XmlElement(nillable = false, required = false)
	private String OrderNo;
	@Description("购物网站代码")
	@XmlElement(nillable = false, required = false)
	private String WaybillNo;
	@Description("购物网站代码")
	@XmlElement(nillable = false, required = false)
	private String Flag;
	@Description("购物网站代码")
	@XmlElement(nillable = false, required = false)
	private RejectedGoodsInfo RejectedGoods;
	public String getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}
	public String getWaybillNo() {
		return WaybillNo;
	}
	public void setWaybillNo(String waybillNo) {
		WaybillNo = waybillNo;
	}
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
	public RejectedGoodsInfo getRejectedGoods() {
		return RejectedGoods;
	}
	public void setRejectedGoods(RejectedGoodsInfo rejectedGoods) {
		RejectedGoods = rejectedGoods;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("OrderNo", OrderNo)
				.append("WaybillNo", WaybillNo)
				.append("Flag", Flag)
				.append("RejectedGoods", RejectedGoods)
				.toString();
	}
}
