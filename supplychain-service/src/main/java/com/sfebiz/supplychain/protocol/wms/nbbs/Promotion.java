package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"ProAmount","ProRemark"})
//@XmlRootElement(name="Body")
@Description("订单优惠清单列表")
public class Promotion implements Serializable{
    private static final long serialVersionUID = 8574296270634137110L;
    @Description("优惠金额")
	@XmlElement(nillable = false, required = false)
	private Double ProAmount;
	
	@Description("优惠信息说明")
	@XmlElement(nillable = false, required = false)
	private String ProRemark;

	public Double getProAmount() {
		return ProAmount;
	}

	public void setProAmount(Double proAmount) {
		ProAmount = proAmount;
	}

	public String getProRemark() {
		return ProRemark;
	}

	public void setProRemark(String proRemark) {
		ProRemark = proRemark;
	}
	
	
}
