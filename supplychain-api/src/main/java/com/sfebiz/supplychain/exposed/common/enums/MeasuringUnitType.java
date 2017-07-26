package com.sfebiz.supplychain.exposed.common.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 出库方案类型
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-12 18:01
 **/
public class MeasuringUnitType extends Enumerable4StringValue {

    private static final long serialVersionUID = 1330557735871687327L;

    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final Lock lock = new ReentrantLock();

    private static volatile transient Map<String, MeasuringUnitType> allbyvalue = new HashMap<String, MeasuringUnitType>();

    private static volatile transient Map<String, MeasuringUnitType> allbyname = new HashMap<String, MeasuringUnitType>();

    public static MeasuringUnitType F10001 = MeasuringUnitType.valueOf("个", "个");

    public static MeasuringUnitType F10002 = MeasuringUnitType.valueOf("台", "台");

    public static MeasuringUnitType F10003 = MeasuringUnitType.valueOf("张", "张");

    public MeasuringUnitType(String value, String name) {
        super(value, name);
    }

    public static MeasuringUnitType valueOf(String value, String name) {
        MeasuringUnitType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, MeasuringUnitType> allbyvalue_new = new HashMap<String, MeasuringUnitType>();
        Map<String, MeasuringUnitType> allbyname_new = new HashMap<String, MeasuringUnitType>();
        e = new MeasuringUnitType(value, name);
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

    public static MeasuringUnitType valueOf(String value) {
        MeasuringUnitType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        MeasuringUnitType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static MeasuringUnitType[] values() {
        return allbyvalue.values().toArray(new MeasuringUnitType[0]);
    }

}