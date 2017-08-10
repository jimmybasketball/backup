package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"mftInfo"})
@Description("申报单信息")
public class MftInfos implements Serializable{
	
	@Description("申报单列表信息")
	@XmlElement(nillable = false, required = false)
	private List<MftInfo> mftInfo;

	public List<MftInfo> getMftInfo() {
		return mftInfo;
	}

	public void setMftInfo(List<MftInfo> mftInfo) {
		this.mftInfo = mftInfo;
	}

	
}
