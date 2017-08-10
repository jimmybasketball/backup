package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Goods",propOrder={"Detail"})
//@XmlRootElement(name="Goods")
@Description("商品信息")
public class GoodsInfo implements Serializable{
	
	@Description("商品列表信息")
	@XmlElement(nillable = false, required = false)
	private List<GoodsDetail> Detail;

	public List<GoodsDetail> getGoodsDetail() {
		return Detail;
	}

	public void setGoodsDetail(List<GoodsDetail> Detail) {
		this.Detail = Detail;
	}
	
	
}
