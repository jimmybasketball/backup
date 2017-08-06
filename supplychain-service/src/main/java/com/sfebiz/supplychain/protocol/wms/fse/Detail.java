package com.sfebiz.supplychain.protocol.wms.fse;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Detail implements Serializable{
    private static final long serialVersionUID = 4295378285273940571L;

    /**
     * 商品编码
     */
    public String commodityCode;

    /**
     * 商品名称
     */
    public String commodityName;

    /**
     * 商品发货数量
     */
    public String qty;

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "commodityCode='" + commodityCode + '\'' +
                ", commodityName='" + commodityName + '\'' +
                ", qty=" + qty +
                '}';
    }
}
