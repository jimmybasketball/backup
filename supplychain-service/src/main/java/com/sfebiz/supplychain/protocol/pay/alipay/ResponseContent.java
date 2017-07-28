package com.sfebiz.supplychain.protocol.pay.alipay;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>
 * 支付宝订单申报返回
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a> Date: 15/5/18 Time:
 * 下午8:54
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "resultCode", "tradeNo", "alipayDeclareNo","buyerCertNo","buyerName",
		"detailErrorCode", "detailErrorDes", "notFound" })
public class ResponseContent implements Serializable {

	private static final long serialVersionUID = 1259228596210794965L;

	@XmlElement(name = "result_code")
	@Description("响应码")
	private String resultCode;

	@XmlElement(name = "trade_no")
	@Description("支付宝交易号")
	private String tradeNo;

	@XmlElement(name = "alipay_declare_no")
	@Description("支付宝报关流水号")
	private String alipayDeclareNo;

	@XmlElement(name = "buyer_cert_no")
	@Description("支付宝支付人身份证号")
	private String buyerCertNo;

	@XmlElement(name = "buyer_name")
	@Description("支付宝支付人姓名")
	private String buyerName;

	@XmlElement(name = "detail_error_code")
	@Description("支付宝报关流水号")
	private String detailErrorCode;

	@XmlElement(name = "detail_error_des")
	@Description("详细错误描述")
	private String detailErrorDes;

	@XmlElement(name = "not_found")
	@Description("查询申报对象")
	private String notFound;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getDetailErrorCode() {
		return detailErrorCode;
	}

	public void setDetailErrorCode(String detailErrorCode) {
		this.detailErrorCode = detailErrorCode;
	}

	public String getDetailErrorDes() {
		return detailErrorDes;
	}

	public void setDetailErrorDes(String detailErrorDes) {
		this.detailErrorDes = detailErrorDes;
	}

	public String getAlipayDeclareNo() {
		return alipayDeclareNo;
	}

	public void setAlipayDeclareNo(String alipayDeclareNo) {
		this.alipayDeclareNo = alipayDeclareNo;
	}

	public String getBuyerCertNo() {
		return buyerCertNo;
	}

	public void setBuyerCertNo(String buyerCertNo) {
		this.buyerCertNo = buyerCertNo;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getNotFound() {
		return notFound;
	}

	public void setNotFound(String notFound) {
		this.notFound = notFound;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("resultCode", resultCode)
				.append("tradeNo", tradeNo)
				.append("detailErrorCode", detailErrorCode)
				.append("detailErrorDes", detailErrorDes).toString();
	}
}
