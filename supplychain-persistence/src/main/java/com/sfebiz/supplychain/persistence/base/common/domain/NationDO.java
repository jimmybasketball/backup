package com.sfebiz.supplychain.persistence.base.common.domain;

import com.sfebiz.common.dao.domain.BaseDO;

public class NationDO extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -63668093456985646L;
	private String nationE;
	private String nation;
	private String code;
	private String caCode;
	public String getNationE() {
		return nationE;
	}
	public void setNationE(String nationE) {
		this.nationE = nationE;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCaCode() {
		return caCode;
	}
	public void setCaCode(String caCode) {
		this.caCode = caCode;
	}
}
