package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderByCode;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * 根据订单号获取单票订单信息 请求xml文件转换的对象
 * @author Administrator
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
@Description("PTOrderByCode")
public class PTOrderByCode implements Serializable {

	private static final long serialVersionUID = -5292001001111818059L;

	@XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
	private PTOrderByCodeBody body;

	@XmlAttribute(name = "SOAP-ENV")
	private String soapenv;

	@XmlAttribute(name = "ns1")
	private String nsl;

	public PTOrderByCodeBody getBody() {
		return body;
	}

	public String getSoapenv() {
		return soapenv;
	}

	public void setSoapenv(String soapenv) {
		this.soapenv = soapenv;
	}

	public String getNsl() {
		return nsl;
	}

	public void setNsl(String nsl) {
		this.nsl = nsl;
	}

	public void setBody(PTOrderByCodeBody body) {
		this.body = body;
	}

	public static void main(String[] args) {
		try {
			// System.out.println(com.sfebiz.logistics.sdk.client.utils.XMLUtil.convertToXml(BuildRequest.buildReuqest()));
			// Test5 t =
			// com.sfebiz.logistics.sdk.client.utils.XMLUtil.converyToJavaBean(com.sfebiz.logistics.sdk.client.utils.XMLUtil.convertToXml(new
			// PTOrderByCode()),Test5.class);
			// System.out.println(com.sfebiz.logistics.sdk.client.utils.XMLUtil.convertToXml(t));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
