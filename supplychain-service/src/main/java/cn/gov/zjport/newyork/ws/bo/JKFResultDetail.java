package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "resultInfo" })
@XmlRootElement(name = "jkfResultDetail")
public class JKFResultDetail {

    /**
     * 处理结果文字信息
     */
	@XmlElement(name="resultInfo")
	private String resultInfo;

	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("resultInfo", resultInfo)
				.toString();
	}
}
