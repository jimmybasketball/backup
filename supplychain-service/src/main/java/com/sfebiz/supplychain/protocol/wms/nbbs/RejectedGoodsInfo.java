package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"Detail"})
@Description("商品信息")
public class RejectedGoodsInfo implements Serializable{
	@Description("商品列表信息")
	@XmlElement(nillable = false, required = false)
	private List<ReturnGoodsDetail> Detail;

	public List<ReturnGoodsDetail> getDetail() {
		return Detail;
	}

	public void setDetail(List<ReturnGoodsDetail> detail) {
		Detail = detail;
	}
	
}
