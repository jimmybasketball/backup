package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息体，综合了所有的内容体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"orderInfoList", "importPayList","productRecordList", "wayBillList", "goodsDeclareModuleList"})
@XmlRootElement(name = "body")
public class Body {

    /**
     * 商品备案内容
     */
    @XmlElement(name = "productRecord")
    @XmlElementWrapper(name = "productRecordList")
    private List<ProductRecord> productRecordList;


    /**
     * 电商企业代理支付企业进行支付申报的支付信息内容
     */
    @XmlElement(name = "importPay")
    @XmlElementWrapper(name = "importPayList")
    private List<ImportPay> importPayList;

    /**
     * 企业订单申报内容
     */
    @XmlElement(name = "orderInfo")
    @XmlElementWrapper(name = "orderInfoList")
    private List<OrderInfo> orderInfoList;


    /**
     * 物流运单内容
     */
    @XmlElement(name = "wayBill")
    @XmlElementWrapper(name = "wayBillList")
    private List<WayBill> wayBillList;

    /**
     * 个人申报单内容
     */
    @XmlElement(name = "goodsDeclareModule")
    @XmlElementWrapper(name = "goodsDeclareModuleList")
    private List<GoodsDeclareModule> goodsDeclareModuleList;

    public List<OrderInfo> getOrderInfoList() {
        if (orderInfoList == null) {
            orderInfoList = new ArrayList<OrderInfo>();
        }
        return orderInfoList;
    }

    public void setOrderInfoList(List<OrderInfo> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    public List<ProductRecord> getProductRecordList() {
        if (productRecordList == null) {
            productRecordList = new ArrayList<ProductRecord>();
        }
        return productRecordList;
    }

    public void setProductRecordList(List<ProductRecord> productRecordList) {
        this.productRecordList = productRecordList;
    }

    public List<WayBill> getWayBillList() {
        if (wayBillList == null) {
            wayBillList = new ArrayList<WayBill>();
        }
        return wayBillList;
    }

    public List<GoodsDeclareModule> getGoodsDeclareModuleList() {
        if (goodsDeclareModuleList == null) {
            goodsDeclareModuleList = new ArrayList<GoodsDeclareModule>();
        }
        return goodsDeclareModuleList;
    }

    public void setGoodsDeclareModuleList(List<GoodsDeclareModule> goodsDeclareModuleList) {
        this.goodsDeclareModuleList = goodsDeclareModuleList;
    }

    public void setWayBillList(List<WayBill> wayBillList) {
        this.wayBillList = wayBillList;
    }

    public List<ImportPay> getImportPayList() {
        if (importPayList == null) {
            importPayList = new ArrayList<ImportPay>();
        }
        return importPayList;
    }

    public void setImportPayList(List<ImportPay> importPayList) {
        this.importPayList = importPayList;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("orderInfoList", orderInfoList)
                .append("importPayList", importPayList)
                .append("productRecordList", productRecordList)
                .append("wayBillList", wayBillList)
                .append("goodsDeclareModuleList", goodsDeclareModuleList)
                .toString();
    }
}
