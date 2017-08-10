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
public class KD100Request {
	private static XStream xstream;
	private String company;
	private String number;
	private String key;

	private HashMap<String, String> parameters = new HashMap<String, String>();

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public HashMap<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
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
			xstream.alias("orderRequest", KD100Request.class);
			xstream.alias("property", Entry.class);  
		}
		return xstream;
	}

	public String toXml(){
		return "<?xml version='1.0' encoding='UTF-8'?>\r\n" + getXStream().toXML(this);
	}

	public static KD100Request fromXml(String sXml){
		return (KD100Request)getXStream().fromXML(sXml);
	}
}
