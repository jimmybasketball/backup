package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mo",namespace="")
@XmlType(propOrder = { "version", "head", "body" })
public class Request {
	@XmlAttribute(name = "version")
	private String version = "1.0.0";

	@XmlElement(name = "head")
	private Head head;

	@XmlElement(name = "body")
	private Body body;

	public Head getHead() {
		if (head == null){
			head = new Head();
		}
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Body getBody() {
		if (body == null){
			body = new Body();
		}
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}
