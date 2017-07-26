package com.sfebiz.supplychain.exposed.line.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 开放物流平台货主供应商线路状态类型
 *
 * @author liujc
 * @create 2017-07-05 16:05
 **/
public class LogisticsLineStateType extends Enumerable4StringValue {

    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = -8306116738895570129L;

    private static volatile transient Map<String, LogisticsLineStateType> allbyvalue = new HashMap<String, LogisticsLineStateType>();

    private static volatile transient Map<String, LogisticsLineStateType> allbyname = new HashMap<String, LogisticsLineStateType>();

    public static LogisticsLineStateType ENABLE = LogisticsLineStateType.valueOf("ENABLE", "启用");

    public static LogisticsLineStateType DISABLE = LogisticsLineStateType.valueOf("DISABLE", "禁用");

    public LogisticsLineStateType(String value, String name) {
        super(value, name);
    }

    public static LogisticsLineStateType valueOf(String value, String name) {
        LogisticsLineStateType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, LogisticsLineStateType> allbyvalue_new = new HashMap<String, LogisticsLineStateType>();
        Map<String, LogisticsLineStateType> allbyname_new = new HashMap<String, LogisticsLineStateType>();
        e = new LogisticsLineStateType(value, name);
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

    public static LogisticsLineStateType valueOf(String value) {
        LogisticsLineStateType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        LogisticsLineStateType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static LogisticsLineStateType[] values() {
        return allbyvalue.values().toArray(new LogisticsLineStateType[0]);
    }
    
}
