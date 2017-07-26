package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

/**
 * <p>杭州商品备案响应内容</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/12
 * Time: 下午3:33
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"productRecordNo", "approveResult", "approveComment", "processTime"})
@XmlRootElement(name = "productRecord")
public class ResponseProductRecord {

    /**
     * 产品国检备案编号
     */
    @XmlElement(name = "productRecordNo")
    private String productRecordNo;

    /**
     * 审批结果: P2:通过 P3:未通过
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


    public String getProductRecordNo() {
        return productRecordNo;
    }

    public void setProductRecordNo(String productRecordNo) {
        this.productRecordNo = productRecordNo;
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
                .append("productRecordNo", productRecordNo)
                .append("approveResult", approveResult)
                .append("approveComment", approveComment)
                .append("processTime", processTime)
                .toString();
    }
}
