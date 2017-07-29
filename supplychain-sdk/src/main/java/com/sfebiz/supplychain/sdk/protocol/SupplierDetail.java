package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by sam on 3/16/15.
 * Email: sambean@126.com
 */

/**
 * 供应商信息体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "supplierDetail", propOrder = {"suppliers"})
public class SupplierDetail {

    @XmlElementWrapper(name = "suppliers")
    @XmlElement(name = "supplier", nillable = false, required = true)
    private List<Supplier> suppliers;

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public String toString() {
        return "SupplierDetail{" +
                "suppliers=" + suppliers +
                '}';
    }
}
