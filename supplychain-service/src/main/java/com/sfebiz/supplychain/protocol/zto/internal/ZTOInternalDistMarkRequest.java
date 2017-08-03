package com.sfebiz.supplychain.protocol.zto.internal;

import com.sfebiz.supplychain.protocol.zto.ZTORequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <p>
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a> Date: 15/12/1 Time:
 * 下午6:41
 */
public class ZTOInternalDistMarkRequest implements ZTORequest {

	private static final long serialVersionUID = -4576187451717759278L;

	/**
	 * 接口唯一id，（有问题是，可以给此属性赋一个唯一值，方便中通跟踪查询问题）
	 */
	private String id;

	/**
	 * 发件市
	 */
	private String sendcity;

	/**
	 * 发件详细地址
	 */
	private String sendaddress;

	/**
	 * 收件市
	 */
	private String receivercity;

	/**
	 * 收件详细地址
	 */
	private String receiveraddress;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendcity() {
		return sendcity;
	}

	public void setSendcity(String sendcity) {
		this.sendcity = sendcity;
	}

	public String getSendaddress() {
		return sendaddress;
	}

	public void setSendaddress(String sendaddress) {
		this.sendaddress = sendaddress;
	}

	public String getReceivercity() {
		return receivercity;
	}

	public void setReceivercity(String receivercity) {
		this.receivercity = receivercity;
	}

	public String getReceiveraddress() {
		return receiveraddress;
	}

	public void setReceiveraddress(String receiveraddress) {
		this.receiveraddress = receiveraddress;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
