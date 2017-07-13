package com.sfebiz.supplychain.exposed.common.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 出库方案类型
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-12 18:01
 **/
public class StockOutPlanType extends Enumerable4StringValue {

    private static final long serialVersionUID = 1330557735871687327L;

    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final Lock lock = new ReentrantLock();

    private static volatile transient Map<String, StockOutPlanType> allbyvalue = new HashMap<String, StockOutPlanType>();

    private static volatile transient Map<String, StockOutPlanType> allbyname = new HashMap<String, StockOutPlanType>();

    public static StockOutPlanType EXPIRE_FIRST = StockOutPlanType.valueOf("EXPIRE_FIRST", "先到期先出");

    public static StockOutPlanType STOCKIN_FIRST = StockOutPlanType.valueOf("STOCKIN_FIRST", "先入库先出");

    public StockOutPlanType(String value, String name) {
        super(value, name);
    }

    public static StockOutPlanType valueOf(String value, String name) {
        StockOutPlanType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, StockOutPlanType> allbyvalue_new = new HashMap<String, StockOutPlanType>();
        Map<String, StockOutPlanType> allbyname_new = new HashMap<String, StockOutPlanType>();
        e = new StockOutPlanType(value, name);
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

    public static StockOutPlanType valueOf(String value) {
        StockOutPlanType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        StockOutPlanType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static StockOutPlanType[] values() {
        return allbyvalue.values().toArray(new StockOutPlanType[0]);
    }

}