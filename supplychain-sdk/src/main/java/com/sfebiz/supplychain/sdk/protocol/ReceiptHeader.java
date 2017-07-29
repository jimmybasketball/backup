package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 16/3/7 下午12:38
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiptHeader", propOrder = {"logo", "title"})
public class ReceiptHeader implements Serializable {
    private static final long serialVersionUID = 4178292735860763465L;


    /**
     * 品牌logo的头部图片
     */
    @XmlElement(nillable = true, required = true)
    public String logo;

    /**
     * 品牌名称和标题
     */
    @XmlElement(nillable = false, required = true)
    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "ReceiptHeader{" +
                "logo='" + logo + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
