package com.sfebiz.supplychain.exposed.route.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 系统路由级别
 *
 * @author liujc
 * @create 2017-07-05 16:05
 **/
public class SystemRouteLevel extends Enumerable4StringValue {


    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = 6171981952160082879L;

    private static volatile transient Map<String, SystemRouteLevel> allbyvalue = new HashMap<String, SystemRouteLevel>();

    private static volatile transient Map<String, SystemRouteLevel> allbyname = new HashMap<String, SystemRouteLevel>();

    public static SystemRouteLevel INFO = SystemRouteLevel.valueOf("INFO", "一般");

    public static SystemRouteLevel WARNING = SystemRouteLevel.valueOf("WARNING", "警告");

    public static SystemRouteLevel ERROR = SystemRouteLevel.valueOf("ERROR", "严重错误");

    public SystemRouteLevel(String value, String name) {
        super(value, name);
    }

    public static SystemRouteLevel valueOf(String value, String name) {
        SystemRouteLevel e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, SystemRouteLevel> allbyvalue_new = new HashMap<String, SystemRouteLevel>();
        Map<String, SystemRouteLevel> allbyname_new = new HashMap<String, SystemRouteLevel>();
        e = new SystemRouteLevel(value, name);
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

    public static SystemRouteLevel valueOf(String value) {
        SystemRouteLevel e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        SystemRouteLevel e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static SystemRouteLevel[] values() {
        return allbyvalue.values().toArray(new SystemRouteLevel[0]);
    }

}
