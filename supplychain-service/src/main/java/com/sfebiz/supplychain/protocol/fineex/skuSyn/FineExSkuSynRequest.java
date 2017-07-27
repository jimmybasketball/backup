package com.sfebiz.supplychain.protocol.fineex.skuSyn;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by wuyun on 2017/3/14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
@XmlType(propOrder = {"items"})
public class FineExSkuSynRequest {

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "items")
    private List<FineExSkuSynItem> items;

    public List<FineExSkuSynItem> getItems() {
        return items;
    }

    public void setItems(List<FineExSkuSynItem> items) {
        this.items = items;
    }
}
