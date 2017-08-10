package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "MftNo", "OrderNo", "CheckFlg", "CheckMsg", "mftinfos","status",
	    "result",
	    "passTime",
	    "checkpointName",
	    "carNo",
	    "logisticsNo",
	    "logisticsName" })
@XmlRootElement(name = "Mft")
@Description("申报单信息")
public class Mft extends NbbsBody implements Serializable {

	private static final long serialVersionUID = -608012125492013388L;
	@Description("申报单号")
	@XmlElement(nillable = false, required = false)
	private String MftNo;

	@Description("订单号")
	@XmlElement(nillable = false, required = false)
	private String OrderNo;

	@Description("预校验标识(0=未通过,1=已通过)")
	@XmlElement(nillable = false, required = false)
	private String CheckFlg;

	@Description("预校验描述")
	@XmlElement(nillable = false, required = false)
	private String CheckMsg;

	@Description("申报单信息")
	@XmlElement(nillable = false, required = false)
	private MftInfos mftinfos;

	@XmlElement(name = "Status", required = true)
	private String status;
	@XmlElement(name = "Result", required = true)
	private String result;
	@XmlElement(name = "PassTime", required = true)
	private Date passTime;
	@XmlElement(name = "CheckpointName", required = true)
	private String checkpointName;
	@XmlElement(name = "CarNo", required = true)
	private String carNo;
	@XmlElement(name = "LogisticsNo", required = true)
	private String logisticsNo;
	@XmlElement(name = "LogisticsName", required = true)
	private String logisticsName;

	public String getMftNo() {
		return MftNo;
	}

	public void setMftNo(String mftNo) {
		MftNo = mftNo;
	}

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public String getCheckFlg() {
		return CheckFlg;
	}

	public void setCheckFlg(String checkFlg) {
		CheckFlg = checkFlg;
	}

	public String getCheckMsg() {
		return CheckMsg;
	}

	public void setCheckMsg(String checkMsg) {
		CheckMsg = checkMsg;
	}

	public MftInfos getMftinfos() {
		return mftinfos;
	}

	public void setMftinfos(MftInfos mftinfos) {
		this.mftinfos = mftinfos;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getPassTime() {
		return passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}

	public String getCheckpointName() {
		return checkpointName;
	}

	public void setCheckpointName(String checkpointName) {
		this.checkpointName = checkpointName;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("MftNo", MftNo)
				.append("OrderNo", OrderNo)
				.append("CheckFlg", CheckFlg)
				.append("CheckMsg", CheckMsg)
				.append("mftinfos", mftinfos)
				.append("status", status)
				.append("result", result)
				.append("passTime", passTime)
				.append("checkpointName", checkpointName)
				.append("carNo", carNo)
				.append("logisticsNo", logisticsNo)
				.append("logisticsName", logisticsName)
				.toString();
	}
}
