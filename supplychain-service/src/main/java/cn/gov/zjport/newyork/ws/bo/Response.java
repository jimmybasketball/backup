package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mo",namespace="")
@XmlType(propOrder = { "version", "head", "body" })
public class Response {
	@XmlAttribute(name = "version")
	private String version = "1.0.0";

	@XmlElement(name = "head")
	private Head head;

	@XmlElement(name = "body")
	private ResponseBody body;

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

	public ResponseBody getBody() {
		return body;
	}

	public void setBody(ResponseBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("version", version)
				.append("head", head)
				.append("body", body)
				.toString();
	}
}
