package com.sfebiz.supplychain.protocol.kd100;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 快递100 最新结果实体
 */
public class KD100CallbackLastResult implements Serializable {

	private static final long serialVersionUID = -1293098377589657887L;


	private String message = "";

	/**
	 * 单号
	 */
	@JsonIgnore
	private String nu = "";

	/**
	 * 是否签收标记
	 */
	@JsonIgnore
	private String ischeck = "0";

	/**
	 * 快递公司编码
	 */
	@JsonIgnore
	private String com = "";


	private String status = "0";


	@JsonIgnore
	private ArrayList<KD100CallbackItem> data = new ArrayList<KD100CallbackItem>();

	/**
	 * 快递单当前签收状态  0 在途, 1 揽件, 2 疑难,  3 签收, 4 退签, 5 派件, 6 退回
	 */
	@JsonIgnore
	private String state = "0";
	@JsonIgnore
	private String condition = "";

	@SuppressWarnings("unchecked")
	public KD100CallbackLastResult clone() {
		KD100CallbackLastResult r = new KD100CallbackLastResult();
		r.setCom(this.getCom());
		r.setIscheck(this.getIscheck());
		r.setMessage(this.getMessage());
		r.setNu(this.getNu());
		r.setState(this.getState());
		r.setStatus(this.getStatus());
		r.setCondition(this.getCondition());
		r.setData((ArrayList<KD100CallbackItem>) this.getData().clone());

		return r;
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

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public ArrayList<KD100CallbackItem> getData() {
		return data;
	}

	public void setData(ArrayList<KD100CallbackItem> data) {
		this.data = data;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	
}
