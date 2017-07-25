package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 16/3/7 下午12:36
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiptFooter", propOrder = {"advert", "desc"})
public class ReceiptFooter implements Serializable {


    /**
     * 品牌的广告
     */
    @XmlElement(nillable = true, required = true)
    public String advert;

    /**
     * 品牌的说明
     */
    @XmlElement(nillable = false, required = true)
    public String desc;

    public String getAdvert() {
        return advert;
    }

    public void setAdvert(String advert) {
        this.advert = advert;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ReceiptFooter{" +
                "advert='" + advert + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
