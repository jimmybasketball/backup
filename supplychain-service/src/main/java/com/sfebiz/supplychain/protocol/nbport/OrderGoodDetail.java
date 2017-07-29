package com.sfebiz.supplychain.protocol.nbport;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by ztc on 2016/12/28.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"productId","qty","price","amount"})
@XmlRootElement(name = "detail")
public class OrderGoodDetail implements Serializable {

    @XmlElement(name = "productId")
    private String productId;

    @XmlElement(name = "qty")
    private String qty;

    @XmlElement(name = "price")
    private String price;

    @XmlElement(name = "amount")
    private String amount;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
