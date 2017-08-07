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
@XmlType(propOrder = {"customsType","customsBatch","customsNo","taxPaymentOfCharge","taxAccount","userDef1","userDef2","userDef3","userDef4","userDef5","userDef6","userDef7","userDef8"})
public class StockoutOrderCustomsDeclarationInfo implements Serializable {

    private static final long serialVersionUID = 6860148523237063862L;
    @Description("报关类型")
    @XmlElement(name = "CustomsType", required = false)
    private String customsType;

    @Description("报关批次")
    @XmlElement(name = "CustomsBatch", required = false)
    private String customsBatch;

    @Description("报关号")
    @XmlElement(name = "CustomsNo", required = false)
    private String customsNo;

    @Description("税金结算方式")
    @XmlElement(name = "TaxPaymentOfCharge", required = false)
    private String taxPaymentOfCharge;

    @Description("税金结算账号")
    @XmlElement(name = "TaxAccount", required = false)
    private String taxAccount;

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

    public String getCustomsType() {
        return customsType;
    }

    public String getCustomsBatch() {
        return customsBatch;
    }

    public String getCustomsNo() {
        return customsNo;
    }

    public String getTaxPaymentOfCharge() {
        return taxPaymentOfCharge;
    }

    public String getTaxAccount() {
        return taxAccount;
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

    public void setCustomsType(String customsType) {
        this.customsType = customsType;
    }

    public void setCustomsBatch(String customsBatch) {
        this.customsBatch = customsBatch;
    }

    public void setCustomsNo(String customsNo) {
        this.customsNo = customsNo;
    }

    public void setTaxPaymentOfCharge(String taxPaymentOfCharge) {
        this.taxPaymentOfCharge = taxPaymentOfCharge;
    }

    public void setTaxAccount(String taxAccount) {
        this.taxAccount = taxAccount;
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
