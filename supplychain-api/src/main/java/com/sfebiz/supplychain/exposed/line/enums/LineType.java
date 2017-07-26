package com.sfebiz.supplychain.exposed.line.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * 线路类型
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-18 16:39
 **/
public class LineType extends Enumerable4StringValue {

    private static final Logger                             log              = LoggerFactory
                                                                                 .getLogger(Enumerable4StringValue.class);

    private static final Lock                               lock             = new ReentrantLock();
    private static final long                               serialVersionUID = 1244584150755160676L;

    private static volatile transient Map<String, LineType> allbyvalue       = new HashMap<String, LineType>();

    private static volatile transient Map<String, LineType> allbyname        = new HashMap<String, LineType>();

    public static LineType                                  DIRECTMAIL       = LineType
                                                                                 .valueOf(
                                                                                     "DIRECTMAIL",
                                                                                     "直邮");

    public static LineType                                  BONDED           = LineType.valueOf(
                                                                                 "BONDED", "保税");

    public static LineType                                  DA_MAO           = LineType.valueOf(
                                                                                 "DA_MAO", "大贸");

    public LineType(String value, String name) {
        super(value, name);
    }

    public static LineType valueOf(String value, String name) {
        LineType e = allbyvalue.get(value);
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

        Map<String, LineType> allbyvalue_new = new HashMap<String, LineType>();
        Map<String, LineType> allbyname_new = new HashMap<String, LineType>();
        e = new LineType(value, name);
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

    public static LineType valueOf(String value) {
        LineType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        LineType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static LineType[] values() {
        return allbyvalue.values().toArray(new LineType[0]);
    }

}