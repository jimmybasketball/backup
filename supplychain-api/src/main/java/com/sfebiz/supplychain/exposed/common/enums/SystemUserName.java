package com.sfebiz.supplychain.exposed.common.enums;

/**
 * 系统内置角色名
 * @author liujc [liujunchi@ifunq.com]
 * @date  2017/7/28 12:00
 */
public enum SystemUserName {
    OPSC("开放物流平台"),
    BSP("BSP"),
    COE("COE"),
    ZEBRA("斑马"),
    COMMON("公用"),
    WMS("仓库"),
    VENDER("供货商"),
    HZ_PORT("杭州电子口岸"),
    CCB("清关供应商"),
    HWT("海外通");

    private String value;

    private SystemUserName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
