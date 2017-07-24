package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"companyCode", "businessNo", "businessType", "declareType", "chkMark" , "noticeDate" , "noticeTime" ,"note","cebFlag"})
@XmlRootElement(name = "jkfSign")
public class JKFSign {
    /**
     * 发送方海关十位数编码
     */
    @XmlElement(name = "companyCode")
    private String companyCode;


    @XmlElement(name = "cebFlag")
    private String cebFlag;

    /**
     * 业务编码
     */
    @XmlElement(name = "businessNo")
    private String businessNo;

    /**
     * 业务类型
     */
    @XmlElement(name = "businessType")
    private String businessType;

    /**
     * 申报类型
     */
    @XmlElement(name = "declareType")
    private String declareType;

    /**
     * 处理结果
     * 1:成功 2:处理失败
     */
    @XmlElement(name = "chkMark")
    private String chkMark;

    /**
     * 通知日期
     */
    @XmlElement(name = "noticeDate")
    private String noticeDate;

    /**
     * 通知时间
     */
    @XmlElement(name = "noticeTime")
    private String noticeTime;

    /**
     * 备注
     */
    @XmlElement(name = "note")
    private String note;


    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getDeclareType() {
        return declareType;
    }

    public void setDeclareType(String declareType) {
        this.declareType = declareType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }


    public String getChkMark() {
        return chkMark;
    }

    public void setChkMark(String chkMark) {
        this.chkMark = chkMark;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getCebFlag() {
        return cebFlag;
    }

    public void setCebFlag(String cebFlag) {
        this.cebFlag = cebFlag;
    }
}
