package com.sfebiz.supplychain.exposed.stockout.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4IntValue;

/**
 * <p>出库单类型</p>
 * User: 心远
 * Date: 14/12/17
 * Time: 上午1:29
 */
public class StockoutOrderType extends Enumerable4IntValue {
    private static final long                                         serialVersionUID        = 2504548743721319723L;
    private static final Logger                                       log                     = LoggerFactory
                                                                                                  .getLogger(Enumerable4IntValue.class);
    private static volatile transient Map<Integer, StockoutOrderType> allbyvalue              = new HashMap<Integer, StockoutOrderType>();
    private static volatile transient Map<String, StockoutOrderType>  allbyname               = new HashMap<String, StockoutOrderType>();
    private static final Lock                                         lock                    = new ReentrantLock();

    /** 销售出库单 */
    public static StockoutOrderType                                   SALES_STOCK_OUT         = StockoutOrderType
                                                                                                  .valueOf(
                                                                                                      1,
                                                                                                      "销售出库单");
    /** 销售预售出库单 */
    public static StockoutOrderType                                   SALES_PRESELL_STOCK_OUT = StockoutOrderType
                                                                                                  .valueOf(
                                                                                                      2,
                                                                                                      "销售预售出库单");
    /** 转运出库单 */
    public static StockoutOrderType                                   TRANSPORT_STOCK_OUT     = StockoutOrderType
                                                                                                  .valueOf(
                                                                                                      3,
                                                                                                      "转运出库单");
    /** 调拨出库单 */
    public static StockoutOrderType                                   ALLOCATION_STOCK_OUT    = StockoutOrderType
                                                                                                  .valueOf(
                                                                                                      4,
                                                                                                      "调拨出库单");

    private StockoutOrderType(int value, String name) {
        super(value, name);
    }

    public static StockoutOrderType valueOf(Integer value, String name) {
        StockoutOrderType e = allbyvalue.get(value);
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

        Map<Integer, StockoutOrderType> allbyvalue_new = new HashMap<Integer, StockoutOrderType>();
        Map<String, StockoutOrderType> allbyname_new = new HashMap<String, StockoutOrderType>();
        e = new StockoutOrderType(value, name);
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

    public static StockoutOrderType valueOf(int value) {
        StockoutOrderType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static StockoutOrderType valueOf(String name) {
        StockoutOrderType e = allbyname.get(name);
        if (e != null) {
            return e;
        } else {
            throw new IllegalArgumentException("No enum defined:" + name);
        }
    }

    public static boolean containValue(int value) {
        StockoutOrderType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static StockoutOrderType[] values() {
        return allbyvalue.values().toArray(new StockoutOrderType[0]);
    }

    /**
     * 获取枚举的字符串列表
     * 
     * @return name:value;name:value;...
     */
    public static String getCodeListStr() {
        StringBuilder sb = new StringBuilder();
        for (StockoutOrderType each : values()) {
            sb.append(each.name).append(":").append(each.value).append(",");
        }
        return sb.toString();
    }
}
