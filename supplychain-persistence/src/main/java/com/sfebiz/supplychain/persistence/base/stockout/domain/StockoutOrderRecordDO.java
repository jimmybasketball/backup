package com.sfebiz.supplychain.persistence.base.stockout.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 
 * <p>出库单跟踪记录实体</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午2:26:57
 */
public class StockoutOrderRecordDO extends BaseDO {

    /** 序号 */
    private static final long serialVersionUID = 1775963648231189487L;

    /** 出库单主键Id */
    private Long              stockoutOrderId;

    /** 业务订单ID */
    private String            bizId;

    /** 支付申报下单状态 */
    private Integer           payState;

    /** 支付申报命令成功发送时间 */
    private Date              payCmdSuccessSendTime;

    /** 口岸下单状态 */
    private Integer           portState;

    /** 口岸申报命令成功发送时间 */
    private Date              portCmdSuccessSendTime;

    /** 口岸验证通过时间 */
    private Date              portValidatePassTime;

    /** 国际物流下单状态 */
    private Integer           tplIntlState;

    /** 国际物流命令成功发送时间 */
    private Date              tplIntlCmdSuccessSendTime;

    /** 国内物流下单状态 */
    private Integer           tplIntrState;

    /** 国内物流命令成功发送时间 */
    private Date              tplIntrCmdSuccessSendTime;

    /** 清关状态 */
    private Integer           ccbState;

    /** 清关下单命令成功发送时间 */
    private Date              ccbCmdSuccessSendTime;

    /** 集货下单状态 */
    private Integer           twsState;

    /** 集货下单命令成功发送时间 */
    private Date              twsCmdSuccessSendTime;

    /** 仓库下单状态 */
    private Integer           wmsState;

    /** 仓库下单命令成功发送时间 */
    private Date              wmsCmdSuccessSendTime;

    /** 仓库的发货时间 */
    private Date              wmsStockoutTime;

    /** 出库单下发流程完成时间 */
    private Date              stockoutCmdsSuccessSendTime;

    /** 海外仓入库时间 */
    private Date              overseasWarehouseStockinTime;

    /** 海外仓出库时间 */
    private Date              overseasWarehouseStockoutTime;

    /** 商户运单号 */
    private String            merchantMailNo;

    /** 转运、集货运单号 */
    private String            transferMailNo;

    /** 清关运单号 */
    private String            ccbMailNo;

    /** 备注 */
    private String            remarks;

    public Long getStockoutOrderId() {
        return stockoutOrderId;
    }

    public void setStockoutOrderId(Long stockoutOrderId) {
        this.stockoutOrderId = stockoutOrderId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Date getPayCmdSuccessSendTime() {
        return payCmdSuccessSendTime;
    }

    public void setPayCmdSuccessSendTime(Date payCmdSuccessSendTime) {
        this.payCmdSuccessSendTime = payCmdSuccessSendTime;
    }

    public Integer getPortState() {
        return portState;
    }

    public void setPortState(Integer portState) {
        this.portState = portState;
    }

    public Date getPortCmdSuccessSendTime() {
        return portCmdSuccessSendTime;
    }

    public void setPortCmdSuccessSendTime(Date portCmdSuccessSendTime) {
        this.portCmdSuccessSendTime = portCmdSuccessSendTime;
    }

    public Date getPortValidatePassTime() {
        return portValidatePassTime;
    }

    public void setPortValidatePassTime(Date portValidatePassTime) {
        this.portValidatePassTime = portValidatePassTime;
    }

    public Integer getTplIntlState() {
        return tplIntlState;
    }

    public void setTplIntlState(Integer tplIntlState) {
        this.tplIntlState = tplIntlState;
    }

    public Date getTplIntlCmdSuccessSendTime() {
        return tplIntlCmdSuccessSendTime;
    }

    public void setTplIntlCmdSuccessSendTime(Date tplIntlCmdSuccessSendTime) {
        this.tplIntlCmdSuccessSendTime = tplIntlCmdSuccessSendTime;
    }

    public Integer getTplIntrState() {
        return tplIntrState;
    }

    public void setTplIntrState(Integer tplIntrState) {
        this.tplIntrState = tplIntrState;
    }

    public Date getTplIntrCmdSuccessSendTime() {
        return tplIntrCmdSuccessSendTime;
    }

    public void setTplIntrCmdSuccessSendTime(Date tplIntrCmdSuccessSendTime) {
        this.tplIntrCmdSuccessSendTime = tplIntrCmdSuccessSendTime;
    }

    public Integer getCcbState() {
        return ccbState;
    }

    public void setCcbState(Integer ccbState) {
        this.ccbState = ccbState;
    }

    public Date getCcbCmdSuccessSendTime() {
        return ccbCmdSuccessSendTime;
    }

    public void setCcbCmdSuccessSendTime(Date ccbCmdSuccessSendTime) {
        this.ccbCmdSuccessSendTime = ccbCmdSuccessSendTime;
    }

    public Integer getTwsState() {
        return twsState;
    }

    public void setTwsState(Integer twsState) {
        this.twsState = twsState;
    }

    public Date getTwsCmdSuccessSendTime() {
        return twsCmdSuccessSendTime;
    }

    public void setTwsCmdSuccessSendTime(Date twsCmdSuccessSendTime) {
        this.twsCmdSuccessSendTime = twsCmdSuccessSendTime;
    }

    public Integer getWmsState() {
        return wmsState;
    }

    public void setWmsState(Integer wmsState) {
        this.wmsState = wmsState;
    }

    public Date getWmsCmdSuccessSendTime() {
        return wmsCmdSuccessSendTime;
    }

    public void setWmsCmdSuccessSendTime(Date wmsCmdSuccessSendTime) {
        this.wmsCmdSuccessSendTime = wmsCmdSuccessSendTime;
    }

    public Date getWmsStockoutTime() {
        return wmsStockoutTime;
    }

    public void setWmsStockoutTime(Date wmsStockoutTime) {
        this.wmsStockoutTime = wmsStockoutTime;
    }

    public Date getStockoutCmdsSuccessSendTime() {
        return stockoutCmdsSuccessSendTime;
    }

    public void setStockoutCmdsSuccessSendTime(Date stockoutCmdsSuccessSendTime) {
        this.stockoutCmdsSuccessSendTime = stockoutCmdsSuccessSendTime;
    }

    public Date getOverseasWarehouseStockinTime() {
        return overseasWarehouseStockinTime;
    }

    public void setOverseasWarehouseStockinTime(Date overseasWarehouseStockinTime) {
        this.overseasWarehouseStockinTime = overseasWarehouseStockinTime;
    }

    public Date getOverseasWarehouseStockoutTime() {
        return overseasWarehouseStockoutTime;
    }

    public void setOverseasWarehouseStockoutTime(Date overseasWarehouseStockoutTime) {
        this.overseasWarehouseStockoutTime = overseasWarehouseStockoutTime;
    }

    public String getMerchantMailNo() {
        return merchantMailNo;
    }

    public void setMerchantMailNo(String merchantMailNo) {
        this.merchantMailNo = merchantMailNo;
    }

    public String getTransferMailNo() {
        return transferMailNo;
    }

    public void setTransferMailNo(String transferMailNo) {
        this.transferMailNo = transferMailNo;
    }

    public String getCcbMailNo() {
        return ccbMailNo;
    }

    public void setCcbMailNo(String ccbMailNo) {
        this.ccbMailNo = ccbMailNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
