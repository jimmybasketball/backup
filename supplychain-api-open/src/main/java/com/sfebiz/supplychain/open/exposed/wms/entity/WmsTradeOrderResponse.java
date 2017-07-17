package com.sfebiz.supplychain.open.exposed.wms.entity;

import java.io.Serializable;
/**
 * Created by wuyun on 2017/6/6.
 */
import java.util.List;

import net.pocrd.annotation.Description;

@Description("商品信息查询、商品同步返回实体")
public class WmsTradeOrderResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 559654873941714393L;
	
	@Description("flag")
	public boolean flag;
	
	@Description("code")
	public String code;
	
	@Description("message")
	public String message;
	
	@Description("查询错误的skuId")
	public List<String> wrongSkus;

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getWrongSkus() {
		return wrongSkus;
	}

	public void setWrongSkus(List<String> wrongSkus) {
		this.wrongSkus = wrongSkus;
	}

	@Override
	public String toString() {
		return "WmsTradeOrderEntity [flag=" + flag + ", code=" + code + ", message=" + message + ", wrongSkus="
				+ wrongSkus + "]";
	}
	
}
