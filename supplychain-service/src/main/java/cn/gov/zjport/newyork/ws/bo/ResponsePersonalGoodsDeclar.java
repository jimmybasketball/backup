package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

/**
 * <p>杭州个人申报单响应内容</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/4
 * Time: 下午3:33
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"personalGoodsFormNo", "approveResult", "approveComment", "processTime"})
@XmlRootElement(name = "jkfGoodsDeclar")
public class ResponsePersonalGoodsDeclar {

    /**
     * 个人物品申报单编号
     * 关区号（4位）+年份（4位）+进出口标志（1位）+流水号（9位），服务平台生成后，反馈管理平台及电商平台
     */
    @XmlElement(name = "personalGoodsFormNo")
    private String personalGoodsFormNo;

    /**
     * 个人物品申报单状态（见参数表）
     */
    @XmlElement(name = "approveResult")
    private String approveResult;

    /**
     * 审批意见
     */
    @XmlElement(name = "approveComment")
    private String approveComment;

    /**
     * 处理时间
     */
    @XmlElement(name = "processTime")
    private String processTime;

    public String getPersonalGoodsFormNo() {
        return personalGoodsFormNo;
    }

    public void setPersonalGoodsFormNo(String personalGoodsFormNo) {
        this.personalGoodsFormNo = personalGoodsFormNo;
    }

    public String getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(String approveResult) {
        this.approveResult = approveResult;
    }

    public String getApproveComment() {
        return approveComment;
    }

    public void setApproveComment(String approveComment) {
        this.approveComment = approveComment;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("personalGoodsFormNo", personalGoodsFormNo)
                .append("approveResult", approveResult)
                .append("approveComment", approveComment)
                .append("processTime", processTime)
                .toString();
    }
}
