package com.sfebiz.supplychain.service.customs.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/11/25 下午3:39
 */
public class CustomsReturnReason extends Enumerable4StringValue {

    private static final Logger                                        log          = LoggerFactory
                                                                                        .getLogger(Enumerable4StringValue.class);

    private static volatile transient Map<String, CustomsReturnReason> allbyvalue   = new HashMap<String, CustomsReturnReason>();

    private static volatile transient Map<String, CustomsReturnReason> allbyname    = new HashMap<String, CustomsReturnReason>();

    private static final Lock                                          lock         = new ReentrantLock();

    public static CustomsReturnReason                                  MONTH_EXCEED = CustomsReturnReason
                                                                                        .valueOf(
                                                                                            "MONTH_EXCEED",
                                                                                            "月度购买次数超限(0258)");

    public static CustomsReturnReason                                  YEAR_EXCEED  = CustomsReturnReason
                                                                                        .valueOf(
                                                                                            "YEAR_EXCEED",
                                                                                            "年度购买额度超限(0257)");

    private CustomsReturnReason(String value, String name) {
        super(value, name);
    }

    public static CustomsReturnReason valueOf(String value, String name) {
        CustomsReturnReason e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name
                         + ", new name:" + name);
            }
        }

        Map<String, CustomsReturnReason> allbyvalue_new = new HashMap<String, CustomsReturnReason>();
        Map<String, CustomsReturnReason> allbyname_new = new HashMap<String, CustomsReturnReason>();
        e = new CustomsReturnReason(value, name);
        lock.lock();
        try {
            allbyvalue_new.putAll(allbyvalue);
            allbyname_new.putAll(allbyname);
            allbyvalue_new.put(value, e);
            allbyname_new.put(name, e);
            allbyvalue = allbyvalue_new;
            allbyname = allbyname_new;
        } finally {
            lock.unlock();
        }
        return e;
    }

    public static CustomsReturnReason valueOf(String value) {
        CustomsReturnReason e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        CustomsReturnReason e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static CustomsReturnReason[] values() {
        return allbyvalue.values().toArray(new CustomsReturnReason[0]);
    }
}
