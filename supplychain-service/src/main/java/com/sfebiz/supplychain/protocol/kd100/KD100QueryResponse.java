package com.sfebiz.supplychain.protocol.kd100;

import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;

public class KD100QueryResponse {

	private static XStream xstream;

	private Boolean result;
	private String returnCode;
	private String message;
	private String nu = "";
	private String ischeck = "0";
	private String com = "";
	private String status = "0";
	private ArrayList<KD100CallbackItem> data = new ArrayList<KD100CallbackItem>();
	private String state = "0";
	private String condition = "";

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

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<KD100CallbackItem> getData() {
		return data;
	}

	public void setData(ArrayList<KD100CallbackItem> data) {
		this.data = data;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	private static XStream getXStream() {
		if (xstream == null) {
			xstream = new XStream();
			xstream.autodetectAnnotations(true);
			xstream.alias("orderResponse", KD100QueryResponse.class);
		}
		return xstream;
	}

	public String toXml(){
		return "<?xml version='1.0' encoding='UTF-8'?>\r\n" + getXStream().toXML(this);
	}

	public static KD100QueryResponse fromXml(String sXml){
		return (KD100QueryResponse)getXStream().fromXML(sXml);
	}
}
