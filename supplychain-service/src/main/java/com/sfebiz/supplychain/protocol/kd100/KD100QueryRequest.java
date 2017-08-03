/**
 * 
 */
package com.sfebiz.supplychain.protocol.kd100;

import com.sfebiz.supplychain.util.JSONUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.mapper.DefaultMapper;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 快递100订阅接口请求参数
 */
public class KD100QueryRequest {
	private static XStream xstream;
	private String com;
	private String num;
	private String queryKey;
	private String queryCustomer;

	private HashMap<String, String> parameters = new HashMap<String, String>();

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public HashMap<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public String getQueryCustomer() {
		return queryCustomer;
	}

	public void setQueryCustomer(String queryCustomer) {
		this.queryCustomer = queryCustomer;
	}

	@Override
	public String toString() {
		return JSONUtil.toJson(this);
	}
	
	private static XStream getXStream() {
		if (xstream == null) {
			xstream = new XStream();
			xstream.registerConverter((Converter) new MapCustomConverter(new DefaultMapper(XStream.class.getClassLoader())));  
			xstream.autodetectAnnotations(true);
			xstream.alias("orderRequest", KD100QueryRequest.class);
			xstream.alias("property", Entry.class);  
		}
		return xstream;
	}

	public String toXml(){
		return "<?xml version='1.0' encoding='UTF-8'?>\r\n" + getXStream().toXML(this);
	}

	public static KD100QueryRequest fromXml(String sXml){
		return (KD100QueryRequest)getXStream().fromXML(sXml);
	}
}
