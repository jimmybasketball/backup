package com.sfebiz.supplychain.exposed.stock.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 批次库存冻结状态
 * @author yangh [yangh@ifunq.com]
 * @date 2017/7/24 16:24
 */

public class StockFreezeState extends Enumerable4StringValue {
    private static final long serialVersionUID = -2825271536467865264L;
    public static final Logger LOGGER = LoggerFactory.getLogger(StockFreezeState.class);

    private static final Lock lock = new ReentrantLock();

    private static volatile transient Map<String, StockFreezeState> allbyvalue = new HashMap<String, StockFreezeState>();

    private static volatile transient Map<String, StockFreezeState> allbyname = new HashMap<String, StockFreezeState>();


    public static StockFreezeState FREEZED = StockFreezeState.valueOf("FREEZED", "已冻结");
    public static StockFreezeState CONSUMED = StockFreezeState.valueOf("CONSUMED", "已消费");
    public static StockFreezeState RELEASED = StockFreezeState.valueOf("RELEASED", "已释放");
    public static StockFreezeState DELETED = StockFreezeState.valueOf("DELETED", "已删除");

    public StockFreezeState(String value, String name) {
        super(value, name);
    }


    public static StockFreezeState valueOf(String value, String name) {
        StockFreezeState e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                LOGGER.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, StockFreezeState> allbyvalue_new = new HashMap<String, StockFreezeState>();
        Map<String, StockFreezeState> allbyname_new = new HashMap<String, StockFreezeState>();
        e = new StockFreezeState(value, name);
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

    public static StockFreezeState valueOf(String value) {
        StockFreezeState e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        StockFreezeState e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static StockFreezeState[] values() {
        return allbyvalue.values().toArray(new StockFreezeState[0]);
    }
}
