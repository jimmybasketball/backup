package cn.gov.zjport.newyork.ws.bo;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@Description("响应类结构体")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "response", propOrder = { "success", "reason", "reasonDesc" })
public class Response4Callback implements Serializable {

	private static final long serialVersionUID = 4250928952064308343L;

	@Description("响应结果 true/false")
	@XmlElement(nillable=false,required=true)
	public String success;
	
	@Description("错误返回码")
	@XmlElement(nillable=false,required=true)
	public String reason;
	
	@Description("错误描述信息")
	@XmlElement(nillable=false,required=true)
	public String reasonDesc;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("success", success)
				.append("reason", reason)
				.append("reasonDesc", reasonDesc)
				.toString();
	}
}
