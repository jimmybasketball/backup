package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "companyCode", "businessNo", "declareType", "chkMark",
		"noticeDate", "noticeTime","note", "jkfResultDetail","businessType" })
@XmlRootElement(name = "jkfResult")
public class JKFResult {
	@XmlElement(name = "companyCode")
	private String companyCode;
	@XmlElement(name = "businessNo")
	private String businessNo;
    @XmlElement(name = "businessType")
    private String businessType;
	@XmlElement(name = "declareType")
	private String declareType;
	@XmlElement(name = "chkMark")
	private String chkMark;
	@XmlElement(name = "noticeDate")
	private String noticeDate;
	@XmlElement(name = "noticeTime")
	private String noticeTime;
    @XmlElement(name = "note")
    private String note;
	@XmlElement(name = "jkfResultDetail")
	@XmlElementWrapper(name="resultList")
	private List<JKFResultDetail> jkfResultDetail;
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
	public List<JKFResultDetail> getJkfResultDetail() {
		return jkfResultDetail;
	}
	public void setJkfResultDetail(List<JKFResultDetail> jkfResultDetail) {
		this.jkfResultDetail = jkfResultDetail;
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

    @Override
    public String toString() {
        return "JKFResult{" +
                "companyCode='" + companyCode + '\'' +
                ", businessNo='" + businessNo + '\'' +
                ", businessType='" + businessType + '\'' +
                ", declareType='" + declareType + '\'' +
                ", chkMark='" + chkMark + '\'' +
                ", noticeDate='" + noticeDate + '\'' +
                ", noticeTime='" + noticeTime + '\'' +
                ", note='" + note + '\'' +
                ", jkfResultDetail=" + jkfResultDetail +
                '}';
    }
}
