package com.sfebiz.supplychain.protocol.bsp;

import java.io.Serializable;

public enum BSPReturnCode  implements Serializable {

    SUCCESS("OK","成功");

    private String  code;

    private String description;

    BSPReturnCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.getCode();
    }
}
