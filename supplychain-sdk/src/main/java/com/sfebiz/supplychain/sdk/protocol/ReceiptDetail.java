package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 16/3/7 下午12:33
 * <p/>
 * 订制购物小票的格式
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiptDetail", propOrder = {"header", "footer"})
public class ReceiptDetail implements Serializable {
    private static final long serialVersionUID = 8814673865893420847L;

    /**
     * 购物小票的头部
     */
    @XmlElement(nillable = false, required = true)
    public ReceiptHeader header;

    /**
     * 购物小票的尾部
     */
    @XmlElement(nillable = false, required = true)
    public ReceiptFooter footer;

    public ReceiptHeader getHeader() {
        return header;
    }

    public void setHeader(ReceiptHeader header) {
        this.header = header;
    }

    public ReceiptFooter getFooter() {
        return footer;
    }

    public void setFooter(ReceiptFooter footer) {
        this.footer = footer;
    }

    @Override
    public String toString() {
        return "ReceiptDetail{" +
                "header=" + header +
                ", footer=" + footer +
                '}';
    }
}
