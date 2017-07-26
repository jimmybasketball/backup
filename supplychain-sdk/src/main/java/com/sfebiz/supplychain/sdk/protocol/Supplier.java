package com.sfebiz.supplychain.sdk.protocol;

/**
 * Created by sam on 3/16/15.
 * Email: sambean@126.com
 */


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 供应商结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "supplier", propOrder = {"customerCode", "supplierId", "supplierName"})
public class Supplier {

    private static final long serialVersionUID = 1220597097403135186L;

    /**
     * customerCode
     */
    @XmlElement(nillable = false, required = true)
    public String customerCode;

    /**
     * supplierId
     */
    @XmlElement(nillable = false, required = true)
    public Integer supplierId;

    /**
     * supplierName
     */
    @XmlElement(nillable = false, required = true)
    public String supplierName;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return "海淘签约供应商";
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "customerCode='" + customerCode + '\'' +
                ", supplierId=" + supplierId +
                ", supplierName='" + supplierName + '\'' +
                '}';
    }
}
