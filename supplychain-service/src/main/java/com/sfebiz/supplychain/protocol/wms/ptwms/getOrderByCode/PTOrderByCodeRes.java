package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderByCode;

import com.sfebiz.supplychain.util.XMLUtil;
import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据订单号获取单票订单信息 反馈xml的转换的对象
 * @author Administrator
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
@Description("PTOrderByCodeRes")
public class PTOrderByCodeRes implements Serializable {

	private static final long serialVersionUID = -5292019754009818059L;

	@XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
	private PTOrderByCodeResBody resBody;

	@XmlAttribute(name = "SOAP-ENV")
	private String soapenv;

	@XmlAttribute(name = "ns1")
	private String nsl;

	public PTOrderByCodeResBody getResBody() {
		return resBody;
	}

	public void setBody(PTOrderByCodeResBody resBody) {
		this.resBody = resBody;
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

	public static void main(String[] args) {
		try {
			System.out.println(XMLUtil.convertToXml(getData()));
			String data = XMLUtil.convertToXml(getData());
			PTOrderByCodeRes t = XMLUtil.converyToJavaBean(data,
					PTOrderByCodeRes.class);
			System.out.println(XMLUtil.convertToXml(t));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PTOrderByCodeRes getData(){
		PTOrderByCodeRes res = new PTOrderByCodeRes();
		PTResDatas datas = new PTResDatas();
		datas.setAsk("sucess");
		datas.setMessage("null");
		PTResItem item = new PTResItem();
		item.setProduct_sku("1");
		item.setQuantity(1);
		PTResItem item1 = new PTResItem();
		item1.setProduct_sku("2");
		item1.setQuantity(2);
		List<PTResItem> l = new ArrayList<PTResItem>();
		l.add(item);
		l.add(item1);
		datas.setMessage("");
		datas.setData(null);
		PTOrderByCodeResService s = new PTOrderByCodeResService();
//		s.setResponse(datas);
		PTOrderByCodeResBody b = new PTOrderByCodeResBody();
		b.setService(s);
		res.setBody(b);
		return res;
		
	}
}
