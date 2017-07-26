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
public class StockBatchOrderType extends Enumerable4StringValue {
    private static final long serialVersionUID = -2825271536467865264L;
    public static final Logger LOGGER = LoggerFactory.getLogger(StockBatchOrderType.class);

    private static final Lock lock = new ReentrantLock();

    private static volatile transient Map<String, StockBatchOrderType> allbyvalue = new HashMap<String, StockBatchOrderType>();

    private static volatile transient Map<String, StockBatchOrderType> allbyname = new HashMap<String, StockBatchOrderType>();


    public static StockBatchOrderType PURCHASE_STOCKIN_ORDER_TYPE = StockBatchOrderType.valueOf("0", "采购入库单");
//    public static StockBatchOrderType PURCHASE_STOCKIN_ORDER_TYPE = StockBatchOrderType.valueOf("0", "采购入库单");

    public StockBatchOrderType(String value, String name) {
        super(value, name);
    }


    public static StockBatchOrderType valueOf(String value, String name) {
        StockBatchOrderType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                LOGGER.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, StockBatchOrderType> allbyvalue_new = new HashMap<String, StockBatchOrderType>();
        Map<String, StockBatchOrderType> allbyname_new = new HashMap<String, StockBatchOrderType>();
        e = new StockBatchOrderType(value, name);
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

    public static StockBatchOrderType valueOf(String value) {
        StockBatchOrderType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        StockBatchOrderType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static StockBatchOrderType[] values() {
        return allbyvalue.values().toArray(new StockBatchOrderType[0]);
    }
}
