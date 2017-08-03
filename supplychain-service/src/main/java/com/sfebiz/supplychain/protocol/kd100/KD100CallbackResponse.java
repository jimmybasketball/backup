package com.sfebiz.supplychain.protocol.kd100;

import com.thoughtworks.xstream.XStream;

import java.io.Serializable;

public class KD100CallbackResponse implements Serializable {

	private static final long serialVersionUID = -9179772546452627428L;

	private static XStream xstream;

	private Boolean result;
	private String returnCode;
	private String message;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private static XStream getXStream() {
		if (xstream == null) {
			xstream = new XStream();
			xstream.autodetectAnnotations(true);
			xstream.alias("pushResponse", KD100CallbackResponse.class);
		}
		return xstream;
	}

	public String toXml() {
		return "<?xml version='1.0' encoding='UTF-8'?>\r\n"
				+ getXStream().toXML(this);
	}

	public static KD100CallbackResponse fromXml(String sXml) {
		return (KD100CallbackResponse) getXStream().fromXML(sXml);
	}

}
