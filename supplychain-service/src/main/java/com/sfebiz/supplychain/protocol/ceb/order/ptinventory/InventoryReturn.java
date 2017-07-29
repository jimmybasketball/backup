package com.sfebiz.supplychain.protocol.ceb.order.ptinventory;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p>
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a> Date: 16/4/7 Time:
 * 下午9:16
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "guid", "customsCode", "ebpCode", "ebcCode", "agentCode", "copNo", "preNo", "invtNo",
		"returnStatus", "returnTime", "returnInfo" })
@XmlRootElement(name = "InventoryReturn")
public class InventoryReturn implements Serializable {

	
	
	public InventoryReturn() {
		super();
	}

	public InventoryReturn(String guid, String customsCode, String ebpCode, String ebcCode, String agentCode,
			String copNo, String preNo, String invtNo, String returnStatus, String returnTime, String returnInfo) {
		super();
		this.guid = guid;
		this.customsCode = customsCode;
		this.ebpCode = ebpCode;
		this.ebcCode = ebcCode;
		this.agentCode = agentCode;
		this.copNo = copNo;
		this.preNo = preNo;
		this.invtNo = invtNo;
		this.returnStatus = returnStatus;
		this.returnTime = returnTime;
		this.returnInfo = returnInfo;
	}

	private static final long serialVersionUID = -2385009829978128840L;
	/**
	 * 系统唯一序号 企业系统生成36位唯一序号（英文字母大写）
	 */
	@XmlElement(name = "guid")
	private String guid;

	@XmlElement(name = "customsCode")
	private String customsCode;

	@XmlElement(name = "ebpCode")
	private String ebpCode;

	@XmlElement(name = "ebcCode")
	private String ebcCode;

	@XmlElement(name = "agentCode")
	private String agentCode;

	@XmlElement(name = "copNo")
	private String copNo;

	@XmlElement(name = "preNo")
	private String preNo;

	@XmlElement(name = "invtNo")
	private String invtNo;

	@XmlElement(name = "returnStatus")
	private String returnStatus;

	@XmlElement(name = "returnTime")
	private String returnTime;

	@XmlElement(name = "returnInfo")
	private String returnInfo;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getCustomsCode() {
		return customsCode;
	}

	public void setCustomsCode(String customsCode) {
		this.customsCode = customsCode;
	}

	public String getEbpCode() {
		return ebpCode;
	}

	public void setEbpCode(String ebpCode) {
		this.ebpCode = ebpCode;
	}

	public String getEbcCode() {
		return ebcCode;
	}

	public void setEbcCode(String ebcCode) {
		this.ebcCode = ebcCode;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
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

	public String getInvtNo() {
		return invtNo;
	}

	public void setInvtNo(String invtNo) {
		this.invtNo = invtNo;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}
	
}
