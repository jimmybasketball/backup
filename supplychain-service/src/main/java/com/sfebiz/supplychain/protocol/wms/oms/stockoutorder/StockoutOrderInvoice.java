package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User:tu.jie@ifunq.com
 * Date: 15/1/21
 * Time: 下午9:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SaleOrder")
@XmlType(propOrder = {"invoiceType","invoiceNo","invoiceTitle","invoiceContent","invoiceAcount","receiverName","receiverMoble","receiverAddress","userDef1","userDef2","userDef3","userDef4","userDef5","userDef6","userDef7","userDef8"})
public class StockoutOrderInvoice implements Serializable {


    private static final long serialVersionUID = -7953307766931141045L;
    @Description("发票类型")
    @XmlElement(name = "InvoiceType", required = false)
    private String invoiceType;

    @Description("发票号")
    @XmlElement(name = "InvoiceNo", required = false)
    private String invoiceNo;

    @Description("发票抬头")
    @XmlElement(name = "InvoiceTitle", required = false)
    private String invoiceTitle;

    @Description("发票内容")
    @XmlElement(name = "InvoiceContent", required = false)
    private String invoiceContent;

    @Description("发票金额")
    @XmlElement(name = "InvoiceAcount", required = false)
    private String invoiceAcount;

    @Description("发票收方名称")
    @XmlElement(name = "ReceiverName", required = false)
    private String receiverName;

    @Description("发票收方手机")
    @XmlElement(name = "ReceiverMoble", required = false)
    private String receiverMoble;

    @Description("收方地址")
    @XmlElement(name = "ReceiverAddress", required = false)
    private String receiverAddress;

    @Description("扩展字段")
    @XmlElement(name = "UserDef1", required = false)
    private String userDef1;

    @Description("扩展字段")
    @XmlElement(name = "UserDef2", required = false)
    private String userDef2;

    @Description("扩展字段")
    @XmlElement(name = "UserDef3", required = false)
    private String userDef3;

    @Description("扩展字段")
    @XmlElement(name = "UserDef4", required = false)
    private String userDef4;

    @Description("扩展字段")
    @XmlElement(name = "UserDef5", required = false)
    private String userDef5;

    @Description("扩展字段")
    @XmlElement(name = "UserDef6", required = false)
    private String userDef6;

    @Description("扩展字段")
    @XmlElement(name = "UserDef7", required = false)
    private String userDef7;

    @Description("扩展字段")
    @XmlElement(name = "UserDef8", required = false)
    private String userDef8;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public String getInvoiceAcount() {
        return invoiceAcount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverMoble() {
        return receiverMoble;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public String getUserDef1() {
        return userDef1;
    }

    public String getUserDef2() {
        return userDef2;
    }

    public String getUserDef3() {
        return userDef3;
    }

    public String getUserDef4() {
        return userDef4;
    }

    public String getUserDef5() {
        return userDef5;
    }

    public String getUserDef6() {
        return userDef6;
    }

    public String getUserDef7() {
        return userDef7;
    }

    public String getUserDef8() {
        return userDef8;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public void setInvoiceAcount(String invoiceAcount) {
        this.invoiceAcount = invoiceAcount;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setReceiverMoble(String receiverMoble) {
        this.receiverMoble = receiverMoble;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public void setUserDef1(String userDef1) {
        this.userDef1 = userDef1;
    }

    public void setUserDef2(String userDef2) {
        this.userDef2 = userDef2;
    }

    public void setUserDef3(String userDef3) {
        this.userDef3 = userDef3;
    }

    public void setUserDef4(String userDef4) {
        this.userDef4 = userDef4;
    }

    public void setUserDef5(String userDef5) {
        this.userDef5 = userDef5;
    }

    public void setUserDef6(String userDef6) {
        this.userDef6 = userDef6;
    }

    public void setUserDef7(String userDef7) {
        this.userDef7 = userDef7;
    }

    public void setUserDef8(String userDef8) {
        this.userDef8 = userDef8;
    }
}
