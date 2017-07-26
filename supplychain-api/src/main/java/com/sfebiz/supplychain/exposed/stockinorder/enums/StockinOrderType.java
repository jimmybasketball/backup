package com.sfebiz.supplychain.exposed.stockinorder.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4IntValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangyajing on 2017/7/17.
 */
public class StockinOrderType extends Enumerable4IntValue {
    private static final long serialVersionUID = 2504548743721319723L;
    private static final Logger log = LoggerFactory.getLogger(Enumerable4IntValue.class);
    private static volatile transient Map<Integer, StockinOrderType> allbyvalue = new HashMap<Integer, StockinOrderType>();
    private static volatile transient Map<String, StockinOrderType> allbyname = new HashMap<String, StockinOrderType>();
    private static final Lock lock = new ReentrantLock();

    public static StockinOrderType SALES_STOCK_IN = StockinOrderType.valueOf(0, "采购入库单");

    public StockinOrderType(int value, String name) {
        super(value, name);
    }

    public static StockinOrderType valueOf(Integer value, String name) {
        StockinOrderType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<Integer, StockinOrderType> allbyvalue_new = new HashMap<Integer, StockinOrderType>();
        Map<String, StockinOrderType> allbyname_new = new HashMap<String, StockinOrderType>();
        e = new StockinOrderType(value, name);
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


    public static StockinOrderType valueOf(int value) {
        StockinOrderType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }


    public static StockinOrderType valueOf(String name) {
        StockinOrderType e = allbyname.get(name);
        if (e != null) {
            return e;
        } else {
            throw new IllegalArgumentException("No enum defined:" + name);
        }
    }

    public static boolean containValue(int value) {
        StockinOrderType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static StockinOrderType[] values() {
        return allbyvalue.values().toArray(new StockinOrderType[0]);
    }
}
