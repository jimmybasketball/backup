package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"promotion"})
//@XmlRootElement(name="Promotions")
@Description("订单优惠清单信息")
public class PromotionsInfo implements Serializable{
	@Description("优惠清单列表")
	@XmlElement(nillable = false, required = false)
	private List<Promotion> promotion;

	public List<Promotion> getPromotion() {
		return promotion;
	}

	public void setPromotion(List<Promotion> promotion) {
		this.promotion = promotion;
	}
	
	
	
	
}
