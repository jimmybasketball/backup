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
@XmlRootElement(name = "CarrierAddedService")
@XmlType(propOrder = {"serviceCode","attr01","attr02","attr03","attr04","attr05","attr06","attr07","attr08"})
public class StockoutOrderCarrierAddedService implements Serializable {

    private static final long serialVersionUID = -6960743166520020309L;
    @Description("寄件方公司")
    @XmlElement(name = "ServiceCode", required = false)
    private String serviceCode;

    @Description("增值服务值1")
    @XmlElement(name = "Attr01", required = false)
    private String attr01;

    @Description("增值服务值2")
    @XmlElement(name = "Attr02", required = false)
    private String attr02;

    @Description("增值服务值3")
    @XmlElement(name = "Attr03", required = false)
    private String attr03;

    @Description("增值服务值4")
    @XmlElement(name = "Attr04", required = false)
    private String attr04;

    @Description("增值服务值5")
    @XmlElement(name = "Attr05", required = false)
    private String attr05;

    @Description("增值服务值6")
    @XmlElement(name = "Attr06", required = false)
    private String attr06;

    @Description("增值服务值7")
    @XmlElement(name = "Attr07", required = false)
    private String attr07;

    @Description("增值服务值8")
    @XmlElement(name = "Attr08", required = false)
    private String attr08;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getAttr01() {
        return attr01;
    }

    public String getAttr02() {
        return attr02;
    }

    public String getAttr03() {
        return attr03;
    }

    public String getAttr04() {
        return attr04;
    }

    public String getAttr05() {
        return attr05;
    }

    public String getAttr06() {
        return attr06;
    }

    public String getAttr07() {
        return attr07;
    }

    public String getAttr08() {
        return attr08;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public void setAttr01(String attr01) {
        this.attr01 = attr01;
    }

    public void setAttr02(String attr02) {
        this.attr02 = attr02;
    }

    public void setAttr03(String attr03) {
        this.attr03 = attr03;
    }

    public void setAttr04(String attr04) {
        this.attr04 = attr04;
    }

    public void setAttr05(String attr05) {
        this.attr05 = attr05;
    }

    public void setAttr06(String attr06) {
        this.attr06 = attr06;
    }

    public void setAttr07(String attr07) {
        this.attr07 = attr07;
    }

    public void setAttr08(String attr08) {
        this.attr08 = attr08;
    }
}
