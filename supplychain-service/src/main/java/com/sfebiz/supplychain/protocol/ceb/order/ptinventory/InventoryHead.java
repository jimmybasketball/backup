package com.sfebiz.supplychain.protocol.ceb.order.ptinventory;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "guid", "appType", "appTime", "appStatus", "orderNo", "ebpCode", "ebpName", "ebcCode", "ebcName",
		"logisticsNo", "logisticsCode", "logisticsName", "copNo", "preNo", "assureCode", "emsNo", "invtNo", "ieFlag",
		"declTime", "customsCode", "portCode", "ieDate", "buyerIdType", "buyerIdNumber", "buyerName", "buyerTelephone",
		"consigneeAddress", "agentCode", "agentName", "areaCode", "areaName", "tradeMode", "trafMode", "trafNo",
		"voyageNo", "billNo", "loctNo", "licenseNo", "country", "freight", "insuredFee", "currency", "wrapType",
		"packNo", "grossWeight", "netWeight", "note" })
@XmlRootElement(name = "ceb:InventoryHead")
public class InventoryHead implements Serializable {

	private static final long serialVersionUID = -8832111101998868112L;

	@XmlElement(name = "ceb:guid")
	public String guid;

	@XmlElement(name = "ceb:appType")
	public String appType;

	@XmlElement(name = "ceb:appTime")
	public String appTime;

	@XmlElement(name = "ceb:appStatus")
	public String appStatus;

	@XmlElement(name = "ceb:orderNo")
	public String orderNo;

	@XmlElement(name = "ceb:ebpCode")
	public String ebpCode;

	@XmlElement(name = "ceb:ebpName")
	public String ebpName;

	@XmlElement(name = "ceb:ebcCode")
	public String ebcCode;

	@XmlElement(name = "ceb:ebcName")
	public String ebcName;

	@XmlElement(name = "ceb:logisticsNo")
	public String logisticsNo;

	@XmlElement(name = "ceb:logisticsCode")
	public String logisticsCode;

	@XmlElement(name = "ceb:logisticsName")
	public String logisticsName;

	@XmlElement(name = "ceb:copNo")
	public String copNo;

	@XmlElement(name = "ceb:preNo")
	public String preNo;

	@XmlElement(name = "ceb:assureCode")
	public String assureCode;

	@XmlElement(name = "ceb:emsNo")
	public String emsNo;

	@XmlElement(name = "ceb:invtNo")
	public String invtNo;

	@XmlElement(name = "ceb:ieFlag")
	public String ieFlag;

	@XmlElement(name = "ceb:declTime")
	public String declTime;

	@XmlElement(name = "ceb:customsCode")
	public String customsCode;

	@XmlElement(name = "ceb:portCode")
	public String portCode;

	@XmlElement(name = "ceb:ieDate")
	public String ieDate;

	@XmlElement(name = "ceb:buyerIdType")
	public String buyerIdType;

	@XmlElement(name = "ceb:buyerIdNumber")
	public String buyerIdNumber;

	@XmlElement(name = "ceb:buyerName")
	public String buyerName;

	@XmlElement(name = "ceb:buyerTelephone")
	public String buyerTelephone;

	@XmlElement(name = "ceb:consigneeAddress")
	public String consigneeAddress;

	@XmlElement(name = "ceb:agentCode")
	public String agentCode;

	@XmlElement(name = "ceb:agentName")
	public String agentName;

	@XmlElement(name = "ceb:areaCode")
	public String areaCode;

	@XmlElement(name = "ceb:areaName")
	public String areaName;

	@XmlElement(name = "ceb:tradeMode")
	public String tradeMode;

	@XmlElement(name = "ceb:trafMode")
	public String trafMode;

	@XmlElement(name = "ceb:trafNo")
	public String trafNo;

	@XmlElement(name = "ceb:voyageNo")
	public String voyageNo;

	@XmlElement(name = "ceb:billNo")
	public String billNo;

	@XmlElement(name = "ceb:loctNo")
	public String loctNo;

	@XmlElement(name = "ceb:licenseNo")
	public String licenseNo;

	@XmlElement(name = "ceb:country")
	public String country;

	@XmlElement(name = "ceb:freight")
	public String freight;

	@XmlElement(name = "ceb:insuredFee")
	public String insuredFee;

	@XmlElement(name = "ceb:currency")
	public String currency;

	@XmlElement(name = "ceb:wrapType")
	public String wrapType;

	@XmlElement(name = "ceb:packNo")
	public String packNo;

	@XmlElement(name = "ceb:grossWeight")
	public String grossWeight;

	@XmlElement(name = "ceb:netWeight")
	public String netWeight;

	@XmlElement(name = "ceb:note")
	public String note;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppTime() {
		return appTime;
	}

	public void setAppTime(String appTime) {
		this.appTime = appTime;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getEbpCode() {
		return ebpCode;
	}

	public void setEbpCode(String ebpCode) {
		this.ebpCode = ebpCode;
	}

	public String getEbpName() {
		return ebpName;
	}

	public void setEbpName(String ebpName) {
		this.ebpName = ebpName;
	}

	public String getEbcCode() {
		return ebcCode;
	}

	public void setEbcCode(String ebcCode) {
		this.ebcCode = ebcCode;
	}

	public String getEbcName() {
		return ebcName;
	}

	public void setEbcName(String ebcName) {
		this.ebcName = ebcName;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getCopNo() {
		return copNo;
	}

	public void setCopNo(String copNo) {
		this.copNo = copNo;
	}

	public String getPreNo() {
		return preNo;
	}

	public void setPreNo(String preNo) {
		this.preNo = preNo;
	}

	public String getAssureCode() {
		return assureCode;
	}

	public void setAssureCode(String assureCode) {
		this.assureCode = assureCode;
	}

	public String getEmsNo() {
		return emsNo;
	}

	public void setEmsNo(String emsNo) {
		this.emsNo = emsNo;
	}

	public String getInvtNo() {
		return invtNo;
	}

	public void setInvtNo(String invtNo) {
		this.invtNo = invtNo;
	}

	public String getIeFlag() {
		return ieFlag;
	}

	public void setIeFlag(String ieFlag) {
		this.ieFlag = ieFlag;
	}

	public String getDeclTime() {
		return declTime;
	}

	public void setDeclTime(String declTime) {
		this.declTime = declTime;
	}

	public String getCustomsCode() {
		return customsCode;
	}

	public void setCustomsCode(String customsCode) {
		this.customsCode = customsCode;
	}

	public String getPortCode() {
		return portCode;
	}

	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	public String getIeDate() {
		return ieDate;
	}

	public void setIeDate(String ieDate) {
		this.ieDate = ieDate;
	}

	public String getBuyerIdType() {
		return buyerIdType;
	}

	public void setBuyerIdType(String buyerIdType) {
		this.buyerIdType = buyerIdType;
	}

	public String getBuyerIdNumber() {
		return buyerIdNumber;
	}

	public void setBuyerIdNumber(String buyerIdNumber) {
		this.buyerIdNumber = buyerIdNumber;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerTelephone() {
		return buyerTelephone;
	}

	public void setBuyerTelephone(String buyerTelephone) {
		this.buyerTelephone = buyerTelephone;
	}

	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getTradeMode() {
		return tradeMode;
	}

	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
	}

	public String getTrafMode() {
		return trafMode;
	}

	public void setTrafMode(String trafMode) {
		this.trafMode = trafMode;
	}

	public String getTrafNo() {
		return trafNo;
	}

	public void setTrafNo(String trafNo) {
		this.trafNo = trafNo;
	}

	public String getVoyageNo() {
		return voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getLoctNo() {
		return loctNo;
	}

	public void setLoctNo(String loctNo) {
		this.loctNo = loctNo;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getInsuredFee() {
		return insuredFee;
	}

	public void setInsuredFee(String insuredFee) {
		this.insuredFee = insuredFee;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getWrapType() {
		return wrapType;
	}

	public void setWrapType(String wrapType) {
		this.wrapType = wrapType;
	}

	public String getPackNo() {
		return packNo;
	}

	public void setPackNo(String packNo) {
		this.packNo = packNo;
	}

	public String getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}

	public String getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}



}
