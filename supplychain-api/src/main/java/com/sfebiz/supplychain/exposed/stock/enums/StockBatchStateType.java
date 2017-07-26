package com.sfebiz.supplychain.exposed.stock.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 批次库存状态枚举
 * @date 2017-07-14 15:02
 **/
public class StockBatchStateType extends Enumerable4StringValue {
    private static final long serialVersionUID = 6901195709637071714L;
    public static final Logger LOGGER = LoggerFactory.getLogger(StockBatchStateType.class);

    private static final Lock lock = new ReentrantLock();

    private static volatile transient Map<String, StockBatchStateType> allbyvalue = new HashMap<String, StockBatchStateType>();

    private static volatile transient Map<String, StockBatchStateType> allbyname = new HashMap<String, StockBatchStateType>();


    public static StockBatchStateType ENABLE = StockBatchStateType.valueOf("ENABLE", "启用");

    public static StockBatchStateType DISABLE = StockBatchStateType.valueOf("DISABLE", "禁用");

    public StockBatchStateType(String value, String name) {
        super(value, name);
    }


    public static StockBatchStateType valueOf(String value, String name) {
        StockBatchStateType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                LOGGER.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, StockBatchStateType> allbyvalue_new = new HashMap<String, StockBatchStateType>();
        Map<String, StockBatchStateType> allbyname_new = new HashMap<String, StockBatchStateType>();
        e = new StockBatchStateType(value, name);
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

    public static StockBatchStateType valueOf(String value) {
        StockBatchStateType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        StockBatchStateType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static StockBatchStateType[] values() {
        return allbyvalue.values().toArray(new StockBatchStateType[0]);
    }
}
