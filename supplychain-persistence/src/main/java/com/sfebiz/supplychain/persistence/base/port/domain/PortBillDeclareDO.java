package com.sfebiz.supplychain.persistence.base.port.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * <p>与口岸交互的单据申报记录对象：目前支持运单&个人物品申报单</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/9
 * Time: 上午11:47
 */
public class PortBillDeclareDO extends BaseDO {

    private static final long serialVersionUID = 646846261104157754L;

    /**
     * 单据ID，如果是运单申报，则为出库单号；如果是个人物品申报单，则为个人物品申报单中的ID
     */
    private Long billId;

    /**
     * 业务编号，一般为订单号
     */
    private String businessNo;

    /**
     * 口岸ID，比如杭州口岸
     */
    private Long portId;

    /**
     * 单据中文名称
     */
    private String billName;

    /**
     * 单据类型
     */
    private String billType;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 申报类型
     */
    private String declareType;

    /**
     * 单据 下发状态，比如：是否向申报单位下发单据
     */
    private String state;

    /**
     * 单据业务状态，比如 单据被拦截、单据放行等
     */
    private String billBusinessState;

    /**
     * 单据发送时间
     */
    private Date billSendTime;

    /**
     * 口岸接受单据时间
     */
    private Date portAcceptTime;

    /**
     * 口岸处理时间
     */
    private Date portProcessTime;

    /**
     * 审批意见
     */
    private String comment;

    /**
     * 备注
     */
    private String note;

    /**
     * 异常结果
     */
    private String result;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getDeclareType() {
        return declareType;
    }

    public void setDeclareType(String declareType) {
        this.declareType = declareType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBillBusinessState() {
        return billBusinessState;
    }

    public void setBillBusinessState(String billBusinessState) {
        this.billBusinessState = billBusinessState;
    }

    public Date getBillSendTime() {
        return billSendTime;
    }

    public void setBillSendTime(Date billSendTime) {
        this.billSendTime = billSendTime;
    }

    public Date getPortAcceptTime() {
        return portAcceptTime;
    }

    public void setPortAcceptTime(Date portAcceptTime) {
        this.portAcceptTime = portAcceptTime;
    }

    public Date getPortProcessTime() {
        return portProcessTime;
    }

    public void setPortProcessTime(Date portProcessTime) {
        this.portProcessTime = portProcessTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
