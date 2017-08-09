package com.sfebiz.supplychain.exposed.stockout.enums;

public enum IDType {
    /** 转运用途 */
    ID_CARD(1, "身份证");

    private int    value;

    private String description;

    IDType(int value, String description) {
        this.value = value;
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
}
