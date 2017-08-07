package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderByCode;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 根据订单号获取单票订单信息 反馈回来的response json文件转换的对象
 * 
 * @author Administrator
 *
 */
public class PTResDatas {

	public String ask;

	public String message;

	public PTResData data;

	@JsonProperty("Error")
	private PTResError error;

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PTResData getData() {
		return data;
	}

	public void setData(PTResData data) {
		this.data = data;
	}

	@JsonIgnore
	public PTResError getError() {
		return error;
	}
	
	@JsonIgnore
	public void setError(PTResError Error) {
		this.error = Error;
	}

}
