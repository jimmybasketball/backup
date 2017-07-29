package com.sfebiz.supplychain.sdk.protocol;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 附件详情类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "attachment1","attachment2"})
public class Attachments implements Serializable {

	private static final long serialVersionUID = 513319178475363428L;

	/**
	 * 附件1的url
	 */
	@XmlElement(nillable=false,required=true)
	public String attachment1;
	
	/**
	 * 附件2的url
	 */
	@XmlElement(nillable=false,required=true)
	public String attachment2;

	public String getAttachment1() {
		return attachment1;
	}

	public void setAttachment1(String attachment1) {
		this.attachment1 = attachment1;
	}

	public String getAttachment2() {
		return attachment2;
	}

	public void setAttachment2(String attachment2) {
		this.attachment2 = attachment2;
	}

	@Override
	public String toString() {
		return "Attachments{" +
				"attachment1='" + attachment1 + '\'' +
				", attachment2='" + attachment2 + '\'' +
				'}';
	}
}
