package com.sfebiz.supplychain.exposed.stockinorder.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4IntValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by QiuJunting on 2015/10/29.
 */
public class StockinTransportType extends Enumerable4IntValue {
    private static final long serialVersionUID = 1316522171726098049L;
    private static final Logger log = LoggerFactory.getLogger(Enumerable4IntValue.class);
    private static volatile transient Map<Integer, StockinTransportType> allbyvalue = new HashMap<Integer, StockinTransportType>();
    private static volatile transient Map<String, StockinTransportType> allbyname = new HashMap<String, StockinTransportType>();
    private static final Lock lock = new ReentrantLock();

    public static StockinTransportType AIR = StockinTransportType.valueOf(0,"空运");
    public static StockinTransportType OCEAN = StockinTransportType.valueOf(1,"海运");
    public static StockinTransportType LAND = StockinTransportType.valueOf(2,"陆运");
    public StockinTransportType(int value, String name) {
        super(value, name);
    }

    public static StockinTransportType valueOf(int value,String name) {
        StockinTransportType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<Integer, StockinTransportType> allbyvalue_new = new HashMap<Integer, StockinTransportType>();
        Map<String, StockinTransportType> allbyname_new = new HashMap<String, StockinTransportType>();
        e = new StockinTransportType(value, name);
        lock.lock();
        try {
            allbyvalue_new.putAll(allbyvalue);
            allbyname_new.putAll(allbyname);
            allbyvalue_new.put(value, e);
            allbyname_new.put(name, e);
            allbyvalue = allbyvalue_new;
            allbyname = allbyname_new;
        }
        finally {
            lock.unlock();
        }
        return e;
    }
    public static StockinTransportType valueOf(int value) {
        StockinTransportType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        }
        else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(int value) {
        StockinTransportType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }
    public static StockinTransportType[] values() {
        return allbyvalue.values().toArray(new StockinTransportType[0]);
    }
}
