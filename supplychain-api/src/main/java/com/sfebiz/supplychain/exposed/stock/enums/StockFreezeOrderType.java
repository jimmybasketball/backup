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

public class StockFreezeOrderType extends Enumerable4StringValue {
    private static final long serialVersionUID = -2825271536467865264L;
    public static final Logger LOGGER = LoggerFactory.getLogger(StockFreezeOrderType.class);

    private static final Lock lock = new ReentrantLock();

    private static volatile transient Map<String, StockFreezeOrderType> allbyvalue = new HashMap<String, StockFreezeOrderType>();

    private static volatile transient Map<String, StockFreezeOrderType> allbyname = new HashMap<String, StockFreezeOrderType>();


    public static StockFreezeOrderType SALE_OUT_ORDER_TYPE = StockFreezeOrderType.valueOf("0", "销售出库单");

    public StockFreezeOrderType(String value, String name) {
        super(value, name);
    }


    public static StockFreezeOrderType valueOf(String value, String name) {
        StockFreezeOrderType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                LOGGER.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, StockFreezeOrderType> allbyvalue_new = new HashMap<String, StockFreezeOrderType>();
        Map<String, StockFreezeOrderType> allbyname_new = new HashMap<String, StockFreezeOrderType>();
        e = new StockFreezeOrderType(value, name);
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

    public static StockFreezeOrderType valueOf(String value) {
        StockFreezeOrderType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        StockFreezeOrderType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static StockFreezeOrderType[] values() {
        return allbyvalue.values().toArray(new StockFreezeOrderType[0]);
    }
}
