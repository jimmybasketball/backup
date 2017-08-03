package com.sfebiz.supplychain.protocol.fse;

/**
 * 快递公司枚举
 * Created by zhangyajing on 2016/2/26.
 */
public enum FSECarrierEnum {

    EMS("EMS","EMS"),
    ZTO("ZTO","ZTO"),
    STO("STO","STO");

    private String key;
    private String value;

    FSECarrierEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getValueByKey(String key) {
        for (FSECarrierEnum item : FSECarrierEnum.values()) {
            if (item.getKey().equals(key)) {
                return item.getValue();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "FSECarrierEnum{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
