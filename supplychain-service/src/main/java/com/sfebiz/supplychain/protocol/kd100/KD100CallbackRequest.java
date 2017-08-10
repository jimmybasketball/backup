package com.sfebiz.supplychain.protocol.kd100;

import com.thoughtworks.xstream.XStream;

import java.io.Serializable;

/**
 * 快递100回调
 */
public class KD100CallbackRequest implements Serializable {

	private static final long serialVersionUID = 7534134870316953331L;
	
	private static XStream xstream;

	/**
	 * 监控状态:polling:监控中，shutdown:结束，abort:中止，updateall：重新推送。其中当快递单为已签收时status=shutdown，当message为“3天查询无记录”或“60天无变化时”
	 */
	private String status = "";

	/**
	 * 监控状态相关消息，如:3天查询无记录，60天无变化
	 */
	private String message = "";

	/**
	 * 快递公司编码是否出错
	 * 0 为本推送信息对应的是我们系统提交的原始快递公司编码
	 * 1 为本推送信息对应的是KD100纠正后的新的快递公司编码
	 */
	private String autoCheck = "";

	/**
	 * 原始的快递公司编码
	 */
	private String comOld = "";

	/**
	 * 纠正后的新的快递公司编码
	 */
	private String comNew = "";

	/**
	 * 最新查询结果
	 */
	private KD100CallbackLastResult lastResult = new KD100CallbackLastResult();

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public KD100CallbackLastResult getLastResult() {
		return lastResult;
	}

	public void setLastResult(KD100CallbackLastResult lastResult) {
		this.lastResult = lastResult;
	}

	public String getComNew() {
		return comNew;
	}

	public void setComNew(String comNew) {
		this.comNew = comNew;
	}

	public String getAutoCheck() {
		return autoCheck;
	}

	public void setAutoCheck(String autoCheck) {
		this.autoCheck = autoCheck;
	}

	public String getComOld() {
		return comOld;
	}

	public void setComOld(String comOld) {
		this.comOld = comOld;
	}

	private static XStream getXStream() {
		if (xstream == null) {
			xstream = new XStream();
			xstream.autodetectAnnotations(true);
			xstream.alias("pushRequest", KD100CallbackRequest.class);
			xstream.alias("item", KD100CallbackRequest.class);
			
		}
		return xstream;
	}

	public String toXml() {
		return "<?xml version='1.0' encoding='UTF-8'?>\r\n" + getXStream().toXML(this);
	}

	public static KD100CallbackRequest fromXml(String sXml) {
		return (KD100CallbackRequest) getXStream().fromXML(sXml);
	}

}
