package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "businessType","discount","batchNumbers","consigneeDitrict","cebFlag"})
@XmlRootElement(name = "head")
public class Head {
	@XmlElement(name = "businessType")
	private String businessType;

	@XmlElement(name = "batchNumbers")
	private String batchNumbers;

	@XmlElement(name = "consigneeDitrict")
	private String consigneeDitrict;

	@XmlElement(name = "discount")
	private String discount;

	@XmlElement(name = "cebFlag")
	private String cebFlag;

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getBatchNumbers() {
		return batchNumbers;
	}

	public void setBatchNumbers(String batchNumbers) {
		this.batchNumbers = batchNumbers;
	}

	public String getConsigneeDitrict() {
		return consigneeDitrict;
	}

	public void setConsigneeDitrict(String consigneeDitrict) {
		this.consigneeDitrict = consigneeDitrict;
	}

	public String getCebFlag() {
		return cebFlag;
	}

	public void setCebFlag(String cebFlag) {
		this.cebFlag = cebFlag;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("businessType", businessType)
				.append("discount", discount)
				.append("batchNumbers", batchNumbers)
				.append("consigneeDitrict", consigneeDitrict)
				.toString();
	}
}
