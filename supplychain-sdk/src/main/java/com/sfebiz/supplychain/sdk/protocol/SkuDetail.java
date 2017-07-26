package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 仓配物品列表，或者购买列表结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "skuDetail", propOrder = {"skus"})
public class SkuDetail implements Serializable {

    /**
     * 仓配物品列表
     */
    @XmlElementWrapper(name = "skus")
    @XmlElement(name = "sku", nillable = false, required = true)
    public List<Sku> skus;

    public List<Sku> getSkus() {
        if (skus == null) {
            skus = new ArrayList<Sku>();
        }
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    @Override
    public String toString() {
        return "SkuDetail{" +
                "skus=" + skus +
                '}';
    }
}
