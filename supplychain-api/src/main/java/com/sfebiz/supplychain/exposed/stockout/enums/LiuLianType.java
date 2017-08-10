package com.sfebiz.supplychain.exposed.stockout.enums;

public enum LiuLianType {
	NEED_CHECK_NO("N","小包"),
	NEED_CHECK_YES("Y","大包"),
	ETK("ETK","e特快"),
	ETK_LINE_NID("HK-COE-ETK-GZ","E特快line");

	private String value;
	
	private String desc;
	
	private LiuLianType(String value, String desc){
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
	
}
