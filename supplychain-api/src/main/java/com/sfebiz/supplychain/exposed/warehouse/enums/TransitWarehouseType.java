package com.sfebiz.supplychain.exposed.warehouse.enums;

/**
 * <p>中转仓类型</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/13
 * Time: 下午6:39
 */
public enum TransitWarehouseType {

    /** 转运用途 */
    TRANSPORT(1, "TRANSPORT", "转运用途"),
    /** 集货用途 */
    STOREGOODS(2, "STOREGOODS", "集货用途");

    private int    value;

    private String nid;

    private String description;

    TransitWarehouseType(int value, String nid, String description) {
        this.value = value;
        this.nid = nid;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.nid;
    }
}
