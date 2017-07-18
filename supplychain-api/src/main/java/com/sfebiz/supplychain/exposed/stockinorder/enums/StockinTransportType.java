package com.sfebiz.supplychain.exposed.stockinorder.enums;

/**
 * Created by QiuJunting on 2015/10/29.
 */
public enum StockinTransportType {
    AIR(0,"空运"),OCEAN(1,"海运"),LAND(2,"陆运");
    private Integer type;
    private String description;

    StockinTransportType(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
