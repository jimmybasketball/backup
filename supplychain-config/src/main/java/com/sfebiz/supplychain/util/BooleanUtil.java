package com.sfebiz.supplychain.util;

public class BooleanUtil {

    /**
     * 获取int对应的boolean值
     * @param value（0：false 1：true）
     * @return
     */
    public boolean getBooleanForInt(int value) {
        if (value == 0) {
            return false;
        } else {
            return true;
        }
    }
}
